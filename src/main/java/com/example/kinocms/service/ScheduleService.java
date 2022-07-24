package com.example.kinocms.service;

import com.example.kinocms.entities.Cinema;
import com.example.kinocms.entities.Film;
import com.example.kinocms.entities.Hall;
import com.example.kinocms.entities.Schedule;
import com.example.kinocms.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;

@Service
public class ScheduleService {
    @Autowired
    ScheduleRepository scheduleRepository;

    public Page<Schedule> findPage(int pageNumber){
        Pageable pageable = PageRequest.of(pageNumber - 1,5);
        return scheduleRepository.findAll(pageable);
    }

    public Page<Schedule> findActivePage(int currentPage) {
        Pageable pageable = PageRequest.of(currentPage - 1,10);
        return scheduleRepository.findActivePage(pageable);
    }

    public List<Schedule> findSchedulesByHall(Hall hall) {
        return scheduleRepository.findSchedulesByHall(hall);
    }

    public List<Schedule> findSchedulesByFilm(Film film) {
        return scheduleRepository.findSchedulesByFilm(film);
    }

    public void saveSchedule(Schedule scheduleForm, String priceString) {
        Schedule schedule = new Schedule();
        schedule.setHall(scheduleForm.getHall());
        schedule.setFilm(scheduleForm.getFilm());
        schedule.setPrice(Double.parseDouble(priceString));
        schedule.setDate(scheduleForm.getDate());
        schedule.setTime(scheduleForm.getTime());
        schedule.setActive(true);
        scheduleRepository.save(schedule);
    }

    public void editSchedule(Schedule scheduleForm, String priceString, Long id) {
        Schedule schedule = scheduleRepository.findById(id).orElseThrow();
        schedule.setHall(scheduleForm.getHall());
        schedule.setFilm(scheduleForm.getFilm());
        schedule.setPrice(Double.parseDouble(priceString));
        schedule.setDate(scheduleForm.getDate());
        schedule.setTime(scheduleForm.getTime());
        schedule.setActive(scheduleForm.isActive());
        scheduleRepository.save(schedule);
    }

    public String formattedDate(Schedule scheduleForm) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate;
        if(scheduleForm.getDate() == null) {
            formattedDate = "";
        } else {
            formattedDate = format.format(scheduleForm.getDate());
        }
        return formattedDate;
    }

    public String validation(Schedule scheduleForm, String priceString, Cinema cinema) {
        if(scheduleForm.getFilm() == null){
            return "Film shouldn't be empty";
        }
        if(cinema == null){
            return "Cinema shouldn't be empty";
        }
        if(scheduleForm.getHall() == null){
            return "Hall shouldn't be empty";
        }
        if(scheduleForm.getDate() == null){
            return "Date shouldn't be empty";
        }
        if(scheduleForm.getTime() == null || scheduleForm.getTime().equals("")){
            return "Time shouldn't be empty";
        }
        try{
            if(priceString == null || priceString.equals("")){
                return "Price shouldn't be empty";
            }
            Double price = Double.parseDouble(priceString);
        }catch (Exception e){
            return "Price shouldn't be in form XX.XX";
        }
        return null;
    }



}
