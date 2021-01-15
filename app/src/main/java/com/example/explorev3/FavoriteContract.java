package com.example.explorev3;

import android.provider.BaseColumns;

public class FavoriteContract {

    public FavoriteContract() { }

    public static final class FavoriteEntry implements BaseColumns {
        public static final String TABLE_NAME = "favorite_list";
        public static final String COLLUMN_XID = "xid";
        public static final String COLLUMN_NAME = "name";
        public static final String COLLUMN_LAT = "lat";
        public static final String COLLUMN_LON = "lon";
        public static final String COLLUMN_RATING = "rating";
        public static final String COLUMN_TIMESTAMP = "timestamp";
    }

}
