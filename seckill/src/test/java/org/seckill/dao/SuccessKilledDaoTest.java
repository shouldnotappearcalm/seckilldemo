package org.seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.Seckill;
import org.seckill.entity.SuccessKilled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * Created by GZR on 2016/11/23.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SuccessKilledDaoTest {
    private Logger logger= LoggerFactory.getLogger(this.getClass());
    @Resource
    private SuccessKilledDao successKilledDao;

    @Test
    public void insertSuccessKilled() throws Exception {
        long seckillId=10003L;
        long userPhone=13345679876L;
        int count=successKilledDao.insertSuccessKilled(seckillId,userPhone);
        logger.info("count={}",count);
    }

    @Test
    public void queryByIdWithSeckill() throws Exception {
        long seckillId=10003L;
        long userPhone=13345679876L;
        SuccessKilled successKilled=successKilledDao.queryByIdWithSeckill(seckillId,userPhone);
        Seckill seckill=successKilled.getSeckill();
        logger.info(successKilled.getSeckillId()+","+successKilled.getUserPhone()+","+successKilled.getState()+","+successKilled.getCreateTime());
        logger.info(seckill.getSeckillId()+","+seckill.getName());
    }

}