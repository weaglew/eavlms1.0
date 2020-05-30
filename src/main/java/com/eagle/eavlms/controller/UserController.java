package com.eagle.eavlms.controller;


import com.eagle.eavlms.entity.User;
import com.eagle.eavlms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("all")
    public Page<User> getAll(Integer page, Integer size) {
        return userService.getAll(PageRequest.of(page, size));
    }

    @GetMapping   //springwebmvc 自动绑定
    public Page<User> query(User user, Integer page, Integer size) {
        return userService.query(user, PageRequest.of(page, size));
    }
    //@PathVariable 根据url路径取值将url上的id传递给形参id
    @GetMapping("{id}")
    public User getById(@PathVariable Integer id) {
        return userService.getById(id);
    }


    @PostMapping("/login")
    public User query(@RequestBody Map<String, String> map) {
        return userService.query(new User().setNumber(map.get("number")).setPassword(map.get("password")));
    }


    @DeleteMapping("{id}")
    public void delete(@PathVariable Integer id) {
        userService.delete(id);
    }

    @PostMapping()
    public User save(@RequestBody User user) {
        return userService.save(user);
    }

}
