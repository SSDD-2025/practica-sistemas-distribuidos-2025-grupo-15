package com.example.demo.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.demo.model.Book;
import com.example.demo.model.Purchase;
import com.example.demo.model.Review;
import com.example.demo.model.User;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.PurchaseRepository;
import com.example.demo.repository.ReviewRepository;
import com.example.demo.repository.UserRepository;

import jakarta.annotation.PostConstruct;

@Component
public class DatabaseInitializer {

    private static final String IMAGE_TARGET_DIR = "uploads/";

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() throws IOException {

        purchaseRepository.deleteAll();
        reviewRepository.deleteAll();
        bookRepository.deleteAll();
        userRepository.deleteAll();
        // Copiar imágenes al directorio de subida
        String filename1 = copyImageToUploads("images/Harry_Potter_img.jpg");
        String filename2 = copyImageToUploads("images/Los_Juegos_Del_Hambre_1.jpg");

        // Crear libros con el nombre del archivo como valor del campo image
        Book book1 = new Book(1, "Harry Potter y la piedra filosofal", "J.K Rowling",
                "Harry Potter nunca ha oído hablar de Hogwarts hasta que empiezan a caer cartas en el felpudo del número 4 de Privet Drive,...",
                16.10, filename1);

        Book book2 = new Book(2, "Los Juegos del Hambre 1", "Suzanne Collins",
                "Cuando Katniss Everdeen, una joven de dieciséis años se presenta voluntaria para ocupar el lugar de su hermana en los juegos,...",
                18.95, filename2);

        bookRepository.save(book1);
        bookRepository.save(book2);

        // Crear usuarios
        User user1 = new User("Paula", passwordEncoder.encode("password"), List.of("USER", "ADMIN"));
        User user2 = new User("Lucía", passwordEncoder.encode("1234"), List.of("USER"));
        User user3 = new User("A", passwordEncoder.encode("1"), List.of("USER"));

        userRepository.saveAll(List.of(user1, user2, user3));

        // Crear reseñas
        Review review1 = new Review(user1, book1, "Esta muy bien y súper interesante...");
        Review review2 = new Review(user3, book1, "Es un libro");

        reviewRepository.saveAll(List.of(review1, review2));

        // Crear compras
        Purchase purchase1 = new Purchase(user1, List.of(book1, book2),
                LocalDateTime.of(2025, 2, 1, 12, 5, 0), "Enviado");

        Purchase purchase2 = new Purchase(user2, List.of(book1),
                LocalDateTime.of(2025, 1, 23, 7, 35, 0), "Enviado");

        purchaseRepository.saveAll(List.of(purchase1, purchase2));
    }

    private String copyImageToUploads(String classpathResource) throws IOException {
        ClassPathResource resource = new ClassPathResource(classpathResource);

        if (!resource.exists()) {
            throw new IOException("Resource not found: " + classpathResource);
        }

        Path targetPath = Paths.get(IMAGE_TARGET_DIR + resource.getFilename());
        Files.createDirectories(targetPath.getParent());

        try (InputStream inputStream = resource.getInputStream()) {
            Files.copy(inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING);
        }

        return resource.getFilename();
    }
}
