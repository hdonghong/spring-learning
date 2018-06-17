package pers.hdh.a_base.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pers.hdh.a_base.component.ComponentBean;

/**
 * HelloController class<br/>
 *
 * @author hdonghong
 * @date 2018/06/17
 */
// 用于Rest服务
@RestController
public class HelloController {

    @Autowired
    private ComponentBean componentBean;


    @GetMapping("/hello/{id}")
    public String hello(@PathVariable("id") Integer id) {
        return "componentBean = " + componentBean + ", id = " + id;
    }
}
