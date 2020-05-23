package com.example.stockwatch;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

class StocksViewHolder extends RecyclerView.ViewHolder{
    public TextView Name;
    public TextView Symbol;
    public TextView price;
    public TextView change;
    public TextView diff;
    public ImageView arrow;
    public View divView;


    public StocksViewHolder(@NonNull View itemView) {
        super(itemView);
        Name = itemView.findViewById(R.id.name);
        Symbol = itemView.findViewById(R.id.symbol);
        price = itemView.findViewById(R.id.price);
        change = itemView.findViewById(R.id.change);
        diff = itemView.findViewById(R.id.diff);
        arrow = itemView.findViewById(R.id.arrow);


    }
}
