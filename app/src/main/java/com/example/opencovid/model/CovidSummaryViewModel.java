package com.example.opencovid.model;

import android.provider.Settings;
import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

//not a view model yet, just a pojo at this point
public class CovidSummaryViewModel {


    public GlobalSummary getGlobalSummary() {
        return globalSummary;
    }

    public void setGlobalSummary(GlobalSummary globalSummary) {
        this.globalSummary = globalSummary;
    }

    public List<SingleCountrySummary> getCountrySummaryList() {
        return countrySummaryList;
    }

    public void setCountrySummaryList(List<SingleCountrySummary> countrySummaryList) {
        this.countrySummaryList = countrySummaryList;
    }

    @SerializedName("Global")
    @Expose
    GlobalSummary globalSummary = null;

    @SerializedName("Countries")
    @Expose
    private List<SingleCountrySummary> countrySummaryList = null;


    public SingleCountrySummary getItem(int position) {
        SingleCountrySummary retVal = null;

        if(countrySummaryList != null && !countrySummaryList.isEmpty()) {
            retVal = countrySummaryList.get(position);
            retVal.calculatedDeathRate = 100 * ((Double.valueOf(retVal.getTotalDeaths()))/(Double.valueOf(retVal.getTotalConfirmed())));
            retVal.onePercentTrueCases = Integer.valueOf(retVal.getTotalDeaths())/.01;
            retVal.twoPercentTrueCases = Integer.valueOf(retVal.getTotalDeaths())/.02;

        }
        else
            Log.d("CSVM::getItem() "," the list is empty");

        return retVal;

    }

    public class SingleCountrySummary{

        //calculated params

        public String getOnePercentTrueCases() {
            return String.valueOf(onePercentTrueCases);
        }

        public void setOnePercentTrueCases(int onePercentTrueCases) {
            this.onePercentTrueCases = onePercentTrueCases;
        }

        public String getTwoPercentTrueCases() {
            return String.valueOf(twoPercentTrueCases);
        }

        public void setTwoPercentTrueCases(int twoPercentTrueCases) {
            this.twoPercentTrueCases = twoPercentTrueCases;
        }

        public String getCalculatedDeathRate() {
            return String.valueOf(calculatedDeathRate).substring(0,4);
        }

        public void setCalculatedDeathRate(int calculatedDeathRate) {
            this.calculatedDeathRate = calculatedDeathRate;
        }

        double onePercentTrueCases = 0;
        double twoPercentTrueCases = 0;
        double calculatedDeathRate = 0;

        // api obtained params

        public String getCountry() {
            return Country;
        }

        public void setCountry(String country) {
            Country = country;
        }

        public String getSlug() {
            return Slug;
        }

        public void setSlug(String slug) {
            Slug = slug;
        }

        public String getNewConfirmed() {
            return NewConfirmed;
        }

        public void setNewConfirmed(String newConfirmed) {
            NewConfirmed = newConfirmed;
        }

        public String getTotalConfirmed() {
            return TotalConfirmed;
        }

        public void setTotalConfirmed(String totalConfirmed) {
            TotalConfirmed = totalConfirmed;
        }

        public String getNewDeaths() {
            return NewDeaths;
        }

        public void setNewDeaths(String newDeaths) {
            NewDeaths = newDeaths;
        }

        public String getTotalDeaths() {
            return TotalDeaths;
        }

        public void setTotalDeaths(String totalDeaths) {
            TotalDeaths = totalDeaths;
        }

        public String getNewRecovered() {
            return NewRecovered;
        }

        public void setNewRecovered(String newRecovered) {
            NewRecovered = newRecovered;
        }

        public String getTotalRecovered() {
            return TotalRecovered;
        }

        public void setTotalRecovered(String totalRecovered) {
            TotalRecovered = totalRecovered;
        }

        public String getDate() { return Date;  }

        public void setDate(String date) { Date = date; }

        @SerializedName("Country")
        String Country;
        @SerializedName("Slug")
        String Slug;
        @SerializedName("NewConfirmed")
        String NewConfirmed;
        @SerializedName("TotalConfirmed")
        String TotalConfirmed;
        @SerializedName("NewDeaths")
        String NewDeaths;
        @SerializedName("TotalDeaths")
        String TotalDeaths;
        @SerializedName("NewRecovered")
        String NewRecovered;
        @SerializedName("TotalRecovered")
        String TotalRecovered;
        @SerializedName("Date")
        String Date;
    }

    public class GlobalSummary{


        public String getNewConfirmed() {
            return NewConfirmed;
        }

        public void setNewConfirmed(String newConfirmed) {
            NewConfirmed = newConfirmed;
        }

        public String getTotalConfirmed() {
            return TotalConfirmed;
        }

        public void setTotalConfirmed(String totalConfirmed) {
            TotalConfirmed = totalConfirmed;
        }

        public String getNewDeaths() {
            return NewDeaths;
        }

        public void setNewDeaths(String newDeaths) {
            NewDeaths = newDeaths;
        }

        public String getTotalDeaths() {
            return TotalDeaths;
        }

        public void setTotalDeaths(String totalDeaths) {
            TotalDeaths = totalDeaths;
        }

        public String getNewRecovered() {
            return NewRecovered;
        }

        public void setNewRecovered(String newRecovered) {
            NewRecovered = newRecovered;
        }

        public String getTotalRecovered() {
            return TotalRecovered;
        }

        public void setTotalRecovered(String totalRecovered) {
            TotalRecovered = totalRecovered;
        }


        @SerializedName("NewConfirmed")
        String NewConfirmed;
        @SerializedName("TotalConfirmed")
        String TotalConfirmed;
        @SerializedName("NewDeaths")
        String NewDeaths;
        @SerializedName("TotalDeaths")
        String TotalDeaths;
        @SerializedName("NewRecovered")
        String NewRecovered;
        @SerializedName("TotalRecovered")
        String TotalRecovered;


    }
}
