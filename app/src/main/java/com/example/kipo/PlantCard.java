package com.example.kipo;

public class PlantCard {
    private int imagePlant;
    private int imagePlantState;
    private String plantName;
    private String plantDescription;

    public PlantCard() {
        imagePlant = R.drawable.cactus;
        imagePlantState = R.drawable.planthappy;
        plantName = "Plant";
        plantDescription = "I am a plant";
    }

    public PlantCard(int imagePlant, int imagePlantState, String plantName, String plantDescription) {
        this.imagePlant = imagePlant;
        this.imagePlantState = imagePlantState;
        this.plantName = plantName;
        this.plantDescription = plantDescription;
    }

    public int getImagePlant() {
        return imagePlant;
    }

    public void setImagePlant(int imagePlant) {
        this.imagePlant = imagePlant;
    }

    public int getImagePlantState() {
        return imagePlantState;
    }

    public void setImagePlantState(int imagePlantState) {
        this.imagePlantState = imagePlantState;
    }

    public String getPlantName() {
        return plantName;
    }

    public void setPlantName(String plantName) {
        this.plantName = plantName;
    }

    public String getPlantDescription() {
        return plantDescription;
    }

    public void setPlantDescription(String plantDescription) {
        this.plantDescription = plantDescription;
    }
}
