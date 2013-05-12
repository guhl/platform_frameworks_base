package android.content.pff;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

public class LocationBean implements Parcelable {
	private long id = -1;
	private String description;
	private double latitude;
	private double longitude;
	private double altitude;

	public static final Parcelable.Creator<LocationBean> CREATOR = new Parcelable.Creator<LocationBean>() {

		public LocationBean createFromParcel(Parcel in) {
		    return new LocationBean(in);
		}

		public LocationBean[] newArray(int size) {
		    return null;
		}

		};

	
	public LocationBean() {		
	}

	public LocationBean(Parcel in) {		
	    readFromParcel(in);
	}
	
	public LocationBean(String Description, double Longitude, double Latitude, double Altitude){
		this.description = Description;
		this.longitude = Longitude;
		this.latitude = Latitude;
		this.altitude = Altitude;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	public long getId() {
		return id;
	}

	public String getDescription() {
		return this.description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public Double getLatitude() {
		return this.latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return this.longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getAltitude() {
		return this.altitude;
	}
	public void setAltitude(Double altitude) {
		this.altitude = altitude;
	}

	public ContentValues getValues() {
		ContentValues values = new ContentValues();
		
		values.put(PFFInfoDatabase.FIELD_LOCATION_DESCRIPTION, description);
		values.put(PFFInfoDatabase.FIELD_LOCATION_LONGITUDE, longitude);
		values.put(PFFInfoDatabase.FIELD_LOCATION_LATITUDE, latitude);
		values.put(PFFInfoDatabase.FIELD_LOCATION_ALTITUDE, altitude);
		
		return values;
	}

	public boolean equals(Object o) {
		if (!(o instanceof LocationBean))
			return false;
		
		LocationBean loaction = (LocationBean)o;
		
		if (loaction.getDescription().equals(description)
			&& loaction.getLatitude() == latitude
			&& loaction.getLongitude() == longitude)
			return true;
			
		return false;
	}

	public int describeContents() {
	    return 0;
	}

	public void writeToParcel(Parcel out, int flags) {
		out.writeLong(id);
	    out.writeString(description);
	    out.writeDouble(latitude);
	    out.writeDouble(longitude);
	    out.writeDouble(altitude);
	}
	public void readFromParcel(Parcel in) {
		id = in.readLong();
	    description = in.readString();
	    latitude = in.readDouble();
	    longitude = in.readDouble();
	    altitude = in.readDouble();
	}	
}

