package ru.itis.chat.config;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.*;

public class ServerContext {
    private String secretKey;
    private Map<Class, Object> componentInstances;

    public ServerContext(String dbPropertiesFile) {
        componentInstances = new HashMap<>();
        componentInstances.put(ConnectionComponent.class, new ConnectionComponent(dbPropertiesFile));
        this.secretKey = "secret";
    }

    public <T> T getComponent(Class<T> componentType) {
        return (T)getComponentInstances().get(componentType);
    }

    public String getSecretKey() {
        return secretKey;
    }

    public Map<Class, Object> getComponentInstances() {
        return componentInstances;
    }

    public void init(Package p) {
        try {
            Class classes[] = getClasses(p.getName());
            for(Class cl : classes) {
                if(isComponent(cl)) {
                    componentInstances.put(cl, recursiveCreateInstance(cl));
                }
            }
        }catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private Object recursiveCreateInstance(Class cl) {
        try {
            //if instance is already created, we don't need to do it
            if(componentInstances.containsKey(cl)) {
                return componentInstances.get(cl);
            }
            //create instance, supposing Component has only 1 constructor,
            // accepting only other Components as arguments
            Constructor con = cl.getConstructors()[0];
            //getting arguments for constructor
            List<Object> conArgs = new ArrayList<>();
            for(Class c : con.getParameterTypes()) {
                if(isComponent(c)) {
                    if(componentInstances.containsKey(c)) {
                        conArgs.add(componentInstances.get(c));
                    }
                    else {
                        conArgs.add(recursiveCreateInstance(c));
                    }
                }
                else  {
                    throw new IllegalStateException("Can't create " + cl +
                            " because it has non-component constructor argument " + c  +
                            ". Please add it manually before init.");
                }
            }
            return con.newInstance(conArgs.toArray());
        }catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private boolean isComponent(Class cl) {
        for(Class c : cl.getInterfaces()) {
            if(c.equals(Component.class)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Scans all classes accessible from the context class loader which belongs to the given package and subpackages.
     *
     * @param packageName The base package
     * @return The classes
     * @throws ClassNotFoundException
     * @throws IOException
     */
    private Class[] getClasses(String packageName)
            throws ClassNotFoundException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if(classLoader == null) {
            throw new IllegalStateException("the hell this shit is null");
        }
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<File>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        ArrayList<Class> classes = new ArrayList<Class>();
        for (File directory : dirs) {
            classes.addAll(this.recursiveFindClasses(directory, packageName));
        }
        return classes.toArray(new Class[classes.size()]);
    }

    /**
     * Recursive method used to find all classes in a given directory and subdirs.
     *
     * @param directory   The base directory
     * @param packageName The package name for classes found inside the base directory
     * @return The classes
     * @throws ClassNotFoundException
     */
    private List<Class> recursiveFindClasses(File directory, String packageName) throws ClassNotFoundException {
        List<Class> classes = new ArrayList<Class>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                if(file.getName().contains(".")) {
                    throw new IllegalStateException("dir name can't contain dots");
                }
                classes.addAll(recursiveFindClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
            }
        }
        return classes;
    }
}
