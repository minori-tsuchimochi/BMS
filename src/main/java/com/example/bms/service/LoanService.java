package com.example.bms.service;

import com.example.bms.entity.Book;
import com.example.bms.entity.LoanHistory;
import com.example.bms.entity.User;
import com.example.bms.repository.BookRepository;
import com.example.bms.repository.LoanHistoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;

@Service
public class LoanService {
    private final BookRepository bookRepository;
    private final LoanHistoryRepository loanHistoryRepository;

    public LoanService(BookRepository bookRepository, LoanHistoryRepository loanHistoryRepository) {
        this.bookRepository = bookRepository;
        this.loanHistoryRepository = loanHistoryRepository;
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
    public void returnBook(Book book) {
        LoanHistory history = loanHistoryRepository
                .findByBookAndReturnDateIsNull(book)
                .orElseThrow(() -> new IllegalStateException("貸出履歴が見つかりません"));

        history.setReturnDate(LocalDate.now());
        book.setStock(book.getStock() + 1);
    }
}
