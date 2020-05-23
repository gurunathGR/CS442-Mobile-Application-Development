package com.example.stockwatch;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;

import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener
{
    private static final String TAG = "MainActivity";
    List<Stock_info> stockInfoArrayList = new ArrayList<>();
    HashMap<String, String> hashMap;
    RecyclerView recyclerview;
    stock_adapter stock_adapter;
    SwipeRefreshLayout refreshLayout;
    SQLDB sqldb;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerview = findViewById(R.id.RECYCLER_VIEW);
        refreshLayout = findViewById(R.id.REFRESH_LAYOUT);

        stock_adapter = new stock_adapter(stockInfoArrayList, this);
        Log.d(TAG, "onCreate: " + stock_adapter);

        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.setAdapter(stock_adapter);

        refreshLayout.setProgressViewOffset(true, 0, 200);
        refreshLayout.setColorSchemeColors
                (
                getResources().getColor(android.R.color.holo_blue_bright)

                );
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                if (!checknetwork())
                {
                    refreshLayout.setRefreshing(false);
                     errorAlert();


                }
                else
                    {
                   refresh();

                    }
            }
        });

        sqldb = new SQLDB(this);
        new LoadName(MainActivity.this).execute();
        ArrayList<Stock_info> infoArrayList = sqldb.loadStocks();
        if (!checknetwork())
        {
            stockInfoArrayList.addAll(infoArrayList);
            Collections.sort(stockInfoArrayList, new Comparator<Stock_info>()
            {
                @Override
                public int compare(Stock_info o1, Stock_info o2) {
                    return o1.getCompanySymbol().compareTo(o2.getCompanySymbol());
                }
            });
            stock_adapter.notifyDataSetChanged();
        }
        else
            {
            for (int i = 0; i < infoArrayList.size(); i++)
            {
                String symbol = infoArrayList.get(i).getCompanySymbol();
                new LoadingStocks(MainActivity.this).execute(symbol);
            }
        }

    }

    private void refresh()
    {
        refreshLayout.setRefreshing(false);
        ArrayList<Stock_info> infoArrayList = sqldb.loadStocks();
        for (int i = 0; i < infoArrayList.size(); i++)
        {
            String symbol = infoArrayList.get(i).getCompanySymbol();
            new LoadingStocks(MainActivity.this).execute(symbol);
        }
        Toast.makeText(this, "Data Refreshed", Toast.LENGTH_SHORT).show();
    }

    private boolean checknetwork()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null)
        {
            Toast.makeText(this, "Error Occurred, Please restart the application", Toast.LENGTH_SHORT).show();
            return false;
        }
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }



    @Override
    protected void onResume()
    {
        super.onResume();
        Log.d(TAG, "onResume: " + stockInfoArrayList.size());
        stock_adapter.notifyDataSetChanged();
    }


    //There is no network available
    public void errorAlert()

    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("No Internet");
        builder.setMessage("Without the internet, Stock info can not be added");
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        Log.d(TAG, "onOptionsItemSelected: start");
        if (!checknetwork())
        {
           errorAlert(); //There is no network available

            return false;
        }
        else
            {
            switch (id)
            {
                case R.id.add_stock:
                    Log.d(TAG, "onOptionsItemSelected: start");
                    addStockDialog();
                    Log.d(TAG, "onOptionsItemSelected: end/dialog");
                    return true;
                    default:
                    Log.d(TAG, "onOptionsItemSelected: end/default");
                    return super.onOptionsItemSelected(item);
            }
        }
    }

    private void addStockDialog()
    {
        if (hashMap == null)
        {
            new LoadName(MainActivity.this).execute();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Selection of stocks");
        builder.setMessage("Please enter the symbol of a stock");
        builder.setCancelable(false);
        final EditText editText = new EditText(this);
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
        editText.setGravity(Gravity.CENTER_HORIZONTAL);
        builder.setView(editText);
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!checknetwork())
                {
                    errorAlert();  //There is no network available

                    return;
                }
                else if (editText.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please Enter Input that is vaild", Toast.LENGTH_SHORT).show();
                    return;
                }
                else
                    {
                    ArrayList<String> tempList = searchStock(editText.getText().toString());   //searching the stocks
                    if (!tempList.isEmpty())
                    {
                        ArrayList<String> stockOptions = new ArrayList<>(tempList);
                        if (stockOptions.size() == 1)
                        {
                            if (checkDuplicate(stockOptions.get(0)))
                            {
                                duplicateItemDialog(editText.getText().toString());
                            }
                            else
                                {
                                addStock(stockOptions.get(0));
                                }
                        }
                        else
                            {
                            duplicateDialog(editText.getText().toString(), stockOptions, stockOptions.size());
                          }
                    }
                    else
                        {
                        noDataAlert(editText.getText().toString());
                    }

                }
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }



    private void duplicateDialog(final String s, ArrayList<String> stockOptions, int size)
    {
        final String[] strings = new String[size];
        for (int i = 0; i < strings.length; i++)
        {
            strings[i] = stockOptions.get(i);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Make a Selection");
        builder.setItems(strings, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (checkDuplicate(strings[which]))
                {
                    duplicateItemDialog(s);
                }
                else
                    {
                    addStock(strings[which]);
                    }
            }
        });

        builder.setNegativeButton("Never mind", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                return;
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void noDataAlert(String toString)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Problem Occurred");
        builder.setMessage("No such data: (" + toString + ") found, please enter valid inputs");
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void addStock(String s)
    {
        String sym = s.split("-")[0].trim();
        new LoadingStocks(MainActivity.this).execute(sym);
        Stock_info ss = new Stock_info();
        ss.setCompanySymbol(sym);
        ss.setCompanyName(hashMap.get(sym));
        sqldb.addStock(ss);

    }

    //searching the stocks
    private ArrayList<String> searchStock(String s)
    {
        ArrayList<String> sList = new ArrayList<>();
        if (hashMap != null && !hashMap.isEmpty())
        {
            for (String symbol : hashMap.keySet())
            {
                String name = hashMap.get(symbol);
                if (symbol.toUpperCase().contains(s.toUpperCase()))
                {
                    sList.add(symbol + " - " + name);
                }
                else if (name.toUpperCase().contains(s.toUpperCase()))
                {
                    sList.add(symbol + " - " + name);
                }

            }
        }
        return sList;
    }
    private void duplicateItemDialog(String s)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Duplicate Stock");
        builder.setMessage("Stock symbol  "+s+"  is already displayed");
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private boolean checkDuplicate(String s)
    {
        Log.d(TAG, "isDuplicateStock: ");
        String sym = s.split("-")[0].trim();
        Stock_info t = new Stock_info();
        t.setCompanySymbol(sym);
        return stockInfoArrayList.contains(t);
    }


    @Override
    public void onClick(View v)
    {
        int i = recyclerview.getChildLayoutPosition(v);
        String marketPlaceURL = "http://www.marketwatch.com/investing/stock/" + stockInfoArrayList.get(i).getCompanySymbol();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(marketPlaceURL));
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        Log.d(TAG, "onCreateOptionsMenu: start");
        getMenuInflater().inflate(R.menu.menu_values, menu);
        Log.d(TAG, "onCreateOptionsMenu: end");
        return true;

    }

    @Override
    public boolean onLongClick(View v)
    {
        final int id = recyclerview.getChildLayoutPosition(v);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Stock");
        builder.setMessage("Delete Stock Symbol " + ((TextView) v.findViewById(R.id.symbol)).getText().toString() + "?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sqldb.deleteStock(stockInfoArrayList.get(id).getCompanySymbol());
                stockInfoArrayList.remove(id);
                stock_adapter.notifyDataSetChanged();
                Toast.makeText(MainActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        return false;
    }

    public void setData(HashMap<String, String> hashMap)
    {
        if (hashMap != null && !hashMap.isEmpty())
        {
            this.hashMap = hashMap;
        }
    }

    public void placeStock(Stock_info stock)
    {
        if (stock != null)
        {
            Log.d(TAG, "In Stock !=null condition");
            int index = stockInfoArrayList.indexOf(stock);
            Log.d(TAG, "The index " + index);
            if (index > -1)
            {
                Log.d(TAG, "In Stock index");
                stockInfoArrayList.remove(index);
            }
            stockInfoArrayList.add(stock);
            Collections.sort(stockInfoArrayList, new Comparator<Stock_info>() {
                @Override
                public int compare(Stock_info o1, Stock_info o2) {
                    return o1.getCompanySymbol().compareTo(o2.getCompanySymbol());
                }
            });
            stock_adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        sqldb.shutDown();
    }
}