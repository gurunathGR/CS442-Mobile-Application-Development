package com.example.newsgateway;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class DownloadeArticle extends AsyncTask<String, Integer, String>
{
    private static final String TAG = "Article Downloading";
    private String sourceId;
    private NewsService service;
    private String API_KEY ="8ddc66fdda504518b9aef0ca1c0eb474";
    private String ARTICLE_QUERY_1 ="https://newsapi.org/v2/everything?sources=";
    private String ARTICLE_QUERY_2 = "&apiKey="+API_KEY;
    private Uri.Builder buildURL = null;
    private StringBuilder sb1;
    private boolean noDataFound=false;
    boolean isNoDataFound =true;
    private ArrayList<Articles> articleArrayList = new ArrayList <Articles>();

    public DownloadeArticle(NewsService service, String sourceId){
        this.sourceId = sourceId;
        this.service= service;

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        service.Articles_Set(articleArrayList);
    }


    public void API_Connect() {

        String urlToUse = buildURL.build().toString();
        sb1 = new StringBuilder();
        try {
            URL url = new URL(urlToUse);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            if(conn.getResponseCode() == HttpURLConnection.HTTP_NOT_FOUND)
            {
                noDataFound=true;
            }
            else {
                conn.setRequestMethod("GET");
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

                String line=null;
                while ((line = reader.readLine()) != null) {
                    sb1.append(line).append('\n');
                }
                isNoDataFound=false;

            }
        }
        catch(FileNotFoundException fe){
            Log.d(TAG, "FileNotFoundException ");
        }
        catch (Exception e) {
            Log.d(TAG, "Exception doInBackground: " + e.getMessage());
        }
    }

    @Override
    protected String doInBackground(String... strings)
    {
        String query ="";

        query = ARTICLE_QUERY_1+sourceId+ARTICLE_QUERY_2;
        buildURL = Uri.parse(query).buildUpon();
        API_Connect();
        if(!isNoDataFound) {
            parseJSON1(sb1.toString());
        }
        return null;
    }


    private void parseJSON1(String s)
    {
        try
        {
            if(!noDataFound)
            {
                JSONObject jObjMain = new JSONObject(s);
                JSONArray articles = jObjMain.getJSONArray("articles");
                for(int i=0;i<articles.length();i++){
                    JSONObject art = (JSONObject) articles.get(i);
                    Articles artObj = new Articles();
                    artObj.setaAuthor(art.getString("author"));
                    artObj.setaDescription(art.getString("description"));
                    artObj.setaPublishedAt(art.getString("publishedAt"));
                    artObj.setaTitle(art.getString("title"));
                    artObj.setaUrlToImage(art.getString("urlToImage"));
                    artObj.setArticleUrl(art.getString("url"));
                    articleArrayList.add(artObj);
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}

