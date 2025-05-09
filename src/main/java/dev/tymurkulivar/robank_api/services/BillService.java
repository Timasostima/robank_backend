package dev.tymurkulivar.robank_api.services;

import dev.tymurkulivar.robank_api.dto.BillDTO;
import dev.tymurkulivar.robank_api.entities.Bill;
import dev.tymurkulivar.robank_api.entities.Category;
import dev.tymurkulivar.robank_api.entities.RobankUser;
import dev.tymurkulivar.robank_api.repositories.*;
import dev.tymurkulivar.robank_api.repositories.BillRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BillService {
    private final BillRepository billRepository;
    private final CategoryRepository categoryRepository;
    private final RobankUserRepository userRepository;

    public BillService(BillRepository billRepository, CategoryRepository categoryRepository, RobankUserRepository userRepository) {
        this.billRepository = billRepository;
        this.categoryRepository = categoryRepository; 
        this.userRepository = userRepository;
    }

    public List<BillDTO> getBills(String userUid) {
        List<Bill> categories = billRepository.findAllByUserUid(userUid);
        List<BillDTO> billDTOs = new ArrayList<>();
        for (Bill bill : categories) {
            BillDTO bDTO = new BillDTO(bill);
            billDTOs.add(bDTO);
        }
            return billDTOs;
    }

    public BillDTO createBill(BillDTO billDTO, String userId) {
        RobankUser user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        System.out.println("categoryID = " + billDTO.getCategoryId());
        Category category = categoryRepository.findById(billDTO.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
        Bill bill = new Bill(billDTO);
        bill.setCategory(category);
        bill.setUser(user);
        billRepository.save(bill);
        return new BillDTO(bill);
    }
}
