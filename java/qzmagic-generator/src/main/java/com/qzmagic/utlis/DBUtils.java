package com.qzmagic.utlis;

import com.qzmagic.config.Constants;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;

public class DBUtils {
    private DBUtils() {
    }

    public static boolean arraysContains(String[] arr, String targetValue) {
        return Arrays.asList(arr).contains(targetValue);
    }

    public static String substringBetween(String str, String open, String close) {
        if (!ObjectUtils.allNotNull(new Object[]{str, open, close})) {
            return null;
        } else {
            int start = str.indexOf(open);
            if (start != -1) {
                int end = str.indexOf(close, start + open.length());
                if (end != -1) {
                    return str.substring(start + open.length(), end);
                }
            }

            return null;
        }
    }

    public static String sqlTypeToJavaType(String type) {
        // 时间类型
        if (arraysContains(Constants.COLUMN_TYPE_TIME, type)) {
            return "Date";
        } else if (arraysContains(Constants.COLUMN_TYPE_NUMBER, type)) {
            // 如果是浮点型 统一用BigDecimal
            String[] str = substringBetween(type, "(", ")").split(",");
            if (str != null && str.length == 2 && Integer.parseInt(str[1]) > 0) {
                return "BigDecimal";
            }
            // 如果是整形
            else if (str != null && str.length == 1 && Integer.parseInt(str[0]) <= 10) {
                return "Integer";
            }
            // 长整形
            else {
                return "Long";
            }
        } else {
            return "String";
        }
    }


    public static String toPascalCase(String input) {
        StringBuilder result = new StringBuilder();
        boolean nextTitleCase = true;

        for (char c : input.toCharArray()) {
            if (Character.isSpaceChar(c) || c == '_') {
                nextTitleCase = true;
            } else if (nextTitleCase) {
                result.append(Character.toTitleCase(c));
                nextTitleCase = false;
            } else {
                result.append(Character.toLowerCase(c));
            }
        }

        return result.toString();
    }

    public static String toCamelCase(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        StringBuilder result = new StringBuilder();
        boolean nextTitleCase = false;

        for (char c : input.toCharArray()) {
            if (Character.isSpaceChar(c) || c == '_') {
                nextTitleCase = true;
            } else if (nextTitleCase) {
                result.append(Character.toTitleCase(c));
                nextTitleCase = false;
            } else {
                result.append(Character.toLowerCase(c));
            }
        }

        return result.toString();
    }
}
