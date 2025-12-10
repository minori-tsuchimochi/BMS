package com.example.bms.controller;

import com.example.bms.entity.Book;
import com.example.bms.repository.BookRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/admin/books")
public class BookController {
    private final BookRepository  bookRepository;
    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @GetMapping
    public String list(@RequestParam(required = false) String keyword, Model model) {
        List<Book> books;

        if (keyword == null || keyword.isEmpty()) {
            books = bookRepository.findAll();
        } else {
            books = bookRepository.findByTitleContainingOrAuthorContaining(keyword, keyword);
        }

        model.addAttribute("books", books);
        return "book/list";
    }

    @GetMapping("/new")
    public String newBook(Model model) {
        model.addAttribute("book", new Book());
        return "book/form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute Book book, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("book", book);
            return "book/form";
        }

        bookRepository.save(book);
        return "redirect:/admin/books";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        Book book = bookRepository.findById(id).orElseThrow();
        model.addAttribute("book", book);
        return "book/form";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        bookRepository.deleteById(id);
        return "redirect:/admin/books";
    }
}
