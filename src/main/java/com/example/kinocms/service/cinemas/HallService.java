package com.example.kinocms.service.cinemas;

import com.example.kinocms.entities.Cinema;
import com.example.kinocms.entities.Hall;
import com.example.kinocms.repository.cinemas.CinemaRepository;
import com.example.kinocms.repository.cinemas.HallRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class HallService {
    @Autowired
    HallRepository hallRepository;
    @Autowired
    CinemaRepository cinemaRepository;
    @Value("${upload.path}")
    private String uploadPath;

    public Page<Hall> findPage(int pageNumber){
        Pageable pageable = PageRequest.of(pageNumber - 1,5);
        return hallRepository.findAll(pageable);
    }

    public Page<Hall> findByCinemaId(Cinema cinema, int pageNumber){
        Pageable pageable = PageRequest.of(pageNumber - 1,5);
        return hallRepository.findAll(cinema, pageable);
    }

    public void saveHall(Hall hallForm, MultipartFile file, Long cinemaId) throws IOException {
        Hall hall = new Hall();
        hall.setNumber(hallForm.getNumber());
        hall.setDescription(hallForm.getDescription());
        hall.setActive(true);
        if(file != null && !file.getOriginalFilename().isEmpty()){      // якшо адмін передав файл
            File uploadDir = new File(uploadPath);
            if(!uploadDir.exists()){
                uploadDir.mkdirs();
            }
            String uuidFile = UUID.randomUUID().toString();      // randomUUID
            uuidFile += "." + file.getOriginalFilename();

            file.transferTo(new File(uploadPath + "/" + uuidFile));
            hall.setScheme(uuidFile);
        }
        Optional<Cinema> cinema = cinemaRepository.findById(cinemaId);
        Cinema fin = cinema.get();
        if(fin.getHalls() == null){
            Set<Hall> halls = new HashSet<>();
            halls.add(hall);
            fin.setHalls(halls);
        }else{
            fin.getHalls().add(hall);
        }
        hallRepository.save(hall);
    }

    public void editHall(Hall hallForm, MultipartFile file, Long hallId) throws IOException {
        Hall hall = hallRepository.findById(hallId).get();
        hall.setNumber(hallForm.getNumber());
        hall.setDescription(hallForm.getDescription());
        hall.setActive(hallForm.isActive());
        if(file != null && !file.getOriginalFilename().isEmpty()){      // якшо адмін передав файл
            File uploadDir = new File(uploadPath);
            if(!uploadDir.exists()){
                uploadDir.mkdirs();
            }
            String uuidFile = UUID.randomUUID().toString();      // randomUUID
            uuidFile += "." + file.getOriginalFilename();

            file.transferTo(new File(uploadPath + "/" + uuidFile));
            hall.setScheme(uuidFile);
        }
        hallRepository.save(hall);
    }

    public String numberValidation(String stringNumber) {
        int number;
        try {
            number = Integer.parseInt(stringNumber);
        } catch (Exception e){
            return "You must enter a number";
        }
        if (number <= 0 || number > 1000000) {
            return "The number must be greater than zero and less than 1000000";
        }
        return null;
    }

    public List<Hall> findHallsByCinema(Cinema cinema) {
        List<Hall> halls = hallRepository.findHallsByCinema(cinema);
        return halls;
    }
}
