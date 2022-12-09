package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import web.model.User;
import web.service.UserService;

import java.security.Principal;

@RestController
public class MainController {

    @GetMapping(value = "/index")
    public String homePage() {
        return "index";
    }

}
