package com.example.rg.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.rg.entity.Employee;
import com.example.rg.mapper.EmployeeMapper;
import com.example.rg.service.EmployeeService;

@Service
public class EmployServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
  
}
