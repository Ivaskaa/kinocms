package com.example.kinocms.service.pages;


import com.example.kinocms.repository.pages.PageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class PageService {
    @Autowired
    PageRepository pageRepository;
    @Value("${upload.path}")
    private String uploadPath;

    public Page<com.example.kinocms.entities.pages.Page> findPage(int pageNumber){
        Pageable pageable = PageRequest.of(pageNumber - 1,5);
        return pageRepository.findAll(pageable);
    }

    public Page<com.example.kinocms.entities.pages.Page> findActiveCommonPage(int currentPage) {
        Pageable pageable = PageRequest.of(currentPage - 1,5);
        return pageRepository.findActiveCommonPage(pageable);
    }

    public Page<com.example.kinocms.entities.pages.Page> findCommonPage(int pageNumber){
        Pageable pageable = PageRequest.of(pageNumber - 1,5);
        return pageRepository.findAllCommon(pageable);
    }

    public List<com.example.kinocms.entities.pages.Page> findMainPage() {
        return pageRepository.findAllMainPage();
    }

    public void savePage(com.example.kinocms.entities.pages.Page pageForm, MultipartFile file) throws IOException {
        com.example.kinocms.entities.pages.Page page = new com.example.kinocms.entities.pages.Page();
        page.setName(pageForm.getName());
        page.setDescription(pageForm.getDescription());
        page.setMainPage(pageForm.isMainPage());
        page.setUrlSEO(pageForm.getUrlSEO());
        page.setTitleSEO(pageForm.getTitleSEO());
        page.setKeywordsSEO(pageForm.getKeywordsSEO());
        page.setDescriptionSEO(pageForm.getDescriptionSEO());
        page.setActive(true);
        if(file != null && !file.getOriginalFilename().isEmpty()){      // якшо адмін передав файл
            File uploadDir = new File(uploadPath);
            if(!uploadDir.exists()){
                uploadDir.mkdirs();
            }
            String uuidFile = UUID.randomUUID().toString();      // randomUUID
            uuidFile += "." + file.getOriginalFilename();

            file.transferTo(new File(uploadPath + "/" + uuidFile));
            page.setPhoto(uuidFile);
        }
        pageRepository.save(page);
    }

    public void editPage(com.example.kinocms.entities.pages.Page pageForm, MultipartFile file, Long id) throws IOException {
        com.example.kinocms.entities.pages.Page page = pageRepository.findById(id).orElseThrow(); // orElseThrow викидає виключення якшо запис був не найдений
        page.setName(pageForm.getName());
        page.setDescription(pageForm.getDescription());
        page.setMainPage(pageForm.isMainPage());
        page.setUrlSEO(pageForm.getUrlSEO());
        page.setTitleSEO(pageForm.getTitleSEO());
        page.setKeywordsSEO(pageForm.getKeywordsSEO());
        page.setDescriptionSEO(pageForm.getDescriptionSEO());
        page.setActive(pageForm.isActive());
        if(file != null && !file.getOriginalFilename().isEmpty()){
            File uploadDir = new File(uploadPath);
            if(!uploadDir.exists()){
                uploadDir.mkdirs();
            }
            String uuidFile = UUID.randomUUID().toString();
            uuidFile += "." + file.getOriginalFilename();

            file.transferTo(new File(uploadPath + "/" + uuidFile));
            page.setPhoto(uuidFile);
        }
        pageRepository.save(page);
    }
}
