package dev.tymurkulivar.robank_backend.controllers;

import dev.tymurkulivar.robank_backend.dto.BillDTO;
import dev.tymurkulivar.robank_backend.services.BillService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bills")
public class BillController {
    private final BillService billService;

    public BillController(BillService billService) {
        this.billService = billService;
    }

    @GetMapping
    public ResponseEntity<?> getBills(@AuthenticationPrincipal User authUser) {
        try {
            return ResponseEntity.ok(billService.getBills(authUser.getUsername()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createBill(@RequestBody BillDTO billDTO, @AuthenticationPrincipal User authUser) {
        try {
            return ResponseEntity.ok(billService.createBill(billDTO, authUser.getUsername()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}