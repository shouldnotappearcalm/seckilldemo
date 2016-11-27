package org.seckill.dao.cache;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import org.seckill.entity.Seckill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Created by GZR on 2016/11/27.
 */
public class RedisDao {

    private Logger logger= LoggerFactory.getLogger(this.getClass());

    private JedisPool jedisPool;
    public RedisDao(String ip,int port){
        jedisPool=new JedisPool(ip,port);
    }

    private RuntimeSchema<Seckill> schema=RuntimeSchema.createFrom(Seckill.class);

    public Seckill getSeckill(long seckillId){
        //redis operation logiv
        try {
            Jedis jedis=jedisPool.getResource();
            try {
                String key="seckill:"+seckillId;
                //not implemented the internal serialization operation
                //get->byte[]->deserialize->Object[Seckill]
                //采用自定义序列化
                //protostuff:pojo
                byte[] bytes=jedis.get(key.getBytes());
                //缓冲中获取到
                if(bytes!=null){
                    //空对象
                    Seckill seckill=schema.newMessage();
                    ProtostuffIOUtil.mergeFrom(bytes,seckill,schema);
                    //seckill被反序列化
                    return seckill;
                }
            }finally {
                jedis.close();
            }
        }catch (Exception e){

        }
        return null;
    }
    public String putSeckill(Seckill seckill){
        //set Object[Seckill]->序列化->byte[]
        try {
            Jedis jedis=jedisPool.getResource();
            try {
                String key="seckill:"+seckill.getSeckillId();
                byte[] bytes=ProtostuffIOUtil.toByteArray(seckill,schema, LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
                //超时缓存  一个小时 60*60
                int timeout=60*60;
                String result=jedis.setex(key.getBytes(),timeout,bytes);
                return result;
            }finally {
                jedis.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

}
