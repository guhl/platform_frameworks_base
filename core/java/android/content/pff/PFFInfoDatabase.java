package android.content.pff;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PFFInfoDatabase  extends SQLiteOpenHelper {

	public final static String TAG = PFFInfoDatabase.class.toString();
	
	public final static String DB_NAME = "pff_infos";
	public final static int DB_VERSION = 1;

	public final static String TABLE_LOCATIONS = "locations";
	public final static String FIELD_LOCATION_DESCRIPTION = "description";
	public final static String FIELD_LOCATION_LONGITUDE = "longitude";
	public final static String FIELD_LOCATION_LATITUDE = "latitude";
	public final static String FIELD_LOCATION_ALTITUDE = "altitude";

	public final static String ENCODING_ASCII = "ASCII";
	public final static String ENCODING_UTF8 = "UTF-8";
	public final static String ENCODING_ISO88591 = "ISO8859_1";

	public static final Object[] dbLock = new Object[0];
	
	public PFFInfoDatabase(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + TABLE_LOCATIONS
				+ " (_id INTEGER PRIMARY KEY, "
				+ FIELD_LOCATION_DESCRIPTION + " TEXT, "
				+ FIELD_LOCATION_LONGITUDE + " FLOAT, "
				+ FIELD_LOCATION_LATITUDE + " FLOAT, "
				+ FIELD_LOCATION_ALTITUDE + " FLOAT " + ")");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATIONS);
        // Create tables again
        onCreate(db);
	}

	/**
	 * Create a new default location.
	 */
	public LocationBean addDefaultLocation() {
		SQLiteDatabase db = this.getWritableDatabase();
		LocationBean loc = new LocationBean("gps",86.925278,27.988056,8848.0);
		long id = db.insert(TABLE_LOCATIONS, null, loc.getValues());
		db.close();
		loc.setId(id);	
		return loc;
	}

	
	/**
	 * Create a new location using the given parameters.
	 */
	public LocationBean addLocation(LocationBean loc) {
		SQLiteDatabase db = this.getWritableDatabase();
		long id = db.insert(TABLE_LOCATIONS, null, loc.getValues());
		db.close();
		loc.setId(id);	
		return loc;
	}
	
	/**
	 * Update a specific location by its <code>_id</code> value.
	 */
	public void updateLocation(LocationBean loc) {
		if (loc.getId() < 0)
			return;

		SQLiteDatabase db = this.getWritableDatabase();
		db.update(TABLE_LOCATIONS, loc.getValues(), "_id = ?", new String[] { String.valueOf(loc.getId()) });
		db.close();
	}

	/**
	 * Delete a specific location by its <code>_id</code> value.
	 */
	public void deleteLocation(LocationBean loc) {
		if (loc.getId() < 0)
			return;
		
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_LOCATIONS, "_id = ?", new String[] { String.valueOf(loc.getId()) });
		db.close();
	}

    /**
     * @param locationId
     * @return
     */
    public LocationBean findLocationById(long locationId) {
    	LocationBean location;

        synchronized (dbLock) {
                SQLiteDatabase db = getReadableDatabase();

                Cursor c = db.query(TABLE_LOCATIONS, null,
                                "_id = ?", new String[] { String.valueOf(locationId) },
                                null, null, null);
                location = getFirstLocationBean(c);
        }
        return location;
    }


    /**
     * @param description
     * @return
     */
    public LocationBean findLocationByDescription(String description) {
    	LocationBean loc;

        synchronized (dbLock) {
                SQLiteDatabase db = getReadableDatabase();

                Cursor c = db.query(TABLE_LOCATIONS, null,
                                FIELD_LOCATION_DESCRIPTION + "=?", new String[] { description },
                                null, null, null);
                loc = getFirstLocationBean(c);
        }
        return loc;
    }
    
    /**
     * @param c
     */
    private List<LocationBean> createLocationBeans(Cursor c) {
            List<LocationBean> locations = new LinkedList<LocationBean>();

            final int COL_ID = c.getColumnIndexOrThrow("_id"),
                    COL_DESCRIPTION = c.getColumnIndexOrThrow(FIELD_LOCATION_DESCRIPTION),
                    COL_LONGITUDE = c.getColumnIndexOrThrow(FIELD_LOCATION_LONGITUDE),
                    COL_LATITUDE = c.getColumnIndexOrThrow(FIELD_LOCATION_LATITUDE),
                    COL_ALTITUDE = c.getColumnIndexOrThrow(FIELD_LOCATION_ALTITUDE);


            while (c.moveToNext()) {
            	LocationBean location = new LocationBean();

            	location.setId(c.getLong(COL_ID));
            	location.setDescription(c.getString(COL_DESCRIPTION));
            	location.setLongitude(c.getDouble(COL_LONGITUDE));
            	location.setLatitude(c.getDouble(COL_LATITUDE));

            	locations.add(location);
            }

            return locations;
    }
    
    /**
     * @param c
     * @return
     */
    private LocationBean getFirstLocationBean(Cursor c) {
    	LocationBean location = null;

            List<LocationBean> locations = createLocationBeans(c);
            if (locations.size() > 0)
            	location = locations.get(0);

            c.close();

            return location;
    }
    
}
