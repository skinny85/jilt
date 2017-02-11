package org.jilt.utils;

import java.util.List;

public abstract class Utils {
    public static String join(List<?> list) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;

        for (Object element : list) {
            if (first) {
                first = false;
            } else {
                sb.append(", ");
            }

            sb.append(element);
        }

        return sb.toString();
    }
}
