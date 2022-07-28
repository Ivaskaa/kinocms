package com.example.kinocms.service.pages;

import com.example.kinocms.entities.pages.Contact;
import com.example.kinocms.repository.pages.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ContactService {

    @Autowired
    ContactRepository contactRepository;

    public Page<Contact> findPage(int pageNumber){
        Pageable pageable = PageRequest.of(pageNumber - 1,5);
        return contactRepository.findAll(pageable);
    }

    public Page<Contact> findActivePage(int currentPage) {
        Pageable pageable = PageRequest.of(currentPage - 1,5);
        return contactRepository.findActiveAll(pageable);
    }

    public String validation(Contact contactForm) {
        if(contactForm.getAddress() == null){
            return "Address shouldn't be empty";
        }
        if(contactForm.getCity() == null){
            return "City shouldn't be empty";
        }
        if(contactForm.getCinema() == null){
            return "Cinema shouldn't be empty";
        }
        return null;
    }

    public void saveContact(Contact contactForm) {
        Contact contact = new Contact();
        contact.setCity(contactForm.getCity());
        contact.setCinema(contactForm.getCinema());
        contact.setAddress(contactForm.getAddress());
        contact.setActive(true);
        contact.setUrlSEO(contactForm.getUrlSEO());
        contact.setTitleSEO(contactForm.getTitleSEO());
        contact.setKeywordsSEO(contactForm.getKeywordsSEO());
        contact.setDescriptionSEO(contactForm.getDescriptionSEO());
        contactRepository.save(contact);
    }

    public void editContact(Contact contactForm, Long id) {
        Contact contact = contactRepository.findById(id).orElseThrow(); // orElseThrow викидає виключення якшо запис був не найдений
        contact.setCity(contactForm.getCity());
        contact.setCinema(contactForm.getCinema());
        contact.setAddress(contactForm.getAddress());
        contact.setActive(contactForm.isActive());
        contact.setUrlSEO(contactForm.getUrlSEO());
        contact.setTitleSEO(contactForm.getTitleSEO());
        contact.setKeywordsSEO(contactForm.getKeywordsSEO());
        contact.setDescriptionSEO(contactForm.getDescriptionSEO());
        contactRepository.save(contact);
    }


}
