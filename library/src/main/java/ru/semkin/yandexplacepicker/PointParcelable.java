package ru.semkin.yandexplacepicker;

import android.os.Parcel;
import android.os.Parcelable;

public class PointParcelable implements Parcelable {

    private double mLatitude;
    private double mLongitude;

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(mLatitude);
        dest.writeDouble(mLongitude);
    }

    private PointParcelable(Parcel in) {
        mLatitude = in.readDouble();
        mLongitude = in.readDouble();
    }

    public PointParcelable(double latitude, double longitude) {
        this.mLatitude = latitude;
        this.mLongitude = longitude;
    }

    public PointParcelable() { }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PointParcelable> CREATOR = new Creator<PointParcelable>() {
        @Override
        public PointParcelable createFromParcel(Parcel in) {
            return new PointParcelable(in);
        }

        @Override
        public PointParcelable[] newArray(int size) {
            return new PointParcelable[size];
        }
    };

    public double getLatitude() {
        return this.mLatitude;
    }

    public double getLongitude() {
        return this.mLongitude;
    }
}
