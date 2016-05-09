package ducere.lechal.pod.sqlite;

import android.provider.BaseColumns;

/**
 * Created by sunde on 28-04-2016.
 */
public class TablesColumns {
    public TablesColumns() {
    }

    private static final String PRIMARY_KEY = " PRIMARY KEY";
    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INTEGER";
    private static final String BIGINT_TYPE = " BIGINT";
    private static final String COMMA_SEP = ",";

    public static abstract class TagEntry implements BaseColumns {
        public static final String TABLE_NAME = "Tags";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_ADDRESS = "address";
        public static final String COLUMN_NAME_MOCK_TITLE = "mockTitle";
        public static final String COLUMN_NAME_TYPE = "type";
        public static final String COLUMN_NAME_SYNCED = "synced";
        public static final String COLUMN_NAME_JSON = "json";
        public static final String COLUMN_NAME_UPDATED = "updated";
        public static final String COLUMN_NAME_PLACE_ID = "placeId";
    }

    public static abstract class FitnessEntry implements BaseColumns {
        public static final String TABLE_NAME = "Fitness";
        public static final String COLUMN_NAME_FITNESS_ID = "fitnessID";
        public static final String COLUMN_NAME_JSON = "json";
        public static final String COLUMN_NAME_UPDATED = "updated";
    }

    static final String SQL_CREATE_TAGS =
            "CREATE TABLE " + TagEntry.TABLE_NAME + " (" +
                    TagEntry.COLUMN_NAME_PLACE_ID + TEXT_TYPE + PRIMARY_KEY + COMMA_SEP +
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

    static final String SQL_CREATE_FITNESS =
            "CREATE TABLE " + FitnessEntry.TABLE_NAME + " (" +
                    FitnessEntry.COLUMN_NAME_FITNESS_ID + BIGINT_TYPE + PRIMARY_KEY + COMMA_SEP +
                    FitnessEntry.COLUMN_NAME_JSON + TEXT_TYPE + COMMA_SEP +
                    FitnessEntry.COLUMN_NAME_UPDATED + BIGINT_TYPE +
                    " )";

}
