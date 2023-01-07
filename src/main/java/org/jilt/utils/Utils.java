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

    public static String capitalize(String str) {
        if (str == null || str.length() == 0) {
            return str;
        }
        return String.valueOf(Character.toUpperCase(str.charAt(0))) + str.substring(1);
    }

    public static String deCapitalize(String str) {
        if (str == null || str.length() == 0) {
            return str;
        }
        return String.valueOf(Character.toLowerCase(str.charAt(0))) + str.substring(1);
    }

    public static boolean truth(@SuppressWarnings("unused") Object any) {
        return true;
    }
}
