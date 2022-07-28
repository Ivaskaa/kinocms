package com.example.kinocms.entities.pages;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
@Entity
@Table(name = "main_page")
public class MainPage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(max=255, message = "Number must be less than 255 characters")
    @NotEmpty(message = "Number shouldn't be empty")
    private String firstNumber;

    @Size(max=255, message = "Number must be less than 255 characters")
    @NotEmpty(message = "Number shouldn't be empty")
    private String secondNumber;

    @Size(max=255, message = "Text must be less than 255 characters")
    @NotEmpty(message = "Text shouldn't be empty")
    private String textSEO;

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
}
