/*
 *   Licensed to the Apache Software Foundation (ASF) under one or more
 *   contributor license agreements.  See the NOTICE file distributed with
 *   this work for additional information regarding copyright ownership.
 *   The ASF licenses this file to You under the Apache License, Version 2.0
 *   (the "License"); you may not use this file except in compliance with
 *   the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */

package com.example.demo.utils;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Type;
import java.util.*;

public class GsonUtil {


    /**
     * 禁用html转义
     */
    private static final Gson GSON = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .setLongSerializationPolicy(LongSerializationPolicy.STRING)
            .disableHtmlEscaping()
            .create();


    private static class MapDeserializer<T, U> implements JsonDeserializer<Map<T, U>> {

        @Override
        public Map<T, U> deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
            if (!json.isJsonObject()) {
                return null;
            }

            JsonObject jsonObject = json.getAsJsonObject();
            Set<Map.Entry<String, JsonElement>> jsonEntrySet = jsonObject.entrySet();
            Map<T, U> deserializedMap = new LinkedHashMap<>();

            for (Map.Entry<String, JsonElement> entry : jsonEntrySet) {
                U value = context.deserialize(entry.getValue(), this.getType(entry.getValue()));
                deserializedMap.put((T) entry.getKey(), value);
            }

            return deserializedMap;
        }

        /**
         * Get JsonElement class type.
         *
         * @param element the element
         * @return Class class
         */
        public Class getType(JsonElement element) {
            if (element.isJsonPrimitive()) {
                final JsonPrimitive primitive = element.getAsJsonPrimitive();
                if (primitive.isString()) {
                    return String.class;
                } else if (primitive.isNumber()) {
                    String numStr = primitive.getAsString();
                    if (numStr.contains(".") || numStr.contains("e")
                            || numStr.contains("E")) {
                        return Double.class;
                    }
                    return Long.class;
                } else if (primitive.isBoolean()) {
                    return Boolean.class;
                }
            }
            return element.getClass();
        }
    }


    /**
     * To json string.
     *
     * @param object the object
     * @return the string
     */
    public static String toJson(final Object object) {
        return GSON.toJson(object);
    }

    /**
     * From json t.
     *
     * @param <T>    the type parameter
     * @param json   the json
     * @param tClass the t class
     * @return the t
     */
    public static <T> T fromJson(final String json, final Class<T> tClass) {
        return GSON.fromJson(json, tClass);
    }

    /**
     * From list list.
     *
     * @param <T>    the type parameter
     * @param string the string
     * @param cls    the cls
     * @return the list
     */
    public static <T> List<T> fromList(String string, Class<T[]> cls) {
        T[] array = GSON.fromJson(string, cls);
        return Arrays.asList(array);
    }


    /**
     * toGetParam.
     *
     * @param json json
     * @return java.lang.String string
     */
    public static String toGetParam(final String json) {
        if (StringUtils.isBlank(json)) {
            return "";
        }
        final Map<String, String> map = toStringMap(json);
        StringBuilder stringBuilder = new StringBuilder();
        map.forEach((k, v) -> stringBuilder.append(k).append("=").append(v).append("&"));
        final String r = stringBuilder.toString();
        return r.substring(0, r.lastIndexOf("&"));

    }

    /**
     * toMap.
     *
     * @param json json
     * @return hashMap map
     */
    public static Map<String, String> toStringMap(final String json) {
        return GSON.fromJson(json, new TypeToken<Map<String, String>>() {
        }.getType());
    }


    /**
     * toList Map.
     *
     * @param json json
     * @return hashMap list
     */
    public static List<Map> toListMap(final String json) {
        return GSON.fromJson(json, new TypeToken<List<Map>>() {
        }.getType());
    }

    /**
     * To object map map.
     *
     * @param json the json
     * @return the map
     */
    public static Map<String, Object> toObjectMap(final String json) {
        TypeToken typeToken = new TypeToken<Map<String, Object>>() {
        };
        Gson gson = new GsonBuilder().serializeNulls().registerTypeHierarchyAdapter(typeToken.getRawType(), new MapDeserializer<String, Object>()).create();
        return gson.fromJson(json, typeToken.getType());
    }
}
