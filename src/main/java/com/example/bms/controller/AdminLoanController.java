package com.example.bms.controller;

import com.example.bms.entity.Book;
import com.example.bms.entity.User;
import com.example.bms.repository.BookRepository;
import com.example.bms.service.LoanService;
import com.example.bms.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.ui.Model;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import java.time.LocalDate;

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

    @GetMapping("/books")
    public String list(@RequestParam(required = false) String keyword, Model model) {
        List<Book> books;
        if (keyword == null || keyword.isEmpty()) {
            books = bookRepository.findAll();
        } else {
            books = bookRepository.findByTitleContainingOrAuthorContaining(keyword, keyword);
        }
        model.addAttribute("books", books);
        model.addAttribute("keyword", keyword);
        return "admin/book/list"; // 管理者用テンプレート
    }

    @GetMapping("/books/new")
    public String newBook(Model model) {
        model.addAttribute("book", new Book());
        return "admin/book/form";
    }

    @PostMapping("/books/save")
    public String save(@Valid @ModelAttribute Book book, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("book", book);
            return "admin/book/form";
        }
        bookRepository.save(book);
        return "redirect:/admin/books";
    }

    @GetMapping("/books/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("書籍が存在しません"));
        model.addAttribute("book", book);
        return "admin/book/form";
    }

    @GetMapping("/books/delete/{id}")
    public String delete(@PathVariable Long id) {
        bookRepository.deleteById(id);
        return "redirect:/admin/books";
    }

    @GetMapping("/loans/new/{bookId}")
    public String loanForm(@PathVariable Long bookId, Model model) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("書籍が存在しません。"));
        List<User> users = userService.findAll();

        model.addAttribute("book", book);
        model.addAttribute("users", users);
        return "admin/loan/form";
    }

    @PostMapping("/loans")
    public String loan(
            @RequestParam Long bookId,
            @RequestParam Long userId,
            @RequestParam String dueDate
    ) {
        loanService.loan(bookId, userId, LocalDate.parse(dueDate));
        return "redirect:/admin/books";
    }

}
