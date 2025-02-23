package com.example.demo.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Book;
import com.example.demo.model.Purchase;
import com.example.demo.model.User;
import com.example.demo.repository.PurchaseRepository;

@Service
public class PurchaseService {
    @Autowired
    PurchaseRepository purchaseRepository;
    
    public Collection<Purchase> getPurchases(){
        return purchaseRepository.findAll();
    }

    public Purchase getPurchase(int id){
        return purchaseRepository.findById(id).get();
    }

    public Collection<Purchase> getPurchases(User user){
        return purchaseRepository.findAllByPurchaseUser(user);
    }

    public Purchase createPurchase(Purchase purchase){
        return purchaseRepository.save(purchase);
    }

    public boolean deleteReview(Purchase purchase){
        if (purchaseRepository.existsById(purchase.getId())){
            purchaseRepository.delete(purchaseRepository.findById(purchase.getId()).get());
            return true;
        }
        return false;
    }

    public boolean updateReview(Purchase purchase){
        if (purchaseRepository.existsById(purchase.getId())){
            purchaseRepository.save(purchase);
            return true;
        }
        return false;
    } 

    public double purchaseTotalPrice(Purchase purchase){
        double totalPrice = 0;
        for (Book book : purchase.getBooks()){
            totalPrice = totalPrice + book.getPrice();
        }
        return totalPrice;
    }
}
