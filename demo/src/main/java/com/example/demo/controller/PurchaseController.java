package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.model.Book;
import com.example.demo.model.Purchase;
import com.example.demo.service.PurchaseService;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@Controller
public class PurchaseController {
    @Autowired
    private PurchaseService purchaseService;

    @GetMapping("/basket")
    public String basket(HttpSession session, Model model) {
        Integer purchaseId = (Integer) session.getAttribute("purchaseId");
        if (purchaseId != null){
            List<Book> books = purchaseService.getPurchase(purchaseId).getBooks();
            model.addAttribute("purchaseBooks", books);
            if (!books.isEmpty()){
                double purchaseTotalPrice = purchaseService.purchaseTotalPrice(purchaseService.getPurchase(purchaseId));
                model.addAttribute("totalPrice", purchaseTotalPrice);
            }
        }
        return "basket";
    }

    @PostMapping("/basket")
    public String basketProcess(HttpSession session) {
        Integer purchaseId = (Integer) session.getAttribute("purchaseId");
        if (purchaseId != null){
            Purchase purchase = purchaseService.getPurchase(purchaseId);
            purchase.setState("Pedido");
            session.setAttribute("purchaseId", null);
        }
        return "redirect:/users";
    }
    
    
}
