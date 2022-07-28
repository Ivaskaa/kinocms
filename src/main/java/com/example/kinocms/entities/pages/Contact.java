package com.example.kinocms.entities.pages;

import com.example.kinocms.entities.Cinema;
import com.example.kinocms.entities.City;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
@Entity
@Table(name = "contacts")
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @JoinColumn
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    private City city;
    @JoinColumn
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    private Cinema cinema;
    @Size(max=255, message = "Address must be less than 255 characters")
    @NotEmpty(message = "Address shouldn't be empty")
    private String address;

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

    public Contact() {}

    @Override
    public String toString() {
        return "Contact{" +
                "id=" + id +
                ", city=" + city +
                ", cinema=" + cinema +
                ", address='" + address + '\'' +
                ", active=" + active +
                '}';
    }
}
