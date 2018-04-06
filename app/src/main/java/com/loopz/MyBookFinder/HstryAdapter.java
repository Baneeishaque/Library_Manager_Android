package com.loopz.MyBookFinder;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by user on 5/1/2017.
 */


public class HstryAdapter extends BaseAdapter {

    Context context;
    List<Books> valueList;

    public HstryAdapter(List<Books> listValue, Context context)
    {
        this.context = context;
        this.valueList = listValue;
    }

    @Override
    public int getCount()
    {
        return this.valueList.size();
    }

    @Override
    public Object getItem(int position)
    {
        return this.valueList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Hstryitem hstryitem = null;

        if(convertView == null)
        {
            hstryitem = new Hstryitem();

            LayoutInflater layoutInfiater = (LayoutInflater)this.context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            convertView = layoutInfiater.inflate(R.layout.bookhstry_items, null);

            hstryitem.TVtitle = (TextView)convertView.findViewById(R.id.isdate);
            hstryitem.TVbkid=(TextView)convertView.findViewById(R.id.bookid);
            hstryitem.TVauthor=(TextView)convertView.findViewById(R.id.retdate);
            hstryitem.TVnum=(TextView)convertView.findViewById(R.id.process);


            convertView.setTag(hstryitem);
        }
        else
        {
            hstryitem = (Hstryitem) convertView.getTag();
        }

        hstryitem.TVbkid.setText(valueList.get(position).bkid);
        hstryitem.TVtitle.setText(valueList.get(position).Title);
        hstryitem.TVauthor.setText(valueList.get(position).Author);
        hstryitem.TVnum.setText(valueList.get(position).num);

        return convertView;
    }
}

class Hstryitem {
    TextView TVtitle, TVbkid, TVauthor, TVnum;
}


