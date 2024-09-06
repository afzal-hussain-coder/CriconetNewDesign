package com.pb.criconet.model.AcademyModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class ScheduledListModel implements Serializable {

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAcademy_id() {
        return academy_id;
    }

    public void setAcademy_id(String academy_id) {
        this.academy_id = academy_id;
    }

    public String getHost_id() {
        return host_id;
    }

    public void setHost_id(String host_id) {
        this.host_id = host_id;
    }

    public String getSchedule_date() {
        return schedule_date;
    }

    public void setSchedule_date(String schedule_date) {
        this.schedule_date = schedule_date;
    }

    public String getSchedule_time() {
        return schedule_time;
    }

    public void setSchedule_time(String schedule_time) {
        this.schedule_time = schedule_time;
    }

    public String getSchedule_date_time() {
        return schedule_date_time;
    }

    public void setSchedule_date_time(String schedule_date_time) {
        this.schedule_date_time = schedule_date_time;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDuration_hrs() {
        return duration_hrs;
    }

    public void setDuration_hrs(String duration_hrs) {
        this.duration_hrs = duration_hrs;
    }

    public String getDuration_mins() {
        return duration_mins;
    }

    public void setDuration_mins(String duration_mins) {
        this.duration_mins = duration_mins;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getChanel_id() {
        return chanel_id;
    }

    public void setChanel_id(String chanel_id) {
        this.chanel_id = chanel_id;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    private String id="";
    private String academy_id="";
    private String host_id="";
    private String schedule_date="";
    private String schedule_time="";
    private String schedule_date_time="";
    private String duration="";
    private String duration_hrs="";
    private String duration_mins="";
    private String title="";
    private String chanel_id="";
    private String types="";
    private String created="";

    public String getJoin_btn() {
        return join_btn;
    }

    public void setJoin_btn(String join_btn) {
        this.join_btn = join_btn;
    }

    private String join_btn="";

    public ArrayList<String> getCoachIdsList() {
        return coachIdsList;
    }

    public void setCoachIdsList(ArrayList<String> coachIdsList) {
        this.coachIdsList = coachIdsList;
    }

    private ArrayList<String>coachIdsList = null;

    public ArrayList<String> getStudentsIdsList() {
        return studentsIdsList;
    }

    public void setStudentsIdsList(ArrayList<String> studentsIdsList) {
        this.studentsIdsList = studentsIdsList;
    }

    private ArrayList<String>studentsIdsList = null;

    public ArrayList<AcademySessionCoach> getAcademySessionCoachList() {
        return academySessionCoachList;
    }

    public void setAcademySessionCoachList(ArrayList<AcademySessionCoach> academySessionCoachList) {
        this.academySessionCoachList = academySessionCoachList;
    }

    private ArrayList<AcademySessionCoach>academySessionCoachList =null;

    public ArrayList<AcademySessionStudent> getAcademySessionStudentList() {
        return academySessionStudentList;
    }

    public void setAcademySessionStudentList(ArrayList<AcademySessionStudent> academySessionStudentList) {
        this.academySessionStudentList = academySessionStudentList;
    }

    private ArrayList<AcademySessionStudent>academySessionStudentList =null;




    public ScheduledListModel(JSONObject jsonObject){

        if(jsonObject.has("id")){
            try {
                this.id = jsonObject.getString("id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("chanel_id")){
            try {
                this.chanel_id = jsonObject.getString("chanel_id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("academy_id")){
            try {
                this.academy_id = jsonObject.getString("academy_id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("host_id")){
            try {
                this.host_id = jsonObject.getString("host_id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("duration")){
            try {
                this.duration = jsonObject.getString("duration");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("schedule_date")){
            try {
                this.schedule_date = jsonObject.getString("schedule_date");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("schedule_time")){
            try {
                this.schedule_time = jsonObject.getString("schedule_time");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("created")){
            try {
                this.created = jsonObject.getString("created");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("title")){
            try {
                this.title = jsonObject.getString("title");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("types")){
            try {
                this.types = jsonObject.getString("types");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("duration_hrs")){
            try {
                this.duration_hrs = jsonObject.getString("duration_hrs");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("duration_mins")){
            try {
                this.duration_mins = jsonObject.getString("duration_mins");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(jsonObject.has("coach_ids")){
            try {
                this.coachIdsList = getCoachIds(jsonObject.getJSONArray("coach_ids"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("student_ids")){
            try {
                this.studentsIdsList = getStudentsIds(jsonObject.getJSONArray("student_ids"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("join_btn")){
            try {
                this.join_btn = jsonObject.getString("join_btn");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(jsonObject.has("coach")){
            try {
                this.academySessionCoachList = getCoachList(jsonObject.getJSONArray("coach"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("student")){
            try {
                this.academySessionStudentList = getStudentList(jsonObject.getJSONArray("student"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static ArrayList<String> getCoachIds(JSONArray coachIdss){
        ArrayList<String>coachIds = new ArrayList<>();
        for(int i = 0; i<coachIdss.length();i++){
            try {
                coachIds.add(coachIdss.getString(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return  coachIds;
    }

    public static ArrayList<String> getStudentsIds(JSONArray studentsIds){
        ArrayList<String>studentsId = new ArrayList<>();
        for(int i = 0; i<studentsIds.length();i++){
            try {
                studentsId.add(studentsIds.getString(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return studentsId;
    }

    public static ArrayList<AcademySessionCoach> getCoachList(JSONArray coachJsonArray){
        ArrayList<AcademySessionCoach>academySessionCoach = new ArrayList<>();
        for(int i = 0; i<coachJsonArray.length();i++){
            try {
                academySessionCoach.add(new AcademySessionCoach(coachJsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return academySessionCoach;
    }

    public static ArrayList<AcademySessionStudent> getStudentList(JSONArray coachJsonArray){
        ArrayList<AcademySessionStudent>academySessionStudents = new ArrayList<>();
        for(int i = 0; i<coachJsonArray.length();i++){
            try {
                academySessionStudents.add(new AcademySessionStudent(coachJsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return academySessionStudents;
    }
}
