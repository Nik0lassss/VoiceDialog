package com.example.user.voicedialog.models;

/**
 * Created by user on 15.05.2015.
 */
public class Image {
    private String id;
    private String image_src;
    private String description;
    private String local_path;


    public Image(String id, String image_src, String description, String local_path) {
        this.id = id;
        this.image_src = image_src;
        this.description = description;
        this.local_path = local_path;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage_src() {
        return image_src;
    }

    public void setImage_src(String image_src) {
        this.image_src = image_src;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocal_path() {
        return local_path;
    }

    public void setLocal_path(String local_path) {
        this.local_path = local_path;
    }
}
