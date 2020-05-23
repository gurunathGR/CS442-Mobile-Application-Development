package com.example.stockwatch;

import java.io.Serializable;

public class Stock_info implements Serializable
{

    private String Name;
    private String Symbol;
    private double price;
    private double change;
    private double diff;

    @Override
    public boolean equals(Object obj)
    {
        boolean output = false;
        if (obj == null || obj.getClass() != getClass())
        {
            output = false;
        }
        else
            {
            Stock_info stk = (Stock_info) obj;

            if (this.Symbol.equals(stk.Symbol))
            {
                output = true;
            }
        }
        return output;
    }



    public String getCompanyName()
    {
        return Name;
    }

    public void setCompanyName(String companyName)
    {
        this.Name = companyName;
    }

    public String getCompanySymbol()
    {
        return Symbol;
    }

    public void setCompanySymbol(String companySymbol)
    {
        this.Symbol = companySymbol;
    }


    public double getPrice()
    {
        return price;
    }

    public void setPrice(double price)
    {
        this.price = price;
    }

    public double getPriceChange()
    {
        return change;
    }

    public void setPriceChange(double priceChange)
    {
        this.change = priceChange;
    }

    public double getChangePercentage()
    {
        return diff;
    }

    public void setChangePercentage(double changePercentage)
    {
        this.diff = changePercentage;
    }

}
