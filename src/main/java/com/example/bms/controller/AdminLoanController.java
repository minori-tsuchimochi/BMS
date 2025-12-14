package com.example.bms.controller;

import com.example.bms.entity.Book;
import com.example.bms.entity.User;
import com.example.bms.repository.BookRepository;
import com.example.bms.service.LoanService;
import com.example.bms.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminLoanController {
    private final LoanService loanService;
    private final BookRepository bookRepository;
    private final UserService userService;

    public AdminLoanController(LoanService loanService, BookRepository bookRepository, UserService userService) {
        this.loanService = loanService;
        this.bookRepository = bookRepository;
        this.userService = userService;
    }


    @PostMapping("/loan/{bookId}")
    public String loan(@PathVariable Long bookId, Authentication authentication) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("書籍が存在しません"));
        User user = userService.findByUsername(authentication.getName());
        loanService.loan(book, user);
        return "redirect:/admin/books";
    }

    @PostMapping("/return/{bookId}")
    public String returnBook(@PathVariable Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("書籍が存在しません"));
        loanService.returnBook(book);

        return "redirect:/admin/books";
    }

}
