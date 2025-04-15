package com.example.demo.controller;

import java.io.IOException;
import java.security.Principal;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.NoSuchElementException;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.dto.BookDTO;
import com.example.demo.dto.BookMapper;
import com.example.demo.dto.UserDTO;
import com.example.demo.dto.UserMapper;
import com.example.demo.model.Book;
import com.example.demo.model.User;
import com.example.demo.service.BookService;
import com.example.demo.service.ReviewService;
import com.example.demo.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class BookController {

    @Autowired
    private BookService bookService;
    
    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/")
    public String showBooks(Model model, HttpServletRequest request) {
        Principal req = request.getUserPrincipal();
        User user = null;
        if (req != null){
            user = userMapper.toDomain(userService.getUser(req.getName()));
        }
        model.addAttribute("books", bookService.getBooks());
        if(user != null){
            model.addAttribute("admin", request.isUserInRole("ADMIN"));
        }
        
        return "home";
    }

    @GetMapping("/book/{id}")
    public String showBook(@PathVariable int id, Model model, HttpSession session, HttpServletRequest request) {
        Principal req = request.getUserPrincipal();
        User user = null;
        if (req != null){
            user = userMapper.toDomain(userService.getUser(req.getName()));
        }
        
        BookDTO bookDTO = bookService.getBook(id);
        session.setAttribute("bookId", id);
        model.addAttribute("book", bookDTO);
        model.addAttribute("reviews", reviewService.getReviews(bookDTO));
        if(user != null){
            model.addAttribute("admin", request.isUserInRole("ADMIN"));
        }

        return "book";

    }

    @GetMapping("book/image/{id}")
    public ResponseEntity<Object> downloadImage(@PathVariable int id) throws SQLException {
        BookDTO book = bookService.getBook(id);

        if (book != null && book.imageFile() != null) {
            Blob image = book.imageFile();
            InputStreamResource file = new InputStreamResource(image.getBinaryStream());

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                    .contentLength(image.length())
                    .body(file);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/newBook")
    public String newBookForm(HttpServletRequest request, Model model) {
        String name = request.getUserPrincipal().getName();
        UserDTO userDTO = userService.getUser(name);

        if (userDTO == null) {
            return "errorNoSesionAddBook";
        }

        model.addAttribute("book", new Book());
        return "newBook";
    }

    @PostMapping("/newBook")
public String createBook(@ModelAttribute BookDTO bookDTO,
                            @RequestParam("image") MultipartFile imageFile,
                            Model model) throws IOException {

    if (bookService.getBookByISBN(bookDTO.ISBN()) != null) {
        model.addAttribute("error", "Error: El libro con ese ISBN ya existe.");
        return "newBook";
    }

    // Mapea a dominio para insertar imagen
    Book book = bookMapper.toDomain(bookDTO);

    if (!imageFile.isEmpty()) {
        book.setImageFile(BlobProxy.generateProxy(imageFile.getInputStream(), imageFile.getSize()));
    }

    // Guarda libro con imagen incluida
    bookService.createBook(bookMapper.toDTO(book));  // Puedes usar directamente aquí o bookService.createBook(bookDTO)

    model.addAttribute("success", "El libro ha sido añadido correctamente.");
    model.addAttribute("books", bookService.getBooks());

    return "redirect:/";
}


    @GetMapping("/editBook")
    public String editBook(@RequestParam int id, HttpServletRequest request, Model model) {
        String name = request.getUserPrincipal().getName();
        UserDTO userDTO = userService.getUser(name);

        if (userDTO == null) {
            model.addAttribute("ISBN", id);
            return "errorNoSesionEditBook";
        }

        BookDTO bookDTO = bookService.getBook(id);

        if (bookDTO != null) {
            model.addAttribute("book", bookDTO);
            return "editBook";
        }

        model.addAttribute("error", "Error: El libro no existe.");
        model.addAttribute("books", bookService.getBooks());
        return "home";
    }

    @PostMapping("/saveEdit")
    public String saveEditedBook(@ModelAttribute BookDTO bookDTO,
            @RequestParam(value = "image", required = false) MultipartFile imageFile, Model model) throws IOException {
        BookDTO existingBook = bookService.getBook(bookDTO.id());

        if (imageFile != null && !imageFile.isEmpty()) {
            bookMapper.toDomain(bookDTO).setImageFile(BlobProxy.generateProxy(imageFile.getInputStream(), imageFile.getSize()));
        } else {
            bookMapper.toDomain(bookDTO).setImageFile(existingBook.imageFile());
        }
        bookService.updateBook(bookDTO.id() ,bookDTO);
        model.addAttribute("success", "El libro ha sido actualizado correctamente.");
        model.addAttribute("books", bookService.getBooks());
        return "home";
    }

    @PostMapping("/deleteBook")
public String deleteBook(@RequestParam int id,
                         HttpServletRequest request,
                         RedirectAttributes redirectAttributes) {
    System.out.println("Intentando eliminar libro con ID: " + id);

    String name = request.getUserPrincipal().getName();
    System.out.println("Usuario autenticado: " + name);

    UserDTO userDTO = userService.getUser(name);

    if (userDTO == null) {
        redirectAttributes.addFlashAttribute("error", "Error: No tienes sesión activa.");
        return "redirect:/errorNoSessionDeleteBook";
    }

    try {
        bookService.deleteBook(id);
        redirectAttributes.addFlashAttribute("success", "Libro eliminado correctamente.");
    } catch (NoSuchElementException e) {
        System.out.println("No se encontró el libro con ID: " + id);
        redirectAttributes.addFlashAttribute("error", "Error: El libro no existe.");
    }

    return "redirect:/";
}


}
