package vn.edu.tdtu.managestudent.Models;

import java.text.DateFormat;

public class Student {
    private String id;
    private String name;
    private  String gender;
    private String birth;
    private  String address;
    private String citizenID;
    private String idDB;



    public Student(String id, String name, String gender, String birth, String address, String citizenID,String idDB) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.birth = birth;
        this.address = address;
        this.citizenID = citizenID;
        this.idDB = idDB;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getAddress() {
        return address;
    }

    public String getIdDB() {
        return idDB;
    }

    public void setIdDB(String IDdb) {
        this.idDB = IDdb;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCitizenID() {
        return citizenID;
    }

    public void setCitizenID(String citizenID) {
        this.citizenID = citizenID;
    }
}
