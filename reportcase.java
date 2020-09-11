package com.example.user.emergencyapps;

/**
 * Created by user on 8/7/2020.
 */

public class reportcase {
    private String Imageid;
    private String Report;
    private String Station;
    private String CrimeType;
    private String Myemail;

    public reportcase() {


    }

    public reportcase(String imageid, String report, String station, String crimeType, String myemail) {
        Imageid = imageid;
        Report = report;
        Station = station;
        CrimeType = crimeType;
        Myemail = myemail;
    }

    public String getImageid() {
        return Imageid;
    }

    public void setImageid(String imageid) {
        Imageid = imageid;
    }

    public String getReport() {
        return Report;
    }

    public void setReport(String report) {
        Report = report;
    }

    public String getStation() {
        return Station;
    }

    public void setStation(String station) {
        Station = station;
    }

    public String getCrimeType() {
        return CrimeType;
    }

    public void setCrimeType(String crimeType) {
        CrimeType = crimeType;
    }

    public String getMyemail() {
        return Myemail;
    }

    public void setMyemail(String myemail) {
        Myemail = myemail;
    }
}
