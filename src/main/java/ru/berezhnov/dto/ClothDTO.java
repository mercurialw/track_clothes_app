package ru.berezhnov.dto;

public class ClothDTO {
    private String name;
    private String photoUrl;
    private ClothTypeDTO type;
    private PlaceDTO place;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public ClothTypeDTO getType() {
        return type;
    }

    public void setType(ClothTypeDTO type) {
        this.type = type;
    }

    public PlaceDTO getPlace() {
        return place;
    }

    public void setPlace(PlaceDTO place) {
        this.place = place;
    }
}
