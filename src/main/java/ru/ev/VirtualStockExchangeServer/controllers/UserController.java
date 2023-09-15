package ru.ev.VirtualStockExchangeServer.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.ev.VirtualStockExchangeServer.models.User.User;
import ru.ev.VirtualStockExchangeServer.services.MainService;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final MainService mainService;
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }


    @PostMapping("/registration")
    public String createUser(User user, Model model) {
        if(!mainService.createUser(user)){
            model.addAttribute("error message","Пользователь с именем "+user.getUsername()+"уже существует");
            return "registration";
        }
        mainService.createUser(user);
        return "redirect:/login";
    }

    @GetMapping("/hello")
    public String securityUrl() {
        return "hello";
    }

//    @GetMapping("/user/{user}")
//    public String userInfo(@PathVariable("user") User user, Model model ){
//        model.addAttribute("user", user);
//        model.addAttribute("products",user.getProducts());
//        return "user-info";
//    }

}
