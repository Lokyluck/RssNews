package com.ictnews.ongtien.rssnews;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.ShareCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class WebViewActivity extends AppCompatActivity {
    private WebView webView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView mConnect_status_text_in_WebView;
    private String articleUrl;
    private String HtmlResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_view);

        //set ToolBar
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.webview_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/

        webView = (WebView) findViewById(R.id.webview);
        mConnect_status_text_in_WebView = (TextView) findViewById(R.id.connect_status_text_in_WebView);

        setWebViewParameter();

        //catch Intent data and show in webview
        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            articleUrl = extra.getString("ArticleStr");
            if (Utils.IsConnected(getBaseContext())){
                showWebView();
                //new FetchDataInWebView().execute();
                webView.loadUrl(articleUrl);
            }
            else showTextDisconnect();
        }
    }

    //create menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.share_action:
                Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                        .setType("text/plain").setText(articleUrl)
                        .getIntent();
                shareIntent.addFlags(524288); //FLAG_ACTIVITY_NEW_DOCUMENT = 524288
                startActivity(shareIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setWebViewParameter(){
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        webView.getSettings().setTextZoom(120);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new myNewWebViewClient());

        //remove add
        /*webView.getSettings().setUserAgentString(
                this.webView.getSettings().getUserAgentString()
                        + " "
                        + getString(R.string.user_agent_suffix)
        );*/
        /*webView.getSettings().
                setUserAgentString("Mozilla/5.0 (Windows NT 6.1; rv:13.0) Gecko/20100101 Firefox/12");*/

        //set refresh action
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout_webview);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Utils.IsConnected(getBaseContext())){
                    showWebView();
                    //new FetchDataInWebView().execute();
                    webView.loadUrl(articleUrl);
                    swipeRefreshLayout.setRefreshing(false);
                }
                else {
                    showTextDisconnect();
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    private class myNewWebViewClient extends WebViewClient {
        /*@Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            if (Uri.parse(request.toString()).getHost().equals("ictnews.vn")) {
                // This is my web site, so do not override; let my WebView load the page
                return false;
            }
            // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(request.toString()));
            startActivity(intent);
            return true;
        }*/

        @SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (Uri.parse(url).getHost().equals("ictnews.vn")) {
                // This is my web site, so do not override; let my WebView load the page
                return false;
            }
            // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
            return true;
        }
    }

    private class FetchDataInWebView extends AsyncTask<Void, Void, Boolean> {
        private Document doc;

        @Override
        protected void onPreExecute() {
            swipeRefreshLayout.setRefreshing(true);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            if (TextUtils.isEmpty(articleUrl)){
                return false;
            }
            HtmlResponse = Utils.GetHtmlResponse(articleUrl);
            String result;
            Elements basicElements;
            ///Element titleElement;
            ///Element topicElement;
            //Elements linkElements;
            //TODO: REMOVE ICON OF ICTNEWS

            if (HtmlResponse != null){
                doc = Jsoup.parse(HtmlResponse);
                //parse HtmlResponse


                basicElements = doc.select("div.container");
                result = basicElements.outerHtml();

                if (!result.equals(""))
                    HtmlResponse = result;
                return true;
            }
            else
                return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            swipeRefreshLayout.setRefreshing(false);
            if (success){
                webView.loadData(HtmlResponse, "text/html; charset=utf-8", null);
            }
        }
}

    //method show WebView
    private void showWebView(){
        mConnect_status_text_in_WebView.setVisibility(View.GONE);
        webView.setVisibility(View.VISIBLE);
    }

    //method show text internet disconnected
    private void showTextDisconnect(){
        mConnect_status_text_in_WebView.setVisibility(View.VISIBLE);
        webView.setVisibility(View.VISIBLE);
    }
}
