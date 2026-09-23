package com.expensetracker.controller;

import com.expensetracker.dto.RegisterRequest;
import com.expensetracker.model.Role;
import com.expensetracker.model.User;
import com.expensetracker.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {
<<<<<<< HEAD

=======
>>>>>>> ddc854d9aa2bb888f13a42a657cf261d844435f8
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String home(Authentication authentication) {
        if (isLoggedIn(authentication)) {
            User user = userService.currentUser(authentication);
            return user.getRole() == Role.ADMIN ? "redirect:/admin/dashboard" : "redirect:/dashboard";
        }
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login(
            Authentication authentication,
            @RequestParam(required = false) String error,
            @RequestParam(required = false) String logout,
            Model model
    ) {
        if (isLoggedIn(authentication)) {
            User user = userService.currentUser(authentication);
            return user.getRole() == Role.ADMIN ? "redirect:/admin/dashboard" : "redirect:/dashboard";
        }
<<<<<<< HEAD

        if (error != null) {
            model.addAttribute("errorMessage", "Invalid email/password or the account is disabled.");
        }
        if (logout != null) {
            model.addAttribute("successMessage", "You have been logged out successfully.");
        }
=======
        if (error != null) model.addAttribute("errorMessage", "Invalid email/password or the account is disabled.");
        if (logout != null) model.addAttribute("successMessage", "You have been logged out successfully.");
>>>>>>> ddc854d9aa2bb888f13a42a657cf261d844435f8
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerPage(Authentication authentication, Model model) {
<<<<<<< HEAD
        if (isLoggedIn(authentication)) {
            return "redirect:/dashboard";
        }
=======
        if (isLoggedIn(authentication)) return "redirect:/dashboard";
>>>>>>> ddc854d9aa2bb888f13a42a657cf261d844435f8
        if (!model.containsAttribute("registerRequest")) {
            model.addAttribute("registerRequest", new RegisterRequest());
        }
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(
            @Valid @ModelAttribute("registerRequest") RegisterRequest request,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
<<<<<<< HEAD
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "password.mismatch", "Passwords do not match");
        }

        if (bindingResult.hasErrors()) {
            return "auth/register";
        }
=======
        if (request.getPassword() != null && !request.getPassword().equals(request.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "password.mismatch", "Passwords do not match");
        }

        if (bindingResult.hasErrors()) return "auth/register";
>>>>>>> ddc854d9aa2bb888f13a42a657cf261d844435f8

        try {
            userService.register(request);
        } catch (IllegalArgumentException exception) {
            bindingResult.reject("registration.failed", exception.getMessage());
            return "auth/register";
        }

<<<<<<< HEAD
        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Registration successful. Please log in with your new account."
        );
=======
        redirectAttributes.addFlashAttribute("successMessage",
                "Registration successful. Please log in with your new account.");
>>>>>>> ddc854d9aa2bb888f13a42a657cf261d844435f8
        return "redirect:/login";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access-denied";
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("OK");
    }

    private boolean isLoggedIn(Authentication authentication) {
        return authentication != null
                && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken);
    }
}
