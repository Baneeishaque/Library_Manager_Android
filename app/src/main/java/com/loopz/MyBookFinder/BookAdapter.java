package com.loopz.MyBookFinder;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class BookAdapter extends BaseAdapter {

    Context context;
    List<Books> valueList;

    public BookAdapter(List<Books> listValue, Context context)
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
        BookItem bookItem = null;

        if(convertView == null)
        {
            bookItem = new BookItem();

            LayoutInflater layoutInfiater = (LayoutInflater)this.context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            convertView = layoutInfiater.inflate(R.layout.book_items, null);

            bookItem.TVtitle = (TextView)convertView.findViewById(R.id.bknm);
            bookItem.TVbkid=(TextView)convertView.findViewById(R.id.bkid);
            bookItem.TVauthor=(TextView)convertView.findViewById(R.id.auth);
            bookItem.TVdep=(TextView)convertView.findViewById(R.id.dep);
            bookItem.TVdes=(TextView)convertView.findViewById(R.id.des);
            bookItem.TVstat=(TextView)convertView.findViewById(R.id.stat);
            bookItem.TVvol=(TextView)convertView.findViewById(R.id.vol);
            bookItem.TVnum=(TextView)convertView.findViewById(R.id.num);


            convertView.setTag(bookItem);
        }
        else
        {
            bookItem = (BookItem) convertView.getTag();
        }

        bookItem.TVbkid.setText("Book ID : "+valueList.get(position).bkid);
        bookItem.TVtitle.setText("Book Title : "+valueList.get(position).Title);
        bookItem.TVauthor.setText("Book Author : "+valueList.get(position).Author);
        bookItem.TVnum.setText("No. Of Copies : "+valueList.get(position).num);
        bookItem.TVvol.setText("Book Volume : "+valueList.get(position).volume);
        bookItem.TVstat.setText("Book Status : "+valueList.get(position).status);
        bookItem.TVdes.setText("Book Description : "+valueList.get(position).desc);
        bookItem.TVdep.setText("Book Department : "+valueList.get(position).department);

        return convertView;
    }
}

class BookItem
{
    TextView TVtitle,TVbkid,TVauthor,TVvol,TVdep,TVdes,TVstat,TVnum,TVid;

}