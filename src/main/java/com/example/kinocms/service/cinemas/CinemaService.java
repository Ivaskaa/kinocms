package com.example.kinocms.service.cinemas;

import com.example.kinocms.entities.Cinema;
import com.example.kinocms.entities.City;
import com.example.kinocms.entities.Film;
import com.example.kinocms.repository.cinemas.CinemaRepository;
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
public class CinemaService {
    @Autowired
    CinemaRepository cinemaRepository;

    @Value("${upload.path}")
    private String uploadPath;

    public Page<Cinema> findPage(int pageNumber){
        Pageable pageable = PageRequest.of(pageNumber - 1,5);
        return cinemaRepository.findAll(pageable);
    }

    public Page<Cinema> findActivePage(int currentPage) {
        Pageable pageable = PageRequest.of(currentPage - 1,5);
        return cinemaRepository.findActive(pageable);
    }

    public List<Cinema> findCinemasByCity(City city){
        List<Cinema> cinemas = cinemaRepository.findCinemasByCity(city);
        return cinemas;
    }

    public String cityValidation(City city) {
        if(city == null){
            return "City shouldn't be empty";
        }
        return null;
    }

    public void saveCinema(Cinema cinemaForm, MultipartFile file) throws IOException {
        Cinema cinema = new Cinema();
        cinema.setName(cinemaForm.getName());
        cinema.setDescription(cinemaForm.getDescription());
        cinema.setCity(cinemaForm.getCity());
        cinema.setActive(true);
        if(file != null && !file.getOriginalFilename().isEmpty()){      // якшо адмін передав файл
            File uploadDir = new File(uploadPath);
            if(!uploadDir.exists()){
                uploadDir.mkdirs();
            }
            String uuidFile = UUID.randomUUID().toString();      // randomUUID
            uuidFile += "." + file.getOriginalFilename();

            file.transferTo(new File(uploadPath + "/" + uuidFile));
            cinema.setLogo(uuidFile);
        }
        cinemaRepository.save(cinema);
    }

    public void editCinema(Cinema cinemaForm, MultipartFile file, Long id) throws IOException {
        Cinema cinema = cinemaRepository.findById(id).orElseThrow(); // orElseThrow викидає виключення якшо запис був не найдений
        cinema.setName(cinemaForm.getName());
        cinema.setCity(cinemaForm.getCity());
        cinema.setActive(cinemaForm.isActive());
        cinema.setDescription(cinemaForm.getDescription());
        if(file != null && !file.getOriginalFilename().isEmpty()){
            File uploadDir = new File(uploadPath);
            if(!uploadDir.exists()){
                uploadDir.mkdirs();
            }
            String uuidFile = UUID.randomUUID().toString();
            uuidFile += "." + file.getOriginalFilename();

            file.transferTo(new File(uploadPath + "/" + uuidFile));
            cinema.setLogo(uuidFile);
        }
        cinemaRepository.save(cinema);
    }


}