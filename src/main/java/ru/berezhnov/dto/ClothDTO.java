package ru.berezhnov.dto;

public class ClothDTO {
    private int id;
    private String name;
    private String photoUrl;
    private String size;
    private ClothTypeDTO type;
    private PlaceDTO place;
    private UserDTO owner;

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

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UserDTO getOwner() {
        return owner;
    }

    public void setOwner(UserDTO owner) {
        this.owner = owner;
    }
}
