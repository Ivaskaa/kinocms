package com.example.kinocms.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "halls")
public class Hall {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotEmpty(message = "Number shouldn't be empty")
    private String number;
    @Size(max=5000, message = "Description must be less than 5000 characters")
    @NotEmpty(message = "Description shouldn't be empty")
    private String description;
    private String scheme; // схема залу

    @JoinColumn(name = "cinema_id")
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JsonBackReference // щоб не було StackOverflowError
    private Cinema cinema;

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
    public Hall() {}


    @Override
    public String toString() {
        return "Hall{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", description='" + description + '\'' +
                ", scheme='" + scheme + '\'' +
                ", active=" + active +
                '}';
    }
}
