package com.expresso.model;

/**
 * Created by Anirdesh_And0001 on 27-05-2015.
 */
public class LocationModel {
    String locationName,locationAddress,locationGeometry;

    public LocationModel(String locationName,String locationAddress,String locationGeometry)
    {
        this.locationName=locationName;
        this.locationAddress=locationAddress;
        this.locationGeometry=locationGeometry;
    }
    public String getLocationAddress() {
        return locationAddress;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationAddress(String locationAaddress) {
        this.locationAddress = locationAaddress;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getLocationGeometry() {
        return locationGeometry;
    }

    public void setLocationGeometry(String locationGeometry) {
        this.locationGeometry = locationGeometry;
    }
}
