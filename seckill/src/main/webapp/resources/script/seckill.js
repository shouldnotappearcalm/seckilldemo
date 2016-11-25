/**
 * Created by GZR on 2016/11/24.
 */
seckill={
    URL:{
        now:function () {
            return "/seckill/time/now";
        },
        exposer:function (seckillId) {
            return "/seckill/"+seckillId+"/exposer";
        },
        seckillUrl:function (seckillId,md5) {
            return "/seckill/"+seckillId+"/"+md5+"/execution";
        }
    },
    validatePhone:function (killPhone) {
      if(killPhone&&killPhone.length==11&&!isNaN(killPhone)){
          return true;
      }
        return false;
    },
    handlerSeckill:function (seckillId,node) {
        node.hide().html('<button class="btn btn-primary btn-lg" id="killBtn">begin seckill</button>');
        $.post(seckill.URL.exposer(seckillId),{},function (result) {
            if(result&&result['success']){
                var exposer=result['data'];
                if(exposer['exposed']){
                    var md5=exposer['md5'];
                    var seckillUrl=seckill.URL.seckillUrl(seckillId,md5);
                    console.log("killUrl:"+seckillUrl);
                    $('#killBtn').one('click',function () {
                        console.log('first click');
                        $(this).addClass('disabled');
                        $.post(seckillUrl,{},function (result) {
                           var killResult= result['data'];
                            var state=killResult['state'];
                            var stateInfo=killResult['stateInfo'];
                            node.html('<span class="label label-success">'+stateInfo+'</span>');
                        });
                    });
                    node.show(300);
                }
            }
        });
    },
    countDwon:function (seckillId,nowTime,startTime,endTime) {
        var seckillBox=$('#seckill-box');
        if(nowTime>endTime){
            seckillBox.html("seckill is end");
        }
        else if(nowTime<startTime){
            console.log('now time is less than startTime');
            var killTime=new Date(startTime+1000);
            seckillBox.countdown(killTime,function (event) {
                var format=event.strftime('seckill countDown:%D day %H hour %M minutes %S second');
                seckillBox.html(format);
            }).on('finish.countdown',function () {
                //handle seckill request
                seckill.handlerSeckill(seckillId,seckillBox);
            });
        }else{
            //handle seckill request
            seckill.handlerSeckill(seckillId,seckillBox);
        }
    },
    detail:{
        init:function (params) {
            var seckillId=params['seckillId'];
            var startTime=params['startTime'];
            var endTime=params['endTime'];
            var createTime=params['createTime'];
            var killPhone=$.cookie('killPhone');
            if(!seckill.validatePhone(killPhone)){
                var killPhoneModal=$('#killPhoneModal');
                killPhoneModal.modal({
                   show:true,
                    backdrop:'static',
                    keyboard:false
                });
                $('#killPhoneBtn').click(function () {
                   var inputPhone=$('#killPhoneKey').val();
                    if(seckill.validatePhone(inputPhone)){
                        $.cookie('killPhone',inputPhone,{expires:7,path:'/seckill'});
                        window.location.reload();
                    }else {
                        $('#killPhoneMessage').hide().html('<label class="label label-danger">手机号错误</label>').show(300);
                    }
                });
            }
            $.get(seckill.URL.now(),function (result) {
               if(result&&result['success']){
                   var nowTime=result['data'];
                   seckill.countDwon(seckillId,nowTime,startTime,endTime);
               }
            });
        }
    }
}