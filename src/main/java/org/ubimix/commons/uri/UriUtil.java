/**
 * 
 */
package org.ubimix.commons.uri;

/**
 * @author kotelnikov
 */
public class UriUtil {

    public static String fromPath(String str) {
        StringBuilder builder = new StringBuilder();
        char[] array = str.toCharArray();
        for (int i = 0; i < array.length; i++) {
            char ch = array[i];
            switch (ch) {
                case '+':
                    builder.append(" ");
                    break;
                case '%':
                    if (i < array.length - 2) {
                        String s = "" + array[++i] + array[++i];
                        int code = Integer.parseInt(s, 16);
                        ch = (char) code;
                        builder.append(ch);
                        break;
                    }
                default:
                    builder.append(ch);
                    break;

            }
        }
        return builder.toString();

    }

    public static String toPath(String str) {
        StringBuilder builder = new StringBuilder();
        for (char ch : str.toCharArray()) {
            switch (ch) {
                case '\\':
                    builder.append("/");
                    break;
                case ' ':
                    builder.append("+");
                    break;
                case '\n':
                case '\t':
                case '?':
                case ':':
                case '#':
                case '%':
                    builder.append("%");
                    String code = Integer.toHexString(ch);
                    if (code.length() < 2) {
                        builder.append('0');
                    }
                    builder.append(code);
                    break;
                default:
                    builder.append(ch);
                    break;

            }
        }
        return builder.toString();
    }

    /**
     * 
     */
    public UriUtil() {
    }
}
