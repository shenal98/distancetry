package com.techtop.distancetry;

public class Employee {
    private String name;
    private String phoneNumber;
    private String vehicleNo;
    public Employee(){}

    public Employee(String name, String phoneNumber, String vehicleNo) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.vehicleNo = vehicleNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }
}
