package com.bjpowernode.crm.exception;

/**
 * DESCRIPTION:
 * user:
 * date:2019/5/13  17:22
 */
public class LoginException extends Exception{//自定义受检异常
    public LoginException (String msg){
        super(msg);
    }
}
