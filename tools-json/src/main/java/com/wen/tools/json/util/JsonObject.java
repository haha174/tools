package com.wen.tools.json.util;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.Map;

public final class JsonObject extends JSONObject {

    private JsonObject() {
    }

    public static final JsonObject create() {

        JsonObject jo = new JsonObject();
        return jo;
    }


    public final JsonObject append(String jsonValue) {
        Map m = JSON.parseObject(jsonValue, Map.class);
        if (m != null) {
            super.getInnerMap().putAll(m);
        }

        return this;
    }

    public final JsonObject append(JsonObject value) {
        if (value != null) {
           super.getInnerMap().putAll(value.toMap());
        }

        return this;
    }


    public JsonObject append(String key, String value) {
        super.getInnerMap().put(key, value);
        return this;
    }

    public JsonObject append(String key, Object value) {
        super.getInnerMap().put(key, value);
        return this;
    }

    public String getValue(String key) {
        Object o = super.getInnerMap().get(key);
        return o == null ? null : String.valueOf(o);
    }

    public <T> T getValue(String key, Class<T> ct) {
        T o = (T)super.getInnerMap().get(key);
        return o == null ? null : o;
    }

    public String toJson() {
        return JSON.toJSONString(super.toJSONString());
    }

    public Map<String, Object> toMap() {
        return super.getInnerMap();
    }

    public String toString() {
        return this.toJson();
    }
}
