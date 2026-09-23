package com.expensetracker.controller;

import com.expensetracker.model.Expense;
import com.expensetracker.model.User;
import com.expensetracker.service.ExpenseService;
import com.expensetracker.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class AdminController {
    private final UserService userService;
    private final ExpenseService expenseService;

    public AdminController(UserService userService, ExpenseService expenseService) {
        this.userService = userService;
        this.expenseService = expenseService;
    }

    @GetMapping("/admin/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        User currentAdmin = userService.currentUser(authentication);
        List<User> users = userService.getAllUsers();
        List<Expense> expenses = expenseService.allForAdmin();

        Map<String, String> ownerEmails = new HashMap<>();
        for (User user : users) ownerEmails.put(user.getId(), user.getEmail());

        model.addAttribute("currentAdmin", currentAdmin);
        model.addAttribute("users", users);
        model.addAttribute("expenses", expenses);
        model.addAttribute("ownerEmails", ownerEmails);
        model.addAttribute("userCount", users.size());
        model.addAttribute("expenseCount", expenses.size());
        model.addAttribute("totalSpent", expenseService.total(expenses));
        return "admin/dashboard";
    }

    @PostMapping("/admin/users/{id}/toggle")
    public String toggleUser(
            Authentication authentication,
            @PathVariable String id,
            RedirectAttributes redirectAttributes
    ) {
        try {
            userService.toggleEnabled(id, authentication.getName());
            redirectAttributes.addFlashAttribute("successMessage", "User status updated.");
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
        }
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/admin/expenses/{id}/delete")
    public String deleteExpense(@PathVariable String id, RedirectAttributes redirectAttributes) {
        try {
            expenseService.deleteAsAdmin(id);
            redirectAttributes.addFlashAttribute("successMessage", "Expense deleted by administrator.");
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
        }
        return "redirect:/admin/dashboard";
    }
}
