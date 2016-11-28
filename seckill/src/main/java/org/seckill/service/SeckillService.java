package org.seckill.service;

import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;

import java.util.List;

/**
 * Created by GZR on 2016/11/23.
 */
public interface SeckillService {
    /**
     * select all goods
     * @return
     */
    List<Seckill> getSeckillList();

    /**
     *
     * @return
     */
    Seckill querySeckillById(long seckillId);
    /**
     * get seckill url
     * @param seckillId
     * @return
     */
    Exposer exportSeckillUrl(long seckillId);

    /**
     * execute seckill by procedure
     * @param seckillId
     * @param userPhone
     * @param md5
     * @return
     * @throws RepeatKillException
     * @throws SeckillCloseException
     * @throws SeckillException
     */
    SeckillExecution executeSeckillProcedure(long seckillId,long userPhone,String md5)throws RepeatKillException,SeckillCloseException,SeckillException;


    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5) throws RepeatKillException, SeckillCloseException, SeckillException;
}
