package com.adarsh.apps.campusstore;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FilterQueryProvider;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseImageView;

import java.util.List;

/**
 * Created by Adarsh on 14-01-2015.
 */
public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    public static List<ItemInfo> pojos;
    private NavigationDrawerCallbacks mNavigationDrawerCallbacks;
    private int mSelectedPosition;
    private String intented;
    private int mTouchedPosition = -1;
    protected FilterQueryProvider mFilterQueryProvider;
    protected CursorFilter mCursorFilter;
    private List<ItemInfo> originaldata;
    private List<ItemInfo> filtereddata;
    //private DataFilter filter;

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
            final TextView price = (TextView) v.findViewById(R.id.price);
            final ParseImageView imageView = (ParseImageView) v.findViewById(R.id.imageView);
            BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
            final Bitmap image = drawable.getBitmap();
            if(pojos.size()==0)
            Toast.makeText(MainActivity.context, "Sorry.There are currently no items available in this category.", Toast.LENGTH_LONG).show();
            //ByteArrayOutputStream stream = new ByteArrayOutputStream();
            //image.compress(Bitmap.CompressFormat.PNG, 100, stream);
            //final byte[] byteArray = stream.toByteArray();

            //final Bundle extras = new Bundle();
            //extras.putParcelable("imagebitmap", image);
            /*v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(),DetailActivity.class);
                    intent.putExtra("key", title.getText().toString());
                    intent.putExtra("key2", desc.getText().toString());
                    //intent.putExtra("picture",byteArray);
                    CommonResources.bmp=image;
                    //intent.putExtras(extras);
                    view.getContext().startActivity(intent);
                }
            });*/
        }

    }

    public abstract static class EndlessRecyclerOnScrollListener extends
            RecyclerView.OnScrollListener {

        int lastVisibleItem, totalItemCount;
        private LinearLayoutManager mLinearLayoutManager;

        public EndlessRecyclerOnScrollListener(
                LinearLayoutManager linearLayoutManager) {
            this.mLinearLayoutManager = linearLayoutManager;
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            totalItemCount = mLinearLayoutManager.getItemCount();
            lastVisibleItem = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
            if (totalItemCount == lastVisibleItem + 1) onLoadMore();
        }

        public abstract void onLoadMore();
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MainAdapter(List<ItemInfo> pojos) {




        this.pojos = pojos;

    }

    public int getCount() {
        return filtereddata.size();
    }

    //This should return a data object, not an int
    public Object getItem(int position) {
        return filtereddata.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    // Create new views (invoked by the layout manager)
    /*public Filter getFilter() {
        if (filter == null) {
            filter = new DataFilter();
        }
        return mCursorFilter;
    }
    public FilterQueryProvider getFilterQueryProvider() {
        return mFilterQueryProvider;
    }
    public void setFilterQueryProvider(FilterQueryProvider filterQueryProvider) {
        mFilterQueryProvider = filterQueryProvider;
    }*/
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
        final TextView desc = (TextView) holder.view.findViewById(R.id.desc);
        final ParseImageView imageView = (ParseImageView) holder.view.findViewById(R.id.imageView);
        final TextView price = (TextView) holder.view.findViewById(R.id.price);
        final TextView cat = (TextView) holder.view.findViewById(R.id.cat);
        //ItemInfo i = new ItemInfo(null, null, null, null, null);
        //i = filtereddata.get(position);
        //imageView.buildDrawingCache();
        //  Bitmap bmap = imageView.getDrawingCache();

        title.setText(pojos.get(position).getTitle());
        desc.setText(pojos.get(position).getUser());
        cat.setText(pojos.get(position).getCat());

        imageView.setImageDrawable(pojos.get(position).getImage());
        price.setText(pojos.get(position).getprice());
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), DetailActivity.class);
                intent.putExtra("key", title.getText().toString());
                intent.putExtra("key2", desc.getText().toString());
                intent.putExtra("key3", pojos.get(position).getDesc());
                intent.putExtra("key4", price.getText().toString());
                BitmapDrawable d = (BitmapDrawable) imageView.getDrawable();
                CommonResources.bmp = d.getBitmap();
                //CommonResources.bmp1=((BitmapDrawable)pojos.get(position).getImage1()).getBitmap();
                //CommonResources.bmp2=((BitmapDrawable)pojos.get(position).getImage2()).getBitmap();
                ScreenSlidePageFragment.bmp=d.getBitmap();
                //ScreenSlidePageFragment.bmp1=((BitmapDrawable)pojos.get(position).getImage1()).getBitmap();
                //ScreenSlidePageFragment.bmp2=((BitmapDrawable)pojos.get(position).getImage2()).getBitmap();
                //Todo Uncomment the above code and delete the below 2 lines....
                ScreenSlidePageFragment.bmp1=d.getBitmap();
                ScreenSlidePageFragment.bmp2=d.getBitmap();

                intent.putExtra("key2", desc.getText().toString());
                intent.putExtra("key3", pojos.get(position).getDesc());
                intent.putExtra("key4", price.getText().toString());
                intent.putExtra("noteId", pojos.get(position).getId());
                BitmapDrawable de = (BitmapDrawable) imageView.getDrawable();
                CommonResources.bmp = de.getBitmap();
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

    public void filterOut(String filter) {
        final int size = pojos.size();
        for (int i = size - 1; i >= 0; i--) {
            if ((pojos.get(i).getUser().equals(filter) == false) && (pojos.get(i).getTitle().equals(filter) == false) && (pojos.get(i).getprice().equals(filter) == false) && (pojos.get(i).getDesc().equals(filter) == false)) {
                pojos.remove(i);
                notifyItemRemoved(i);
            }
        }
    }
}

/*private class DataFilter extends Filter {

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        pojos = (List<ItemInfo>) results.values;
        notifyDataSetChanged();
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {

        FilterResults r = new FilterResults();

        if (datavalues == null) {

            synchronized (data) {
                datavalues = new ArrayList<Info>(data);
            }

        }

        if (constraint== null || constraint.length() == 0) {

            synchronized (data) {
                ArrayList<Info> list = new ArrayList<Info>(datavalues);
                results.values = list;
                results.count = list.size();
            }

        }
        else {

            ArrayList<Info> values = (ArrayList<Info>)datavalues;
            int count = values.size();
            ArrayList<Info> list = new ArrayList<Info>();
            String prefix = constraint.toString().toLowerCase();

            for (int i=0; i<count; i++) {

                if( values.get(i).appName.toLowerCase().contains(prefix) ) {
                    list.add(data.get(i));
                }

            }

            r.values = list;
            r.count  = list.size();

        }

        return r;

    }*/
