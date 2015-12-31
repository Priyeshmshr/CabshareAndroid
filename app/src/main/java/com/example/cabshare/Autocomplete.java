package com.example.cabshare;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.Filterable;

public class Autocomplete extends Activity implements OnItemClickListener {

    AutoCompleteTextView auto;

    //private ArrayList<String> resultList;
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.autocomplete);
        auto = (AutoCompleteTextView) findViewById(R.id.autoComplete);
        Bundle extra = getIntent().getExtras();
        String hint = extra.getString("hint", "Enter address...");
        auto.setHint(hint);
        auto.setAdapter(new PlacesAutoCompleteAdapter(this, R.layout.list_item));
        auto.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapter, View view, int position, long arg3) {
        // TODO Auto-generated method stub
        String str = (String) adapter.getItemAtPosition(position);
        Intent in = new Intent();
        in.putExtra("Place", str);
        setResult(RESULT_OK, in);
        finish();
    }
}

class PlacesAutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {
    Context ctx;
    private ArrayList<String> resultList;

    public PlacesAutoCompleteAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        ctx = context;
    }

    @Override
    public int getCount() {
        return resultList.size();
    }

    @Override
    public String getItem(int index) {
        return resultList.get(index);
    }

    @Override
    public Filter getFilter() {
        // TODO Auto-generated method stub
        Filter filter = new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                // TODO Auto-generated method stub
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    HttpPostData result = new HttpPostData(ctx);
                    resultList = result.autocomplete(constraint.toString());
                    filterResults.values = resultList;
                    filterResults.count = resultList.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
                // TODO Auto-generated method stub
            }
        };
        return filter;
    }
}