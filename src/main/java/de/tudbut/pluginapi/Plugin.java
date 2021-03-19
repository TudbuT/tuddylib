package de.tudbut.pluginapi;


import tudbut.tools.BetterClassLoader;
import tudbut.logger.DetailedLogger;
import tudbut.logger.Logger;
import tudbut.logger.LoggerSink;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class Plugin {
    static void loadClass() {
    
    }
    
    private LoggerSink logger;
    private Logger nLogger;
    private DetailedLogger dLogger;
    private BetterClassLoader cl;
    private String jar;

    public abstract void onLoad();

    public abstract void onUnload();

    public abstract void onEvent(PluginEvent event);

    public BetterClassLoader getCL() {
        return cl;
    }

    public LoggerSink getLogger() {
        return logger;
    }

    public LoggerSink getLogger(String sub) {
        return logger.subChannel(sub);
    }

    public void setLoggerType(boolean detailed) {
        logger = detailed ? dLogger : nLogger;
    }

    public void setLogger(String name) {
        this.nLogger = new Logger(name);
        this.dLogger = new DetailedLogger(name);
        setLoggerType(false);
    }

    public void setCL(BetterClassLoader cl, String jar) throws PluginException {
        if (this.cl == null) {
            this.cl = cl;
            this.jar = jar;
        }
        else
            throw new PluginException("Redefining classLoader");
    }

    public String getName() {
        return jar;
    }

    public Object useCustomMethod(String methodName, Object... args) {
        for (Method method : getClass().getDeclaredMethods()) {
            try {
                if(method.getName().equals(methodName))
                    return method.invoke(this, args);
            } catch (IllegalAccessException | InvocationTargetException ignore) {
            }
        }
        return null;
    }

    public Object useStaticCustomMethod(String methodName, Object... args) {
        for (Method method : getClass().getDeclaredMethods()) {
            try {
                if(method.getName().equals(methodName))
                    return method.invoke(null, args);
            } catch (IllegalAccessException | InvocationTargetException ignore) {
            }
        }
        return null;
    }

    public Object useStaticCustomMethodInCustomClass(String className, String methodName, Object... args) {
        for (Method method : getCustomClass(className).getDeclaredMethods()) {
            try {
                if(method.getName().equals(methodName))
                    return method.invoke(null, args);
            } catch (IllegalAccessException | InvocationTargetException ignore) {
            }
        }
        return null;
    }

    public Object useCustomMethodInCustomClass(String methodName, Object instance, Object... args) {
        for (Method method : instance.getClass().getDeclaredMethods()) {
            try {
                if(method.getName().equals(methodName))
                    return method.invoke(instance, args);
            } catch (IllegalAccessException | InvocationTargetException ignore) {
            }
        }
        return null;
    }

    public Class<?> getCustomClass(String className) {
        try {
            return getCL().friendlyGet(className);
        } catch (NullPointerException ignore) { }
        return null;
    }
}
