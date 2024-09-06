package com.pb.criconet.model.AcademyModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class AcademyStudenModel implements Serializable {
    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String user_id="";
    private String name ="";
    private String username ="";
    private String first_name="";
    private String last_name="";
    private String mid_name ="";
    private String avatar="";
    private String cover ="";
    private String phone_number="";
    private String email ="";

    public String getCricheroes_profile_link() {
        return cricheroes_profile_link;
    }

    public void setCricheroes_profile_link(String cricheroes_profile_link) {
        this.cricheroes_profile_link = cricheroes_profile_link;
    }

    private String cricheroes_profile_link="";

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    private boolean isChecked;

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    private String gender ="";

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPlayer_type() {
        return player_type;
    }

    public void setPlayer_type(String player_type) {
        this.player_type = player_type;
    }

    public String getBatting_hand() {
        return batting_hand;
    }

    public void setBatting_hand(String batting_hand) {
        this.batting_hand = batting_hand;
    }

    public String getBowling_arm() {
        return bowling_arm;
    }

    public void setBowling_arm(String bowling_arm) {
        this.bowling_arm = bowling_arm;
    }

    public String getType_of_ball() {
        return type_of_ball;
    }

    public void setType_of_ball(String type_of_ball) {
        this.type_of_ball = type_of_ball;
    }

    public String getAge_group() {
        return age_group;
    }

    public void setAge_group(String age_group) {
        this.age_group = age_group;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    private String password ="";
    private String player_type="";
    private String batting_hand="";
    private String bowling_arm ="";
    private String type_of_ball ="";
    private String age_group="";
    private String student_id ="";




    public String getSeesion_time() {
        return seesion_time;
    }

    public void setSeesion_time(String seesion_time) {
        this.seesion_time = seesion_time;
    }

    public void setPresent(boolean present) {
        isPresent = present;
    }

    private String seesion_time ="";

    public boolean isPresent() {
        return isPresent;
    }


    private boolean isPresent=false;

    public boolean isAbsent() {
        return isAbsent;
    }

    public void setAbsent(boolean absent) {
        isAbsent = absent;
    }

    private boolean isAbsent=false;

   public AcademyStudenModel(JSONObject jsonObject){
       if(jsonObject.has("user_id")){
           try {
               this.user_id = jsonObject.getString("user_id");
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
       if(jsonObject.has("username")){
           try {
               this.username = jsonObject.getString("username");
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
       if(jsonObject.has("phone_number")){
           try {
               this.phone_number = jsonObject.getString("phone_number");
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
       if(jsonObject.has("seesion_time")){
           try {
               this.seesion_time = jsonObject.getString("seesion_time");
           } catch (JSONException e) {
               e.printStackTrace();
           }
       }
       if(jsonObject.has("password")){
           try {
               this.password = jsonObject.getString("password");
           } catch (JSONException e) {
               e.printStackTrace();
           }
       }

       if(jsonObject.has("cricheroes_profile_link")){
           try {
               this.cricheroes_profile_link = jsonObject.getString("cricheroes_profile_link");
           } catch (JSONException e) {
               e.printStackTrace();
           }
       }

       if(jsonObject.has("player_type")){
           try {
               this.player_type = jsonObject.getString("player_type");
           } catch (JSONException e) {
               e.printStackTrace();
           }
       }
       if(jsonObject.has("batting_hand")){
           try {
               this.batting_hand = jsonObject.getString("batting_hand");
           } catch (JSONException e) {
               e.printStackTrace();
           }
       }
       if(jsonObject.has("bowling_arm")){
           try {
               this.bowling_arm = jsonObject.getString("bowling_arm");
           } catch (JSONException e) {
               e.printStackTrace();
           }
       }
       if(jsonObject.has("type_of_ball")){
           try {
               this.type_of_ball = jsonObject.getString("type_of_ball");
           } catch (JSONException e) {
               e.printStackTrace();
           }
       }
       if(jsonObject.has("age_group")){
           try {
               this.age_group = jsonObject.getString("age_group");
           } catch (JSONException e) {
               e.printStackTrace();
           }
       }
       if(jsonObject.has("student_id")){
           try {
               this.student_id = jsonObject.getString("student_id");
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
   }

}
