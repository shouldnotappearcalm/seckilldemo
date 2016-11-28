package org.seckill.controller;

import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.dto.SeckillResult;
import org.seckill.entity.Seckill;
import org.seckill.enums.SeckillStateEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
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

    @RequestMapping(value="/list",method = RequestMethod.GET)
    public String getList(Model model){
        logger.error("come list","hahah");
        List<Seckill> list=seckillService.getSeckillList();
        logger.debug(""+list.size());
        for(Seckill seckill:list){
            logger.info("xxxxx"+seckill.getSeckillId()+","+seckill.getName()+","+seckill.getNumber()+","+seckill.getStartTime());
        }
        model.addAttribute("list",list);
        model.addAttribute("test","666666");
        return "list";
    }
    @RequestMapping(value = "/{seckillId}/detail",method = RequestMethod.GET)
    public String detail(@PathVariable("seckillId") Long seckillId, Model model){
        if(seckillId==null){
            return "redirect:/seckill/list";
        }
        Seckill seckill=seckillService.querySeckillById(seckillId);
        if(seckill==null){
            return "forward:/seckill/list";
        }
        model.addAttribute("seckill",seckill);
        return "detail";
    }

    @RequestMapping(value="/{seckillId}/exposer",method = RequestMethod.POST,produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult<Exposer> exportUrl(@PathVariable("seckillId") Long seckillId){
        try {
            Exposer exposer=seckillService.exportSeckillUrl(seckillId);
            return new SeckillResult<Exposer>(true,exposer);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return new SeckillResult<Exposer>(false,e.getMessage());
        }
    }

    @RequestMapping(value="/{seckillId}/{md5}/execution",method = RequestMethod.POST,produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult<SeckillExecution> executeSeckill(@PathVariable("seckillId") Long seckillId, @PathVariable("md5") String md5,@CookieValue(value="killPhone",required = false) Long userPhone){
        if(userPhone==null){
            return new SeckillResult<SeckillExecution>(false,"未注册");
        }
        try {
            //call procedure
            SeckillExecution seckillExecution=seckillService.executeSeckillProcedure(seckillId,userPhone,md5);
            return new SeckillResult<SeckillExecution>(true,seckillExecution);
        }catch (SeckillCloseException e1){
            SeckillExecution seckillExecution=new SeckillExecution(seckillId,SeckillStateEnum.END);
            return new SeckillResult<SeckillExecution>(true,seckillExecution);
        }catch (RepeatKillException e2){
            SeckillExecution seckillExecution=new SeckillExecution(seckillId,SeckillStateEnum.REPEAT_KILL);
            return new SeckillResult<SeckillExecution>(true,seckillExecution);
        }catch (SeckillException e3){
            SeckillExecution seckillExecution=new SeckillExecution(seckillId,SeckillStateEnum.INNER_ERROR);
            return new SeckillResult<SeckillExecution>(true,seckillExecution);
        }catch (Exception e){
            logger.info(e.getMessage(),e);
            SeckillExecution execution=new SeckillExecution(seckillId, SeckillStateEnum.INNER_ERROR);
            return new SeckillResult<SeckillExecution>(true,execution);
        }
    }

    @RequestMapping(value="/time/now",method = RequestMethod.GET)
    @ResponseBody
    public SeckillResult<Long> now(){
        Date date=new Date();
        return new SeckillResult<Long>(true,date.getTime());
    }

}
