package org.seckill.dao;

import org.apache.ibatis.annotations.Param;
import org.seckill.entity.Seckill;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by GZR on 2016/11/23.
 */
public interface SeckillDao {

    /**
     * get all shops ,method default is public
     * @return
     */
    List<Seckill> getAll(@Param("offset")int offset,@Param("limit")int limit);

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

    /**
     * execute seckill by procedure
     * @param paramMap
     */
    void killByProcedure(Map<String,Object> paramMap);

}
