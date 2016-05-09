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
}
