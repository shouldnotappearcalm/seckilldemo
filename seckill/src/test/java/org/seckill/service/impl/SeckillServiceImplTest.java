package org.seckill.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by GZR on 2016/11/23.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml","classpath:spring/spring-service.xml"})
public class SeckillServiceImplTest {
    private Logger logger= LoggerFactory.getLogger(this.getClass());
    @Resource
    private SeckillService seckillService;

    @Test
    public void getSeckillList() throws Exception {
        List<Seckill> list=seckillService.getSeckillList();
        for(Seckill seckill:list){
            logger.info(seckill.getSeckillId()+","+seckill.getName()+","+seckill.getNumber());
        }
    }

    @Test
    public void exportSeckillUrl() throws Exception {
        Long seckillId=10000L;//0ef23feafa56d2384d71837eba85646f
        Exposer exposer=seckillService.exportSeckillUrl(seckillId);
        logger.info(exposer.isExposed()+","+exposer.getSeckillId()+","+exposer.getMd5());
    }

    @Test
    public void executeSeckill() throws Exception {
        Long seckillId=10000L;
        Long userPhone=13348970746L;
        String md5="0ef23feafa56d2384d71837eba85646f";
        SeckillExecution seckillExecution=seckillService.executeSeckill(seckillId,userPhone,md5);
        logger.info(seckillExecution.getSeckillId()+","+seckillExecution.getStateInfo()+","+seckillExecution.getSuccessKilled());
    }

}