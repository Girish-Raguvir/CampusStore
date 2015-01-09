package com.example.android.io2014;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Context;
import android.app.Activity;
import android.widget.AdapterView.OnItemClickListener;

import java.util.Collections;
import java.util.List;

/**
 * Created by Adarsh on 1/3/2015.
 */
public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {
    List<Information> data = Collections.emptyList();
    private LayoutInflater inflater;
    private DrawerCallbacks mDrawerCallbacks;
    private int mSelectedPosition;
    private int mTouchedPosition = -1;

    public Adapter(Context context, List<Information> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    public void setNavigationDrawerCallbacks(DrawerCallbacks drawerCallbacks) {
        mDrawerCallbacks = drawerCallbacks;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, final int i) {
        Information current = data.get(i);
        viewHolder.title.setText(current.title);
        viewHolder.icon.setImageResource(current.iconId);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                                                   @Override
                                                   public void onClick(View v) {
                                                       if (mDrawerCallbacks != null)
                                                           mDrawerCallbacks.onNavigationDrawerItemSelected(i);
                                                           mSelectedPosition = i;
                                                   }
                                               }
        );

    if (mSelectedPosition == i || mTouchedPosition == i) {
            viewHolder.itemView.setBackgroundColor(viewHolder.itemView.getContext().getResources().getColor(R.color.selection));
                     }

             else {
            viewHolder.itemView.setBackgroundColor(R.color.transparent);
        }
    }

    public void selectPosition(int position) {
        int lastPosition = mSelectedPosition;
        mSelectedPosition = position;
        notifyItemChanged(lastPosition);
        notifyItemChanged(position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView icon;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.listText);
            icon = (ImageView) itemView.findViewById(R.id.listIcon);
        }
    }
}
