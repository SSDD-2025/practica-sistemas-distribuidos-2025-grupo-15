package com.example.demo.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Blob;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.demo.dto.BookMapper;
import com.example.demo.dto.PurchaseMapper;
import com.example.demo.dto.ReviewMapper;
import com.example.demo.dto.UserMapper;
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

        @Autowired
        private UserMapper userMapper;

        @Autowired
        private BookMapper bookMapper;

        @Autowired
        private PurchaseMapper purchaseMapper;

        @Autowired
        private ReviewMapper reviewMapper;

        @Autowired
        PasswordEncoder passwordEncoder;

        @PostConstruct
        public void init() throws IOException {

                /* Load images */
                Blob image1 = loadImage("demo\\src\\main\\resources\\images\\Harry_Potter_img.jpg");
                Blob image2 = loadImage("demo\\src\\main\\resources\\images\\Los_Juegos_Del_Hambre_1.jpg");

                /* Create some books */
                Book book1 = new Book(1, "Harry Potter y la piedra filosofal", "J.K Rowling",
                                "Harry Potter nunca ha oído hablar de Hogwarts hasta que empiezan a caer cartas en el felpudo del número 4 de Privet Drive,...",
                                16.10, image1);

                Book book2 = new Book(2, "Los Juegos del Hambre 1", "Suzanne Collins",
                                "Cuando Katniss Everdeen, una joven de dieciséis años se presenta voluntaria para ocupar el lugar de su hermana en los juegos,...",
                                18.95, image2);

                bookService.createBook(bookMapper.toDTO(book1));
                bookService.createBook(bookMapper.toDTO(book2));

                /* Create some users */
                User user1 = new User("Paula", passwordEncoder.encode("password"),
                                new ArrayList<>(Arrays.asList("USER", "ADMIN")));
                User user2 = new User("Lucía", passwordEncoder.encode("1234"), new ArrayList<>(Arrays.asList("USER")));
                User user3 = new User("A", passwordEncoder.encode("1"), new ArrayList<>(Arrays.asList("USER")));

                userService.createUser(userMapper.toDTO(user1));
                userService.createUser(userMapper.toDTO(user2));
                userService.createUser(userMapper.toDTO(user3));

                /* Create some reviews */
                Review review1 = new Review(user1, book1,
                                "Esta muy bien y súper interesante se lo recomiendo a la gente que le gusta la Fantasia y el misterio");
                Review review2 = new Review(user3, book1, "Es un libro");

                reviewService.createReview(reviewMapper.toDTO(review1));
                reviewService.createReview(reviewMapper.toDTO(review2));

                /* Create some purchases */
                Purchase purchase1 = new Purchase(user1, new ArrayList<Book>(List.of(book1, book2)),
                                LocalDateTime.of(2025, 2, 1, 12, 5, 0), "Enviado");
                Purchase purchase2 = new Purchase(user2, new ArrayList<Book>(List.of(book1)),
                                LocalDateTime.of(2025, 1, 23, 7, 35, 0), "Enviado");

                purchaseService.createPurchase(purchaseMapper.toDTO(purchase1));
                purchaseService.createPurchase(purchaseMapper.toDTO(purchase2));
        }

        private Blob loadImage(String path) throws IOException {
                InputStream img = Files.newInputStream(Paths.get(path));
                byte[] imageBytes = img.readAllBytes();
                return BlobProxy.generateProxy(imageBytes);
        }

}