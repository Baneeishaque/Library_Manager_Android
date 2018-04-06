package com.loopz.MyBookFinder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Map extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        Intent in=getIntent();
        Bundle extras=in.getExtras();
        String s= null;
        if (extras != null) {
            s = extras.getString("a");
        }
        String btn="r"+s;
        int resid=getResources().getIdentifier(btn,"id",getPackageName());
        Button f = (Button) findViewById(resid);
        f.setBackgroundTintList(getResources().getColorStateList(R.color.btncolor));
        f.setClickable(true);

    }
    public void shelf(View v){
        Intent i=new Intent(getApplicationContext(),Shelf.class);
        Intent in=getIntent();
        Bundle extras=in.getExtras();
        if (extras != null) {
            i.putExtras(extras);
        }
        startActivity(i);
    }
}
