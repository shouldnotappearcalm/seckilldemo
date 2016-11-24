package org.seckill.dao;

import org.apache.ibatis.annotations.Param;
import org.seckill.entity.Seckill;

import java.util.Date;
import java.util.List;

/**
 * Created by GZR on 2016/11/23.
 */
public interface SeckillDao {

    /**
     * get all shops ,method default is public
     * @return
     */
    List<Seckill> getAll();

    /**
     *
     * @param seckillId
     * @param killTime
     * @return
     */
    int reduceNumber(@Param("seckillId") long seckillId,@Param("killTime") Date killTime);

    /**
     * select seckill goods  by seckillId
     * @param seckillId
     * @return
     */
    Seckill queryById(@Param("seckillId") long seckillId);

}