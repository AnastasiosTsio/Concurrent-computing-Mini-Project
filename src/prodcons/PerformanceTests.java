package prodcons;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PerformanceTests {
    public static void main(String[] args) {
        long[] times = new long[5];
        // Loop through versions (v1, v2, v3, ...)
        for (int i = 1; i<=5 ; i++) {
            String versionedClassName = "prodcons.v" + i + ".ProdConsTestV" + i;
            System.out.println("Trying to load " + versionedClassName);
            try {
                System.out.println("Loading " + versionedClassName);
                // Dynamically load the class
                Class<?> versionedClass = Class.forName(versionedClassName);
                System.out.println("Loaded " + versionedClassName);
                // Find the main method
                Method mainMethod = versionedClass.getMethod("main", String[].class);

                // Measure execution time
                long startTime = System.currentTimeMillis();
                // Invoke the main method without printing anything of the main method
                mainMethod.invoke(null, new Object[] {args});
                long endTime = System.currentTimeMillis();

                times[i - 1] = endTime - startTime;
                // Print the version and execution time
            } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException |
                    InvocationTargetException e) {
                // No more versions found or version doesn't have a main method
                break;
            }
        }
        for (int i = 0; i < times.length; i++) {
            System.out.println("Version " + (i + 1) + " took " + times[i] + "ms");
        }
    }
}
