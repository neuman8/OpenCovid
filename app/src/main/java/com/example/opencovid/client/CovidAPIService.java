package com.example.opencovid.client;

import com.example.opencovid.model.CovidCountriesViewModel;
import com.example.opencovid.model.CovidSummaryViewModel;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface CovidAPIService {

    //TO CONVERT RXJAVA TO LIVEDATA WE CAN ONLY USE FLOWABLE OR FLATMAPSINGLE
    @GET("summary")
    Observable<CovidSummaryViewModel> getCovidSummaryModel();

    @GET("countries")
    Observable<CovidCountriesViewModel> getCovidCountriesModel();

}
