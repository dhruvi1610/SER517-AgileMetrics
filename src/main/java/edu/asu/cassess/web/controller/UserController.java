package edu.asu.cassess.web.controller;

import edu.asu.cassess.persist.entity.security.User;
import edu.asu.cassess.persist.repo.UserRepo;
import edu.asu.cassess.service.security.IUserService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Transactional
@RestController
@Api(description = "Users management API")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private IUserService usersService;

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public
    @ResponseBody
    List<User> usersList() {
        logger.debug("get users list");
        return userRepo.findAll();
    }

    @RequestMapping(value = "/users/{userId}", method = RequestMethod.GET)
    public
    @ResponseBody
    User getUser(@PathVariable Long userId) {
        logger.debug("get user");
        return userRepo.findOne(userId);
    }


    @RequestMapping(value = "/user_email", method = RequestMethod.GET)
    public User getUserByEmail(@RequestHeader(name = "email", required = true) String email) {
        System.out.println("*************************-----------------------+++++++++++++++++!!!!!!!!!!!!!!!Email: " + email);
        logger.debug("get user");
        User user = userRepo.findByEmail(email);
        return user;
    }

    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public
    @ResponseBody
    <T> Object saveUser(@RequestHeader(name = "first_name", required = true) String first_name,
                        @RequestHeader(name = "family_name", required = true) String family_name,
                        @RequestHeader(name = "email", required = true) String email,
                        @RequestHeader(name = "password", required = true) String password,
                        @RequestHeader(name = "role", required = true) String role) {
        //registerUser passes email address as login
        return usersService.registerUser(first_name, family_name, email, password, role);
    }
}

 
