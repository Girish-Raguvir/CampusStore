package com.adarsh.apps.campusstore;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.adarsh.apps.materialtests.DetailActivity;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adarsh on 14-01-2015.
 */
public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    private List<ItemInfo> pojos;
    private NavigationDrawerCallbacks mNavigationDrawerCallbacks;
    private int mSelectedPosition;
    private String intented;
    private int mTouchedPosition = -1;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View view;

        public ViewHolder(View v) {
            super(v);
            view = v;
            final TextView title = (TextView) v.findViewById(R.id.title);
            final TextView desc = (TextView) v.findViewById(R.id.desc);
            ImageView imageView = (ImageView) v.findViewById(R.id.imageView);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(),DetailActivity.class);
                    intent.putExtra("key", title.getText().toString());
                    intent.putExtra("key2", desc.getText().toString());
                    view.getContext().startActivity(intent);
                }
            });
        }

    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MainAdapter(List<ItemInfo> pojos) {
        this.pojos = pojos;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MainAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;

    }

    // Replace the contents of a view (invoked by the layout manager)

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final TextView title = (TextView) holder.view.findViewById(R.id.title);
        final  TextView desc = (TextView) holder.view.findViewById(R.id.desc);
        final ImageView imageView = (ImageView) holder.view.findViewById(R.id.imageView);
        imageView.buildDrawingCache();
        Bitmap bmap = imageView.getDrawingCache();

        title.setText(pojos.get(position).getTitle());
        desc.setText(pojos.get(position).getDesc());
        imageView.setImageDrawable(pojos.get(position).getImage());
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(),DetailActivity.class);
                intent.putExtra("key", title.getText().toString());
                intent.putExtra("key2", desc.getText().toString());
                holder.itemView.getContext().startActivity(intent);
            }
        });
       /* holder.itemView.setOnTouchListener(new View.OnTouchListener() {
                                                   @Override
                                                   public boolean onTouch(View v, MotionEvent event) {

                                                       switch (event.getAction()) {
                                                           case MotionEvent.ACTION_DOWN:
                                                               touchPosition(position);
                                                               return false;
                                                           case MotionEvent.ACTION_CANCEL:
                                                               touchPosition(-1);
                                                               return false;
                                                           case MotionEvent.ACTION_MOVE:
                                                               return false;
                                                           case MotionEvent.ACTION_UP:
                                                               touchPosition(-1);
                                                               return false;
                                                       }
                                                       return true;
                                                   }
                                               }
        );*/
        /*holder.itemView.setOnClickListener(new View.OnClickListener() {
                                                   @Override
                                                   public void onClick(View v) {
                                                       if (mNavigationDrawerCallbacks != null)
                                                           mNavigationDrawerCallbacks.onNavigationDrawerItemSelected(position);
                                                   }
                                               }
        );

        //TODO: selected menu position, change layout accordingly
        if (mSelectedPosition == position || mTouchedPosition == position) {
            Intent intent = new Intent(holder.itemView.getContext(),DetailActivity.class);
            intent.putExtra("key", title.getText().toString());
            intent.putExtra("key2", desc.getText().toString());
             // your bitmap
            /*ByteArrayOutputStream bs = new ByteArrayOutputStream();
            bmap.compress(Bitmap.CompressFormat.PNG, 50, bs);
            byte[] byteArray = bs.toByteArray();
            intent.putExtra("byteArray", byteArray);
            holder.itemView.getContext().startActivity(intent);
        }*/

    }

    private void touchPosition(int position) {
        int lastPosition = mTouchedPosition;
        mTouchedPosition = position;
        if (lastPosition >= 0)
            notifyItemChanged(lastPosition);
        if (position >= 0)
            notifyItemChanged(position);
    }

    @Override
    public int getItemCount() {
        return pojos.size();
    }
}
