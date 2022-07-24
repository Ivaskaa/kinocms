package com.example.kinocms.entities.pages;

import com.example.kinocms.entities.Cinema;
import com.example.kinocms.entities.City;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;


@Entity
@Table(name = "contacts")
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
//    @NotEmpty(message = "City shouldn't be empty")
    @JoinColumn
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    private City city;
    @JoinColumn
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    private Cinema cinema;
    @NotEmpty(message = "Address shouldn't be empty")
    private String address;

    private boolean active;

    public Contact() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Cinema getCinema() {
        return cinema;
    }

    public void setCinema(Cinema cinema) {
        this.cinema = cinema;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

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
