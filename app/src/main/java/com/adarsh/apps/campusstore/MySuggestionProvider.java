package com.adarsh.apps.campusstore;

import android.content.SearchRecentSuggestionsProvider;

/**
 * Created by Adarsh on 17-01-2015.
 */
public class MySuggestionProvider extends SearchRecentSuggestionsProvider {
    public final static String AUTHORITY = "com.example.MySuggestionProvider";
    public final static int MODE = DATABASE_MODE_QUERIES;

    public MySuggestionProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }
}
