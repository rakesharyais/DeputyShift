package com.deputyshift.UI.Model;

public class ShiftDetailsUIModel {

    public String mShiftStartTime;
    public String mShiftEndTime;
    public double mShiftStartLat;
    public double mShiftStartLon;
    public double mShiftEndLat;
    public double mShiftEndLon;
    public String mStartLocationAddress;
    public String mEndLocationAddress;
    public int mShiftId;
    public String mImage;

    public String getStartLocationAddress() {
        return mStartLocationAddress;
    }

    public void setStartLocationAddress(String mStartLocationAddress) {
        this.mStartLocationAddress = mStartLocationAddress;
    }

    public String getEndLocationAddress() {
        return mEndLocationAddress;
    }

    public void setEndLocationAddress(String mEndLocationAddress) {
        this.mEndLocationAddress = mEndLocationAddress;
    }

    public String getShiftStartTime() {
        return mShiftStartTime;
    }

    public void setShiftStartTime(String mShiftStartTime) {
        this.mShiftStartTime = mShiftStartTime;
    }

    public String getShiftEndTime() {
        return mShiftEndTime;
    }

    public void setshiftEndTime(String mShiftEndTime) {
        this.mShiftEndTime = mShiftEndTime;
    }

    public double getShiftStartLat() {
        return mShiftStartLat;
    }

    public void setShiftStartLat(double mShiftStartLat) {
        this.mShiftStartLat = mShiftStartLat;
    }

    public double getShiftStartLon() {
        return mShiftStartLon;
    }

    public void setShiftStartLon(double mShiftStartLon) {
        this.mShiftStartLon = mShiftStartLon;
    }

    public double getShiftEndLat() {
        return mShiftEndLat;
    }

    public void setShiftEndLat(double mShiftEndLat) {
        this.mShiftEndLat = mShiftEndLat;
    }

    public double getShiftEndLon() {
        return mShiftEndLon;
    }

    public void setShiftEndLon(double mShiftEndLon) {
        this.mShiftEndLon = mShiftEndLon;
    }

    public int getShiftId() {
        return mShiftId;
    }

    public void setShiftId(int mShiftId) {
        this.mShiftId = mShiftId;
    }

    public String getImage() {
        return mImage;
    }

    public void setImage(String mImage) {
        this.mImage = mImage;
    }
}
