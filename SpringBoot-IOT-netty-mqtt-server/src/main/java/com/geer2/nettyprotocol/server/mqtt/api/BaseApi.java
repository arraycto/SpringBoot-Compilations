package com.geer2.nettyprotocol.server.mqtt.api;

import javax.validation.constraints.NotNull;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * 逻辑操作封装
 *
 * @author JiaweiWu
 * @create
 **/
public interface BaseApi {


    /**
     *  if else
     * @param t
     * @param predicate
     * @param consumer
     * @param <T>
     */
    default  <T> void  doIfElse(T t, Predicate<T> predicate, Consumer<T> consumer){
        if(t!=null){
            if(predicate.test(t)){
                consumer.accept(t);
            }
        }
    }


    /**
     * if else
     * @param t
     * @param predicate
     * @param consumer
     * @param consumer2
     * @param <T>
     */
    default  <T> void  doIfElse(T t, Predicate<T> predicate, Consumer<T> consumer, Consumer<T> consumer2){
        if(t!=null){
            if(predicate.test(t)){
                consumer.accept(t);
            }
            else{
                consumer2.accept(t);
            }
        }
    }

    /**
     * if
     * @param t
     * @param predicates
     * @param <T>
     * @return
     */
    default  <T> boolean  doIf(T t, Predicate<T>... predicates){
        if(t!=null){
            for(Predicate<T> p:predicates){
                if(!p.test(t)){
                    return false;
                }
            }
            return true;
        }
        return  false;
    }

    /**
     * if and
     * @param t
     * @param consumer2
     * @param predicates
     * @param <T>
     */
    default  <T> void   doIfAnd(T t, Consumer<T> consumer2, Predicate<T>... predicates){
        boolean flag =true;
        if(t!=null){
            for(Predicate<T> p:predicates){
                if(!p.test(t)){
                    flag= false;
                    break;
                }
            }
        }
        if(flag){
            consumer2.accept(t);
        }
    }

    /**
     * if and
     * @param t
     * @param consumer2
     * @param predicates
     * @param <T>
     */
    default  <T> void   doIfAnd1(@NotNull T t, @NotNull Consumer<T> consumer2, @NotNull Predicate<T>... predicates){
        Predicate<T> one = predicates[0];
        int l;
        if((l=predicates.length)>1){
            for(int i=1;i<l;i++){
                one=one.and(predicates[i]);
            }
        }
        if(one.test(t)){
            consumer2.accept(t);
        }
    }

}
