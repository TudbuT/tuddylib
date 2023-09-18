package de.tudbut.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DiscoverClasses {

    ClassLoader loader;
    String pkg;
    Class<? extends Annotation> annotation;
    Class<?> superClass;
    ArrayList<Class<?>> interfaces = new ArrayList<>();

    private DiscoverClasses() {}

    public static DiscoverClasses of(ClassLoader loader) {
        DiscoverClasses discover = new DiscoverClasses();
        discover.loader = loader;
        return discover;
    }

    public DiscoverClasses in(String pkg) {
        this.pkg = pkg;
        return this;
    }

    public DiscoverClasses with(Class<? extends Annotation> annotation) {
        this.annotation = annotation;
        return this;
    }

    public DiscoverClasses extending(Class<?> superClass) {
        if(superClass.isInterface()) throw new IllegalArgumentException("Interfaces can't be superclasses!");
        this.superClass = superClass;
        return this;
    }

    public DiscoverClasses implementing(Class<?> iface) {
        if(!iface.isInterface()) throw new IllegalArgumentException("Superclasses can't be interfaces!");
        this.interfaces.add(iface);
        return this;
    }

    public List<Class<?>> run() {
        InputStream stream = loader.getResourceAsStream(pkg.replace('.', '/'));
        if(stream == null)
            return null;
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        List<String> files = reader.lines()
                .filter(line -> line.endsWith(".class") || !line.contains("."))
                .map(line -> pkg.replace('.', '/') + "/" + line)
                .collect(Collectors.toList());
        for (int i = 0; i < files.size(); i++) {
            String file = files.get(i);
            if(!file.endsWith(".class")) {
                try {
                    BufferedReader subReader = new BufferedReader(new InputStreamReader(loader.getResourceAsStream(file)));
                    List<String> subFiles = subReader.lines()
                            .filter(line -> line.endsWith(".class") || !line.contains("."))
                            .map(line -> file + "/" + line)
                            .collect(Collectors.toList());
                    files.addAll(subFiles);
                } catch (Exception e) {}
            }
        }
        return files.stream()
                .map(line -> {
                    try {
                        return loader.loadClass(line.replace('/', '.').substring(0, line.length() - ".class".length()));
                    } catch (ClassNotFoundException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .filter(x -> {
                    if(annotation != null) {
                        return x.getDeclaredAnnotation(annotation) != null;
                    }
                    return true;
                })
                .filter(x -> {
                    if(superClass != null) {
                        return x.getSuperclass() == superClass;
                    }
                    return true;
                })
                .filter(x -> {
                    if(!interfaces.isEmpty()) {
                        List<Class<?>> list = Arrays.asList(x.getInterfaces());
                        for (Class<?> iface : interfaces) {
                            if(!list.contains(iface))
                                return false;
                        }
                    }
                    return true;
                })
                .collect(Collectors.toList());
    }
}
