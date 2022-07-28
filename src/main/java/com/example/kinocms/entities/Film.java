package com.example.kinocms.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "films")
public class Film {
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
    @Size(max=255, message = "Trailer link must be less than 255 characters")
    @NotEmpty(message = "Trailer link shouldn't be empty")
    private String link;

    @NotEmpty(message = "Time shouldn't be empty")
    private String time;
    @DateTimeFormat( pattern = "yyyy-MM-dd" )
    private Date movieRelease;

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

    public Film(){}

    @Override
    public String toString() {
        return "Film{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", photo='" + photo + '\'' +
                ", link='" + link + '\'' +
                ", movieReleaseDate=" + movieRelease +
                ", active=" + active +
                '}';
    }
}
