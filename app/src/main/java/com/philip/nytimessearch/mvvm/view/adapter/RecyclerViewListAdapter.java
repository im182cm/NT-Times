package com.philip.nytimessearch.mvvm.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.philip.nytimessearch.R;
import com.philip.nytimessearch.mvvm.model.local.entity.DocEntity;
import com.philip.nytimessearch.mvvm.view.listener.RecyclerViewOnClickListener;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewListAdapter extends RecyclerView.Adapter<RecyclerViewListAdapter.CustomViewHolder> {
    private final int VIEW_TYPE_ITEM = 1;
    private final int VIEW_TYPE_LOADING = 2;

    private final RequestManager requestManager;
    private final RecyclerViewOnClickListener listener;

    private List<DocEntity> list;

    public RecyclerViewListAdapter(List<DocEntity> list, RequestManager requestManager, RecyclerViewOnClickListener listener) {
        this.list = list;
        this.requestManager = requestManager;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_list_item, parent, false);
            return new ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_list_item_progress_bar, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            DocEntity item = list.get(position);
            requestManager.load(item.getThumbnail() == null ? R.drawable.no_image : item.getThumbnail())
                    .placeholder(R.drawable.no_image).
                    into(((ItemViewHolder) holder).imageView);
            ((ItemViewHolder) holder).textView.setText(item.getHeadline().getMain());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position) != null) {
            return VIEW_TYPE_ITEM;
        } else {
            return VIEW_TYPE_LOADING;
        }
    }

    /**
     * Add null data for loading
     */
    public void addNullData() {
        list.add(null);
        notifyItemInserted(list.size() - 1);
    }

    /**
     * Remove loading
     */
    public void removeNull() {
        list.remove(list.size() - 1);
        notifyItemRemoved(list.size());
    }

    public void addData(List<DocEntity> list) {
        int oldItemCount = this.list.size();
        this.list.addAll(list);
        notifyItemRangeInserted(oldItemCount, list.size());
    }

    public void clearData() {
        list.clear();
        notifyDataSetChanged();
    }

    class ItemViewHolder extends CustomViewHolder {
        private ImageView imageView;
        private TextView textView;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.list_item_image_view);
            textView = itemView.findViewById(R.id.list_item_headline_text_view);

            itemView.setOnClickListener(v -> listener.onClick(list.get(getAdapterPosition()).getWebUrl()));
        }
    }

    class LoadingViewHolder extends CustomViewHolder {
        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    /**
     * Common base class for ViewHolder.
     */
    class CustomViewHolder extends RecyclerView.ViewHolder {
        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
