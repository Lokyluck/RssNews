package com.ictnews.ongtien.rssnews;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

/**
 * Created by TNT on 11/6/2017.
 *
 */

public class PagerFragment extends Fragment {
    private String UrlLink;
    SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView connectStatus_TextView;
    private RecyclerView mRecycleView;
    private Activity mActivity;
    private List<RssFeedModel> mFeedModelList;
    // Store instance variables


    public static PagerFragment newInstance(String url){
        PagerFragment mPagerFragment = new PagerFragment();
        Bundle args = new Bundle();
        args.putString("url", url);
        mPagerFragment.setArguments(args);
        return mPagerFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        UrlLink = getArguments().getString("url");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.pager_fragment, container, false);
        //variables
        mRecycleView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        mRecycleView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        connectStatus_TextView = (TextView) rootView.findViewById(R.id.connect_status_text);

        //check Network connection
        if (Utils.IsConnected(mActivity)){
            connectStatus_TextView.setVisibility(GONE);
            new FetchFeedTask().execute();
        }
        else
            showTextDisconnect();

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Utils.IsConnected(mActivity))
                {
                    connectStatus_TextView.setVisibility(GONE);
                    new FetchFeedTask().execute();
                }
                else {
                    showTextDisconnect();
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        });

        return rootView;
    }



    //method show text "no internet connection"
    private void showTextDisconnect(){
        connectStatus_TextView.setVisibility(View.VISIBLE);
        connectStatus_TextView.setText(R.string.noInternetConnection);
    }

    class FetchFeedTask extends AsyncTask<Void, Void, Boolean> {

        FetchFeedTask(){
        }

        @Override
        protected void onPreExecute() {
            mSwipeRefreshLayout.setRefreshing(true);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            if (TextUtils.isEmpty(UrlLink)){
                return false;
            }
            HttpURLConnection urlConnection;
            try {
                if (!UrlLink.startsWith("http://") && !UrlLink.startsWith("https://"))
                    UrlLink = "http://" + UrlLink;
                Log.i("TRY_CATCH", "UrlLink2 " + UrlLink);
                URL url = new URL(UrlLink);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream inputStream = url.openConnection().getInputStream();
                if (inputStream != null)
                    mFeedModelList = parseFeed(inputStream);
                return true;
            }
            catch (IOException | XmlPullParserException e){
                Log.e("DO_IN_BACK_GROUND", "Error", e);
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            mSwipeRefreshLayout.setRefreshing(false);
            if (success){
                RssFeedListAdapter adapter = new RssFeedListAdapter(mFeedModelList, mActivity);
                mRecycleView.setAdapter(adapter);
            }
            else{
                connectStatus_TextView.setVisibility(View.VISIBLE);
                connectStatus_TextView.setText(R.string.FailToLoadData);
            }
        }

        //method which parsing XML data and return List of item
        private List<RssFeedModel> parseFeed(InputStream inputstream)
                throws XmlPullParserException, IOException {
            //parse XML data
            Element ImageElement;
            String title = null;
            String link = null;
            String description = null;
            String thumbnailUrl = null;
            boolean isItem = false;
            int isItemFlag = 0;
            ArrayList<RssFeedModel> itemsList = new ArrayList<>();
            try{
                XmlPullParser xmlPullParser = Xml.newPullParser();
                xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                xmlPullParser.setInput(inputstream, null);
                xmlPullParser.nextTag();
                int eventType = 0;

                while (eventType != XmlPullParser.END_DOCUMENT)
                {
                    eventType = xmlPullParser.next();
                    String name = xmlPullParser.getName();
                    if (name == null)
                        continue;

                    if (eventType == XmlPullParser.END_TAG){
                        if (name.equalsIgnoreCase("item"))
                            isItem = false;
                        continue;
                    }

                    else if (eventType == XmlPullParser.START_TAG){
                        if (name.equalsIgnoreCase("item")) {
                            isItem = true;
                            isItemFlag++;
                            if (isItemFlag == 1){
                                title = null; link = null; description = null; thumbnailUrl = null;
                            }
                            continue;
                        }
                    }

                    String result = "";
                    if (xmlPullParser.next() == XmlPullParser.TEXT){
                        result = xmlPullParser.getText();
                        xmlPullParser.next();
                    }

                    switch (name){
                        case "title":
                            title = result;
                            break;
                        case "link":
                            link = result;
                            break;
                        case "description":{
                            Document doc = Jsoup.parse(result); //doc type Document
                            description = doc.body().text();
                            Document doc2 = Jsoup.parse(description);
                            description = doc2.body().text();

                            ImageElement = doc2.select("img").first();
                            if (ImageElement != null) {
                                thumbnailUrl = ImageElement.attr("src");
                            }
                            break;}
                    }

                    if (title!=null && link!=null && description!=null){
                        if (isItem){
                            RssFeedModel item = new RssFeedModel(title, link, description, thumbnailUrl);
                            itemsList.add(item);
                        }

                        title = null; link = null; description = null; thumbnailUrl = null;
                    }
                }
                return itemsList;
            }
            finally {
                inputstream.close();
            }
        }
    }

}
