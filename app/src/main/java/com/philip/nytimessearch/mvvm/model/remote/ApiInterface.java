package com.philip.nytimessearch.mvvm.model.remote;

import com.philip.nytimessearch.mvvm.model.remote.response_model.ApiResponse;
import com.philip.nytimessearch.mvvm.model.remote.response_model.ArticleSearchResponse;

import androidx.lifecycle.LiveData;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Retrofit API end points
 */
public interface ApiInterface {
    String baseUrl = "https://api.nytimes.com/svc/search/v2/";

    @GET("articlesearch.json")
    LiveData<ApiResponse<ArticleSearchResponse>> getArticles(@Query("q") String query, @Query("page") int page);
}
