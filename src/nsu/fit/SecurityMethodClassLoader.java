package nsu.fit;

import java.io.*;

public class SecurityMethodClassLoader extends ClassLoader {
    private final String scanPath;

    public SecurityMethodClassLoader(String scanPath, ClassLoader parent) {
        super(parent);
        this.scanPath = scanPath;
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        try {
            byte[] bytes = loadClassIntoMemory(scanPath + File.separator + name + ".class");
            Class<?> aClass = super.defineClass(bytes, 0, bytes.length);
            super.resolveClass(aClass);
            return aClass;
        } catch (IOException ex) {
            return super.loadClass(name);
        }
    }

    private byte[] loadClassIntoMemory(String path) throws IOException {
        try (InputStream is = new FileInputStream(new File(path))) {
            return is.readAllBytes();
        }
    }
}
