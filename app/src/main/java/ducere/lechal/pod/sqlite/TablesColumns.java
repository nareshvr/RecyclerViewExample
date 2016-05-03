package ducere.lechal.pod.sqlite;

import android.provider.BaseColumns;

/**
 * Created by sunde on 28-04-2016.
 */
public class TablesColumns {
    public TablesColumns() {
    }

    public static abstract class TagEntry implements BaseColumns {
        public static final String TABLE_NAME = "Tags";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_ADDRESS = "address";
        public static final String COLUMN_NAME_MOCK_TITLE = "mockTitle";
        public static final String COLUMN_NAME_TYPE = "type";
        public static final String COLUMN_NAME_SYNCED = "synced";
        public static final String COLUMN_NAME_JSON = "json";
        public static final String COLUMN_NAME_UPDATED = "updated";
    }

    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INTEGER";
    private static final String BIGINT_TYPE = " BIGINT";
    private static final String COMMA_SEP = ",";
    static final String SQL_CREATE_TAGS =
            "CREATE TABLE " + TagEntry.TABLE_NAME + " (" +
                    TagEntry._ID + " INTEGER AUTO_INCREMENT PRIMARY KEY" + COMMA_SEP +
                    TagEntry.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                    TagEntry.COLUMN_NAME_ADDRESS + TEXT_TYPE + COMMA_SEP +
                    TagEntry.COLUMN_NAME_MOCK_TITLE + TEXT_TYPE + COMMA_SEP +
                    TagEntry.COLUMN_NAME_TYPE + INT_TYPE + COMMA_SEP +
                    TagEntry.COLUMN_NAME_SYNCED + INT_TYPE + COMMA_SEP +
                    TagEntry.COLUMN_NAME_JSON + TEXT_TYPE + COMMA_SEP +
                    TagEntry.COLUMN_NAME_UPDATED + BIGINT_TYPE +
                    " )";

    private static final String SQL_DELETE_TAGS =
            "DROP TABLE IF EXISTS " + TagEntry.TABLE_NAME;
}
