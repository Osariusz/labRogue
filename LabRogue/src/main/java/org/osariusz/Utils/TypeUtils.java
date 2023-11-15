package org.osariusz.Utils;

import org.osariusz.GameElements.Wall;
import org.osariusz.Items.Item;

import java.util.ArrayList;
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
