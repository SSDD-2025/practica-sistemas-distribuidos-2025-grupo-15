package com.example.demo.controller;

import com.example.demo.dto.PurchaseDTO;
import com.example.demo.dto.PurchaseMapper;
import com.example.demo.model.Purchase;
import com.example.demo.repository.PurchaseRepository;
import com.example.demo.service.PurchaseService;
import com.example.demo.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

import java.net.URI;
import java.util.Collection;

@RestController
@RequestMapping("/api/purchases")
public class PurchaseRestController {

    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private UserService userService;

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private PurchaseMapper purchaseMapper;


    @GetMapping("/")
    public Page<PurchaseDTO> getPurchases(Pageable pageable) {

        return purchaseRepository.findAll(pageable).map(this::toDTO);
    }

 
    @GetMapping("/{id}")
    public PurchaseDTO getPurchase(@PathVariable int id) {
       return purchaseService.getPurchase(id);
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<PurchaseDTO> createPurchase(@RequestBody PurchaseDTO purchaseDTO) {
        purchaseDTO = purchaseService.createPurchase(purchaseDTO);
        URI location = fromCurrentRequest().path("/purchase/{id}").buildAndExpand(purchaseDTO.id()).toUri();
        return ResponseEntity.created(location).body(purchaseDTO); 
    }

   
    @PutMapping("/{id}")
    public PurchaseDTO updatePurchase(@PathVariable int id, @RequestBody PurchaseDTO updatedPurchaseDTO) {
        return purchaseService.updatePurchase(id, updatedPurchaseDTO);
    }

    
    @DeleteMapping("/{id}")
    public PurchaseDTO deletePurchase(@PathVariable int id) {
      return purchaseService.deletePurchase(id);
    }

    @GetMapping("/user/{id}")
    public Page<PurchaseDTO> getPurchasesByUser(@PathVariable int id, Pageable pageable) {
        
        return purchaseRepository.findByPurchaseUser_Id(id, pageable).map(this::toDTO);
    
    }


    private PurchaseDTO toDTO(Purchase purchase){
        return purchaseMapper.toDTO(purchase);
    }

}