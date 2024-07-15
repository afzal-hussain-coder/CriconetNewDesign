package com.pb.criconet.model.StreamingModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class LiveStreamingNewModel implements Serializable {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlayer_id() {
        return player_id;
    }

    public void setPlayer_id(String player_id) {
        this.player_id = player_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getScoring_link() {
        return scoring_link;
    }

    public void setScoring_link(String scoring_link) {
        this.scoring_link = scoring_link;
    }

    public String getTeam_a_name() {
        return team_a_name;
    }

    public void setTeam_a_name(String team_a_name) {
        this.team_a_name = team_a_name;
    }

    public String getTeam_b_name() {
        return team_b_name;
    }

    public void setTeam_b_name(String team_b_name) {
        this.team_b_name = team_b_name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public String getIs_match_started() {
        return is_match_started;
    }

    public void setIs_match_started(String is_match_started) {
        this.is_match_started = is_match_started;
    }

    public String getMatch_date() {
        return match_date;
    }

    public void setMatch_date(String match_date) {
        this.match_date = match_date;
    }

    public String getMatch_time() {
        return match_time;
    }

    public void setMatch_time(String match_time) {
        this.match_time = match_time;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPlayer_title() {
        return player_title;
    }

    public void setPlayer_title(String player_title) {
        this.player_title = player_title;
    }

    public String getFirst_player() {
        return first_player;
    }

    public void setFirst_player(String first_player) {
        this.first_player = first_player;
    }

    public String getFirst_player_type() {
        return first_player_type;
    }

    public void setFirst_player_type(String first_player_type) {
        this.first_player_type = first_player_type;
    }

    public String getFirst_player_link() {
        return first_player_link;
    }

    public void setFirst_player_link(String first_player_link) {
        this.first_player_link = first_player_link;
    }

    public String getFirst_player_status() {
        return first_player_status;
    }

    public void setFirst_player_status(String first_player_status) {
        this.first_player_status = first_player_status;
    }

    public String getSecond_player() {
        return second_player;
    }

    public void setSecond_player(String second_player) {
        this.second_player = second_player;
    }

    public String getSecond_player_type() {
        return second_player_type;
    }

    public void setSecond_player_type(String second_player_type) {
        this.second_player_type = second_player_type;
    }

    public String getSecond_player_link() {
        return second_player_link;
    }

    public void setSecond_player_link(String second_player_link) {
        this.second_player_link = second_player_link;
    }

    public String getIs_power() {
        return is_power;
    }

    public void setIs_power(String is_power) {
        this.is_power = is_power;
    }

    public String getPower_msg() {
        return power_msg;
    }

    public void setPower_msg(String power_msg) {
        this.power_msg = power_msg;
    }

    public String getStreaming_feedback_id() {
        return streaming_feedback_id;
    }

    public void setStreaming_feedback_id(String streaming_feedback_id) {
        this.streaming_feedback_id = streaming_feedback_id;
    }

    public String getIs_free_access() {
        return is_free_access;
    }

    public void setIs_free_access(String is_free_access) {
        this.is_free_access = is_free_access;
    }

    private String id;
    private String player_id;
    private String title;
    private String scoring_link ;
    private String team_a_name;
    private String team_b_name ;
    private String location ;
    private String country_id;
    private String state_id;
    private String city_id;
    private String is_match_started;
    private String match_date ;
    private String match_time;
    private String created ;
    private String user_id;
    private String status;
    private String player_title;
    private String first_player ;
    private String first_player_type;
    private String first_player_link;
    private String first_player_status;
    private String second_player;
    private String second_player_type;
    private String second_player_link;
    private String is_power;
    private String power_msg;
    private String streaming_feedback_id;
    private String is_free_access;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getIs_match_start_lebel() {
        return is_match_start_lebel;
    }

    public void setIs_match_start_lebel(String is_match_start_lebel) {
        this.is_match_start_lebel = is_match_start_lebel;
    }

    private String desc;
    private String cover;
    private String is_match_start_lebel;

    public String getSecond_player_status() {
        return second_player_status;
    }

    public void setSecond_player_status(String second_player_status) {
        this.second_player_status = second_player_status;
    }

    private String second_player_status="";

    public LiveStreamingNewModel(JSONObject jsonObject){
        if(jsonObject.has("id")){
            try {
                this.id = jsonObject.getString("id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("player_id")){
            try {
                this.player_id = jsonObject.getString("player_id");
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
        if(jsonObject.has("scoring_link")){
            try {
                this.scoring_link = jsonObject.getString("scoring_link");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("team_a_name")){
            try {
                this.team_a_name = jsonObject.getString("team_a_name");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("team_b_name")){
            try {
                this.team_b_name = jsonObject.getString("team_b_name");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("location")){
            try {
                this.location = jsonObject.getString("location");
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
        if(jsonObject.has("is_match_started")){
            try {
                this.is_match_started = jsonObject.getString("is_match_started");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("match_date")){
            try {
                this.match_date = jsonObject.getString("match_date");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("match_time")){
            try {
                this.match_time = jsonObject.getString("match_time");
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
        if(jsonObject.has("user_id")){
            try {
                this.user_id = jsonObject.getString("user_id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("status")){
            try {
                this.status = jsonObject.getString("status");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("player_title")){
            try {
                this.player_title = jsonObject.getString("player_title");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("first_player")){
            try {
                this.first_player = jsonObject.getString("first_player");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("first_player_type")){
            try {
                this.first_player_type = jsonObject.getString("first_player_type");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("first_player_link")){
            try {
                this.first_player_link = jsonObject.getString("first_player_link");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("first_player_status")){
            try {
                this.first_player_status = jsonObject.getString("first_player_status");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("second_player")){
            try {
                this.second_player = jsonObject.getString("second_player");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("second_player_type")){
            try {
                this.second_player_type = jsonObject.getString("second_player_type");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("second_player_link")){
            try {
                this.second_player_link = jsonObject.getString("second_player_link");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("is_power")){
            try {
                this.is_power = jsonObject.getString("is_power");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("power_msg")){
            try {
                this.power_msg = jsonObject.getString("power_msg");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("streaming_feedback_id")){
            try {
                this.streaming_feedback_id = jsonObject.getString("streaming_feedback_id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("is_free_access")){
            try {
                this.is_free_access = jsonObject.getString("is_free_access");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("desc")){
            try {
                this.desc = jsonObject.getString("desc");
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
        if(jsonObject.has("is_match_start_lebel")){
            try {
                this.is_match_start_lebel = jsonObject.getString("is_match_start_lebel");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(jsonObject.has("second_player_status")){
            try {
                this.second_player_status = jsonObject.getString("second_player_status");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
