package com.pb.criconet.model.AcademyModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class AttendanceReportViewChild implements Serializable {

    public String getAttendance_date() {
        return attendance_date;
    }

    public void setAttendance_date(String attendance_date) {
        this.attendance_date = attendance_date;
    }

    public String getPresent() {
        return Present;
    }

    public void setPresent(String present) {
        Present = present;
    }

    public String getAbsent() {
        return Absent;
    }

    public void setAbsent(String absent) {
        Absent = absent;
    }

    String attendance_date="";
String Present="";
String Absent="";

public AttendanceReportViewChild(JSONObject jsonObject){
    if(jsonObject.has("attendance_date")){
        try {
            this.attendance_date = jsonObject.getString("attendance_date");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    if(jsonObject.has("Present")){
        try {
            this.Present = jsonObject.getString("Present");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    if(jsonObject.has("Absent")){
        try {
            this.Absent = jsonObject.getString("Absent");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

}
