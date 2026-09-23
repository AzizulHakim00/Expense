package com.expensetracker.controller;

import com.expensetracker.dto.ExpenseRequest;
import com.expensetracker.model.Expense;
import com.expensetracker.model.User;
import com.expensetracker.service.ExpenseService;
import com.expensetracker.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
public class ExpenseController {
    private final UserService userService;
    private final ExpenseService expenseService;

    public ExpenseController(UserService userService, ExpenseService expenseService) {
        this.userService = userService;
        this.expenseService = expenseService;
    }

    @GetMapping("/dashboard")
    public String dashboard(
            Authentication authentication,
            @RequestParam(defaultValue = "all") String period,
            Model model
    ) {
        User user = userService.currentUser(authentication);
        LocalDate today = LocalDate.now();
        List<Expense> all = expenseService.allForUser(user.getId());
        List<Expense> week = expenseService.weeklyForUser(user.getId(), today);
        List<Expense> month = expenseService.monthlyForUser(user.getId(), today);
        List<Expense> visible = expenseService.forPeriod(user.getId(), period, today);

        model.addAttribute("user", user);
        model.addAttribute("period", normalizePeriod(period));
        model.addAttribute("expenses", visible);
        model.addAttribute("weeklyTotal", expenseService.total(week));
        model.addAttribute("monthlyTotal", expenseService.total(month));
        model.addAttribute("allTotal", expenseService.total(all));
        model.addAttribute("expenseCount", all.size());
        return "dashboard";
    }

    @GetMapping("/expenses/new")
    public String createPage(Model model) {
        if (!model.containsAttribute("expenseRequest")) {
            model.addAttribute("expenseRequest", new ExpenseRequest());
        }
        model.addAttribute("pageTitle", "Add Expense");
        model.addAttribute("submitText", "Save Expense");
        model.addAttribute("formAction", "/expenses");
        return "expenses/form";
    }

    @PostMapping("/expenses")
    public String create(
            Authentication authentication,
            @Valid @ModelAttribute("expenseRequest") ExpenseRequest request,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", "Add Expense");
            model.addAttribute("submitText", "Save Expense");
            model.addAttribute("formAction", "/expenses");
            return "expenses/form";
        }

        User user = userService.currentUser(authentication);
        expenseService.create(user.getId(), request);
        redirectAttributes.addFlashAttribute("successMessage", "Expense created successfully.");
        return "redirect:/dashboard";
    }

    @GetMapping("/expenses/{id}/edit")
    public String editPage(
            Authentication authentication,
            @PathVariable String id,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        try {
            User user = userService.currentUser(authentication);
            Expense expense = expenseService.getOwned(user.getId(), id);
            ExpenseRequest request = new ExpenseRequest();
            request.setTitle(expense.getTitle());
            request.setAmount(expense.getAmount());
            request.setCategory(expense.getCategory());
            request.setExpenseDate(expense.getExpenseDate());
            request.setNote(expense.getNote());

            model.addAttribute("expenseRequest", request);
            model.addAttribute("pageTitle", "Edit Expense");
            model.addAttribute("submitText", "Update Expense");
            model.addAttribute("formAction", "/expenses/" + id + "/update");
            return "expenses/form";
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
            return "redirect:/dashboard";
        }
    }

    @PostMapping("/expenses/{id}/update")
    public String update(
            Authentication authentication,
            @PathVariable String id,
            @Valid @ModelAttribute("expenseRequest") ExpenseRequest request,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", "Edit Expense");
            model.addAttribute("submitText", "Update Expense");
            model.addAttribute("formAction", "/expenses/" + id + "/update");
            return "expenses/form";
        }

        try {
            User user = userService.currentUser(authentication);
            expenseService.update(user.getId(), id, request);
            redirectAttributes.addFlashAttribute("successMessage", "Expense updated successfully.");
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
        }
        return "redirect:/dashboard";
    }

    @PostMapping("/expenses/{id}/delete")
    public String delete(
            Authentication authentication,
            @PathVariable String id,
            RedirectAttributes redirectAttributes
    ) {
        try {
            User user = userService.currentUser(authentication);
            expenseService.deleteOwned(user.getId(), id);
            redirectAttributes.addFlashAttribute("successMessage", "Expense deleted successfully.");
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
        }
        return "redirect:/dashboard";
    }

    private String normalizePeriod(String period) {
        if ("week".equalsIgnoreCase(period)) return "week";
        if ("month".equalsIgnoreCase(period)) return "month";
        return "all";
    }
}
