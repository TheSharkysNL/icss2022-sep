package nl.han.ica.icss.gui;

import nl.han.ica.datastructures.HANHashMap;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        HANHashMap<String, String> map = new HANHashMap<>();
        map.put("test", "value");
        map.put("test2", "value2");
        map.put("test3", "value3");
        map.put("test4", "value4");
        String value = map.get("test");
        String value2 = map.get("test2");
        String value3 = map.get("test3");
        String value4 = map.get("test4");

        String value5 = map.remove("test4");
        String value6 = map.remove("test4");
        String value7 = map.get("test4");

        MainGui.launch(MainGui.class,args);
    }
}
