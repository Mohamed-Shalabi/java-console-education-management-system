package utils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public final class Utils {

    private Utils() {

    }

    public static <T> String arrayListAsString(ArrayList<T> arrayList, String start) {
        StringBuilder s = new StringBuilder();
        if (start != null) {
            s.append(start).append(" : \n");
        }
        int i = 0;
        for (T t : arrayList) {
            i++;
            s.append(i).append(". ").append(t.toString()).append("\n");
        }
        return s.toString();
    }

    public static <K, V> String mapAsString(LinkedHashMap<K, V> map, String keyStart, String valueStart) {
        StringBuilder s = new StringBuilder();
        if (keyStart != null && valueStart != null) {
            s.append(keyStart).append(" : ").append(valueStart).append("\n");
        }
        int i = 0;
        for (K key : map.keySet()) {
            i++;
            s.append(i).append(". ");
            if (key != null) {
                if (map.get(key) != null) {
                    s.append(key.toString()).append(" : ").append(map.get(key).toString()).append("\n");
                } else {
                    s.append(key.toString()).append(" : ").append("no data").append("\n");
                }
            }

        }
        return s.toString();
    }

    public static <T, K> void addGeneralToMap(Map<T, K> map, T element, K element1) {
        for (T element2 : map.keySet()) {
            if (element2.equals(element)) {
                map.put(element2, element1);
                return;
            }
        }
        map.put(element, element1);
    }

    public static <T> boolean addGeneralToArrayList(ArrayList<T> arrayList, T element) {
        for (T element2 : arrayList) {
            if (element2.equals(element)) {
                return false;
            }
        }
        arrayList.add(element);
        return true;
    }

    public static <T> boolean arrayListContains(ArrayList<T> list, T t) {
        for (T t1 : list) {
            if (t.equals(t1)) return true;
        }
        return false;
    }

    public static <T> T getGeneralFromLinkedSetOrNull(Set<T> set, int index) {
        int i = -1;
        for (T t : set) {
            i++;
            if (i == index) {
                return t;
            }
        }
        return null;
    }
}
