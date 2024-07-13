package com.example.tvshowapplication.parser;

import com.example.tvshowapplication.entity.AreaData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class AreaDataParser {
    
    public static List<AreaData> parseAreaData(String xmlData) {
        List<AreaData> areaDataList = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject();
            JSONObject data = jsonObject.getJSONObject("ArrayOfString");
            JSONArray dataArray = data.getJSONArray("string");

            for (int i = 0; i < dataArray.length(); i++) {
                String[] values = dataArray.getString(i).split("@");
                
                AreaData areaData = new AreaData();
                areaData.setAreaId(Integer.parseInt(values[0]));
                areaData.setArea(values[1]);
                areaData.setZone(values[2]);

                areaDataList.add(areaData);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return areaDataList;
    }
}

