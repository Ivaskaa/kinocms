package com.example.kinocms.entities;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "films")
public class Film {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotEmpty(message = "Name shouldn't be empty")
    @Size(min=2, max=30, message = "Name must be between 2 and 30 characters")
    private String name;
    @NotEmpty(message = "Description shouldn't be empty")
    private String description;
    private String photo;
    @NotEmpty(message = "Trailer link shouldn't be empty")
    private String link;
    @NotEmpty(message = "Time shouldn't be empty")
    private String time;

    @DateTimeFormat( pattern = "yyyy-MM-dd" )
    private Date movieRelease;
    private boolean active;

    public Film(){}

    public Long getId() {
        return id;
    }

    public void setId(int Long) {
        this.id = id;
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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Date getMovieRelease() {
        return movieRelease;
    }

    public void setMovieRelease(Date movieRelease) {
        this.movieRelease = movieRelease;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

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
