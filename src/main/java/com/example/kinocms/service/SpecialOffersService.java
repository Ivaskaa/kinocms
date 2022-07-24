package com.example.kinocms.service;

import com.example.kinocms.entities.SpecialOffer;
import com.example.kinocms.repository.SpecialOffersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class SpecialOffersService {
    @Autowired
    SpecialOffersRepository specialOffersRepository;
    @Value("${upload.path}")
    private String uploadPath;

    public Page<SpecialOffer> findPage(int pageNumber){
        Pageable pageable = PageRequest.of(pageNumber - 1,5);
        return specialOffersRepository.findAll(pageable);
    }

    public Page<SpecialOffer> findActivePage(int currentPage) {
        Pageable pageable = PageRequest.of(currentPage - 1,5);
        return specialOffersRepository.findActiveAll(pageable);
    }

    public void saveSpecialOffer(SpecialOffer specialOfferForm, MultipartFile file) throws IOException {
        SpecialOffer specialOffer = new SpecialOffer();
        specialOffer.setName(specialOfferForm.getName());
        specialOffer.setDescription(specialOfferForm.getDescription());
        specialOffer.todayDate();
        specialOffer.setActive(true);
        if(file != null && !file.getOriginalFilename().isEmpty()){      // якшо адмін передав файл
            File uploadDir = new File(uploadPath);
            if(!uploadDir.exists()){  // якщо такої папки не існує
                uploadDir.mkdirs();
            }
            String uuidFile = UUID.randomUUID().toString();      // randomUUID
            uuidFile += "." + file.getOriginalFilename();

            file.transferTo(new File(uploadPath + "/" + uuidFile));
            specialOffer.setPhoto(uuidFile);
        }
        specialOffersRepository.save(specialOffer);
    }

    public void editSpecialOffer(SpecialOffer specialOfferForm, MultipartFile file, Long id) throws IOException {
        SpecialOffer specialOffer = specialOffersRepository.findById(id).orElseThrow(); // orElseThrow викидає виключення якшо запис був не найдений
        specialOffer.setName(specialOfferForm.getName());
        specialOffer.setDescription(specialOfferForm.getDescription());
        specialOffer.setActive(specialOfferForm.isActive());
        specialOffer.todayDate();
        if(file != null && !file.getOriginalFilename().isEmpty()){      // якшо адмін передав файл
            File uploadDir = new File(uploadPath);
            if(!uploadDir.exists()){  // якщо такої папки не існує
                uploadDir.mkdirs();
            }
            String uuidFile = UUID.randomUUID().toString();      // randomUUID
            uuidFile += "." + file.getOriginalFilename();

            file.transferTo(new File(uploadPath + "/" + uuidFile));
            specialOffer.setPhoto(uuidFile);
        }
        specialOffersRepository.save(specialOffer);
    }
}
