package com.example.demo.controller;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.demo.model.Book;
import com.example.demo.model.Purchase;
import com.example.demo.model.User;
import com.example.demo.service.BookService;
import com.example.demo.service.PurchaseService;
import com.example.demo.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PurchaseController {
    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private UserService userService;

    @Autowired
    BookService bookService;

    @GetMapping("/basket")
    public String basket(HttpSession session, Model model) {
        Integer purchaseId = (Integer) session.getAttribute("purchaseId");

        if (purchaseId != null) {
            List<Book> books = purchaseService.getPurchase(purchaseId).getBooks();
            model.addAttribute("purchaseBooks", books);
            if (!books.isEmpty()) {
                double purchaseTotalPrice = purchaseService.purchaseTotalPrice(purchaseService.getPurchase(purchaseId));
                model.addAttribute("totalPrice", purchaseTotalPrice);
            }
        }
        return "basket";
    }

    @PostMapping("/basket")
    public String basketProcess(HttpSession session, Model model, HttpServletRequest request) {
        Integer purchaseId = (Integer) session.getAttribute("purchaseId");
        String name = request.getUserPrincipal().getName();
        User user = userService.getUser(name);

        if (user == null) {
            return "redirect:/noLogged";
        }
        if (purchaseId != null && user != null) {
            Purchase purchase = purchaseService.getPurchase(purchaseId);
            purchase.setPurchaseUser(user);
            purchase.setState("Pedido");
            purchase.setDate(LocalDateTime.now());
            purchaseService.updatePurchase(purchase);
            session.setAttribute("purchaseId", null);
        }
        return "redirect:/";
    }

    @GetMapping("/noLogged")
    public String getNoLogged() {
        return "errorNoSessionBuy";
    }

    @PostMapping("/addToBasket")
    public String getMethodName(HttpSession session, Model model) {
        Integer purchaseId = (Integer) session.getAttribute("purchaseId");
        Integer bookId = (Integer) session.getAttribute("bookId");
        Book book = bookService.getBook(bookId);
        if (purchaseId == null) {
            Purchase newPurchase = new Purchase();
            newPurchase.addBook(book);
            purchaseService.createPurchase(newPurchase);
            session.setAttribute("purchaseId", newPurchase.getId());
        } else {
            Purchase purchase = purchaseService.getPurchase(purchaseId);
            purchase.addBook(book);
            purchaseService.updatePurchase(purchase);

        }
        return "redirect:/basket";
    }

    @GetMapping("/myPurchases")
    public String myPurchases(HttpServletRequest request, Model model) {
        String name = request.getUserPrincipal().getName();
        User user = userService.getUser(name);
        if (user != null) {
            Collection<Purchase> purchases = purchaseService.getPurchases(user);
            if (purchases != null) {
                model.addAttribute("purchases", purchases);
            }
        }
        
        return "myPurchases";
    }

    @PostMapping("/deletePurchase/{id}")
    public String deletePurchase(@PathVariable int id) {
        Purchase purchase = purchaseService.getPurchase(id);
        if (purchase == null) {
            return "error";
        }
        purchaseService.deletePurchase(purchase);
        return "redirect:/myPurchases";
    }

}
