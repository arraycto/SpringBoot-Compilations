package com.wjwcloud.elk.controller;

import com.wjwcloud.elk.aspect.annotation.SysLog;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LogstashController {

    @SysLog(isLog = true)
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public String get(@RequestParam String test) {
        return "Success";
    }
}
