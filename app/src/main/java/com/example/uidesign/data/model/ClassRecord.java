package com.example.uidesign.data.model;

public class ClassRecord {
    private int id;
    private String className;
    private String originText;
    private String summary;
    private String date;
    public ClassRecord(int id, String className,String originText, String summary, String date) {
        this.id = id;
        this.className = className;
        this.originText = originText;
        this.summary = summary;
        this.date = date;
    }
    public int getID() {
        return id;
    }
    public String getClassName() {
        return className;
    }
    public String getOriginText() {
        return originText;
    }
    public String getSummary() {
        return summary;
    }
    public String getDate() {
        return date;
    }
}
