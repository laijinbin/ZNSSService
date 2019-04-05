package com.ittest.controller;

import com.github.pagehelper.PageInfo;
import com.ittest.entiry.Device;
import com.ittest.service.DeviceService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.swing.*;

@Secured("ROLE_ADMIN")
@Controller
@RequestMapping("/device")
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    @RequestMapping("/findAll")
    public ModelAndView findAll( @RequestParam(defaultValue = "1") int pageNum,
                                 @RequestParam(defaultValue = "5") int pageSize){
        PageInfo<Device> pageInfo=deviceService.findAll(pageNum,pageSize);
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("product-list");
        modelAndView.addObject("pageInfo",pageInfo);
        return modelAndView;
    }

    @RequestMapping("/save")
    public String save(Device device){
        try {
            deviceService.save(device);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/device/findAll";

    }
    @RequestMapping("/toUpdate")
    public ModelAndView toUpdate(String deviceId){
        ModelAndView mv=new ModelAndView();
        Device device=deviceService.findById(deviceId);
        mv.addObject("device",device);
        mv.setViewName("product-update");
        return mv;
    }
    @RequestMapping("/update")
    public String update(Device device){
        deviceService.update(device);
        return "redirect:/device/findAll";
    }
    @RequestMapping("/delete")
    public String delete(String ids){
        String[] deviceIds=ids.split(",");
        deviceService.delete(deviceIds);
        return "redirect:/device/findAll";
    }
}
