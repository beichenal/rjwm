package com.example.rg.common;

import java.sql.SQLIntegrityConstraintViolationException;

import javax.xml.crypto.dsig.keyinfo.RetrievalMethod;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

/**
 * 全局异常处理
 */
@ControllerAdvice(annotations = { RestController.class, Controller.class })
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

  /**
   * 处理 SQLIntegrityConstraintViolationException
   * @return
   */
  @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
  public R<String> excepionHandler(SQLIntegrityConstraintViolationException ex) {
    log.info("全局处理 SQLIntegrityConstraintViolationException 异常");
    log.error(ex.getMessage());
    if(ex.getMessage().contains("Duplicate entry")){
      String[] split = ex.getMessage().split(" ");
      String msg = split[2] + " 已存在";
      return R.error(msg);
    }
    return R.error("未知错误");
  }
}
