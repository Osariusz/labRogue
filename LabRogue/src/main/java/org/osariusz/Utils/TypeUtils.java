package org.osariusz.Utils;

import java.util.List;

public class TypeUtils {
    public static boolean objectOfTypes(Object object, List<Class<?>> types) {
        for (Class<?> exclusiveType : types) {
            if (exclusiveType.isInstance(object)) {
                return true;
            }
        }
        return false;
    }
}
