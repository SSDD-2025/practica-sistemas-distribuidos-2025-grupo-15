package com.example.demo.service;

import java.util.Collection;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.PurchaseDTO;
import com.example.demo.dto.PurchaseMapper;
import com.example.demo.model.Book;
import com.example.demo.model.Purchase;
import com.example.demo.model.User;
import com.example.demo.repository.PurchaseRepository;

@Service
public class PurchaseService {
    @Autowired
    PurchaseRepository purchaseRepository;

    @Autowired
    PurchaseMapper purchaseMapper;

    public Collection<PurchaseDTO> getPurchases() {
        return toDTOs(purchaseRepository.findAll());
    }

    public PurchaseDTO getPurchase(int id) {
        return toDTO(purchaseRepository.findById(id).orElseThrow());
    }

    public Collection<PurchaseDTO> getPurchases(User user) {
        return toDTOs(purchaseRepository.findAllByPurchaseUser(user));
    }

    public PurchaseDTO createPurchase(PurchaseDTO purchaseDTO) {
        Purchase purchase = toDomain(purchaseDTO);
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

    public double purchaseTotalPrice(Purchase purchase) {
        double totalPrice = 0;
        for (Book book : purchase.getBooks()) {
            totalPrice = totalPrice + book.getPrice();
        }
        return totalPrice;
    }

    public void quitBookFromPurchases(Book book) {
        Collection<PurchaseDTO> allPurchases = this.getPurchases();
        for (PurchaseDTO purchase : allPurchases) {
            do {
                if (toDomain(purchase).getBooks().contains(book)) {
                    toDomain(purchase).getBooks().remove(book);
                }
            } while (toDomain(purchase).getBooks().contains(book));

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
