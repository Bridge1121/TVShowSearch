package com.example.tvshowapplication;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tvshowapplication.entity.AreaData;
import com.example.tvshowapplication.entity.TVChannel;
import com.example.tvshowapplication.entity.TVProgram;
import com.example.tvshowapplication.entity.TVStation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
//    通过频道和日期查询，首先根据分类电视ID或者省市ID查电视台，再根据电视台查询频道，根据频道查节目
    private EditText channelEditText;
    private EditText dateEditText;
    private ListView programListView;
    private ArrayAdapter<String> adapter;
    private ApiService apiService;
    private List<AreaData> areaDataList = new ArrayList<>();
    private List<TVStation> tvStations = new ArrayList<>();
    private List<TVChannel> tvChannels = new ArrayList<>();
    private List<TVProgram> tvPrograms = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        apiService = RetrofitClient.getInstance(null).create(ApiService.class);
        channelEditText = findViewById(R.id.channelEditText);
        dateEditText = findViewById(R.id.dateEditText);
        programListView = findViewById(R.id.programListView);

        apiService.getAreaString().enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                areaDataList = parseAreaData(response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });


        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>());
        programListView.setAdapter(adapter);

        Button searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String channel = channelEditText.getText().toString();
                String date = dateEditText.getText().toString();

            }
        });
    }

    public List<AreaData> parseAreaData(String xmlData) {
        List<AreaData> areaDataList = new ArrayList<>();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new ByteArrayInputStream(xmlData.getBytes()));

            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("string");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Element element = (Element) nodeList.item(i);
                String[] values = element.getTextContent().split("@");

                AreaData areaData = new AreaData();
                areaData.setAreaId(Integer.parseInt(values[0]));
                areaData.setArea(values[1]);
                areaData.setZone(values[2]);

                areaDataList.add(areaData);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return areaDataList;
    }

    public List<TVStation> parseTVStation(String xmlData) {
        List<TVStation> tvStations = new ArrayList<>();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new ByteArrayInputStream(xmlData.getBytes()));

            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("string");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Element element = (Element) nodeList.item(i);
                String[] values = element.getTextContent().split("@");

                TVStation tvStation = new TVStation();
                tvStation.setTvStationId(Integer.parseInt(values[0]));
                tvStation.setTvStationName(values[1]);

                tvStations.add(tvStation);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return tvStations;
    }


}