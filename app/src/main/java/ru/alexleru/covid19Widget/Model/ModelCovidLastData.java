package ru.alexleru.covid19Widget.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelCovidLastData {
    @SerializedName("country")
    @Expose
    public String country;

    @SerializedName("date")
    @Expose
    public String date;

    @SerializedName("sumC")
    @Expose
    public int sumC;

    @SerializedName("sumR")
    @Expose
    public int sumR;

    @SerializedName("sumC_change")
    @Expose
    public int sumC_change;

    @SerializedName("sumR_change")
    @Expose
    public int sumR_change;
}
