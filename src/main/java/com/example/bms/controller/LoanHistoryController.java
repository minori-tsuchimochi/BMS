package com.example.bms.controller;

import com.example.bms.entity.LoanHistory;
import com.example.bms.entity.User;
import com.example.bms.repository.LoanHistoryRepository;
import com.example.bms.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@Controller
@PreAuthorize("hasRole('USER')")
public class LoanHistoryController {
    private final LoanHistoryRepository loanHistoryRepository;
    private final UserService userService;

    public LoanHistoryController(LoanHistoryRepository loanHistoryRepository, UserService userService) {
        this.loanHistoryRepository = loanHistoryRepository;
        this.userService = userService;
    }

    @GetMapping("/loans")
    public String list(Authentication authentication, Model model) {
        User user = userService.findByUsername(authentication.getName());

        List<LoanHistory> histories = loanHistoryRepository.findByUserOrderByLoanDateDesc(user);

        model.addAttribute("histories", histories);

        return "user/loan/list";
    }
}
