package com.example.bms.service;

import com.example.bms.entity.Book;
import com.example.bms.entity.LoanHistory;
import com.example.bms.entity.User;
import com.example.bms.repository.BookRepository;
import com.example.bms.repository.LoanHistoryRepository;
import com.example.bms.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;

@Service
public class LoanService {
    private final BookRepository bookRepository;
    private final LoanHistoryRepository loanHistoryRepository;
    private final UserRepository userRepository;

    public LoanService(BookRepository bookRepository, LoanHistoryRepository loanHistoryRepository, UserRepository userRepository) {
        this.bookRepository = bookRepository;
        this.loanHistoryRepository = loanHistoryRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void loan(Book book, User user) {
        if(book.getStock() <= 0) {
            throw new IllegalStateException("在庫がありません");
        }

        book.setStock(book.getStock() - 1);

        LoanHistory history = new LoanHistory();
        history.setBook(book);
        history.setUser(user);
        history.setLoanDate(LocalDate.now());
        history.setDueDate(LocalDate.now().plusWeeks(2));

        bookRepository.save(book);
        loanHistoryRepository.save(history);
    }

    @Transactional
    public void returnLoan(Long loanHistoryId) {
        LoanHistory history = loanHistoryRepository.findById(loanHistoryId)
                .orElseThrow(() -> new IllegalArgumentException("貸出履歴が存在しません"));

        if (history.getReturnDate() != null) {
            throw new IllegalStateException("既に返却済みです");
        }

        history.setReturnDate(LocalDate.now());
        Book book = history.getBook();
        book.setStock(book.getStock() + 1);
        loanHistoryRepository.save(history);
        bookRepository.save(book);
    }

    @Transactional
    public void loan(Long bookId, Long userId, LocalDate dueDate) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("書籍が存在しません"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("ユーザーが存在しません"));

        if (book.getStock() <= 0) {
            throw new IllegalStateException("在庫がありません");
        }

        book.setStock(book.getStock() - 1);

        LoanHistory history = new LoanHistory();
        history.setBook(book);
        history.setUser(user);
        history.setLoanDate(LocalDate.now());
        history.setDueDate(dueDate);

        bookRepository.save(book);
        loanHistoryRepository.save(history);
    }

}
