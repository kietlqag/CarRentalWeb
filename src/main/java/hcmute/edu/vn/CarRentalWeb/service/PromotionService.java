package hcmute.edu.vn.CarRentalWeb.service;

import hcmute.edu.vn.CarRentalWeb.entity.Promotion;
import hcmute.edu.vn.CarRentalWeb.repository.PromotionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PromotionService {
    @Autowired
    private PromotionRepository promotionRepository;

    public List<Promotion> getAllPromotion(){
         return promotionRepository.findAll();
    }
    public void save(Promotion promotion) {
        promotionRepository.save(promotion);
    }

    public Promotion getPromotionById(int id) {
        return promotionRepository.findById(id).orElseThrow(() -> new RuntimeException("Promotion not found"));
    }

    public void deletePromotionById(int id) {
        promotionRepository.deleteById(id);
    }

    public List<Promotion> getAllPromotionByTypes(List<Integer> types) {
        return promotionRepository.findAllByTypeIn(types);
    }

    public long countPromotionByTypes(List<Integer> types) {
        return promotionRepository.countByTypeIn(types);
    }
}
