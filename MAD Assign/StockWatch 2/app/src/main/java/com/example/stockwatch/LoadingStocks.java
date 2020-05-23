package com.example.stockwatch;

import android.net.Uri;
import android.os.AsyncTask;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LoadingStocks extends AsyncTask<String,Void,String>
{
    private MainActivity mainActivity;
    private static final String DATAURL = "https://cloud.iexapis.com/stable/stock/", appendURL="/quote?token=sk_82db9b2864cc4e8ea444d49a1e4c8207";
    public LoadingStocks(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    protected String doInBackground(String... strings)
    {
        String stock = strings[0];
        String API_URL = DATAURL + stock + appendURL;
        Uri uri = Uri.parse(API_URL);
        String url_string = uri.toString();
        StringBuilder stringBuilder = new StringBuilder();
        try
        {
            URL url = new URL(url_string);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            InputStream inputStream = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            while((line = bufferedReader.readLine())!=null)
            {
                stringBuilder.append(line).append("\n");
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
        return stringBuilder.toString();
    }


    private Stock_info jsonToMap(String s)
    {
        Stock_info ss = new Stock_info();
        try
        {
            JSONObject j_obj = new JSONObject(s);
            String symbol = j_obj.getString("symbol");
            String name = j_obj.getString("companyName");
            double price = j_obj.getDouble("latestPrice");
            double priceChange = j_obj.getDouble("change");
            double changePercentage = j_obj.getDouble("changePercent");

            ss.setCompanyName(name);
            ss.setCompanySymbol(symbol);
            ss.setPrice(price);
            ss.setPriceChange(priceChange);
            ss.setChangePercentage(changePercentage);
            return ss;
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
        Stock_info stock = jsonToMap(s);
        mainActivity.placeStock(stock);
    }
}

