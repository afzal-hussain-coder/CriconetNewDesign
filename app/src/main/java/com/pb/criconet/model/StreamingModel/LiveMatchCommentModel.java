package com.pb.criconet.model.StreamingModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class LiveMatchCommentModel {

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    public String getMatch_id() {
        return match_id;
    }

    public void setMatch_id(String match_id) {
        this.match_id = match_id;
    }

    public String getEmojiImage() {
        return emojiImage;
    }

    public void setEmojiImage(String emojiImage) {
        this.emojiImage = emojiImage;
    }

    public String getComment_text() {
        return comment_text;
    }

    public void setComment_text(String comment_text) {
        this.comment_text = comment_text;
    }

    public String getCreated_time() {
        return created_time;
    }

    public void setCreated_time(String created_time) {
        this.created_time = created_time;
    }

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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    private String comment_id="";
    private String match_id="";
    private String emojiImage="";
    private String comment_text="";
    private String created_time="";
    private String user_id="";
    private String name="";
    private String username="";
    private String avatar="";

    public int getParty_group_id() {
        return party_group_id;
    }

    public void setParty_group_id(int party_group_id) {
        this.party_group_id = party_group_id;
    }

    private int party_group_id;


    private String verified ="";
    private String criconet_verified="";


    public static final int simple_type = 0;
    public static final int emoji_type = 1;

    /*{"comment_id":4,
            "match_id":5,
            "emojiImage":"not-out-r.gif",
            "comment_text":"hhhhhhh",
            "type":1,
            "created_time":1709019079,
            "user_id":1387,
            "name":"singhsinghpooja",
            "avatar":"https:\/\/www.criconet.com\/upload\/photos\/2023\/12\/ftFOmamt6Cqw9TGeppgn_23_fe45c9a78c1eda35dd4a3ba832744284_avatar.jpg"}
*/

//    public String getVerified() {
//        return verified;
//    }
//
//    public void setVerified(String verified) {
//        this.verified = verified;
//    }
//
//    public String getCriconet_verified() {
//        return criconet_verified;
//    }
//
//    public void setCriconet_verified(String criconet_verified) {
//        this.criconet_verified = criconet_verified;
//    }







//    public ArrayList<TagUser> getSearchUserList() {
//        return searchUserList;
//    }
//
//    public void setSearchUserList(ArrayList<TagUser> searchUserList) {
//        this.searchUserList = searchUserList;
//    }
//
//    ArrayList<TagUser> searchUserList = null;


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int type;

    public LiveMatchCommentModel(JSONObject jsonObject){
        //this.type = type;



        if(jsonObject.has("comment_id")){
            try {
                this.comment_id = jsonObject.getString("comment_id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("match_id")){
            try {
                this.match_id = jsonObject.getString("match_id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

//        if(jsonObject.has("verified")){
//            try {
//                this.verified = jsonObject.getString("verified");
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//        if(jsonObject.has("criconet_verified")){
//            try {
//                this.criconet_verified = jsonObject.getString("criconet_verified");
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }


        if(jsonObject.has("emojiImage")){
            try {
                this.emojiImage = jsonObject.getString("emojiImage");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("comment_text")){
            try {
                this.comment_text = jsonObject.getString("comment_text");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("created_time")){
            try {
                this.created_time = jsonObject.getString("created_time");
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
        if(jsonObject.has("avatar")){
            try {
                this.avatar = jsonObject.getString("avatar");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

//        if(jsonObject.has("tagsusers")){
//            try {
//                this.searchUserList = getSearchUserList(jsonObject.getJSONArray("tagsusers"));
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
        if(jsonObject.has("type")){
            try {
                this.type = jsonObject.getInt("type");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private ArrayList<TagUser> getSearchUserList(JSONArray jsonArray) {
        ArrayList<TagUser> academySlots = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                academySlots.add(new TagUser(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return academySlots;
    }

    public class TagUser implements Serializable {
        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMatch_name() {
            return match_name;
        }

        public void setMatch_name(String match_name) {
            this.match_name = match_name;
        }

        private String username = "";
        private String name ="";
        private String match_name = "";

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        private String user_id="";

        public TagUser(JSONObject  jsonObject){
            if(jsonObject.has("username")){
                try {
                    this.username = jsonObject.getString("username");
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
            if(jsonObject.has("match_name")){
                try {
                    this.match_name = jsonObject.getString("match_name");
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

            if(jsonObject.has("party_group_id")){
                try {
                    party_group_id = jsonObject.getInt("party_group_id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }



        }

    }
}
