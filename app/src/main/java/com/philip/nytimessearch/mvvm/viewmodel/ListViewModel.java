package com.philip.nytimessearch.mvvm.viewmodel;

import com.philip.nytimessearch.mvvm.model.local.NYTimesSearchDatabase;
import com.philip.nytimessearch.mvvm.model.local.entity.DocEntity;
import com.philip.nytimessearch.mvvm.model.remote.ApiInterface;
import com.philip.nytimessearch.mvvm.model.remote.response_model.ApiResponse;
import com.philip.nytimessearch.mvvm.model.remote.response_model.ArticleSearchResponse;
import com.philip.nytimessearch.mvvm.model.remote.response_model.Meta;
import com.philip.nytimessearch.mvvm.model.remote.response_model.Response;
import com.philip.nytimessearch.mvvm.model.AppExecutors;
import com.philip.nytimessearch.mvvm.model.NetworkBoundResource;
import com.philip.nytimessearch.mvvm.model.Resource;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

/**
 * ViewModel for ListFragment
 */
public class ListViewModel extends BaseViewModel {
    private int page = 0;
    private String query;
    private boolean hasMoreArticles = false;

    @Inject
    public ListViewModel(ApiInterface apiInterface, NYTimesSearchDatabase database, AppExecutors appExecutors) {
        super(apiInterface, database, appExecutors);
    }

    public void setQuery(String query) {
        this.query = query;
        this.page = 0;
        hasMoreArticles = false;
    }

    public void setFetchFailed() {
        page--;
    }

    public boolean hasMoreArticles() {
        return hasMoreArticles;
    }

    public LiveData<Resource<List<DocEntity>>> fetchArticleSearch() {
        return new NetworkBoundResource<List<DocEntity>, ArticleSearchResponse>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull ArticleSearchResponse item) {
                Response response = item.getResponse();
                if (response.getDocs().size() > 0) {
                    database.docDao().insertCompanyTickers(item.getResponse().getDocs());
                }
            }

            @Override
            protected boolean shouldFetch() {
                return true;
            }

            @Override
            protected List<DocEntity> getResultType(ArticleSearchResponse articleSearchResponse) {
                Meta meta = articleSearchResponse.getResponse().getMeta();
                hasMoreArticles = meta.getHits() > meta.getOffset();

                return articleSearchResponse.getResponse().getDocs();
            }

            @NonNull
            @Override
            protected LiveData<List<DocEntity>> loadFromDb() {
                return database.docDao().queryDocList();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<ArticleSearchResponse>> createCall() {
                return apiInterface.getArticles(query, ++page);
            }
        }.asLiveData();
    }

}
