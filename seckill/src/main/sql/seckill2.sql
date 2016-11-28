--seckill execute procedure
DELIMITER $$  --console ; convert to $$
--define procedure
--parameter
--row_count() 返回上一条修改类型sql(delete insert update)的影响行数
--row_count() 0:未修改数据 >0 ;表示修改的行数  <0;sql错误，未执行修改sql
CREATE PROCEDURE  execute_seckill
(in v_seckill_id bigint,in v_phone bigint,in v_kill_time TIMESTAMP ,out r_result int)
BEGIN
  DECLARE insert_count int default 0;
  START TRANSACTION ;
  insert ignore into success_killed (seckill_id,user_phone,create_time)
  values(v_seckill_id,v_phone,v_kill_time);
  select row_count() into insert_count;
  if(insert_count=0)THEN
    ROLLBACK ;
    set r_result=-1;
  elseif(insert_count<0)THEN
    ROLLBACK ;
    set r_result=-2;
  else
    update seckill set number=number-1
    where seckill_id=v_seckill_id
      and end_time>v_kill_time
      and start_time<v_kill_time
      and number>0;
    select row_count() into insert_count;
    if(insert_count==0)THEN
      ROLLBACK ;
      set r_result=0;
    elseif(insert_count<0)THEN
      ROLLBACK ;
      set r_result=-2;
    else
      COMMIT ;
      set r_result=1;
    end if;
  end if;
end;
$$
--换行符

--procedure is end
DELIMITER ;
set @result=-3;
--execute procedure
call execute_seckill(10003,11111,now(),@result);

--get result
select @result;

--存储过程
--1.存储过程优化 ：事物行级锁持有的时间
--2.不要过度依赖于存储过程
--3.简单的逻辑可以应用存储过程
--4.QPS:一个商品接近6000左右的qps