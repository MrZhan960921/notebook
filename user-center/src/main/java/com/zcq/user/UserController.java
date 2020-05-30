package com.zcq.user;


import com.itmuch.usercenter.domain.entity.user.User;

import com.zcq.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    @Autowired
    private  UserService userService;


    @GetMapping("/{id}")
    public com.itmuch.usercenter.domain.entity.user.User findById(@PathVariable Integer id) {
        log.info("我被请求了...");
        return this.userService.findById(id);
    }


}
