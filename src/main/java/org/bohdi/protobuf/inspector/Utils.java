package org.bohdi.protobuf.inspector;

import java.util.List;

/**
 * Created by chris on 3/26/17.
 */
public class Utils {

    public static String join(List<?> list, String delim) {
        int len = list.size();
        if (len == 0)
            return "";
        StringBuilder sb = new StringBuilder(list.get(0).toString());
        for (int i = 1; i < len; i++) {
            sb.append(delim);
            sb.append(list.get(i).toString());
        }
        return sb.toString();
    }
}
