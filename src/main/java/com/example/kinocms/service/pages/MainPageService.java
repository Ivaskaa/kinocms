package com.example.kinocms.service.pages;

import com.example.kinocms.entities.pages.MainPage;
import com.example.kinocms.repository.pages.MainPageRepository;
import com.example.kinocms.repository.pages.PageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MainPageService {
    @Autowired
    MainPageRepository mainPageRepository;

    public String numberValidation(String StringNumber) {
        int number;
        try {
            number = Integer.parseInt(StringNumber);
        } catch (Exception e){
            return "You must enter a number";
        }
        if (number <= 0 || number > 2000000000) {
            return "The number must be valid";
        }
        return null;
    }

    public void save(MainPage mainPageForm) {
        MainPage mainPage = mainPageRepository.findById(1L).get();
        mainPage.setFirstNumber(mainPageForm.getFirstNumber());
        mainPage.setSecondNumber(mainPageForm.getSecondNumber());
        mainPage.setTextSEO(mainPageForm.getTextSEO());
        mainPage.setUrlSEO(mainPageForm.getUrlSEO());
        mainPage.setTitleSEO(mainPageForm.getTitleSEO());
        mainPage.setKeywordsSEO(mainPageForm.getKeywordsSEO());
        mainPage.setDescriptionSEO(mainPageForm.getDescriptionSEO());
        mainPageRepository.save(mainPage);
    }
}
