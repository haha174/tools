package com.wen.tools.json.util;


import com.alibaba.fastjson.JSON;

import java.util.Map;
import java.util.TreeMap;

public final class JsonObject {
    private Map<String, Object> values = new TreeMap();

    private JsonObject() {
    }

    public static final JsonObject create() {
        JsonObject jo = new JsonObject();
        jo.values = new TreeMap();
        return jo;
    }


    public final JsonObject append(String jsonValue) {
        Map m = JSON.parseObject(jsonValue, Map.class);
        if (m != null) {
            this.values.putAll(m);
        }

        return this;
    }

    public final JsonObject append(JsonObject value) {
        if (value != null) {
            this.values.putAll(value.values);
        }

        return this;
    }


    public JsonObject append(String key, String value) {
        this.values.put(key, value);
        return this;
    }

    public JsonObject append(String key, Object value) {
        this.values.put(key, value);
        return this;
    }

    public String getValue(String key) {
        Object o = this.values.get(key);
        return o == null ? null : String.valueOf(o);
    }

    public <T> T getValue(String key, Class<T> ct) {
        T o = (T) this.values.get(key);
        return o == null ? null : o;
    }

    public String toJson() {
        return JSON.toJSONString(this.values);
    }

    public Map<String, Object> toMap() {
        return this.values;
    }

    public String toString() {
        return this.toJson();
    }
}
