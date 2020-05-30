package com.eagle.eavlms;

import org.junit.Test;

import java.util.Random;

public class DemoTest {
    @Test
    public void test(){
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            int i1 = random.nextInt(10);
            System.out.println(i1);
        }
    }
}
