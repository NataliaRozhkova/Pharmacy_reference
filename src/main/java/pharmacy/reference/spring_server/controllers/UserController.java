package pharmacy.reference.spring_server.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pharmacy.reference.spring_server.entitis.User;
import pharmacy.reference.spring_server.services.UsersService;


@Controller
@RequestMapping("/user")
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(MedicineController.class);

    @Autowired
    private UsersService usersService;


    @GetMapping("/changePassword")
    public String changePassword() {
        return "user_change_password";
    }

    @GetMapping("/createUser")
    public String createNewUser() {
        return "new_user";
    }

    @PostMapping("/savePassword")
    public String savePassword(@RequestParam(name = "userName") String userName, final Model model, @RequestParam("password") final String password) {
        User user = usersService.getUser(userName);
        if (!password.matches("^.*(?=.{8,})(?=..*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$")) {
            model.addAttribute("error", "Слишком простой пароль");
            return "user_change_password";
        }
        try {
            usersService.changeUserPassword(user, password);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "user_change_password";
        }

        model.addAttribute("text", "Пароль успешно обновлен");
        logger.info("Change password " + userName + ": Operator " + SecurityContextHolder.getContext().getAuthentication().getName());

        return "base_page";
    }

    @PostMapping("/saveNewUser")
    public String saveNewUser(@RequestParam(name = "userName") String userName, final Model model, @RequestParam("password") final String password){
        if (!password.matches("^.*(?=.{8,})(?=..*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$")) {
            model.addAttribute("error", "Слишком простой пароль");
            return "new_user";
        }

        try {
            usersService.saveUser(userName, password);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "new_user";

        }
        logger.info("Create user " + userName + ": Operator " + SecurityContextHolder.getContext().getAuthentication().getName());
        model.addAttribute("text", "Пользователь успешно сохранен");
        return "base_page";

    }

}
