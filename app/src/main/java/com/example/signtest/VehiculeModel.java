package com.example.signtest;

public class VehiculeModel {
    private String VehiculeName, VehiculeModel, VehiculeNumber, VehiculeLongitude, VehiculeLatitude, VehiculeImage, VehiculeDocId, CurrentUserID, carRealAddress;

    public VehiculeModel() {
    }

    public VehiculeModel(String vehiculeName, String vehiculeModel, String vehiculeNumber, String vehiculeLongitude, String vehiculeLatitude, String vehiculeImage, String vehiculeDocId, String currentUserID, String carRealAddress) {
        VehiculeName = vehiculeName;
        VehiculeModel = vehiculeModel;
        VehiculeNumber = vehiculeNumber;
        VehiculeLongitude = vehiculeLongitude;
        VehiculeLatitude = vehiculeLatitude;
        VehiculeImage = vehiculeImage;
        VehiculeDocId = vehiculeDocId;
        CurrentUserID = currentUserID;
        this.carRealAddress = carRealAddress;
    }

    public String getVehiculeName() {
        return VehiculeName;
    }

    public void setVehiculeName(String vehiculeName) {
        VehiculeName = vehiculeName;
    }

    public String getVehiculeModel() {
        return VehiculeModel;
    }

    public void setVehiculeModel(String vehiculeModel) {
        VehiculeModel = vehiculeModel;
    }

    public String getVehiculeNumber() {
        return VehiculeNumber;
    }

    public void setVehiculeNumber(String vehiculeNumber) {
        VehiculeNumber = vehiculeNumber;
    }

    public String getVehiculeLongitude() {
        return VehiculeLongitude;
    }

    public void setVehiculeLongitude(String vehiculeLongitude) {
        VehiculeLongitude = vehiculeLongitude;
    }

    public String getVehiculeLatitude() {
        return VehiculeLatitude;
    }

    public void setVehiculeLatitude(String vehiculeLatitude) {
        VehiculeLatitude = vehiculeLatitude;
    }

    public String getVehiculeImage() {
        return VehiculeImage;
    }

    public void setVehiculeImage(String vehiculeImage) {
        VehiculeImage = vehiculeImage;
    }

    public String getVehiculeDocId() {
        return VehiculeDocId;
    }

    public void setVehiculeDocId(String vehiculeDocId) {
        VehiculeDocId = vehiculeDocId;
    }

    public String getCurrentUserID() {
        return CurrentUserID;
    }

    public void setCurrentUserID(String currentUserID) {
        CurrentUserID = currentUserID;
    }

    public String getCarRealAddress() {
        return carRealAddress;
    }

    public void setCarRealAddress(String carRealAddress) {
        this.carRealAddress = carRealAddress;
    }
}
