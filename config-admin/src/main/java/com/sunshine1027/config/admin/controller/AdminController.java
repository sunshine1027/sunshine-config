package com.sunshine1027.config.admin.controller;

import com.sunshine1027.config.core.SunshineConfigClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author sunshine1027 [sunshine10271993@gmail.com]
 */
@Controller
public class AdminController {
    @RequestMapping(value = "/getAllData", method = RequestMethod.GET)
    @ResponseBody
    public String getAllData() {
        SunshineConfigClient sunshineConfigClient = new SunshineConfigClient();
        String s = sunshineConfigClient.getValue("testKey");
        return s;
    }
}
