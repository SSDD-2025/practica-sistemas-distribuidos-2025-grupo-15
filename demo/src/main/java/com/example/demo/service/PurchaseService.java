package com.example.demo.service;

import java.util.Collection;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.PurchaseDTO;
import com.example.demo.dto.PurchaseMapper;
import com.example.demo.dto.UserDTO;
import com.example.demo.dto.UserMapper;
import com.example.demo.model.Book;
import com.example.demo.model.Purchase;
import com.example.demo.model.User;
import com.example.demo.repository.PurchaseRepository;
import com.example.demo.repository.UserRepository;

@Service
public class PurchaseService {
    @Autowired
    PurchaseRepository purchaseRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PurchaseMapper purchaseMapper;

    @Autowired 
    UserMapper userMapper; 

    public Collection<PurchaseDTO> getPurchases() {
        return toDTOs(purchaseRepository.findAll());
    }

    public PurchaseDTO getPurchase(int id) {
        return toDTO(purchaseRepository.findById(id).orElseThrow());
    }

    public Collection<PurchaseDTO> getPurchases(UserDTO user) {
        return toDTOs(purchaseRepository.findAllByPurchaseUser(userMapper.toDomain(user)));
    }

    public PurchaseDTO createPurchase(PurchaseDTO purchaseDTO) {
        Purchase purchase = toDomain(purchaseDTO);
        purchaseRepository.save(purchase);
        return toDTO(purchase);
    }

    public PurchaseDTO createPurchase(PurchaseDTO purchaseDTO, Integer userID) {
        Purchase purchase = toDomain(purchaseDTO);
        if(userID == null) {
            purchase.setPurchaseUser(null);
        }
        else{
            User user = userRepository.findById(userID).orElseThrow();
            purchase.setPurchaseUser(user);
        }
        
        
        purchaseRepository.save(purchase);
        return toDTO(purchase);
    }

    public PurchaseDTO deletePurchase(int id) {
        Purchase purchase = purchaseRepository.findById(id).orElseThrow();
        purchaseRepository.deleteById(id);
        return toDTO(purchase);
    }

    public PurchaseDTO updatePurchase(int id, PurchaseDTO purchaseDTO) {
        if (purchaseRepository.existsById(id)) {
            Purchase updatePurchase = toDomain(purchaseDTO);
            updatePurchase.setId(id);
            purchaseRepository.save(updatePurchase);
            return toDTO(updatePurchase);
        } else {
            throw new NoSuchElementException();
        }
    }

    public double purchaseTotalPrice(PurchaseDTO purchase) {
        double totalPrice = 0;
        for (Book book : toDomain(purchase).getBooks()) {
            totalPrice = totalPrice + book.getPrice();
        }
        return totalPrice;
    }

    public void quitBookFromPurchases(Book book) {
        Collection<PurchaseDTO> allPurchases = this.getPurchases();
    
        for (PurchaseDTO purchaseDTO : allPurchases) {
            Purchase purchase = toDomain(purchaseDTO); 
            while (purchase.getBooks().contains(book)) {
                purchase.getBooks().remove(book);
            }
            purchaseRepository.save(purchase); 
        }
    }
    

    private PurchaseDTO toDTO(Purchase purchase) {
        return purchaseMapper.toDTO(purchase);
    }

    private Purchase toDomain(PurchaseDTO purchaseDTO) {
        return purchaseMapper.toDomain(purchaseDTO);
    }

    private Collection<PurchaseDTO> toDTOs(Collection<Purchase> purchases) {
        return purchaseMapper.toDTOs(purchases);
    }
}
