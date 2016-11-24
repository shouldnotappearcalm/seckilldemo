package org.seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.Seckill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by GZR on 2016/11/23.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SeckillDaoTest {
    private Logger logger= LoggerFactory.getLogger(this.getClass());
    @Resource
    private SeckillDao seckillDao;

    @Test
    public void getAll() throws Exception {
        List<Seckill> list=seckillDao.getAll(0,4);
        for(Seckill seckill:list){
            logger.info(seckill.getSeckillId()+","+seckill.getName()+","+seckill.getNumber()+","+seckill.getStartTime());
        }
    }

    @Test
    public void reduceNumber() throws Exception {
        long seckillId=10000L;
        int count=seckillDao.reduceNumber(seckillId,new Date());
        logger.info("count:"+count);
    }

    @Test
    public void queryById() throws Exception {
        long seckillId=10000L;
        Seckill seckill=seckillDao.queryById(seckillId);
        logger.info(seckill.getSeckillId()+","+seckill.getName()+","+seckill.getNumber()+","+seckill.getStartTime());
    }

}