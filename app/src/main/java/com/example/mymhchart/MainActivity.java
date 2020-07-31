package com.example.mymhchart;

import androidx.annotation.LongDef;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mymhchart.model.OneTimeData;
import com.example.mymhchart.model.dataDocker;
import com.example.mymhchart.ui.TimeSharingView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private MHChartViewModel mMHChartViewModel;
    private TextView titleTextView;
    private String mStockDetailString = "";
    private String mStockDataString = "";
    private String mStockCode = "";
    private Timer timer;
    private TimerTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        titleTextView = findViewById(R.id.stockName);
        TimeSharingView timeSharingView = findViewById(R.id.hmView);
        SearchView searchView = findViewById(R.id.seach_bar);
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if(TextUtils.isEmpty(s) || s.length()!= 6)
                {
                    Toast.makeText(MainActivity.this, "请输入正确的查找内容！", Toast.LENGTH_SHORT).show();
                }else {
                    mStockCode = s;
                    mMHChartViewModel.refreshData(mStockCode);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                    if (!mStockCode.equals("")) {
                        mMHChartViewModel.refreshData(mStockCode);
                        Log.d(TAG, "run: " + "定时器自动刷新了！");
                    }
            }
        };


        mMHChartViewModel = new ViewModelProvider(this).get(MHChartViewModel.class);
        mMHChartViewModel.getmStockDetail().observe(this, string -> {
            mStockDetailString = string;
            if (!mStockDataString.equals("")) {
                timeSharingView.refreshView(analyseData(mStockDataString, mStockDetailString));
                mStockDataString = "";
                mStockDetailString = "";
            }
        });
        mMHChartViewModel.getmStockMinuteData().observe(this, string -> {
            mStockDataString = string;
            if (!mStockDetailString.equals("")) {
                timeSharingView.refreshView(analyseData(mStockDataString, mStockDetailString));
                mStockDataString = "";
                mStockDetailString = "";
            }
        });
        mMHChartViewModel.getHasVisitInternetFail().observe(this, hasfail -> {
            Toast.makeText(this, "网络错误！！", Toast.LENGTH_LONG).show();
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        timer.schedule(task,60000,60000);
    }

    @Override
    protected void onStop() {
        super.onStop();
        timer.cancel();
    }

    private dataDocker analyseData(String mStockDataString, String mStockDetailString) {
        //设置标题
        //如果请求代码出错，将mStockCode置为“”；
        dataDocker temp;
        String data = mStockDataString.split("\"")[1];
        String detail = mStockDetailString.split("\"")[1];
        if (detail.isEmpty()) {
            mStockCode = "";
            titleTextView.setText("股票名称");
            temp = new dataDocker("----", "----", "----", "0.00", "----", "----", "----", 0f, new ArrayList<>());
            Toast.makeText(this,"请输入正确的股票代码！！", Toast.LENGTH_LONG).show();
        }else {

            String [] dataBuffer = data.split("\n");
            String [] detailBuffer = detail.split(",");
            titleTextView.setText(detailBuffer[0]);
            if (dataBuffer.length <= 3){
                String s = dataBuffer[2].split(" ")[1];
                temp = new dataDocker("----", s, "----", "0.00", "----", "----", "----", Float.valueOf(s), new ArrayList<>());
            }else {
                temp = new dataDocker();
                ArrayList<OneTimeData> t = new ArrayList<>();
                for (int i = 2; i < dataBuffer.length; i++) {
                    if( i < 4) {
                        t.add(new OneTimeData(Float.parseFloat(dataBuffer[i].split(" ")[1]),
                                Float.parseFloat(dataBuffer[i].split(" ")[1])-0.5f,
                                Float.parseFloat(dataBuffer[i].split(" ")[1])/2));
                    }else {
                        String s1 = dataBuffer[i-1].split(" ")[2];
                        String s2 = dataBuffer[i-2].split(" ")[2];
                        t.add(new OneTimeData(Float.parseFloat(dataBuffer[i].split(" ")[1]),
                                Float.parseFloat(dataBuffer[i].split(" ")[1])-0.5f,
                                Integer.parseInt(s1.substring(0,s1.length()-3)) - Integer.parseInt(s2.substring(0,s2.length()-3))));
                    }

                    Log.d(TAG, "databuffer: " + dataBuffer[i]);
                }
                temp.setOneTimeData(t);
                float nowPrice = t.get(t.size()-1).getmPrice();
                temp.setDetailTime(dataBuffer[dataBuffer.length-1].split(" ")[0]);
                temp.setCurrentPrice(String.valueOf(nowPrice));
                temp.setAveragePrice(String.valueOf(t.get(t.size()-1).getmEveragePrice()));
                temp.setUpAndDown(String.valueOf(keepPointAfterTwo(nowPrice/Float.parseFloat(detailBuffer[2]))));
                temp.setYestodaysClose(Float.parseFloat(detailBuffer[2]));
                temp.setAmountOfInCrease("----%");
                temp.setSize(String.valueOf(t.get(t.size()-1).getmStrength()));
                temp.setMoney(String.valueOf(nowPrice*t.get(t.size()-1).getmStrength()));
            }
        }

        return temp;
    }
    private float keepPointAfterTwo(double n) {
        return Math.round(n*100)/100f;
    }
}