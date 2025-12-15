package com.example.bms.controller;

import com.example.bms.entity.Book;
import com.example.bms.entity.User;
import com.example.bms.repository.BookRepository;
import com.example.bms.repository.LoanHistoryRepository;
import com.example.bms.service.LoanService;
import com.example.bms.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/books")
@PreAuthorize("hasRole('USER')")
public class UserBookController {
    private final BookRepository bookRepository;
    private final LoanService loanService;
    private final UserService userService;
    private final LoanHistoryRepository loanHistoryRepository;

    public UserBookController(BookRepository bookRepository, LoanService loanService, UserService userService, LoanHistoryRepository loanHistoryRepository) {
        this.bookRepository = bookRepository;
        this.loanService = loanService;
        this.userService = userService;
        this.loanHistoryRepository = loanHistoryRepository;
    }

    @GetMapping
    public String list(Model model, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        List<Book> books = bookRepository.findAll();

        for (Book book : books) {
            boolean hasLoaned = loanHistoryRepository.existsByBookAndUserAndReturnDateIsNull(book, user);
            book.setUserHasLoaned(hasLoaned);
        }

        model.addAttribute("books", books);
        return "user/book/list";
    }

    @PostMapping("/{id}/loan")
    public String loanBook(@PathVariable Long id, Authentication authentication) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("書籍が存在しません"));
        User user = userService.findByUsername(authentication.getName());
        loanService.loan(book, user);
        return "redirect:/books";
    }

    @PostMapping("/{id}/return")
    public String returnBook(@PathVariable Long id) {
        Book book = bookRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("書籍が存在しません"));
        loanService.returnBook(book);
        return "redirect:/books";
    }
}
