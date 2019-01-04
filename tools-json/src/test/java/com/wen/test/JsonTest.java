package com.wen.test;

import com.wen.tools.json.util.JsonObject;

public class JsonTest {
    public static void main(String[] args) {
        JsonObject jsonObject=JsonObject.create();
        jsonObject.append("{\"id\":1}");
        jsonObject.append("name",2);
        System.out.println(jsonObject.containsKey("name"));
        jsonObject=JsonObject.create();
        System.out.println(jsonObject.containsKey("name"));
    }
}
