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

package com.philip.nytimessearch.di.module;

import com.philip.nytimessearch.NYTimesSearchApplication;
import com.philip.nytimessearch.mvvm.model.local.NYTimesSearchDatabase;
import com.philip.nytimessearch.mvvm.model.local.dao.DocDao;
import com.philip.nytimessearch.mvvm.model.remote.ApiInterface;
import com.philip.nytimessearch.mvvm.model.remote.LiveDataCallAdapterFactory;

import java.io.IOException;

import javax.inject.Singleton;

import androidx.room.Room;
import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module(includes = ViewModelModule.class)
public class AppModule {
    @Singleton
    @Provides
    ApiInterface provideApiService() {
        return new Retrofit.Builder()
                .baseUrl(ApiInterface.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .client(createOkHttpClient())
                .build()
                .create(ApiInterface.class);
    }

    private static OkHttpClient createOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(interceptor);
        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                HttpUrl originalHttpUrl = original.url();

                HttpUrl url = originalHttpUrl.newBuilder()
                        .addQueryParameter("api-key", "d31fe793adf546658bd67e2b6a7fd11a")
                        .build();

                // Request customization: add request headers
                Request.Builder requestBuilder = original.newBuilder()
                        .url(url);

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });
        return builder.build();
    }

    @Singleton
    @Provides
    NYTimesSearchDatabase provideDb(NYTimesSearchApplication app) {
        return Room.databaseBuilder(app, NYTimesSearchDatabase.class, "ny_times_search_database.db").build();
    }

    @Singleton
    @Provides
    DocDao provideDocDao(NYTimesSearchDatabase db) {
        return db.docDao();
    }
}
