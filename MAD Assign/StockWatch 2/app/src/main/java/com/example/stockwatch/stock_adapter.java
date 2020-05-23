package com.example.stockwatch;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.Locale;

public class stock_adapter extends RecyclerView.Adapter<StocksViewHolder>
{
    private List<Stock_info> list;
    private MainActivity local_mainActivity;
    public stock_adapter(List<Stock_info> stockList, MainActivity mainActivity)
    {
        this.list = stockList;
        local_mainActivity=mainActivity;
    }

    @NonNull
    @Override
    public StocksViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.stocks_list,viewGroup,false);
        view.setOnClickListener(local_mainActivity);
        view.setOnLongClickListener(local_mainActivity);
        return new StocksViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StocksViewHolder stocksViewHolder, int i)
    {
        Stock_info stock = list.get(i);
        if (stock.getPriceChange() < 0)
        {
            setRedColor(stocksViewHolder);
        }
        else
            {
            setGreenColor(stocksViewHolder);
        }
        setDetails(stocksViewHolder, stock);
    }


    private void setDetails(StocksViewHolder holder, Stock_info stock)
    {
        holder.Name.setText(stock.getCompanyName());
        holder.Symbol.setText(stock.getCompanySymbol());
        holder.price.setText(String.format(Locale.US, "%.2f", stock.getPrice()));
        holder.change.setText(String.format(Locale.US, "%.2f", stock.getPriceChange()));
        holder.diff.setText(String.format(Locale.US, "(%.2f%%)", stock.getChangePercentage()));
    }

    private void setRedColor(StocksViewHolder holder)
    {
        holder.Name.setTextColor(Color.RED);
        holder.Symbol.setTextColor(Color.RED);
        holder.price.setTextColor(Color.RED);
        holder.change.setTextColor(Color.RED);
        holder.diff.setTextColor(Color.RED);
        holder.arrow.setImageResource(R.drawable.down);
        holder.arrow.setColorFilter(Color.RED);
    }

    private void setGreenColor(StocksViewHolder holder)
    {
        holder.Name.setTextColor(Color.GREEN);
        holder.Symbol.setTextColor(Color.GREEN);
        holder.price.setTextColor(Color.GREEN);
        holder.change.setTextColor(Color.GREEN);
        holder.diff.setTextColor(Color.GREEN);
        holder.arrow.setImageResource(R.drawable.up);
        holder.arrow.setColorFilter(Color.GREEN);
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

}

