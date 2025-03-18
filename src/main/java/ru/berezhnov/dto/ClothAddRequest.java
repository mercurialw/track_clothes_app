package ru.berezhnov.dto;

import lombok.Data;

@Data
public class ClothAddRequest {
    private String name;
    private String photoUrl;
    private String size;
    private Integer type;
    private Integer place;
}
