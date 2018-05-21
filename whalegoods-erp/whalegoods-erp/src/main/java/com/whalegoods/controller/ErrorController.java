package com.whalegoods.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 错误处理API
 * @author henrysun
 * 2018年5月14日 下午7:40:55
 */
@Controller
@RequestMapping(value = "/error")
public class ErrorController {

  @GetMapping(value = "404")
  public String pageNotFound(){
  return "error/404";
}

  @GetMapping(value = "403")
  public String NotFound(String message,Model model){
    if(!StringUtils.isEmpty(message)){
      model.addAttribute("message",message);
    }
  return "error/403";
}

}
