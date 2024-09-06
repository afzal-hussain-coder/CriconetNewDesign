package com.pb.criconet.model.AcademyModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class ManageCoachesModel implements Serializable {

    public String getProfile_title() {
        return profile_title;
    }

    public void setProfile_title(String profile_title) {
        this.profile_title = profile_title;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCoach_id() {
        return coach_id;
    }

    public void setCoach_id(String coach_id) {
        this.coach_id = coach_id;
    }

    public String getExp_years() {
        return exp_years;
    }

    public void setExp_years(String exp_years) {
        this.exp_years = exp_years;
    }

    public String getExp_months() {
        return exp_months;
    }

    public void setExp_months(String exp_months) {
        this.exp_months = exp_months;
    }

    public String getAchievement() {
        return achievement;
    }

    public void setAchievement(String achievement) {
        this.achievement = achievement;
    }

    public String getAbout_coach_profile() {
        return about_coach_profile;
    }

    public void setAbout_coach_profile(String about_coach_profile) {
        this.about_coach_profile = about_coach_profile;
    }

    public String getCharge_amount() {
        return charge_amount;
    }

    public void setCharge_amount(String charge_amount) {
        this.charge_amount = charge_amount;
    }

    public String getCuurency_code() {
        return cuurency_code;
    }

    public void setCuurency_code(String cuurency_code) {
        this.cuurency_code = cuurency_code;
    }

    public String getSkills_you_learn() {
        return skills_you_learn;
    }

    public void setSkills_you_learn(String skills_you_learn) {
        this.skills_you_learn = skills_you_learn;
    }

    public String getWhat_you_teach() {
        return what_you_teach;
    }

    public void setWhat_you_teach(String what_you_teach) {
        this.what_you_teach = what_you_teach;
    }

    public String getBooking_close_time() {
        return booking_close_time;
    }

    public void setBooking_close_time(String booking_close_time) {
        this.booking_close_time = booking_close_time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getMid_name() {
        return mid_name;
    }

    public void setMid_name(String mid_name) {
        this.mid_name = mid_name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getCat_title() {
        return cat_title;
    }

    public void setCat_title(String cat_title) {
        this.cat_title = cat_title;
    }

    public String getSelected_cat_id() {
        return selected_cat_id;
    }

    public void setSelected_cat_id(String selected_cat_id) {
        this.selected_cat_id = selected_cat_id;
    }

    public String getProfile_link() {
        return profile_link;
    }

    public void setProfile_link(String profile_link) {
        this.profile_link = profile_link;
    }

    public void setPresent(boolean present) {
        isPresent = present;
    }

    private String profile_title="";
    private String user_id ="";
    private String coach_id ="";
    private String exp_years ="";
    private String exp_months ="";
    private String achievement ="";
    private String about_coach_profile="";
    private String charge_amount ="";
    private String cuurency_code ="";
    private String skills_you_learn ="";
    private String what_you_teach ="";
    private String booking_close_time="";
    private String name ="";
    private String lang="";
    private String first_name ="";
    private String last_name ="";
    private String mid_name ="";
    private String username ="";
    private String avatar ="";
    private String cover ="";
    private String cat_title ="";
    private String selected_cat_id ="";
    private String profile_link ="";

    public String getVerified() {
        return verified;
    }

    public void setVerified(String verified) {
        this.verified = verified;
    }

    public String getCriconet_verified() {
        return criconet_verified;
    }

    public void setCriconet_verified(String criconet_verified) {
        this.criconet_verified = criconet_verified;
    }

    private String verified ="";
    private String criconet_verified="";



    public Integer getIs_owner() {
        return is_owner;
    }

    public void setIs_owner(Integer is_owner) {
        this.is_owner = is_owner;
    }

    private Integer is_owner;

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    private String pincode ="";

    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    public String getState_name() {
        return state_name;
    }

    public void setState_name(String state_name) {
        this.state_name = state_name;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    private String country_name="";
    private String state_name="";
    private String city_name ="";


    public ArrayList<Specializationcategory> getTeamsArrayList_info() {
        return teamsArrayList_info;
    }

    public void setTeamsArrayList_info(ArrayList<Specializationcategory> teamsArrayList_info) {
        this.teamsArrayList_info = teamsArrayList_info;
    }

    private ArrayList<Specializationcategory>teamsArrayList_info=null;

    public String getRoleJsonObject() {
        return roleJsonObject;
    }

    public void setRoleJsonObject(String roleJsonObject) {
        this.roleJsonObject = roleJsonObject;
    }

    private String roleJsonObject=null;

    public String getPhone_code() {
        return phone_code;
    }

    public void setPhone_code(String phone_code) {
        this.phone_code = phone_code;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCountry_id() {
        return country_id;
    }

    public void setCountry_id(String country_id) {
        this.country_id = country_id;
    }

    public String getState_id() {
        return state_id;
    }

    public void setState_id(String state_id) {
        this.state_id = state_id;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    private String phone_code ="";
    private String gender ="";
    private String country_id ="";
    private String state_id ="";
    private String city_id ="";
    private String address ="";
    private String address2 ="";


    /// dummy data..

    public String getSerial_no() {
        return serial_no;
    }

    public void setSerial_no(String serial_no) {
        this.serial_no = serial_no;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSessionTime() {
        return sessionTime;
    }

    public void setSessionTime(String sessionTime) {
        this.sessionTime = sessionTime;
    }

    private String serial_no="";
    private String userName="";
    private String phone="";
    private String email="";
    private String sessionTime="";


    public ManageCoachesModel(String serial_no, String userName, String name, String phone, String email,
                              String sessionTime){
        this.serial_no = serial_no;
        this.userName = userName;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.sessionTime = sessionTime;

    }


    public boolean isPresent() {
        return isPresent;
    }

    public void setCheck(boolean check) {
        isPresent = check;
    }

    private boolean isPresent=false;

    public boolean isAbsent() {
        return isAbsent;
    }

    public void setAbsent(boolean absent) {
        isAbsent = absent;
    }

    private boolean isAbsent=false;

    public ManageCoachesModel(JSONObject jsonObject){
        if(jsonObject.has("user_id")){
            try {
                this.user_id = jsonObject.getString("user_id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("coach_id")){
            try {
                this.coach_id = jsonObject.getString("coach_id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("verified")){
            try {
                this.verified = jsonObject.getString("verified");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("criconet_verified")){
            try {
                this.criconet_verified = jsonObject.getString("criconet_verified");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(jsonObject.has("exp_years")){
            try {
                this.exp_years = jsonObject.getString("exp_years");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("exp_months")){
            try {
                this.exp_months = jsonObject.getString("exp_months");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("achievement")){
            try {
                this.achievement = jsonObject.getString("achievement");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("about_coach_profile")){
            try {
                this.about_coach_profile = jsonObject.getString("about_coach_profile");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("charge_amount")){
            try {
                this.charge_amount = jsonObject.getString("charge_amount");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("cuurency_code")){
            try {
                this.cuurency_code = jsonObject.getString("cuurency_code");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("skills_you_learn")){
            try {
                this.skills_you_learn = jsonObject.getString("skills_you_learn");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("what_you_teach")){
            try {
                this.what_you_teach = jsonObject.getString("what_you_teach");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("booking_close_time")){
            try {
                this.booking_close_time = jsonObject.getString("booking_close_time");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("name")){
            try {
                this.name = jsonObject.getString("name");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("lang")){
            try {
                this.lang = jsonObject.getString("lang");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("first_name")){
            try {
                this.first_name = jsonObject.getString("first_name");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("last_name")){
            try {
                this.last_name = jsonObject.getString("last_name");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("mid_name")){
            try {
                this.mid_name = jsonObject.getString("mid_name");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("username")){
            try {
                this.username = jsonObject.getString("username");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("avatar")){
            try {
                this.avatar = jsonObject.getString("avatar");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("cover")){
            try {
                this.cover = jsonObject.getString("cover");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("cat_title")){
            try {
                this.cat_title = jsonObject.getString("cat_title");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("selected_cat_id")){
            try {
                this.selected_cat_id = jsonObject.getString("selected_cat_id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("profile_link")){
            try {
                this.profile_link = jsonObject.getString("profile_link");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("profile_title")){
            try {
                this.profile_title = jsonObject.getString("profile_title");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("email")){
            try {
                this.email = jsonObject.getString("email");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("phone_number")){
            try {
                this.phone = jsonObject.getString("phone_number");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("phone_code")){
            try {
                this.phone_code = jsonObject.getString("phone_code");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("gender")){
            try {
                this.gender = jsonObject.getString("gender");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("country_id")){
            try {
                this.country_id = jsonObject.getString("country_id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("state_id")){
            try {
                this.state_id = jsonObject.getString("state_id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("city_id")){
            try {
                this.city_id = jsonObject.getString("city_id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("address")){
            try {
                this.address = jsonObject.getString("address");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("address2")){
            try {
                this.address2 = jsonObject.getString("address2");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("Specialization_category")){
            try {
                this.teamsArrayList_info=getTeamsList(jsonObject.getJSONArray("Specialization_category"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(jsonObject.has("role_id")){
            try {
                this.roleJsonObject = jsonObject.getJSONObject("role_id").toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(jsonObject.has("country_name")){
            try {
                this.country_name=jsonObject.getString("country_name");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("state_name")){
            try {
                this.state_name=jsonObject.getString("state_name");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("city_name")){
            try {
                this.city_name=jsonObject.getString("city_name");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("pincode")){
            try {
                this.pincode=jsonObject.getString("pincode");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("is_owner")){
            try {
                this.is_owner=jsonObject.getInt("is_owner");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }



    }
    public static class  Specializationcategory implements Serializable{
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        private String id="";
        private String title ="";

        public Specializationcategory(JSONObject jsonObject){
            if(jsonObject.has("id")){
                try {
                    this.id= jsonObject.getString("id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if(jsonObject.has("title")){
                try {
                    this.title= jsonObject.getString("title");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public static ArrayList<Specializationcategory> getTeamsList(JSONArray jsonArray){

        ArrayList<Specializationcategory> teams=new ArrayList<>();
        for(int i=0;i<jsonArray.length();i++){
            try {
                teams.add(new Specializationcategory(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return teams;
    }



}
