package com.example.kinocms.entities.pages;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity
@Table(name = "pages")
public class Page {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotEmpty(message = "Name shouldn't be empty")
    @Size(min=2, max=30, message = "Name must be between 2 and 30 characters")
    private String name;
    @NotEmpty(message = "Description shouldn't be empty")
    private String description;
    private String photo;
    private boolean active;
    private boolean mainPage;

    public Page() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isMainPage() {
        return mainPage;
    }

    public void setMainPage(boolean mainPage) {
        this.mainPage = mainPage;
    }
}
