package com.example.bms.entity;

import jakarta.persistence.*;
import lombok.Getter;
import  lombok.Setter;
import java.time.LocalDate;

@Entity
@Table(name = "loan_history")
@Getter
@Setter
public class LoanHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    private LocalDate loanDate;
    private LocalDate dueDate;
    private LocalDate returnDate;


}
