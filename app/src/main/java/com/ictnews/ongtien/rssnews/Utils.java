package com.ictnews.ongtien.rssnews;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

class Utils {
    private final static String LOG_TAG = "UTILS";

    static String GetHtmlResponse(String articleUrl){
        String result = "";
        URL UrlArticle = null;

        //create Url of Article
        try
        {
            UrlArticle = new URL(articleUrl);
        }
        catch (MalformedURLException e){
            Log.e(LOG_TAG, "Error Url");
        }

        //load Html data
        if (UrlArticle != null){
            try{
                result = GetHttpRequest(UrlArticle);
            }
            catch (IOException e)
            {
                Log.e(LOG_TAG, "Error Connection");
            }
        }

        return result;
    }

    private static String GetHttpRequest(URL url) throws IOException{
            String HtmlResponse = "";
            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;

            //check if url is null
            if (url == null)
            {
                return null;
            }

            //try to create HttpConnection
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setConnectTimeout(10000);
                urlConnection.setReadTimeout(15000);
                urlConnection.connect();
                //get Response
                if (urlConnection.getResponseCode() == 200) {
                    inputStream = urlConnection.getInputStream();
                    HtmlResponse = readFromStream(inputStream);
                }
                else Log.e(LOG_TAG, "Bad Request, code = " + urlConnection.getResponseCode());
            }
            catch(IOException e){
                Log.e(LOG_TAG, "can not create HttpConnection", e);
            }
            finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
                if (inputStream != null)
                    inputStream.close();
            }
            return HtmlResponse;
        }

    //declare readFromStream() to read inputStream from connection
    private static String readFromStream(InputStream inputStream) throws IOException{
        StringBuilder output = new StringBuilder();
        if (inputStream != null)
        {
            try {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while (line != null) {
                    output.append(line);
                    line = reader.readLine();
                }
            }
            catch (IOException e)
            {
                Log.e(LOG_TAG, "Error readFromStream()");
            }
        }
        return output.toString();
    }
    
    static Boolean IsConnected(Context context){
        ConnectivityManager cnm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cnm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnectedOrConnecting())
            return true;
        else return false;
    }
}
