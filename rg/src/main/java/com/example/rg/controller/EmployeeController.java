package com.example.rg.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.rg.service.EmployeeService;

import lombok.extern.slf4j.Slf4j;

import com.example.rg.entity.Employee;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.rg.common.R;;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
  @Autowired
  private EmployeeService employeeService;

  @PostMapping("/login")
  public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
    log.info("登录中。。。");
    String password = employee.getPassword();
    password = DigestUtils.md5DigestAsHex(password.getBytes());
    LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(Employee::getUsername, employee.getUsername());
    Employee emp = employeeService.getOne(queryWrapper);
    if(emp == null) {
      return R.error("登录失败");
    }
    if(!emp.getPassword().equals(password)){
      return R.error("登录失败");
    }

    if(emp.getStatus() == 0){
      return R.error("账号已禁用");
    }

    request.getSession().setAttribute("employee", emp.getId());

    return R.success(emp);
  }
}
