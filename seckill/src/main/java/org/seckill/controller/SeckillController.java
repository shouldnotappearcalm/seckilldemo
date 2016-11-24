package org.seckill.controller;

import org.seckill.entity.Seckill;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by GZR on 2016/11/24.
 */
@Controller
@RequestMapping("/seckill")
public class SeckillController {
    Logger logger= LoggerFactory.getLogger(this.getClass());
    @Resource
    private SeckillService seckillService;

    @RequestMapping(value="/list",method = RequestMethod.POST)
    public String getList(Model model){
        List<Seckill> list=seckillService.getSeckillList();
        model.addAttribute("list",list);
        return "list";
    }

}
