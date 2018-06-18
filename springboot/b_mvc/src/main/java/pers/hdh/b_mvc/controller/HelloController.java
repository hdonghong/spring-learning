package pers.hdh.b_mvc.controller;

import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import pers.hdh.b_mvc.entity.EmployeeForm;

import java.util.List;

/**
 * HelloController class<br/>
 *
 * @author hdonghong
 * @date 2018/06/17
 */
@RestController
public class HelloController {

    /**
     * 讲解@RequestMapping：
     * SpringBoot使用 @GetMapping、@PostMapping、@PutMapping、@DeleteMapping、@PatchMapping简化了SpringMVC的@RequestMapping
     * consumes属性对应HTTP头的Content-Type媒体类型
     * produces属性对应HTTP头的Accept字段
     * params属性和headers属性类似，要求请求参数或请求头中包含指定参数或者包含指定参数和指定值或者不包含指定参数
     *
     * 以下该方法表示：
     * 要求请求方式为Get，uri为/hello，Content-Type为application/json，Accept为application/json;charset=UTF-8，
     * 请求参数包含myParam且对应值为myValue，请求头包含myHeader且对应值为myValue
     *
     * 更多关于@RequestMapping的可查询官方文档：
     * https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html#mvc-ann-requestmapping
     * @return string
     */
    @GetMapping(
            value = "/hello",
            consumes = "application/json",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            params = "myParam=myValue",
            headers = "myHeader=myValue")
    public String hello() {
        return "ok";
    }

    /**
     * 讲解方法参数：
     * 注解@PathVariable，可以将url中的值映射到方法中。Spring支持矩阵变量，即;分隔。
     * Model & ModelAndView，映射视图。
     * JavaBean，接收HTTP参数。
     * 注解@RequestBody，接收JSON。
     * MultipartFile，接收文件，多个文件可用数组，max-file-size默认1mb。
     * 注解@ModelAttibute，通常作用在Controller的某个方法上，将方法结果作为Model的属性再调用对应的Controller处理方法。
     *
     * 以下该方法表示：
     * 访问/user/{id}请求时，会先调用findUserById方法添加Model属性，然后才执行controller的方法
     *
     * @return string
     */
    @GetMapping("/user/{id}")
    public String user(Model model) {
        System.out.println(model.containsAttribute("user"));
        return "ok";
    }

    /*
    // 暂时注释掉，否则影响其它方法的测试
    @ModelAttribute
    public void findUserById(@PathVariable Long id, Model model) {
        model.addAttribute("user", id);
    }
    */

    /**
     * 讲解验证框架：
     * Spring Boot使用@Valid或Validated注解（默认Hibernate validator实现）对参数对象进行校验，校验结果放在BindingResult对象中
     *
     */
    @PostMapping("/add")
    public void validateAdd(@Validated(EmployeeForm.Add.class)EmployeeForm employeeForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<ObjectError> allErrors = bindingResult.getAllErrors();
            allErrors.forEach(e -> {
                FieldError error = (FieldError) e;
                System.out.println(error.getField() + ": " + e.getDefaultMessage());
            });
        }

        System.out.println(employeeForm);
    }

    @GetMapping("/thymeleaf")
    public ModelAndView thymeleaf(ModelAndView view) {
        view.setViewName("index");
        view.addObject("name", "thymeleaf模板");
        return view;
    }

}
