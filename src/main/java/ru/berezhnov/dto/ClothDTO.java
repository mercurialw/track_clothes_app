package ru.berezhnov.dto;

import lombok.Data;

@Data
public class ClothDTO {
    private Integer id;
    private String name;
    private String photoUrl;
    private String size;
    private ClothTypeDTO type;
    private PlaceDTO place;
}
