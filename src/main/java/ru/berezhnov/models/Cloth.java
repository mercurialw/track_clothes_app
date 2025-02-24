package ru.berezhnov.models;

import jakarta.persistence.*;

import java.util.ArrayList;

@Entity
@Table(name = "cloth")
public class Cloth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "photo_url")
    private String photoUrl;

    @ManyToOne
    @JoinColumn(name = "type", referencedColumnName = "id")
    private ClothType type;

    @ManyToOne
    @JoinColumn(name = "owner", referencedColumnName = "id")
    private User owner;

    @ManyToOne
    @JoinColumn(name = "place", referencedColumnName = "id")
    private Place place;

    @Column(name = "size")
    private String size;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ClothType getType() {
        return type;
    }

    public void setType(ClothType type) {
        this.type = type;
        if (this.type.getClothes() == null)
            this.type.setClothes(new ArrayList<>());
        this.type.getClothes().add(this);
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
        if (this.owner.getClothes() == null)
            this.owner.setClothes(new ArrayList<>());
        this.owner.getClothes().add(this);
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
        if (this.place.getClothes() == null)
            this.place.setClothes(new ArrayList<>());
        this.place.getClothes().add(this);
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
