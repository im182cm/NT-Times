/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.philip.nytimessearch.mvvm.model;

import com.philip.nytimessearch.mvvm.model.remote.response_model.ApiResponse;

import java.util.Objects;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

/**
 * A generic class that can provide a resource backed by both the sqlite database and the network.
 *
 * @param <ResultType>  output
 * @param <RequestType> input
 */
public abstract class NetworkBoundResource<ResultType, RequestType> {
    private final AppExecutors appExecutors;

    private final MediatorLiveData<Resource<ResultType>> result = new MediatorLiveData<>();

    /**
     * If should fetch from the server(for now it is always true), fetch.
     * If not get data from the datatbase.
     */
    @MainThread
    public NetworkBoundResource(AppExecutors appExecutors) {
        this.appExecutors = appExecutors;
        result.setValue((Resource<ResultType>) Resource.loading(null));
        if (shouldFetch()) {
            fetchFromNetwork();
        } else {
            // Not used for now, but leave this for future expansion
            final LiveData<ResultType> dbSource = loadFromDb();
            result.addSource(dbSource, new Observer<ResultType>() {
                @Override
                public void onChanged(@Nullable ResultType data) {
                    result.removeSource(dbSource);
                    setValue(Resource.success(data));
                }
            });
        }
    }

    @MainThread
    private void setValue(Resource<ResultType> newValue) {
        if (!Objects.equals(result.getValue(), newValue)) {
            result.setValue(newValue);
        }
    }

    /**
     * Fetch from the server and save the data into the database.
     */
    private void fetchFromNetwork() {
        final LiveData<ApiResponse<RequestType>> apiResponse = createCall();
        result.addSource(apiResponse, new Observer<ApiResponse<RequestType>>() {
            @Override
            public void onChanged(@Nullable final ApiResponse<RequestType> response) {
                result.removeSource(apiResponse);
                if (response.isSuccessful()) {
                    appExecutors.diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            saveCallResult(processResponse(response));
                        }
                    });
                    setValue(Resource.success(getResultType(response.body)));
                } else {
                    onFetchFailed();
                    setValue(Resource.error(response.errorMessage, null));
                }
            }
        });
    }

    private void onFetchFailed() {
    }

    public LiveData<Resource<ResultType>> asLiveData() {
        return result;
    }

    @WorkerThread
    private RequestType processResponse(ApiResponse<RequestType> response) {
        return response.body;
    }

    /**
     * Save data into database.
     */
    @WorkerThread
    protected abstract void saveCallResult(@NonNull RequestType item);

    /**
     * If this is true, then fetch from the server.
     */
    @MainThread
    protected abstract boolean shouldFetch();

    /**
     * Transform RequestType to ResultType
     */
    @MainThread
    protected abstract ResultType getResultType(RequestType requestType);

    /**
     * Load data from database
     */
    @NonNull
    @MainThread
    protected abstract LiveData<ResultType> loadFromDb();

    /**
     * Get Retrofit API to fetch.
     */
    @NonNull
    @MainThread
    protected abstract LiveData<ApiResponse<RequestType>> createCall();
}
