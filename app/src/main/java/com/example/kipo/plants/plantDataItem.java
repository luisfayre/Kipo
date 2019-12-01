package com.example.kipo.plants;

/**
 * Created by Luis Angel on 23/03/2017.
 */
public class plantDataItem {
    public String name;
    public String description;
    public String type;
    public String image;


    public plantDataItem(){

    }

    public plantDataItem(String name, String description, String type, String image) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
