package com.lee.learn;

import com.lee.pojo.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ThreadLearn {

    public static void main(String[] args) throws Exception{
        final List<User> list = new ArrayList<>();
        for (int i = 1; i < 1000; i++) {
            User user = new User();
            user.setId(i);
            user.setName("name" + i);
            user.setAge(i % 2 == 0 ? 18 : 20);
            list.add(user);
        }

        ExecutorService executor = Executors.newFixedThreadPool(10);

        long time1 = System.currentTimeMillis();
//        for (final User a : list) {
//            Runnable task = new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        a.setAge(11);
//                        Thread.sleep(4);
//                    } catch (Exception e) {
//                    }
//                }
//            };
//            executor.execute(task);
//        }
        final List<User> list1 = new ArrayList<>();
        for (final User a : list) {
            Runnable task = new Runnable() {
                @Override
                public void run() {
                    try {
                        a.setAge(11);
                        Thread.sleep(4);
                        synchronized (ThreadLearn.class) {
                            list1.add(a);
                        }
                    } catch (Exception e) {
                    }
                }
            };
            executor.execute(task);
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);
        System.out.println("处理时间："+(System.currentTimeMillis() - time1));

        System.out.println(list.size());

        System.out.println(list1.size());
//        for (User user : list) {
//            System.out.println(user.getAge());
//        }
    }
}
