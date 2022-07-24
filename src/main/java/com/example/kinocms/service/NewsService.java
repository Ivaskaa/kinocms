package com.example.kinocms.service;

import com.example.kinocms.entities.News;
import com.example.kinocms.repository.NewsRepository;
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
public class NewsService {
    @Autowired
    NewsRepository newsRepository;

    @Value("${upload.path}")
    private String uploadPath;

    public Page<News> findPage(int pageNumber){
        Pageable pageable = PageRequest.of(pageNumber - 1,5);
        return newsRepository.findAll(pageable);
    }

    public Page<News> findActivePage(int currentPage) {
        Pageable pageable = PageRequest.of(currentPage - 1,5);
        return newsRepository.findActiveAll(pageable);
    }

    public void saveNews(News newsForm, MultipartFile file) throws IOException {
        News news = new News();
        news.setName(newsForm.getName());
        news.setDescription(newsForm.getDescription());
        news.setLink(newsForm.getLink());
        news.todayDate();
        news.setActive(true);
        if(file != null && !file.getOriginalFilename().isEmpty()){      // якшо адмін передав файл
            File uploadDir = new File(uploadPath);
            if(!uploadDir.exists()){
                uploadDir.mkdirs();
            }
            String uuidFile = UUID.randomUUID().toString();      // randomUUID
            uuidFile += "." + file.getOriginalFilename();

            file.transferTo(new File(uploadPath + "/" + uuidFile));
            news.setPhoto(uuidFile);
        }
        newsRepository.save(news);
    }

    public void editNews(News newsForm, MultipartFile file, Long id) throws IOException {
        News news = newsRepository.findById(id).orElseThrow(); // orElseThrow викидає виключення якшо запис був не найдений
        news.setName(newsForm.getName());
        news.setDescription(newsForm.getDescription());
        news.setLink(newsForm.getLink());
        news.setActive(newsForm.isActive());
        news.todayDate();
        if(file != null && !file.getOriginalFilename().isEmpty()){
            File uploadDir = new File(uploadPath);
            if(!uploadDir.exists()){
                uploadDir.mkdirs();
            }
            String uuidFile = UUID.randomUUID().toString();
            uuidFile += "." + file.getOriginalFilename();

            file.transferTo(new File(uploadPath + "/" + uuidFile));
            news.setPhoto(uuidFile);
        }
        newsRepository.save(news);
    }


}
