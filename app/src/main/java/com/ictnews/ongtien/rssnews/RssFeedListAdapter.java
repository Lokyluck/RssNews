package com.ictnews.ongtien.rssnews;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by TNT on 11/6/2017.
 *
 */

class RssFeedListAdapter extends RecyclerView.Adapter<RssFeedListAdapter.FeedModelViewHolder>
{
    private List<RssFeedModel> mFeedModelList;
    private Activity mActivity;

    RssFeedListAdapter(List<RssFeedModel> rssFeedModels, Activity activity) {
        mFeedModelList = rssFeedModels;
        mActivity = activity;
    }

    class FeedModelViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private View rssFeedView;
        private FeedModelViewHolder(View view) {
            super(view);
            rssFeedView = view;
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            RssFeedModel article = mFeedModelList.get(adapterPosition);
            String articleStr = article.Link;
            Intent intent = new Intent(mActivity, WebViewActivity.class);
            intent.putExtra("ArticleStr", articleStr);
            intent.setType("text/plain");
            mActivity.startActivity(intent);
        }
    }


    @Override
    public int getItemViewType(int position) {
        if(position == 0)
            return 0;
        else return 1;
    }

    @Override
    public FeedModelViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        switch (type) {
            case 0:
                View v0 = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.first_item_in_recycleview, parent, false);
                return new FeedModelViewHolder(v0);
            default:
                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_rss_feed, parent, false);
                return new FeedModelViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(FeedModelViewHolder holder, int position) {
        final RssFeedModel rssFeedModel = mFeedModelList.get(position);
        switch (holder.getItemViewType()) {
            case 0:
                ((TextView) holder.rssFeedView.findViewById(R.id.titleText)).setText(rssFeedModel.Title);
                //set ImageView of an Article
                ImageView imageView0 = ((ImageView) holder.rssFeedView.findViewById(R.id.imageView));
                Glide.with(mActivity).load(rssFeedModel.ThumbnailUrl).into(imageView0);
                break;

            default:
                ((TextView) holder.rssFeedView.findViewById(R.id.titleText)).setText(rssFeedModel.Title);
                ((TextView) holder.rssFeedView.findViewById(R.id.descriptionText))
                        .setText(rssFeedModel.Description);
                //set ImageView of an Article
                ImageView imageView = ((ImageView) holder.rssFeedView.findViewById(R.id.imageView));
                Glide.with(mActivity).load(rssFeedModel.ThumbnailUrl).thumbnail(0.1f).into(imageView);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mFeedModelList.size();
    }

    void setActivity(Activity activity){
        mActivity = activity;
    }
}