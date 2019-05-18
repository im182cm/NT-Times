package com.philip.nytimessearch.mvvm.viewmodel;

import com.philip.nytimessearch.mvvm.model.local.NYTimesSearchDatabase;
import com.philip.nytimessearch.mvvm.model.remote.ApiInterface;
import com.philip.nytimessearch.mvvm.model.AppExecutors;

import androidx.lifecycle.ViewModel;

/**
 * Base ViewModel
 */
public class BaseViewModel extends ViewModel {
    ApiInterface apiInterface;
    NYTimesSearchDatabase database;
    AppExecutors appExecutors;

    public BaseViewModel(ApiInterface apiInterface, NYTimesSearchDatabase database, AppExecutors appExecutors) {
        this.apiInterface = apiInterface;
        this.database = database;
        this.appExecutors = appExecutors;
    }
}
