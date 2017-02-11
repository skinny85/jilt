package org.jilt.utils;

public abstract class Utils {
    public static String join(Iterable<?> elements) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;

        for (Object element : elements) {
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
