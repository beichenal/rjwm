package com.example.rg.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.rg.entity.Employee;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
  
}
