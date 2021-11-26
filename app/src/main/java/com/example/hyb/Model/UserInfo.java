package com.example.hyb.Model;

import java.util.Date;

/**
 * Model for user. Does not contain login-info since firebase auth handles that
 */
public class UserInfo {
    private String Uid;
    private String firstName;
    private String lastName;
    private int phoneNumber;
    private Date birthDay;
    private String residentId;

    public UserInfo(String uid, String firstName, String lastName, int phoneNumber, Date birthDay, String residentId) {
        this.Uid = uid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.birthDay = birthDay;
        this.residentId = residentId;
    }

    public UserInfo() {

    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }

    public String getResidentId() {
        return residentId;
    }

    public void setResidentId(String residentId) {
        this.residentId = residentId;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNumber=" + phoneNumber +
                ", birthDay=" + birthDay +
                ", residentId='" + residentId + '\'' +
                '}';
    }
}
