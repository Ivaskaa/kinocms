package com.example.kinocms.entities;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.sql.Time;
import java.util.Date;

@Entity
@Table(name = "schedules")
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @JoinColumn
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    private Hall hall;
    @JoinColumn
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    private Film film;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;
    private String time;
    @NumberFormat(pattern = "#,###.##")
    private Double price;
    private boolean active;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

//    public Cinema getCinema() {
//        return cinema;
//    }
//
//    public void setCinema(Cinema cinema) {
//        this.cinema = cinema;
//    }

    public Hall getHall() {
        return hall;
    }

    public void setHall(Hall hall) {
        this.hall = hall;
    }

    public Film getFilm() {
        return film;
    }

    public void setFilm(Film film) {
        this.film = film;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "id=" + id +
                ", film=" + film +
                ", time=" + time +
                ", price=" + price +
                ", active=" + active +
                '}';
    }
}
