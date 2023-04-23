package com.example.rg.controller;

import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.rg.service.EmployeeService;

import lombok.extern.slf4j.Slf4j;

import com.example.rg.entity.Employee;
import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
    if (emp == null) {
      return R.error("登录失败");
    }
    if (!emp.getPassword().equals(password)) {
      return R.error("登录失败");
    }

    if (emp.getStatus() == 0) {
      return R.error("账号已禁用");
    }

    request.getSession().setAttribute("employee", emp.getId());

    return R.success(emp);
  }

  /**
   * 员工退出
   * 
   * @param request
   * @return
   */
  @PostMapping("/logout")
  public R<String> logout(HttpServletRequest request) {
    request.getSession().removeAttribute("employee");
    return R.success("退出成功");
  }

  @PostMapping
  public R<String> save(HttpServletRequest request, @RequestBody Employee employee) {
    employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

    employee.setCreateTime(LocalDateTime.now());
    employee.setUpdateTime(LocalDateTime.now());

    Long empId = (Long) request.getSession().getAttribute("employee");
    employee.setCreateUser(empId);
    employee.setUpdateUser(empId);

    log.info("新增员工: {}", employee.toString());
    employeeService.save(employee);
    return R.success("新增员工成功");
  }

  @GetMapping("/page")
  public R<Page<Employee>> page(@RequestParam int page, @RequestParam int pageSize, @RequestParam(required=false) String name) {
    log.info("page={}, pageSize={}, name={}", page, pageSize, name);
    // 分页器
    Page<Employee> pageInfo = new Page<Employee>(page, pageSize);
    // 条件查询器
    LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<Employee>();
    queryWrapper.like(!StringUtils.isEmpty(name), Employee::getName, name);
    queryWrapper.orderByDesc(Employee::getUpdateTime);
    employeeService.page(pageInfo, queryWrapper);

    return R.success(pageInfo);
  }

}
