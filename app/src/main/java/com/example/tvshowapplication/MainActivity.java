package com.example.tvshowapplication;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tvshowapplication.adapter.MyTreeListViewAdapter;
import com.example.tvshowapplication.entity.AreaData;
import com.example.tvshowapplication.entity.TVChannel;
import com.example.tvshowapplication.entity.TVProgram;
import com.example.tvshowapplication.entity.TVStation;
import com.example.tvshowapplication.vo.MyNodeVo;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
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

    private String selectedDate;//选择的日期
    public Integer tvChannelId; //选择的频道id

    private final List<MyNodeVo> mDatas = new ArrayList<>();
    private AlertDialog mAlert;
    private MyTreeListViewAdapter<MyNodeVo> myTreeListViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("电视节目查询");
        apiService = RetrofitClient.getInstance(null).create(ApiService.class);
        channelEditText = findViewById(R.id.channelEditText);
        dateEditText = findViewById(R.id.dateEditText);
        programListView = findViewById(R.id.programListView);

        //点击选择日期
        dateEditText.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        MainActivity.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.setAccentColor(R.color.teal_700);
                dpd.show(getSupportFragmentManager(), "选择日期");
            }
        });
        apiService.getAreaString().enqueue(new Callback<String>() {//查询所有地区和分类电视
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                areaDataList = parseAreaData(response.body());
                for (AreaData areaData : areaDataList) {
                    mDatas.add(new MyNodeVo(areaData.getAreaId(), 0, areaData.getArea()));
                    apiService.getTVstationString(areaData.getAreaId()).enqueue(new Callback<String>() {//根据地区id查询电视台
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            tvStations = parseTVStation(response.body());
                            for (TVStation tvStation : tvStations
                            ) {
                                mDatas.add(new MyNodeVo(tvStation.getTvStationId(), areaData.getAreaId(), tvStation.getTvStationName()));
                                apiService.getTVchannelString(tvStation.getTvStationId()).enqueue(new Callback<String>() {//根据电视台id查询所有频道信息
                                    @Override
                                    public void onResponse(Call<String> call, Response<String> response) {
                                        tvChannels = parseTVChannel(response.body());
                                        for (TVChannel tvChannel : tvChannels
                                        ) {
                                            mDatas.add(new MyNodeVo(tvChannel.getTvChannelId(), tvStation.getTvStationId(), tvChannel.getTvChannel()));
                                        }

                                    }

                                    @Override
                                    public void onFailure(Call<String> call, Throwable t) {
                                        Log.e("电视频道查询出错啦！！", t.getMessage());
                                    }
                                });
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Log.e("电视台查询出错啦！！", t.getMessage());
                        }
                    });
                }
//                initTreeListView();
                // 从布局文件中加载 AlertDialog 需要显示的 view

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("地区查询出错啦！！", t.getMessage());

            }
        });
        // 弹出自定义对话框
//        channelEditText.setOnClickListener(v -> mAlert.show());
        channelEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获取地区信息
                LayoutInflater inflater = MainActivity.this.getLayoutInflater();
                View customView = inflater.inflate(R.layout.view_tree, null, false);

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                // 指定 AlertDialog 需要显示的 view
                builder.setView(customView);
                // 点击空白处是否自动隐藏对话框（默认值为 true）
                builder.setCancelable(true);
                // 创建 AlertDialog 对象
                mAlert = builder.create();
                mAlert.show();

                // 设置自定义 view 中的显示内容
                ListView treeLv = customView.findViewById(R.id.tree_lv);
                try {
                    myTreeListViewAdapter = new MyTreeListViewAdapter<>(treeLv, MainActivity.this,
                            mDatas, 0, true);

                    myTreeListViewAdapter.setOnTreeNodeClickListener((node, position) -> {
                        if (node.isLeaf()) {
                            channelEditText.setText(node.getName());
                            mAlert.dismiss();
                        }
                    });
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    e.printStackTrace();
                }
                treeLv.setAdapter(adapter);

                // 自定义 view 中的关闭按钮的点击事件
                customView.findViewById(R.id.cancel).setOnClickListener(v -> mAlert.dismiss());
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

    //解析获取到的地区信息
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

    //解析获取到的电视台信息
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

    //解析获取到的频道信息
    public List<TVChannel> parseTVChannel(String xmlData) {
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

                TVChannel tvChannel = new TVChannel();
                tvChannel.setTvChannelId(Integer.parseInt(values[0]));
                tvChannel.setTvChannel(values[1]);

                tvChannels.add(tvChannel);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return tvChannels;
    }

    private void initTreeListView() {
        // 从布局文件中加载 AlertDialog 需要显示的 view
        LayoutInflater inflater = this.getLayoutInflater();
        View customView = inflater.inflate(R.layout.view_tree, null, false);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // 指定 AlertDialog 需要显示的 view
        builder.setView(customView);
        // 点击空白处是否自动隐藏对话框（默认值为 true）
        builder.setCancelable(true);
        // 创建 AlertDialog 对象
        mAlert = builder.create();


        // 设置自定义 view 中的显示内容
        ListView treeLv = customView.findViewById(R.id.tree_lv);
        try {
            myTreeListViewAdapter = new MyTreeListViewAdapter<>(treeLv, this,
                    mDatas, 0, true);

            myTreeListViewAdapter.setOnTreeNodeClickListener((node, position) -> {
                if (node.isLeaf()) {
                    channelEditText.setText(node.getName());
                    mAlert.dismiss();
                }
            });
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
        treeLv.setAdapter(adapter);

        // 自定义 view 中的关闭按钮的点击事件
        customView.findViewById(R.id.cancel).setOnClickListener(v -> mAlert.dismiss());
    }

    private void initDatas() {
        //获取地区信息
        apiService.getAreaString().enqueue(new Callback<String>() {//查询所有地区和分类电视
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                areaDataList = parseAreaData(response.body());
                for (AreaData areaData : areaDataList) {
                    mDatas.add(new MyNodeVo(areaData.getAreaId(), 0, areaData.getArea()));
                    apiService.getTVstationString(areaData.getAreaId()).enqueue(new Callback<String>() {//根据地区id查询电视台
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            tvStations = parseTVStation(response.body());
                            for (TVStation tvStation : tvStations
                            ) {
                                mDatas.add(new MyNodeVo(tvStation.getTvStationId(), areaData.getAreaId(), tvStation.getTvStationName()));
                                apiService.getTVchannelString(tvStation.getTvStationId()).enqueue(new Callback<String>() {//根据电视台id查询所有频道信息
                                    @Override
                                    public void onResponse(Call<String> call, Response<String> response) {
                                        tvChannels = parseTVChannel(response.body());
                                        for (TVChannel tvChannel : tvChannels
                                        ) {
                                            mDatas.add(new MyNodeVo(tvChannel.getTvChannelId(), tvStation.getTvStationId(), tvChannel.getTvChannel()));
                                        }

                                    }

                                    @Override
                                    public void onFailure(Call<String> call, Throwable t) {
                                        Log.e("电视频道查询出错啦！！", t.getMessage());
                                    }
                                });
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Log.e("电视台查询出错啦！！", t.getMessage());
                        }
                    });
                }
//                initTreeListView();
                // 从布局文件中加载 AlertDialog 需要显示的 view
                LayoutInflater inflater = MainActivity.this.getLayoutInflater();
                View customView = inflater.inflate(R.layout.view_tree, null, false);

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                // 指定 AlertDialog 需要显示的 view
                builder.setView(customView);
                // 点击空白处是否自动隐藏对话框（默认值为 true）
                builder.setCancelable(true);
                // 创建 AlertDialog 对象
                mAlert = builder.create();
                mAlert.show();


                // 设置自定义 view 中的显示内容
                ListView treeLv = customView.findViewById(R.id.tree_lv);
                try {
                    myTreeListViewAdapter = new MyTreeListViewAdapter<>(treeLv, MainActivity.this,
                            mDatas, 0, true);

                    myTreeListViewAdapter.setOnTreeNodeClickListener((node, position) -> {
                        if (node.isLeaf()) {
                            channelEditText.setText(node.getName());
                            mAlert.dismiss();
                        }
                    });
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    e.printStackTrace();
                }
                treeLv.setAdapter(adapter);

                // 自定义 view 中的关闭按钮的点击事件
                customView.findViewById(R.id.cancel).setOnClickListener(v -> mAlert.dismiss());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("地区查询出错啦！！", t.getMessage());

            }
        });
//
    }


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, monthOfYear, dayOfMonth);
        selectedDate = sdf.format(calendar.getTime());
        Log.e("选择的日期如下：", selectedDate.toString());
        dateEditText.setText(selectedDate.toString());
    }
}