package com.example.opencovid;


import android.Manifest;
import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.opencovid.databinding.FragmentCovidBinding;

import com.example.opencovid.client.CovidAPIService;
import com.example.opencovid.model.Covid;
import com.example.opencovid.model.CovidSummaryViewModel;
import com.example.opencovid.client.RetrofitClient;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class CovidFragment extends Fragment {


    private FragmentCovidBinding covidBinding;
    private RxPermissions rxPermissions;

    CovidSummaryViewModel summaryModel;
    Covid covid;

    public CovidFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        covidBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_covid, container, false);

        rxPermissions = new RxPermissions(CovidFragment.this);
        covid = new Covid(this.getContext());

        return covidBinding.getRoot();
    }

    @SuppressLint("CheckResult")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rxPermissions.request(Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe(granted -> {
                    if (granted) {
                        getCovidData();
                        Log.d("CovidFragment", "Permission is granted");
                    } else {
                        Log.d("CovidFragment", "Permission is not granted");
                    }
                });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    private void getCovidData() {
        covidBinding.progress.setVisibility(View.VISIBLE);

        CovidAPIService covidApiService = RetrofitClient.getApiService();

        Observable<CovidSummaryViewModel> covidSummaryModel = covidApiService.getCovidSummaryModel()
                .subscribeOn(Schedulers.io())//Will run on background
                .observeOn(AndroidSchedulers.mainThread());//Will run on main thread

        covidSummaryModel.subscribe(new Observer<CovidSummaryViewModel>() {
            @Override
            public void onSubscribe(Disposable d) {
                Disposable dis = d;
            }

            @Override
            public void onNext(CovidSummaryViewModel covidResponse) {
                summaryModel = covidResponse;
                findMyCountryStats();
            }

            @Override
            public void onError(Throwable e) {
                showToast();
                covidBinding.progress.setVisibility(View.GONE);
            }

            @Override
            public void onComplete() {
                covidBinding.progress.setVisibility(View.GONE);
            }
        });

    }

    public void findMyCountryStats()
    {
        if(covid.getCountry() != null && !covid.getCountry().isEmpty()) {

            List<CovidSummaryViewModel.SingleCountrySummary> countryList = summaryModel.getCountrySummaryList();
            for(int i = 0; i < countryList.size(); i++)
            {
                CovidSummaryViewModel.SingleCountrySummary singleCountry = countryList.get(i);
                if (singleCountry.getCountry().contains(covid.getCountry())) {
                    Log.d("CovidFragment", "FOUND IT, my country: " + covid.getCountry() + " response country: " + singleCountry.getCountry() + " index: " + i);
                    covid.setCountryIndex(i);
                }
            }
        }
        else {
            if(summaryModel.getGlobalSummary() != null)
            {
                Log.d("CovidFragment", "Could not get location, will try to get Global Data here");

            }
            showToast();
        }
        //letting the data binder know we are done with the network data
        covidBinding.setCovid(covid);
        covidBinding.setCovidSummary(summaryModel);
        covidBinding.progress.setVisibility(View.GONE);

    }

    public void showToast()
    {
        Toast.makeText(this.getContext(), R.string.error_toast, Toast.LENGTH_LONG).show();
    }

}

//Sample JSON response from the /summary endpoint
//{"Global":
// {"NewConfirmed":106598,"TotalConfirmed":1252421,"NewDeaths":5972,"TotalDeaths":67572,"NewRecovered":11066,"TotalRecovered":241599},
// "Countries":[
// {"Country":"ALA Aland Islands","CountryCode":"AX","Slug":"ala-aland-islands","NewConfirmed":0,"TotalConfirmed":0,"NewDeaths":0,"TotalDeaths":0,"NewRecovered":0,"TotalRecovered":0,"Date":"2020-04-06T03:20:30Z"},
// {"Country":"United States of America","CountryCode":"US","Slug":"united-states","NewConfirmed":61483,"TotalConfirmed":337065,"NewDeaths":2532,"TotalDeaths":9619,"NewRecovered":0,"TotalRecovered":0,"Date":"2020-04-06T03:20:30Z"}
// ]}

