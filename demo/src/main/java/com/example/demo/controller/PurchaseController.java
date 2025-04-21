package com.example.demo.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.demo.dto.BookDTO;
import com.example.demo.dto.BookMapper;
import com.example.demo.dto.PurchaseDTO;
import com.example.demo.dto.PurchaseMapper;
import com.example.demo.dto.UserDTO;
import com.example.demo.dto.UserMapper;
import com.example.demo.model.Book;
import com.example.demo.model.Purchase;
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

    @Autowired
    PurchaseMapper purchaseMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    private BookMapper bookMapper;

    @GetMapping("/basket")
    public String basket(HttpSession session, Model model) {
        Integer purchaseId = (Integer) session.getAttribute("purchaseId");

        if (purchaseId != null) {
            List<Book> books = purchaseService.getBooksFromPurchase(purchaseId);
            model.addAttribute("purchaseBooks", books);
            if (!books.isEmpty()) {
                double purchaseTotalPrice = purchaseService.purchaseTotalPrice(books);
                model.addAttribute("totalPrice", purchaseTotalPrice);
            }
        }
        return "basket";
    }

    @PostMapping("/basket")
    public String basketProcess(HttpSession session, Model model, HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        if (principal == null) {
            return "redirect:/noLogged";
        }

        String name = principal.getName();
        UserDTO userDTO = userService.getUser(name);

        if (userDTO == null) {
            return "redirect:/noLogged";
        }

        Integer purchaseId = (Integer) session.getAttribute("purchaseId");
        if (purchaseId != null) {
            PurchaseDTO purchaseDTO = purchaseService.getPurchase(purchaseId);
            Purchase purchase = purchaseMapper.toDomain(purchaseDTO);
            purchase.setState("Pedido");
            purchase.setDate(LocalDateTime.now());
            List<Book> books = purchaseService.getBooksFromPurchase(purchaseId);
            purchaseDTO = purchaseMapper.toDTO(purchase);
            purchaseService.updatePurchase(purchaseDTO.id(), purchaseDTO, books, userDTO.id());
            session.setAttribute("purchaseId", null);
        }

        return "redirect:/";
    }

    @GetMapping("/noLogged")
    public String getNoLogged() {
        return "errorNoSessionBuy";
    }

    @PostMapping("/addToBasket")
    public String addToBasket(HttpSession session, Model model, HttpServletRequest request) {
        try {
            Integer purchaseId = (Integer) session.getAttribute("purchaseId");
            Integer bookId = (Integer) session.getAttribute("bookId");

            if (bookId == null) {
                model.addAttribute("error", "No se encontró el ID del libro en la sesión.");
                return "error";
            }

            BookDTO bookDTO = bookService.getBook(bookId);
            Book book = bookMapper.toDomain(bookDTO);

            if (purchaseId == null) {
                Purchase newPurchase = new Purchase();

                PurchaseDTO savedPurchase = purchaseService.createPurchase(purchaseMapper.toDTO(newPurchase),
                        List.of(book));
                session.setAttribute("purchaseId", savedPurchase.id());
            } else {
                PurchaseDTO purchaseDTO = purchaseService.getPurchase(purchaseId);
                Purchase purchase = purchaseMapper.toDomain(purchaseDTO);
                List<Book> books = purchaseService.getBooksFromPurchase(purchaseId);
                books.add(book);

                purchaseService.updatePurchase(purchase.getId(), purchaseMapper.toDTO(purchase), books);
            }

            return "redirect:/basket";

        } catch (Exception e) {
            model.addAttribute("error", "Error al añadir el libro a la cesta: " + e.getMessage());
            e.printStackTrace();
            return "error";
        }
    }

    @GetMapping("/myPurchases")
    public String myPurchases(HttpServletRequest request, Model model) {
        String name = request.getUserPrincipal().getName();
        UserDTO user = userService.getUser(name);
        if (user != null) {
            Collection<PurchaseDTO> purchases = purchaseService.getPurchases(user);
            if (purchases != null) {
                model.addAttribute("purchases", purchases);
            }
        }

        return "myPurchases";
    }

    @PostMapping("/deletePurchase/{id}")
    public String deletePurchase(@PathVariable int id) {
        PurchaseDTO purchase = purchaseService.getPurchase(id);
        if (purchase == null) {
            return "error";
        }
        purchaseService.deletePurchase(purchase.id());
        return "redirect:/myPurchases";
    }

}
