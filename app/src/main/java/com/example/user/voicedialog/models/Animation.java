package com.example.user.voicedialog.models;

/**
 * Created by user on 15.05.2015.
 */
public class Animation {
    private String id;
    private String animation_src;
    private String desription;
    private String local_path;



    public Animation(String local_path, String id, String animation_src, String desription) {
        this.local_path = local_path;
        this.id = id;
        this.animation_src = animation_src;
        this.desription = desription;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAnimation_src() {
        return animation_src;
    }

    public void setAnimation_src(String animation_src) {
        this.animation_src = animation_src;
    }

    public String getDesription() {
        return desription;
    }

    public void setDesription(String desription) {
        this.desription = desription;
    }

    public String getLocal_path() {
        return local_path;
    }

    public void setLocal_path(String local_path) {
        this.local_path = local_path;
    }
}
