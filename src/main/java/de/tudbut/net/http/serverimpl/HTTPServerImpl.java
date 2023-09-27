package de.tudbut.net.http.serverimpl;

import de.tudbut.net.http.*;
import de.tudbut.obj.TLMap;
import de.tudbut.tools.Tools;
import de.tudbut.net.http.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Objects;

import static de.tudbut.net.http.serverimpl.Method.from;

public class HTTPServerImpl implements HTTPServer.HTTPHandler {
    
    ArrayList<Object> listeners = new ArrayList<>();
    TLMap<String, ArrayList<Method>> methods = new TLMap<>();
    TLMap<Method, Object> methodObjects = new TLMap<>();
    
    public void addListener(Object listener) {
        if(!checkValidity(listener))
            throw new IllegalArgumentException("The listener contains a method that is annotated with @Serve " +
                                               "but doesn't have matching arguments! Required: " +
                                               "de.tudbut.net.http.HTTPServerRequest, de.tudbut.net.http.ParsedHTTPValue, [etc]"
            );
        listeners.add(listener);
        reindex();
    }
    public void removeListener(Object listener) {
        listeners.remove(listener);
        reindex();
    }
    
    private void reindex() {
        methods = new TLMap<>();
        for (int i = 0; i < listeners.size(); i++) {
            Object listener = listeners.get(i);
            Class<?> clazz = listener.getClass();
            Method[] clazzMethods = clazz.getDeclaredMethods();
            for (int j = 0; j < clazzMethods.length; j++) {
                Method method = clazzMethods[j];
                if(method.isAnnotationPresent(Serve.class)) {
                    Serve annotation = method.getAnnotation(Serve.class);
                    Class<?>[] params = method.getParameterTypes();
                    if(
                            params[0] != HTTPServerRequest.class ||
                            params[1] != ParsedHTTPValue.class ||
                            checkParams(method, params)
                    )
                        continue;
                    method.setAccessible(true);
                    ArrayList<Method> list = methods.get(annotation.type().toString().charAt(0) + annotation.value(), ArrayList::new);
                    list.add(method);
                    if(list.size() == 1)
                        methods.set(annotation.type().toString().charAt(0) + annotation.value(), list);
                    methodObjects.set(method, listener);
                }
            }
        }
    }
    
    private boolean checkParams(Method method, Class<?>[] params) {
        Annotation[][] paramAnnotations = method.getParameterAnnotations();
        boolean gvalid = true;
        for (int k = 2; k < params.length; k++) {
            boolean valid = false;
            for (int l = 0; l < paramAnnotations[k].length; l++) {
                if(
                        paramAnnotations[k][l].annotationType() == Parameter.class ||
                        paramAnnotations[k][l].annotationType() == Path.class ||
                        paramAnnotations[k][l].annotationType() == Header.class
                ) {
                    valid = true;
                    break;
                }
            }
            gvalid = gvalid && valid;
        }
        return !gvalid;
    }
    
    private boolean checkValidity(Object listener) {
        Class<?> clazz = listener.getClass();
        Method[] clazzMethods = clazz.getDeclaredMethods();
        for (int j = 0; j < clazzMethods.length; j++) {
            Method method = clazzMethods[j];
            if(method.isAnnotationPresent(Serve.class)) {
                Class<?>[] params = method.getParameterTypes();
                if(params[0] != HTTPServerRequest.class)
                    return false;
                if(params[1] != ParsedHTTPValue.class)
                    return false;
                if(checkParams(method, params))
                    return false;
            }
        }
        return true;
    }
    
    private static boolean checkRequestMethods(Method method, HTTPRequestType requestType) {
        HTTPRequestType[] values = HTTPRequestType.values();
        ArrayList<HTTPRequestType> allowed = new ArrayList<>();
        for (int i = 0, valuesLength = values.length ; i < valuesLength ; i++) {
            HTTPRequestType value = values[i];
            if(method.getDeclaredAnnotation(from(value)) != null) {
                allowed.add(value);
            }
        }
        return allowed.size() == 0 || allowed.contains(requestType);
    }
    
    private Method find(ArrayList<Method> methods, ParsedHTTPValue httpValue) {
        for (int j = 0 ; j < methods.size() ; j++) {
            Method val = methods.get(j);
            if(!checkRequestMethods(val, (HTTPRequestType) httpValue.getStatusCodeAsEnum())) {
                continue;
            }
            return val;
        }
        return null;
    }
    
    @Override
    public void handle(HTTPServerRequest request) throws Exception {
        ParsedHTTPValue httpValue = request.parse();
        Method method = null;
        String[] keys = methods.keys().toArray(new String[0]);
        ArrayList<Method>[] vals = ((ArrayList<Method>[]) methods.values().toArray(new ArrayList[0]));
        String path = httpValue.getPath();
        while (!path.equals("/") && path.endsWith("/"))
            path = path.substring(0, path.length() - 1);
        for (int i = 0; i < keys.length; i++) {
            String key = keys[i].substring(1);
            String[] splitKey = key.split("/");
            String[] splitPath = path.split("/");
            if(!key.endsWith("/*") && splitKey.length != splitPath.length) {
                continue;
            }
            boolean valid = true;
            for (int k = 0 ; k < splitKey.length && k < splitPath.length ; k++) {
                String s = splitKey[k];
                switch (keys[i].charAt(0)) {
                    case 'W':
                        if (!splitPath[k].matches(Tools.wildcardToRegex(splitKey[k])))
                            valid = false;
                        break;
                    case 'P':
                        if (!splitPath[k].equals(splitKey[k]))
                            valid = false;
                        break;
                    case 'R':
                        if (!splitPath[k].matches(splitKey[k]))
                            valid = false;
                        break;
                }
            }
            if(valid) {
                Method m = find(vals[i], httpValue);
                if(m != null)
                    method = m;
            }
        }
        if(method == null) {
            method = find(methods.get("404"), httpValue);
        }
        Object[] params = createParams(method, httpValue, request);
        if(params == null) {
            method = find(methods.get("400"), httpValue);
            params = new Object[] {request, httpValue};
        }
        try {
            if (Objects.requireNonNull(method).getReturnType() == HTTPResponse.class) {
                request.respond((HTTPResponse) method.invoke(methodObjects.get(method), params));
                return;
            }
            String s = method.invoke(methodObjects.get(method), params).toString();
            ContentType contentType = method.getDeclaredAnnotation(ContentType.class);
            if(contentType == null) {
                contentType = new ContentType() {
                    @Override
                    public Class<? extends Annotation> annotationType() {
                        return ContentType.class;
                    }
    
                    @Override
                    public HTTPContentType value() {
                        return HTTPContentType.HTML;
                    }
                };
            }
            request.respond(HTTPResponseFactory.create(HTTPResponseCode.OK, s, contentType.value()));
        } catch (InvocationTargetException exception) {
            if(exception.getCause() instanceof Response) {
                request.respond(((Response) exception.getCause()).response);
            }
            else
                throw exception;
        }
    }
    
    private Parameter getParameterAnnotation(Annotation[] annotations) {
        for (int i = 0; i < annotations.length; i++) {
            if(annotations[i].annotationType() == Parameter.class)
                return (Parameter) annotations[i];
        }
        return null;
    }
    
    private Path getPathAnnotation(Annotation[] annotations) {
        for (int i = 0; i < annotations.length; i++) {
            if(annotations[i].annotationType() == Path.class)
                return (Path) annotations[i];
        }
        return null;
    }
    
    private Header getHeaderAnnotation(Annotation[] annotations) {
        for (int i = 0; i < annotations.length; i++) {
            if(annotations[i].annotationType() == Header.class)
                return (Header) annotations[i];
        }
        return null;
    }
    
    private Object[] createParams(Method method, ParsedHTTPValue v, HTTPServerRequest r) {
        try {
            ArrayList<Object> list = new ArrayList<>();
            list.add(r);
            list.add(v);
            Class<?>[] parameterTypes = method.getParameterTypes();
            Annotation[][] parameters = method.getParameterAnnotations();
            String[] splitPath = v.getPath().split("/");
            for (int i = 2; i < parameterTypes.length; i++) {
                Parameter param = getParameterAnnotation(parameters[i]);
                Path path = getPathAnnotation(parameters[i]);
                Header header = getHeaderAnnotation(parameters[i]);
                String s = null;
                if (header != null) {
                    HTTPHeader[] headers = v.getHeaders();
                    for (int j = 0, headersLength = headers.length ; j < headersLength ; j++) {
                        HTTPHeader httpHeader = headers[j];
                        if (httpHeader.key().equalsIgnoreCase(header.value())) {
                            s = httpHeader.value();
                        }
                    }
                }
                if (s == null && param != null) {
                    s = v.getQuery().get(param.value());
                }
                if (s == null && path != null) {
                    if (splitPath.length > path.value()) {
                        s = splitPath[path.value()];
                    }
                }
                if(s == null) {
                    list.add(null);
                    continue;
                }
                if(parameterTypes[i] == String.class) {
                    list.add(s);
                }
                else if(parameterTypes[i] == Integer.class) {
                    list.add(Integer.parseInt(s));
                }
                else if(parameterTypes[i] == Boolean.class) {
                    list.add(!s.equals("0") && !s.equals("false") && !s.equals("null"));
                }
                else if(parameterTypes[i] == Long.class) {
                    list.add(Long.parseLong(s));
                }
                else if(parameterTypes[i] == Float.class) {
                    list.add(Float.parseFloat(s));
                }
                else if(parameterTypes[i] == Double.class) {
                    list.add(Double.parseDouble(s));
                }
                else if(parameterTypes[i] == Short.class) {
                    list.add(Short.parseShort(s));
                }
                else {
                    return null;
                }
            }
            return list.toArray(new Object[0]);
        } catch (Exception e) {
            return null;
        }
    }
}
