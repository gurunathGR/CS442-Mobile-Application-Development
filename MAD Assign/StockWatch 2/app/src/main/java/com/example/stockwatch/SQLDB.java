package com.example.stockwatch;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class SQLDB extends SQLiteOpenHelper
{
    private static final String TAG = "SQLDB";
    private static final String DATABASE_NAME = "StockWatchDB";
    private static final String TABLE_NAME = "StockWatchTable";
    private static final String SYMBOL = "StockSymbol";
    private static final String CNAME = "CompanyName";
    private static final int DB_VERSION = 1;
    private static final String TABLECREATE = "CREATE TABLE " + TABLE_NAME + " (" + SYMBOL + " TEXT not null unique," + CNAME + " TEXT not null)";

    private SQLiteDatabase database;

    public SQLDB(Context context)
    {
        super(context, DATABASE_NAME, null, DB_VERSION);
        database = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(TABLECREATE);
        Log.d(TAG, "onCreate: Table Created" + TABLE_NAME);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }

    public ArrayList<Stock_info> loadStocks()
    {
        ArrayList<Stock_info> stocks = new ArrayList<>();
        Cursor cursor = database.query(
                TABLE_NAME, // The table to query
                new String[]{SYMBOL, CNAME}, // The columns to return
                null, // The columns for the WHERE clause
                null, // The values for the WHERE clause
                null, // don't group the rows
                null,
                null
        );
        if (cursor != null)
        {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++)
            {
                String symbol = cursor.getString(0);
                String name = cursor.getString(1);
                Stock_info s = new Stock_info();
                s.setCompanySymbol(symbol);
                s.setCompanyName(name);
                s.setPrice(0.0);
                s.setPriceChange(0.0);
                s.setChangePercentage(0.0);
                stocks.add(s);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return stocks;
    }

    public void addStock(Stock_info newStock)
    {
        ContentValues values = new ContentValues();
        values.put(SYMBOL, newStock.getCompanySymbol());
        values.put(CNAME, newStock.getCompanyName());
        long key = database.insert(TABLE_NAME, null, values);
    }

    public void deleteStock(String symbol)
    {
        int cnt = database.delete(TABLE_NAME, SYMBOL + " = ?", new String[]{symbol});
    }

    public void shutDown() {
        database.close();
    }
}

