package com.fps.opendagen;

import com.lightcurb.sdk.model.Promotion;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by David on 24-12-2017.
 */

public class Util {
    public static String promotionMapToJsonString(Map<String, String> inputMap){
        JSONObject jsonObject = new JSONObject(inputMap);
        String jsonString = jsonObject.toString();
        return jsonString;
    }

    public static Map jsonStringToPromotionMap(String jsonString) {
        Map<String, String> promotionMap = new HashMap<String, String>();

        if (!jsonString.isEmpty()) {
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                Iterator<String> keysItr = jsonObject.keys();
                while (keysItr.hasNext()) {
                    String key = keysItr.next();
                    String value = (String) jsonObject.get(key);
                    promotionMap.put(key, value);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return  promotionMap;
    }

    public static String promotionToJson(Promotion promotion) {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("name", promotion.name);
            jsonObject.put("message", promotion.message);
            jsonObject.put("title", promotion.title);
            jsonObject.put("uri", promotion.uri);

            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }

}
