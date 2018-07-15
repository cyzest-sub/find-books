package com.cyzest.findbooks.controller;

import com.cyzest.findbooks.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
public class UserController {

    private UserService userService;

    @PostMapping("/users")
    public String registerUser(@RequestParam String id, @RequestParam String password) throws Exception {

        userService.registerUser(id, password);

        return "success";
    }

}
