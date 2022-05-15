package com.alwxkxk.server.entity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

@Slf4j
public class CommonUtil {
    public static Integer sizeString2Number(String sizeString) {
        try {
            if (sizeString.contains("GB")) {
                return Integer.valueOf(sizeString.replace("GB", "")) * 1024 * 1024 * 1024 * 8;
            } else if (sizeString.contains("MB")) {
                return Integer.valueOf(sizeString.replace("MB", "")) * 1024 * 1024 * 8;
            } else if (sizeString.contains("KB")) {
                return Integer.valueOf(sizeString.replace("KB", "")) * 1024 * 8;
            } else if (sizeString.contains("B")) {
                return Integer.valueOf(sizeString.replace("B", "")) * 8;
            } else {
                return Integer.valueOf(sizeString);
            }
        } catch (NumberFormatException e) {
            log.error("websocket.maxSize配置异常", e);
            return 0;
        }
    }

    /**
     *  将HashMap obj 转换成 JSON字符串
     * @param obj
     * @return
     */
    public static String objectToJson(HashMap<String,Object> obj){
        ObjectMapper objectMapper = new ObjectMapper();
        String json = "";
        try{
            json = objectMapper.writeValueAsString(obj);
        }catch (JsonProcessingException e) {
            log.error(e.toString());
        }
        return json;
    }

    /**
     *  将JSON对象转换成 HashMap obj
     * @param json
     * @return
     */
    public static HashMap<String,Object> jsonToObject(String json){
        ObjectMapper objectMapper = new ObjectMapper();

        HashMap<String, Object> obj = new HashMap<String, Object>();
        try {
            obj = objectMapper.readValue(json, new TypeReference<HashMap<String,Object>>(){});
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return obj;
    }
}
