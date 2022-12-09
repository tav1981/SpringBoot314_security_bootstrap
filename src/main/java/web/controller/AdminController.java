
package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import web.model.Role;
import web.model.User;
import web.service.RoleService;
import web.service.UserService;

import java.security.Principal;
import java.util.List;
import java.util.Set;

@Controller
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/admin")
    public String printUsers(Model model, Principal principal) {

        List<Role> roles = roleService.listRole();
        model.addAttribute("roles", roles);
        List<User> users = userService.listUsers();

        User user = userService.findByUsername(principal.getName());

        model.addAttribute("user", user);
        model.addAttribute("users", users);

        return "admin";
    }

    @PostMapping(value = "/admin")
    public String createUsers(
                              @RequestParam(required=false) Long id,
                              @RequestParam String username,
                              @RequestParam String email,
                              @RequestParam String password,
                              @RequestParam Set<Role> roles
                              ) {

        System.out.println("id - " + id);
        User user = null;
        String encoderPassword = new BCryptPasswordEncoder(12).encode(password);

        if (id != null){
            User oldUser = userService.getForId(id);

            System.out.println(id + " юзер определен: " + oldUser.getUsername());

            oldUser.setRoles(roles);
            oldUser.setUsername(username);
            oldUser.setEmail(email);
            oldUser.setPassword("{bcrypt}" + encoderPassword);

            user = oldUser;
        } else {
            System.out.println("новый юзер");
            user = new User(username, email, "{bcrypt}" + encoderPassword, roles);
        }
        System.out.println("запись в базу");
        userService.add(user);
        return "redirect:/admin";
    }

    @GetMapping(value = "admin/delete/{deleteId:\\d+}")
    public String deleteUser(@PathVariable Long deleteId) {

        userService.dell(deleteId);
        return "redirect:/admin";
    }
}

