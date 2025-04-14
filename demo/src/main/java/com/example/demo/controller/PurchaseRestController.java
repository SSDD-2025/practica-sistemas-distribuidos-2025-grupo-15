package com.example.demo.controller;

import com.example.demo.dto.PurchaseDTO;
import com.example.demo.service.PurchaseService;


import org.springframework.beans.factory.annotation.Autowired;
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


    // Get all purchases
    @GetMapping
    public Collection<PurchaseDTO> getPurchases() {

        return purchaseService.getPurchases();
    }

    // Get a specific purchase by ID
    @GetMapping("/{id}")
    public PurchaseDTO getPurchase(@PathVariable int id) {
       return purchaseService.getPurchase(id);
    }

    // Create a new purchase
    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<PurchaseDTO> createPurchase(@RequestBody PurchaseDTO purchaseDTO) {
        purchaseDTO = purchaseService.createPurchase(purchaseDTO);
        URI location = fromCurrentRequest().path("/purchase/{id}").buildAndExpand(purchaseDTO.id()).toUri();
        return ResponseEntity.created(location).body(purchaseDTO); 
    }

    // Update an existing purchase
    @PutMapping("/{id}")
    public PurchaseDTO updatePurchase(@PathVariable int id, @RequestBody PurchaseDTO updatedPurchaseDTO) {
        return purchaseService.updatePurchase(id, updatedPurchaseDTO);
    }

    // Delete a purchase
    @DeleteMapping("/{id}")
    public PurchaseDTO deletePurchase(@PathVariable int id) {
      return purchaseService.deletePurchase(id);
    }

    // Get all purchases for a specific user
    @GetMapping("/user/purchase/{id}")
    public Collection<PurchaseDTO> getPurchasesByUser(@PathVariable int id) {
        
        return purchaseService.getPurchases();
    
    }

}