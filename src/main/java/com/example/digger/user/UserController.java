package com.example.digger.user;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute UserDTO userDTO, RedirectAttributes redirectAttributes) {
        boolean isRegistered = userService.registerUser(userDTO);

        if (!isRegistered) {
            redirectAttributes.addFlashAttribute("error", "이미 등록된 이메일입니다.");
            return "redirect:/api/user/register";
        }
        return "redirect:/api/user/login";
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(@ModelAttribute UserDTO userDTO,RedirectAttributes redirectAttributes, Model model) {
        try {
            User user = userService.login(userDTO.getEmail(), userDTO.getPassword());
            model.addAttribute("user", user);
            return "redirect:/api/home";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/api/user/login";
        }
    }
}
