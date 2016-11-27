package org.seckill.dao.cache;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dao.SeckillDao;
import org.seckill.entity.Seckill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * Created by GZR on 2016/11/27.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class RedisDaoTest {

    private Logger logger= LoggerFactory.getLogger(this.getClass());
    @Resource
    private RedisDao redisDao;
    @Resource
    private SeckillDao seckillDao;

    private long id=10002L;
    @Test
    public void testSeckill()throws  Exception{
        Seckill seckill=redisDao.getSeckill(id);
        if(seckill==null){
            seckill=seckillDao.queryById(id);
            if(seckill!=null){
                String result=redisDao.putSeckill(seckill);
                logger.info("result={}",result);
                seckill=redisDao.getSeckill(id);
                logger.info("seckill={}",seckill);
            }
        }
    }

}