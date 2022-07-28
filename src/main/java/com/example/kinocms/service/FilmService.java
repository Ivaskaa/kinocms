package com.example.kinocms.service;

import com.example.kinocms.entities.Film;
import com.example.kinocms.repository.FilmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

@Service
public class FilmService {
    @Autowired
    FilmRepository filmRepository;

    @Value("${upload.path}")
    private String uploadPath;

    public Page<Film> findPage(int pageNumber){
        Pageable pageable = PageRequest.of(pageNumber - 1,5);
        return filmRepository.findAll(pageable);
    }

    public Page<Film> findActivePage(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber - 1,5);
        return filmRepository.findActiveAll(pageable);
    }

    public String findCountFilms() {
        return filmRepository.findCountFilms();
    }

    public void saveFilm(Film filmForm, MultipartFile file) throws IOException {
        Film film = new Film();
        film.setName(filmForm.getName());
        film.setDescription(filmForm.getDescription());
        film.setTime(filmForm.getTime());
        film.setLink(filmForm.getLink());
        film.setUrlSEO(filmForm.getUrlSEO());
        film.setTitleSEO(filmForm.getTitleSEO());
        film.setKeywordsSEO(filmForm.getKeywordsSEO());
        film.setDescriptionSEO(filmForm.getDescriptionSEO());
        film.setActive(true);
        System.out.println(filmForm.getMovieRelease());
        film.setMovieRelease(filmForm.getMovieRelease());
        if(file != null && !file.getOriginalFilename().isEmpty()){      // якшо адмін передав файл
            File uploadDir = new File(uploadPath);
            if(!uploadDir.exists()){
                uploadDir.mkdirs();
            }
            String uuidFile = UUID.randomUUID().toString();      // randomUUID
            uuidFile += "." + file.getOriginalFilename();

            file.transferTo(new File(uploadPath + "/" + uuidFile));
            film.setPhoto(uuidFile);
        }
        filmRepository.save(film);
    }

    public void editFilm(Film filmForm, MultipartFile file, Long id) throws IOException {
        Film film = filmRepository.findById(id).orElseThrow(); // orElseThrow викидає виключення якшо запис був не найдений
        film.setName(filmForm.getName());
        film.setDescription(filmForm.getDescription());
        film.setTime(filmForm.getTime());
        film.setLink(filmForm.getLink());
        film.setUrlSEO(filmForm.getUrlSEO());
        film.setTitleSEO(filmForm.getTitleSEO());
        film.setKeywordsSEO(filmForm.getKeywordsSEO());
        film.setDescriptionSEO(filmForm.getDescriptionSEO());
        film.setActive(filmForm.isActive());
        film.setMovieRelease(filmForm.getMovieRelease());
        if(file != null && !file.getOriginalFilename().isEmpty()){
            File uploadDir = new File(uploadPath);
            if(!uploadDir.exists()){
                uploadDir.mkdirs();
            }
            String uuidFile = UUID.randomUUID().toString();
            uuidFile += "." + file.getOriginalFilename();

            file.transferTo(new File(uploadPath + "/" + uuidFile));
            film.setPhoto(uuidFile);
        }
        filmRepository.save(film);
    }

    public String dateValidation(Date date){
        if(date == null){
            return "Movie release date shouldn't be empty";
        }
        return null;
    }

    public String timeValidation(String time) {
        if(time.contains(":")) {
            String[] names = time.split(":");
            int i = 0;
            for (String s : names){
                i++;
            }
            if(i != 2){
                return "Time must be entered in XX:XX format";
            }
            if(names[0].length() <= 2){
                int hours = 0;
                try{
                    hours = Integer.parseInt(names[0]);
                } catch (Exception e){
                    return "Before : should be hours";
                }
                if (hours > 6){
                    return "Hours cannot be more than 6";
                }
            } else{
                return "Before : should be hours";
            }
            if(names[1].length() <= 2){
                int minutes = 0;
                try{
                    minutes = Integer.parseInt(names[1]);
                } catch (Exception e){
                    return "After : should be minutes";
                }
                if (minutes > 60){
                    return "Minutes cannot be more than 60";
                }
            } else{
                return "After : should be minutes";
            }
        } else {
            return "Time must be entered in XX:XX format";
        }
        return null;
    }



}
