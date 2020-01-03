package com.example.friendsapp;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListViewAdapter extends BaseAdapter{

    //variables
    Context mContext;
    LayoutInflater inflater;
    List<frndmodel> modellist;
    ArrayList<frndmodel> arrayList;

    //constructor
    public ListViewAdapter(Context context, List<frndmodel> modellist) {
        mContext = context;
        this.modellist = modellist;
        inflater = LayoutInflater.from(mContext);
        this.arrayList = new ArrayList<frndmodel>();
        this.arrayList.addAll(modellist);
    }

    public class ViewHolder{
        TextView mTitleTv, mDescTv;
        ImageView mIconIv;
    }

    @Override
    public int getCount() {
        return modellist.size();
    }

    @Override
    public Object getItem(int i) {
        return modellist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if (view==null){
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.frndrow, null);

            //locate the views in row.xml
            holder.mTitleTv = view.findViewById(R.id.mainTitle);
            holder.mDescTv = view.findViewById(R.id.mainDesc);
            holder.mIconIv = view.findViewById(R.id.mainIcon);

            view.setTag(holder);

        }
        else {
            holder = (ViewHolder)view.getTag();
        }
        //set the results into textviews
        holder.mTitleTv.setText(modellist.get(position).getTitle());
        holder.mDescTv.setText(modellist.get(position).getDesc());
        //set the result in imageview
        holder.mIconIv.setImageResource(modellist.get(position).getIcon());

        //listview item clicks
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //code later
                if (modellist.get(position).getTitle().equals("Battery")){
                    //start friendstat with title for actionbar and text for textview
                    Intent intent = new Intent(mContext, friendstat.class);
                    intent.putExtra("actionBarTitle", "Battery");
                    intent.putExtra("contentTv", "This is Battery detail...");
                    mContext.startActivity(intent);
                }
                if (modellist.get(position).getTitle().equals("Cpu")){
                    //start friendstat with title for actionbar and text for textview
                    Intent intent = new Intent(mContext, friendstat.class);
                    intent.putExtra("actionBarTitle", "Cpu");
                    intent.putExtra("contentTv", "This is Cpu detail...");
                    mContext.startActivity(intent);
                }
                if (modellist.get(position).getTitle().equals("Display")){
                    //start friendstat with title for actionbar and text for textview
                    Intent intent = new Intent(mContext, friendstat.class);
                    intent.putExtra("actionBarTitle", "Display");
                    intent.putExtra("contentTv", "This is Display detail...");
                    mContext.startActivity(intent);
                }
                if (modellist.get(position).getTitle().equals("Memory")){
                    //start friendstat with title for actionbar and text for textview
                    Intent intent = new Intent(mContext, friendstat.class);
                    intent.putExtra("actionBarTitle", "Memory");
                    intent.putExtra("contentTv", "This is Memory detail...");
                    mContext.startActivity(intent);
                }
                if (modellist.get(position).getTitle().equals("Sensor")){
                    //start friendstat with title for actionbar and text for textview
                    Intent intent = new Intent(mContext, friendstat.class);
                    intent.putExtra("actionBarTitle", "Sensor");
                    intent.putExtra("contentTv", "This is Sensor detail...");
                    mContext.startActivity(intent);
                }
            }
        });

        return view;
    }

    //filter
    public void filter(String charText){
        charText = charText.toLowerCase(Locale.getDefault());
        modellist.clear();
        if (charText!=null && !charText.isEmpty()){
            ArrayList<frndmodel> lstFound=new ArrayList<>();
            for(frndmodel model:arrayList)
            {
                if(model.getTitle().toLowerCase((Locale.getDefault())).contains(charText)){
                  lstFound.add(model);
                }
                modellist=lstFound;
            }
        }
        else {
            modellist=arrayList;
        }
        notifyDataSetChanged();
    }

}