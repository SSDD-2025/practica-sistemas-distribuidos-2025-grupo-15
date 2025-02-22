package com.example.demo.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.model.Book;
import com.example.demo.model.Purchase;
import com.example.demo.model.Review;
import com.example.demo.model.User;

import jakarta.annotation.PostConstruct;

@Component
public class DatabaseInitializer {

    @Autowired
    private BookService bookService;
    
    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private UserService userService;

    @PostConstruct
    public void init() throws IOException{
        
        /* Create some books */
         Book book1 = new Book(1, "Harry Potter y la piedra filosofal", "J.K Rowling",
         "Harry Potter nunca ha oído hablar de Hogwarts hasta que empiezan a caer cartas en el felpudo del número 4 de Privet Drive,...",
         16.10);

         Book book2 = new Book(2, "Los Juegos del Hambre 1", "Suzanne Collins",
         "Cuando Katniss Everdeen, una joven de dieciséis años se presenta voluntaria para ocupar el lugar de su hermana en los juegos,...",
         18.95);

         bookService.createBook(book1);
         bookService.createBook(book2);

         /* Create some users */
         User user1 = new User("Paula", "password");
         User user2 = new User("Lucía", "1234");
         User user3 = new User("Rubén", "contraseña");

         userService.createUser(user1);
         userService.createUser(user2);
         userService.createUser(user3);

         /* Create some reviews */
         Review review1 = new Review(user1, book1, "Esta muy bien y súper interesante se lo recomiendo a la gente que le gusta la Fantasia y el misterio");
         Review review2 = new Review(user3, book1, "Es un libro");

         reviewService.createReview(review1);
         reviewService.createReview(review2);

         /* Create some purchases */
         Purchase purchase1 = new Purchase(user1, new ArrayList<Book>(List.of(book1, book2)), LocalDateTime.of(2025, 2, 1, 12, 5, 0), "Enviado");
         Purchase purchase2 = new Purchase(user2, new ArrayList<Book>(List.of(book1)), LocalDateTime.of(2025, 1, 23, 7, 35, 0), "Enviado");

         purchaseService.createPurchase(purchase1);
         purchaseService.createPurchase(purchase2);
    }

}