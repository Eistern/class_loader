package nsu.fit;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Main {

    public static void main(String[] args) throws ClassNotFoundException {
        if (args.length != 1) {
            System.err.println("Usage: <path/to/scan/dir>");
            return;
        }

        String pathToScanDir = args[0];
        File scanDir = new File(pathToScanDir);
        if (!scanDir.isDirectory()) {
            System.err.println("Given path is not a directory");
            return;
        }

        String[] files = scanDir.list();
        assert files != null;

        SecurityMethodClassLoader customClassLoader = new SecurityMethodClassLoader(pathToScanDir, Main.class.getClassLoader());
        for (String fileName : files) {
            String className = fileName.split(".class$")[0];
            Class<?> loadedClass = customClassLoader.loadClass(className);
            try {
                Object instance = loadedClass.getDeclaredConstructor().newInstance();
                Method getSecurityMessageMethod = loadedClass.getDeclaredMethod("getSecurityMessage");
                System.out.println(loadedClass.getCanonicalName() + "#getSecurityMessage(): ");
                getSecurityMessageMethod.setAccessible(true);
                String result = (String) getSecurityMessageMethod.invoke(instance);
                System.out.println(result);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                System.out.println("No getSecurityMessage() found in " + loadedClass.getCanonicalName());
            }

        }
    }
}
