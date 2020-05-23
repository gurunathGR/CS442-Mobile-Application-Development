package com.example.stockwatch;

import android.net.Uri;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

class LoadName extends AsyncTask<Void,Void,String>
{
    private MainActivity mainActivity;
    private static final String DATAURL = "https://api.iextrading.com/1.0/ref-data/symbols";
    public LoadName(MainActivity mainActivity) {
        this.mainActivity =mainActivity;
    }

    @Override
    protected String doInBackground(Void... voids)
    {
        Uri uri = Uri.parse(DATAURL);
        String url_string = uri.toString();
        StringBuilder string_Builder = new StringBuilder();

        try
        {
            URL url = new URL(url_string);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            InputStream inputStream = connection.getInputStream();
            BufferedReader bufferReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            while ((line=bufferReader.readLine())!=null)
            {
                string_Builder.append(line).append("\n");
            }
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return string_Builder.toString();
    }



    private HashMap<String, String> jsonToMap(String s)
    {
        HashMap<String,String> stringHashMap = new HashMap<>();
        try
        {
            JSONArray jsonArray = new JSONArray(s);
            for (int i = 0;i<jsonArray.length();i++)
            {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                String sym = jsonObject.getString("symbol");
                String name = jsonObject.getString("name");
                stringHashMap.put(sym,name);
            }
            return stringHashMap;
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    protected void onPostExecute(String s)
    {
        super.onPostExecute(s);
        HashMap<String,String> hashMap = jsonToMap(s);
        mainActivity.setData(hashMap);
    }
}

