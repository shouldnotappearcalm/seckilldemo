package org.seckill.enums;

/**
 * Created by GZR on 2016/11/23.
 */
public enum  SeckillStateEnum {
    SUCCESS(1,"success seckill"),
    END(0,"end of seckill"),
    REPEAT_KILL(-1,"repeat kill"),
    INNER_ERROR(-2,"inner error"),
    DATE_REWRITE(-3,"data tamper");

    private int state;
    private String stateinfo;

    SeckillStateEnum(int state, String stateinfo) {
        this.state = state;
        this.stateinfo = stateinfo;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateinfo() {
        return stateinfo;
    }

    public void setStateinfo(String stateinfo) {
        this.stateinfo = stateinfo;
    }
    public static SeckillStateEnum stateOf(int index){
        for(SeckillStateEnum state:values()){
            if(state.getState()==index){
                return state;
            }
        }
        return null;
    }
}
