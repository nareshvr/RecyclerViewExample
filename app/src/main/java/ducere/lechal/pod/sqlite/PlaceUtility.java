package ducere.lechal.pod.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import ducere.lechal.pod.beans.Place;

import ducere.lechal.pod.podsdata.FitnessData;
import ducere.lechal.pod.podsdata.Session;

/**
 * Created by sunde on 28-04-2016.
 */
public class PlaceUtility {
    private Context context;
    private static PlaceUtility utility;

    public PlaceUtility(Context context) {
        this.context = context;
    }

    PlaceUtility getInstance(Context context) {
        if (utility == null) {
            utility = new PlaceUtility(context);
        }
        return utility;
    }

    SQLiteDatabase getReadableDatabase() {
        return new DatabaseHelper(context).getReadableDatabase();
    }

    SQLiteDatabase getWritableDatabase() {
        return new DatabaseHelper(context).getWritableDatabase();
    }

    public List<Place> getTags() {
        List<Place> places = new ArrayList<>();
        SQLiteDatabase readableDatabase = getReadableDatabase();
        Cursor cursor = readableDatabase.query(TablesColumns.TagEntry.TABLE_NAME, null, TablesColumns.TagEntry.COLUMN_NAME_TYPE+"=?", new String[]{"0"}, null, null, TablesColumns.TagEntry.COLUMN_NAME_UPDATED + " DESC");
        try {
            Gson gson = new Gson();
            if (cursor != null && cursor.moveToFirst() && cursor.getCount() > 0) {
                do {
                    Place place = gson.fromJson(cursor.getString(cursor.getColumnIndex(TablesColumns.TagEntry.COLUMN_NAME_JSON)), Place.class);
                    places.add(place);
                }
                while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            readableDatabase.close();
        }
        return places;
    }
    public List<Place> getHistory() {
        List<Place> places = new ArrayList<>();
        SQLiteDatabase readableDatabase = getReadableDatabase();
        Cursor cursor = readableDatabase.query(TablesColumns.TagEntry.TABLE_NAME, null, TablesColumns.TagEntry.COLUMN_NAME_TYPE+"=?", new String[]{"1"}, null, null, TablesColumns.TagEntry.COLUMN_NAME_UPDATED + " DESC");
        try {
            Gson gson = new Gson();
            if (cursor != null && cursor.moveToFirst() && cursor.getCount() > 0) {
                do {
                    Place place = gson.fromJson(cursor.getString(cursor.getColumnIndex(TablesColumns.TagEntry.COLUMN_NAME_JSON)), Place.class);
                    places.add(place);
                }
                while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            readableDatabase.close();
        }
        return places;
    }

    public long putTag(Place place) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TablesColumns.TagEntry.COLUMN_NAME_PLACE_ID, place.getPlaceId());
        values.put(TablesColumns.TagEntry.COLUMN_NAME_TITLE, place.getTitle());
        values.put(TablesColumns.TagEntry.COLUMN_NAME_ADDRESS, place.getVicinity());
        values.put(TablesColumns.TagEntry.COLUMN_NAME_MOCK_TITLE, place.getMockName());
        values.put(TablesColumns.TagEntry.COLUMN_NAME_TYPE, place.getType());
        values.put(TablesColumns.TagEntry.COLUMN_NAME_SYNCED, place.isSynced() ? "1" : "0");
        values.put(TablesColumns.TagEntry.COLUMN_NAME_JSON, new Gson().toJson(place));
        values.put(TablesColumns.TagEntry.COLUMN_NAME_UPDATED, System.currentTimeMillis());
        long id = -1;
        try {
            id = writableDatabase.insertOrThrow(TablesColumns.TagEntry.TABLE_NAME, null, values);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            writableDatabase.close();
        }
        return id;
    }

    public int deleteTag(String placeID) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        try {
            return writableDatabase.delete(TablesColumns.TagEntry.TABLE_NAME, TablesColumns.TagEntry.COLUMN_NAME_PLACE_ID + "=?", new String[]{placeID});
        } finally {
            writableDatabase.close();
        }
    }

    /**
     * delete place then insert
     */
    public long updateTagWillDeleteAndInsert(Place place) {
        deleteTag(place.getPlaceId());
        return putTag(place);
    }

    public List<Place> getPlacesByMockName(String name) {
        List<Place> places = new ArrayList<>();
        SQLiteDatabase readableDatabase = getReadableDatabase();
        Cursor cursor = readableDatabase.query(TablesColumns.TagEntry.TABLE_NAME, null, TablesColumns.TagEntry.COLUMN_NAME_MOCK_TITLE + " LIKE ?", new String[]{name + "%"}, null, null, null);
        try {
            Gson gson = new Gson();
            if (cursor != null && cursor.moveToFirst() && cursor.getCount() > 0) {
                do {
                    Place place = gson.fromJson(cursor.getString(cursor.getColumnIndex(TablesColumns.TagEntry.COLUMN_NAME_JSON)), Place.class);
                    places.add(place);
                }
                while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            readableDatabase.close();
        }
        return places;
    }

    private long putFitnessData(FitnessData fitnessData) {
        ContentValues values = new ContentValues();
        values.put(TablesColumns.FitnessEntry.COLUMN_NAME_FITNESS_ID, fitnessData.getDay());
        values.put(TablesColumns.FitnessEntry.COLUMN_NAME_JSON, new Gson().toJson(fitnessData));
        values.put(TablesColumns.FitnessEntry.COLUMN_NAME_UPDATED, System.currentTimeMillis());
        SQLiteDatabase writableDatabase = getWritableDatabase();
        long id = -1;
        try {
            id = writableDatabase.insertOrThrow(TablesColumns.FitnessEntry.TABLE_NAME, null, values);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            writableDatabase.close();
        }
        return id;
    }

    public void updateFitnessWillDeleteAndInsert(FitnessData fitnessData) {
        deleteFitness(fitnessData.getDay());
        putFitnessData(fitnessData);
    }

    private int deleteFitness(long fitnessID) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        try {
            return writableDatabase.delete(TablesColumns.FitnessEntry.TABLE_NAME, TablesColumns.FitnessEntry.COLUMN_NAME_FITNESS_ID + "=?", new String[]{String.valueOf(fitnessID)});
        } finally {
            writableDatabase.close();
        }

    }

    public FitnessData getFitness(long fitnessID) {
        FitnessData fitnessData = new FitnessData();
        SQLiteDatabase readableDatabase = getReadableDatabase();
        Cursor cursor = readableDatabase.query(TablesColumns.FitnessEntry.TABLE_NAME, null, TablesColumns.FitnessEntry.COLUMN_NAME_FITNESS_ID + "=?", new String[]{String.valueOf(fitnessID)}, null, null, null);

        try {
            Gson gson = new Gson();
            if (cursor != null && cursor.moveToFirst() && cursor.getCount() > 0) {
                fitnessData = gson.fromJson(cursor.getString(cursor.getColumnIndex(TablesColumns.FitnessEntry.COLUMN_NAME_JSON)), FitnessData.class);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            readableDatabase.close();
        }
        return fitnessData;
    }

    private int deleteSession(long sessionID) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        try {
            return writableDatabase.delete(TablesColumns.SessionEntry.TABLE_NAME, TablesColumns.SessionEntry._ID + "=?", new String[]{String.valueOf(sessionID)});
        } finally {
            writableDatabase.close();
        }
    }

    public void updateSessionWillDeleteAndInsert(Session session) {
        deleteSession(session.getId());
        putSession(session);
    }

    private long putSession(Session session) {
        ContentValues values = new ContentValues();
        values.put(TablesColumns.SessionEntry._ID, session.getId());
        values.put(TablesColumns.SessionEntry.COLUMN_NAME_NAME, session.getName());
        values.put(TablesColumns.SessionEntry.COLUMN_NAME_ACTIVITY_TYPE, session.getActivityType());
        values.put(TablesColumns.SessionEntry.COLUMN_NAME_GOAL_TYPE, session.getGoalType());
        values.put(TablesColumns.SessionEntry.COLUMN_NAME_GOAL_VALUE, session.getGoalValue());
        values.put(TablesColumns.SessionEntry.COLUMN_NAME_MILESTONE_FREQ, session.getMilestoneFreq());
        values.put(TablesColumns.SessionEntry.COLUMN_NAME_STEPS, session.getSteps());
        values.put(TablesColumns.SessionEntry.COLUMN_NAME_CALORIES, session.getCalories());
        values.put(TablesColumns.SessionEntry.COLUMN_NAME_TIME, session.getTime());
        values.put(TablesColumns.SessionEntry.COLUMN_NAME_DISTANCE, session.getDistance());
        values.put(TablesColumns.SessionEntry.COLUMN_NAME_STATUS, session.getStatus());
        values.put(TablesColumns.SessionEntry.COLUMN_NAME_SYNC, session.isSync());
        values.put(TablesColumns.SessionEntry.COLUMN_NAME_START_TIME, session.getStartTime());
        values.put(TablesColumns.SessionEntry.COLUMN_NAME_END_TIME, session.getEndTime());

        SQLiteDatabase writableDatabase = getWritableDatabase();
        long id = -1;
        try {
            id = writableDatabase.insertOrThrow(TablesColumns.SessionEntry.TABLE_NAME, null, values);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            writableDatabase.close();
        }
        return id;
    }

    public Session getSession(long sessionID) {
        SQLiteDatabase readableDatabase = getReadableDatabase();
        Cursor cursor = readableDatabase.query(TablesColumns.SessionEntry.TABLE_NAME, null, TablesColumns.SessionEntry._ID + "=?", new String[]{String.valueOf(sessionID)}, null, null, null);
        try {
            if (cursor != null && cursor.moveToFirst() && cursor.getCount() > 0) {
                Session session = new Session();
                session.setId(cursor.getInt(cursor.getColumnIndex(TablesColumns.SessionEntry._ID)));
                session.setName(cursor.getString(cursor.getColumnIndex(TablesColumns.SessionEntry.COLUMN_NAME_NAME)));
                session.setActivityType(cursor.getInt(cursor.getColumnIndex(TablesColumns.SessionEntry.COLUMN_NAME_ACTIVITY_TYPE)));
                session.setGoalType(cursor.getInt(cursor.getColumnIndex(TablesColumns.SessionEntry.COLUMN_NAME_GOAL_TYPE)));
                session.setGoalValue(cursor.getInt(cursor.getColumnIndex(TablesColumns.SessionEntry.COLUMN_NAME_GOAL_VALUE)));
                session.setMilestoneFreq(cursor.getInt(cursor.getColumnIndex(TablesColumns.SessionEntry.COLUMN_NAME_MILESTONE_FREQ)));
                session.setSteps(cursor.getInt(cursor.getColumnIndex(TablesColumns.SessionEntry.COLUMN_NAME_STEPS)));
                session.setCalories(cursor.getInt(cursor.getColumnIndex(TablesColumns.SessionEntry.COLUMN_NAME_CALORIES)));
                session.setTime(cursor.getInt(cursor.getColumnIndex(TablesColumns.SessionEntry.COLUMN_NAME_TIME)));
                session.setDistance(cursor.getInt(cursor.getColumnIndex(TablesColumns.SessionEntry.COLUMN_NAME_DISTANCE)));
                session.setStartTime(cursor.getLong(cursor.getColumnIndex(TablesColumns.SessionEntry.COLUMN_NAME_START_TIME)));
                session.setEndTime(cursor.getLong(cursor.getColumnIndex(TablesColumns.SessionEntry.COLUMN_NAME_END_TIME)));
                session.setStatus(cursor.getInt(cursor.getColumnIndex(TablesColumns.SessionEntry.COLUMN_NAME_STATUS)));
                session.setSync(cursor.getInt(cursor.getColumnIndex(TablesColumns.SessionEntry.COLUMN_NAME_SYNC)) == 0);
                return session;
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            readableDatabase.close();
        }
        return null;
    }
}
