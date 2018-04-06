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


public class RqstAdapter extends BaseAdapter {

    Context context;
    List<Books> valueList;

    public RqstAdapter(List<Books> listValue, Context context)
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
        Rqstitem rqstitem = null;

        if(convertView == null)
        {
            rqstitem = new Rqstitem();

            LayoutInflater layoutInfiater = (LayoutInflater)this.context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            convertView = layoutInfiater.inflate(R.layout.rqst_items, null);

            rqstitem.TVtitle = (TextView)convertView.findViewById(R.id.authu);
            rqstitem.TVbkid=(TextView)convertView.findViewById(R.id.booknm);
            rqstitem.TVauthor=(TextView)convertView.findViewById(R.id.volu);
            rqstitem.TVnum=(TextView)convertView.findViewById(R.id.datu);


            convertView.setTag(rqstitem);
        }
        else
        {
            rqstitem = (Rqstitem) convertView.getTag();
        }

        rqstitem.TVbkid.setText(valueList.get(position).bkid);
        rqstitem.TVtitle.setText(valueList.get(position).Title);
        rqstitem.TVauthor.setText(valueList.get(position).Author);
        rqstitem.TVnum.setText(valueList.get(position).num);

        return convertView;
    }
}

class Rqstitem {
    TextView TVtitle, TVbkid, TVauthor, TVnum;
}


