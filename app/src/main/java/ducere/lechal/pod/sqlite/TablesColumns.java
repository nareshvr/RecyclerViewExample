package ducere.lechal.pod.sqlite;

import android.provider.BaseColumns;

/**
 * Created by sunde on 28-04-2016.
 */
public class TablesColumns {
    public TablesColumns() {
    }

    private static final String CREATE_TABLE = "CREATE TABLE ";
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
    public static abstract class GroupJourney implements BaseColumns {
        public static final String TABLE_NAME = "GroupJourney";
        public static final String COLUMN_NAME_GROUP_ID = "groupId";
        public static final String COLUMN_NAME_JSON = "json";
        public static final String COLUMN_NAME_CREATED = "created";
    }

    public static abstract class SessionEntry implements BaseColumns {
        public static final String TABLE_NAME = "Session";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_ACTIVITY_TYPE = "activityType";
        public static final String COLUMN_NAME_GOAL_TYPE = "goalType";
        public static final String COLUMN_NAME_GOAL_VALUE = "goalValue";
        public static final String COLUMN_NAME_MILESTONE_FREQ = "milestoneFreq";
        public static final String COLUMN_NAME_STEPS = "steps";
        public static final String COLUMN_NAME_CALORIES = "calories";
        public static final String COLUMN_NAME_TIME = "time";
        public static final String COLUMN_NAME_DISTANCE = "distance";
        public static final String COLUMN_NAME_STATUS = "status";
        public static final String COLUMN_NAME_SYNC = "sync";
        public static final String COLUMN_NAME_START_TIME = "startTime";
        public static final String COLUMN_NAME_END_TIME = "endTime";
    }

    static final String SQL_CREATE_TAGS =
            CREATE_TABLE + TagEntry.TABLE_NAME + " (" +
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
            CREATE_TABLE + FitnessEntry.TABLE_NAME + " (" +
                    FitnessEntry.COLUMN_NAME_FITNESS_ID + BIGINT_TYPE + PRIMARY_KEY + COMMA_SEP +
                    FitnessEntry.COLUMN_NAME_JSON + TEXT_TYPE + COMMA_SEP +
                    FitnessEntry.COLUMN_NAME_UPDATED + BIGINT_TYPE +
                    " )";

    static final String SQL_CREATE_SESSIONS =
            CREATE_TABLE + SessionEntry.TABLE_NAME + " (" +
                    SessionEntry._ID + INT_TYPE + PRIMARY_KEY + COMMA_SEP +
                    SessionEntry.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                    SessionEntry.COLUMN_NAME_ACTIVITY_TYPE + INT_TYPE + COMMA_SEP +
                    SessionEntry.COLUMN_NAME_GOAL_TYPE + INT_TYPE + COMMA_SEP +
                    SessionEntry.COLUMN_NAME_GOAL_VALUE + INT_TYPE + COMMA_SEP +
                    SessionEntry.COLUMN_NAME_MILESTONE_FREQ + INT_TYPE + COMMA_SEP +
                    SessionEntry.COLUMN_NAME_STEPS + INT_TYPE + COMMA_SEP +
                    SessionEntry.COLUMN_NAME_CALORIES + INT_TYPE + COMMA_SEP +
                    SessionEntry.COLUMN_NAME_TIME + INT_TYPE + COMMA_SEP +
                    SessionEntry.COLUMN_NAME_DISTANCE + INT_TYPE + COMMA_SEP +
                    SessionEntry.COLUMN_NAME_START_TIME + BIGINT_TYPE + COMMA_SEP +
                    SessionEntry.COLUMN_NAME_END_TIME + BIGINT_TYPE + COMMA_SEP +
                    SessionEntry.COLUMN_NAME_STATUS + INT_TYPE + COMMA_SEP +
                    SessionEntry.COLUMN_NAME_SYNC + INT_TYPE +
                    " )";
    static final String SQL_CREATE_GROUP =
            CREATE_TABLE + GroupJourney.TABLE_NAME + " (" +
                    GroupJourney.COLUMN_NAME_GROUP_ID + TEXT_TYPE + PRIMARY_KEY + COMMA_SEP +
                    GroupJourney.COLUMN_NAME_JSON + TEXT_TYPE  + COMMA_SEP +
                    GroupJourney.COLUMN_NAME_CREATED + BIGINT_TYPE +
                    " )";

}
