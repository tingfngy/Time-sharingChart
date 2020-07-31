package com.example.mymhchart.model;

import java.util.ArrayList;

public class dataDocker {

    private String DetailTime = "0903";
    private String CurrentPrice = "150.30";
    private String AveragePrice = "137.56";
    private String UpAndDown = "1.01";
    private String AmountOfInCrease = "-0.73%";
    private String Size = "1675";
    private String Money = "1933万";
    private float YestodaysClose = 145.99f;//要求保留小数点后两位
    private ArrayList<com.example.mymhchart.model.OneTimeData> OneTimeData;

    public dataDocker(String detailTime, String currentPrice, String averagePrice, String upAndDown, String amountOfInCrease, String size, String money, float yestodaysClose, ArrayList<OneTimeData> oneTimeData) {
        DetailTime = detailTime;
        CurrentPrice = currentPrice;
        AveragePrice = averagePrice;
        UpAndDown = upAndDown;
        AmountOfInCrease = amountOfInCrease;
        Size = size;
        Money = money;
        YestodaysClose = yestodaysClose;
        OneTimeData = oneTimeData;
    }

    public dataDocker() {

    }

    public String getDetailTime() {
        return DetailTime;
    }

    public void setDetailTime(String detailTime) {
        DetailTime = detailTime;
    }

    public String getCurrentPrice() {
        return CurrentPrice;
    }

    public void setCurrentPrice(String currentPrice) {
        CurrentPrice = currentPrice;
    }

    public String getAveragePrice() {
        return AveragePrice;
    }

    public void setAveragePrice(String averagePrice) {
        AveragePrice = averagePrice;
    }

    public String getUpAndDown() {
        return UpAndDown;
    }

    public void setUpAndDown(String upAndDown) {
        UpAndDown = upAndDown;
    }

    public String getAmountOfInCrease() {
        return AmountOfInCrease;
    }

    public void setAmountOfInCrease(String amountOfInCrease) {
        AmountOfInCrease = amountOfInCrease;
    }

    public String getSize() {
        return Size;
    }

    public void setSize(String size) {
        Size = size;
    }

    public String getMoney() {
        return Money;
    }

    public void setMoney(String money) {
        Money = money;
    }

    public float getYestodaysClose() {
        return YestodaysClose;
    }

    public void setYestodaysClose(float yestodaysClose) {
        YestodaysClose = yestodaysClose;
    }

    public ArrayList<com.example.mymhchart.model.OneTimeData> getOneTimeData() {
        return OneTimeData;
    }

    public void setOneTimeData(ArrayList<com.example.mymhchart.model.OneTimeData> oneTimeData) {
        OneTimeData = oneTimeData;
    }
}
