package org.seckill.exception;

/**
 * Created by GZR on 2016/11/23.
 */
public class SeckillCloseException extends  SeckillException {

    public SeckillCloseException(String message) {
        super(message);
    }

    public SeckillCloseException(String message, Throwable cause) {
        super(message, cause);
    }
}
