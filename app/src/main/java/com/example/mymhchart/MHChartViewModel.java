package com.example.mymhchart;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.util.Objects;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MHChartViewModel extends ViewModel {
    private static final String TAG = "mhChartViewModel";
    private static final String DETAILBASESTRING = "https://hq.sinajs.cn/list=sz";
    private static final String DATABASESTRING = "https://data.gtimg.cn/flashdata/hushen/minute/sz";
    private static final String DATABASEENDSTRING = ".js?maxage=110&0.28163905744440854";

    private MutableLiveData<String> mStockDetail;
    private MutableLiveData<String> mStockMinuteData;
    private MutableLiveData<Boolean> hasVisitInternetFail;
    private OkHttpClient mOkHttpClient;
    private String mStockDetailsUrlString;
    private String mStockDataUrlString;

    public MHChartViewModel() {
        mOkHttpClient = new OkHttpClient();
        mStockDetail = new MutableLiveData<>();
        mStockMinuteData = new MutableLiveData<>();
        hasVisitInternetFail = new MutableLiveData<>();
    }

    void loadData(String url, MutableLiveData<String> location) {
        final Request request = new Request.Builder().url(url).build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                hasVisitInternetFail.postValue(true);
                Log.e(TAG, "onFailure: " + "Internet error!");
                Log.e(TAG, "onFailure: ", e.fillInStackTrace());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                location.postValue(Objects.requireNonNull(response.body()).string());
            }
        });
    }

    public MutableLiveData<String> getmStockDetail() {
        return mStockDetail;
    }

    public MutableLiveData<String> getmStockMinuteData() {
        return mStockMinuteData;
    }

    MutableLiveData<Boolean> getHasVisitInternetFail() {
        return hasVisitInternetFail;
    }

    public void refreshData(String code) {
        mStockDetailsUrlString = DETAILBASESTRING + code;
        mStockDataUrlString = DATABASESTRING + code + DATABASEENDSTRING;
        loadData(mStockDetailsUrlString, mStockDetail);
        loadData(mStockDataUrlString, mStockMinuteData);
    }
}
