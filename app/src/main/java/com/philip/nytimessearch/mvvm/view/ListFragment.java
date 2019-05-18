package com.philip.nytimessearch.mvvm.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.philip.nytimessearch.R;
import com.philip.nytimessearch.mvvm.model.local.entity.DocEntity;
import com.philip.nytimessearch.mvvm.model.Resource;
import com.philip.nytimessearch.mvvm.view.adapter.RecyclerViewListAdapter;
import com.philip.nytimessearch.mvvm.view.listener.PaginationScrollListener;
import com.philip.nytimessearch.mvvm.view.listener.RecyclerViewOnClickListener;
import com.philip.nytimessearch.mvvm.viewmodel.ListViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Showing article search results with RecyclerView.
 */
public class ListFragment extends BaseFragment<ListViewModel> implements RecyclerViewOnClickListener, PaginationScrollListener.OnLoadMoreListener {
    private RecyclerViewListAdapter adapter;
    private PaginationScrollListener listener;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.list_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        listener = new PaginationScrollListener(layoutManager, this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation());
        adapter = new RecyclerViewListAdapter(new ArrayList<>(), Glide.with(this), this);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.addOnScrollListener(listener);
    }

    /**
     * When query is submitted from MainActivity, fetch data from the server.
     */
    public void querySubmit(String query) {
        adapter.clearData();
        listener.setHasMoreItems(true);
        viewModel.setQuery(query);
        loadData();
    }

    @Override
    public void onClick(String url) {
        startActivity(DetailActivity.getCallingIntent(getActivity(), url));
    }

    @Override
    public void onLoadMore() {
        loadData();
    }

    private void loadData() {
        listener.setLoading(true);
        adapter.addNullData();
        viewModel.fetchArticleSearch().observe(this, new Observer<Resource<List<DocEntity>>>() {
            @Override
            public void onChanged(Resource<List<DocEntity>> listResource) {
                if (listResource.getStatus() == Resource.Status.LOADING)
                    return;

                adapter.removeNull();

                if (listResource.getStatus() == Resource.Status.SUCCESS) {
                    adapter.addData(listResource.getData());
                    if (!viewModel.hasMoreArticles()) {
                        listener.setHasMoreItems(false);
                    }
                } else {
                    Toast.makeText(getContext(), listResource.getMessage(), Toast.LENGTH_SHORT).show();
                    viewModel.setFetchFailed();
                }
                listener.setLoading(false);
            }
        });
    }
}
