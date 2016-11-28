package org.seckill.service.impl;

import org.apache.commons.collections.MapUtils;
import org.seckill.dao.SeckillDao;
import org.seckill.dao.SuccessKilledDao;
import org.seckill.dao.cache.RedisDao;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.entity.SuccessKilled;
import org.seckill.enums.SeckillStateEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by GZR on 2016/11/23.
 */
@Service("seckillService")
public class SeckillServiceImpl implements SeckillService{
    private Logger logger= LoggerFactory.getLogger(this.getClass());
    @Resource
    private SeckillDao seckillDao;
    @Resource
    private SuccessKilledDao successKilledDao;
    @Resource
    private RedisDao redisDao;
    //salt
    private  String salt="daewaewq'.;/e213sd.;.,,lksd;lad";

    public List<Seckill> getSeckillList() {
        return seckillDao.getAll(0,4);
    }

    public Seckill querySeckillById(long seckillId) {
        return seckillDao.queryById(seckillId);
    }

    public Exposer exportSeckillUrl(long seckillId) {
        //need to optimize,cache optimization
        /**
         * get from cache
         * if null
         *      get db
         * else
         *      get redis cache
         */
        Seckill seckill=redisDao.getSeckill(seckillId);
        if(seckill==null){
            //get seckill from db
            seckill=seckillDao.queryById(seckillId);
            if(seckill==null){
                return new Exposer(false,seckillId);
            }else{
                //3 put seckill in redis
                redisDao.putSeckill(seckill);
            }
        }


        Date nowTime=new Date();
        Date startTime=seckill.getStartTime();
        Date endTime=seckill.getEndTime();
        if(nowTime.getTime()>endTime.getTime()||nowTime.getTime()<startTime.getTime()){
            return new Exposer(false,seckillId,nowTime.getTime(),startTime.getTime(),endTime.getTime());
        }
        String md5=getMD5(seckillId);
        return new Exposer(true,md5,seckillId);
    }

    private String getMD5(long seckillId){
        String base=seckillId+"/"+salt;
        String md5= DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }

    @Transactional
    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5) throws RepeatKillException, SeckillCloseException, SeckillException {
        try {
            if(null==md5||!md5.equals(getMD5(seckillId))){
                throw  new SeckillException("seckill data rewrite");
            }
            Date nowTime=new Date();
            //record purchase
            int insertCount=successKilledDao.insertSuccessKilled(seckillId,userPhone);
            if(insertCount<=0){
                //repeat seckill
                throw new RepeatKillException("repeat seckill");
            }else{
                //get rowLock
                int updateCount=seckillDao.reduceNumber(seckillId,nowTime);
                if(updateCount<=0){
                    throw new SeckillCloseException("seckill is closed");
                }else{
                    //seckill is success,commit
                    SuccessKilled successKilled=successKilledDao.queryByIdWithSeckill(seckillId,userPhone);
                    return new SeckillExecution(seckillId, SeckillStateEnum.SUCCESS,successKilled);
                }
            }

        }catch (SeckillCloseException e1){
            throw e1;
        }catch (RepeatKillException e2){

        }catch (Exception e){
            logger.error(e.getMessage(),e);
            throw new SeckillException("seckill inner error:"+e.getMessage());
        }

        return null;
    }

    public SeckillExecution executeSeckillProcedure(long seckillId, long userPhone, String md5) throws RepeatKillException, SeckillCloseException, SeckillException {
        if(null==md5||!md5.equals(getMD5(seckillId))){
            return new SeckillExecution(seckillId,SeckillStateEnum.DATE_REWRITE);
        }
        Date killTime=new Date();
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("seckillId",seckillId);
        map.put("phone",userPhone);
        map.put("killTime",killTime);
        map.put("result",null);
        try {
            seckillDao.killByProcedure(map);
            //get result
            int result=MapUtils.getInteger(map,"result",-2);
            if(result==1){
                SuccessKilled successKilled=successKilledDao.queryByIdWithSeckill(seckillId,userPhone);
                return new SeckillExecution(seckillId,SeckillStateEnum.SUCCESS,successKilled);
            }else{
                return new SeckillExecution(seckillId,SeckillStateEnum.stateOf(result));
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return new SeckillExecution(seckillId,SeckillStateEnum.INNER_ERROR);
        }
    }
}
