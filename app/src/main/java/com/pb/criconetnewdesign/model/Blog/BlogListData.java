package com.pb.criconetnewdesign.model.Blog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class BlogListData implements Serializable {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPosted() {
        return posted;
    }

    public void setPosted(String posted) {
        this.posted = posted;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public String getShared() {
        return shared;
    }

    public void setShared(String shared) {
        this.shared = shared;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPublished_time() {
        return published_time;
    }

    public void setPublished_time(String published_time) {
        this.published_time = published_time;
    }

    public String getEmail_send() {
        return email_send;
    }

    public void setEmail_send(String email_send) {
        this.email_send = email_send;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCategory_link() {
        return category_link;
    }

    public void setCategory_link(String category_link) {
        this.category_link = category_link;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public boolean isIs_post_admin() {
        return is_post_admin;
    }

    public void setIs_post_admin(boolean is_post_admin) {
        this.is_post_admin = is_post_admin;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public ArrayList<String> getTagList() {
        return tagList;
    }

    public void setTagList(ArrayList<String> tagList) {
        this.tagList = tagList;
    }

    private String id = "";
    private String user = "";
    private String title = "";
    private String description = "";
    private String posted = "";
    private String category = "";
    private String thumbnail = "";
    private String view = "";
    private String shared = "";
    private String tags = "";
    private String status = "";
    private String published_time = "";
    private String email_send = "";
    private String url = "";
    private String category_link = "";
    private String category_name = "";
    private boolean is_post_admin;
    private Author author;
    private ArrayList<String> tagList = null;


    public BlogListData(JSONObject jsonObject) {
        if (jsonObject.has("id")) {
            try {
                this.id = jsonObject.getString("id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (jsonObject.has("user")) {
            try {
                this.user = jsonObject.getString("user");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (jsonObject.has("title")) {
            try {
                this.title = jsonObject.getString("title");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (jsonObject.has("description")) {
            try {
                this.description = jsonObject.getString("description");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (jsonObject.has("posted")) {
            try {
                this.posted = jsonObject.getString("posted");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (jsonObject.has("category")) {
            try {
                this.category = jsonObject.getString("category");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (jsonObject.has("thumbnail")) {
            try {
                this.thumbnail = jsonObject.getString("thumbnail");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (jsonObject.has("view")) {
            try {
                this.view = jsonObject.getString("view");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (jsonObject.has("shared")) {
            try {
                this.shared = jsonObject.getString("shared");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (jsonObject.has("tags")) {
            try {
                this.tags = jsonObject.getString("tags");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (jsonObject.has("status")) {
            try {
                this.status = jsonObject.getString("status");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (jsonObject.has("published_time")) {
            try {
                this.published_time = jsonObject.getString("published_time");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (jsonObject.has("email_send")) {
            try {
                this.email_send = jsonObject.getString("email_send");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (jsonObject.has("url")) {
            try {
                this.url = jsonObject.getString("url");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (jsonObject.has("category_link")) {
            try {
                this.category_link = jsonObject.getString("category_link");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (jsonObject.has("category_name")) {
            try {
                this.category_name = jsonObject.getString("category_name");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (jsonObject.has("is_post_admin")) {
            try {
                this.is_post_admin = jsonObject.getBoolean("is_post_admin");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (jsonObject.has("tags_array")) {
            try {
                this.tagList = getTagList(jsonObject.getJSONArray("tags_array"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (jsonObject.has("author")) {
            try {
                this.author = getAuthor(jsonObject.getJSONObject("author"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private Author getAuthor(JSONObject jsonObject) {
        return new Author(jsonObject);

    }

    public class Author implements Serializable {
        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
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

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

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

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        private String user_id = "";
        private String username = "";
        private String first_name = "";
        private String last_name = "";
        private String avatar = "";
        private String cover = "";
        private String gender = "";
        private String verified = "";
        private String criconet_verified = "";
        private String url = "";
        private String name = "";

        public Author(JSONObject jsonObject) {
            if (jsonObject.has("user_id")) {
                try {
                    this.user_id = jsonObject.getString("user_id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (jsonObject.has("username")) {
                try {
                    this.username = jsonObject.getString("username");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if (jsonObject.has("first_name")) {
                try {
                    this.first_name = jsonObject.getString("first_name");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if (jsonObject.has("last_name")) {
                try {
                    this.last_name = jsonObject.getString("last_name");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if (jsonObject.has("avatar")) {
                try {
                    this.avatar = jsonObject.getString("avatar");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if (jsonObject.has("cover")) {
                try {
                    this.cover = jsonObject.getString("cover");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if (jsonObject.has("gender")) {
                try {
                    this.gender = jsonObject.getString("gender");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if (jsonObject.has("verified")) {
                try {
                    this.verified = jsonObject.getString("verified");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if (jsonObject.has("criconet_verified")) {
                try {
                    this.criconet_verified = jsonObject.getString("criconet_verified");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if (jsonObject.has("url")) {
                try {
                    this.url = jsonObject.getString("url");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if (jsonObject.has("name")) {
                try {
                    this.name = jsonObject.getString("name");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    private ArrayList<String> getTagList(JSONArray jsonArray) {
        ArrayList<String> academySlots = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                academySlots.add(jsonArray.get(i).toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return academySlots;
    }

}
