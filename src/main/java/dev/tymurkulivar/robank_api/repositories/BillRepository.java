package dev.tymurkulivar.robank_api.repositories;

import dev.tymurkulivar.robank_api.Entities.Bill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BillRepository extends JpaRepository<Bill, Long> {
    List<Bill> findAllByUserUid(String userUid);
}