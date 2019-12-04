package com.example.kipo;

public class infoPlantType {
    private String name;
    private String description;
    private String family;
    private String kingdom;

    public infoPlantType() {

    }

    public infoPlantType(String name, String description, String family, String kingdom) {
        this.name = name;
        this.description = description;
        this.family = family;
        this.kingdom = kingdom;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getKingdom() {
        return kingdom;
    }

    public void setKingdom(String kingdom) {
        this.kingdom = kingdom;
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
}
