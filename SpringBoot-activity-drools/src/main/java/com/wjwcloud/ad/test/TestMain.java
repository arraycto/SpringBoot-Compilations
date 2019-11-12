package com.wjwcloud.ad.test;

import com.wjwcloud.ad.client.constant.ResultCodeEnum;
import com.wjwcloud.ad.system.exception.BusinessRuntimeException;

public class TestMain {
    public static void main(String[] args) {
       Error error = new Error();
       error.testError();
    }

   public static class Error{
       public void testError(){
           if (true) {
               throw new BusinessRuntimeException(ResultCodeEnum.RULE_CHECK_FAIL.getCode(), "error");
           }
           System.out.println("-----------re");
           return;
        }
    }
}
