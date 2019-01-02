package com.ansgar.rdroidpc.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CollectionsUtil {
    public static int getPositionInSet(HashMap hashMap, int key) {
        List list = new ArrayList(hashMap.keySet());
        return list.indexOf(key);
    }
}
