package com.adarsh.apps.campusstore;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by srivatsan on 2/1/15.
 */
public class ScreenSlidePageFragment extends Fragment {
    public static final String ARG_PAGE = "page";
    public static Bitmap bmp=null;
    public static Bitmap bmp1=null;
    public static Bitmap bmp2=null;
    /**
     * The fragment's page number, which is set to the argument value for {@link #ARG_PAGE}.
     */
    private int mPageNumber;

    /**
     * Factory method for this fragment class. Constructs a new fragment for the given page number.
     */
    public static ScreenSlidePageFragment create(int pageNumber) {
        ScreenSlidePageFragment fragment = new ScreenSlidePageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public ScreenSlidePageFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNumber = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout containing a title and body text.
        ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.activity_image_slide, container, false);

        // Set the title view to show the page number.
        switch (getPageNumber()) {
            case 0:((ImageView) rootView.findViewById(R.id.sliding_image)).setImageBitmap(bmp);
                break;
            case 1:((ImageView) rootView.findViewById(R.id.sliding_image)).setImageBitmap(bmp1);
                break;
            case 2:((ImageView) rootView.findViewById(R.id.sliding_image)).setImageBitmap(bmp2);
                break;
        }

        return rootView;
    }

    /**
     * Returns the page number represented by this fragment object.
     */
    public int getPageNumber() {
        return mPageNumber;
    }
}
