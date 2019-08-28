package com.wjwcloud.elk.test;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

public class test {

    public static void main(String[] args) {
        Map<Object, String> hashMap = new HashMap<>();
        Map<Object, String> treeMap = new TreeMap<>();
        Map<Object,String> hashTable = new Hashtable<>();
        Map<Object,String> concurrentHashMap = new ConcurrentHashMap<>();
    }

}
