package org.seckill.exception;

/**
 * Created by GZR on 2016/11/23.
 */
public class RepeatKillException  extends  SeckillException{

    public RepeatKillException(String message) {
        super(message);
    }

    public RepeatKillException(String message, Throwable cause) {
        super(message, cause);
    }
}
