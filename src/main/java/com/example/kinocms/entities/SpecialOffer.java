package com.example.kinocms.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "special_offers")
public class SpecialOffer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotEmpty(message = "Name shouldn't be empty")
    @Size(max=255, message = "Name must be less than 255 characters")
    private String name;

    @Size(max=5000, message = "Description must be less than 5000 characters")
    @NotEmpty(message = "Description shouldn't be empty")
    private String description;

    private String photo;
    private Date date;

    @Size(max=255, message = "Url must be less than 255 characters")
    @NotEmpty(message = "Url shouldn't be empty")
    private String urlSEO;

    @Size(max=255, message = "Title must be less than 255 characters")
    @NotEmpty(message = "Title shouldn't be empty")
    private String titleSEO;

    @Size(max=255, message = "Keywords must be less than 255 characters")
    @NotEmpty(message = "Keywords shouldn't be empty")
    private String keywordsSEO;

    @Size(max=5000, message = "Description must be less than 5000 characters")
    @NotEmpty(message = "Description shouldn't be empty")
    private String descriptionSEO;

    private boolean active;

    public SpecialOffer() {}

    public void todayDate(){
        long time = System.currentTimeMillis();
        this.date = new Date(time);
    }
}
