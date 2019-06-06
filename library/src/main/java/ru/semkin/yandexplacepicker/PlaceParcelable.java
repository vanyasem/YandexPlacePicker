package ru.semkin.yandexplacepicker;

import android.os.Parcel;
import android.os.Parcelable;

import com.yandex.mapkit.GeoObject;
import com.yandex.mapkit.geometry.Point;

public class PlaceParcelable implements Parcelable {

    private String mName;
    private String mAddress;
    private double mLat;
    private double mLng;

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeString(mAddress);
        dest.writeDouble(mLat);
        dest.writeDouble(mLng);
    }

    private PlaceParcelable(Parcel in) {
        mName = in.readString();
        mAddress = in.readString();
        mLat = in.readDouble();
        mLng = in.readDouble();
    }

    public PlaceParcelable(GeoObject place) {
        mName = place.getName();
        mAddress = place.getDescriptionText();
        Point point = place.getGeometry().get(0).getPoint();
        mLat = point.getLatitude();
        mLng = point.getLongitude();
    }

    public PlaceParcelable() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PlaceParcelable> CREATOR = new Creator<PlaceParcelable>() {
        @Override
        public PlaceParcelable createFromParcel(Parcel in) {
            return new PlaceParcelable(in);
        }

        @Override
        public PlaceParcelable[] newArray(int size) {
            return new PlaceParcelable[size];
        }
    };

    public String getName() {
        return mName;
    }

    public String getAddress() {
        return mAddress;
    }

    public Point getPoint() {
        return new Point(mLat, mLng);
    }
}
