package com.sunshine1027.config.admin.controller;

import com.sunshine1027.config.core.SunshineConfigClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
        return sunshineConfigClient.getAllKeyValues().toString();
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    @ResponseBody
    public String addNew(@RequestParam String key, @RequestParam String value) {
        SunshineConfigClient sunshineConfigClient = new SunshineConfigClient();
        sunshineConfigClient.setValue(key, value);
        return "success";
    }

    @RequestMapping(value = "/update", method = RequestMethod.GET)
    @ResponseBody
    public String update(@RequestParam String key, @RequestParam String value) {
        SunshineConfigClient sunshineConfigClient = new SunshineConfigClient();
        sunshineConfigClient.setValue(key, value);
        return "success";
    }
}
