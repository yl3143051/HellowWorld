package com.lee.learn;

public class VolatileLearnTest {
    //并发编程三大特性 可见性 原子性 有序性

    //不用volatile修饰那么主线程中的共享变量不是实时可见的
    private static boolean initFlag = false;

    /*加了volatile关键字后 让每总线程和每个线程之间有了MESI缓存一致性协议
    当每个单线程中对共享的参数进行store原子操作的时候 就会触发总线嗅探机制进行监听 如果监测到了store操作
    那么这个参数在各自的线程中的内存副本中参数就会失效

     volatile 缓存可见性实现原理
     底层实现主要通过汇编lock前缀指令，他会锁定这块内存区域的缓存（缓存行锁定）并回显到主内存
     IA-32架构软件开发者手册对lock指令的说明
     (1)会将当前处理器缓存的数据立即写回到系统内存中
     (2)这个写会内存的操作会引起在其他CPU里缓存了该内存地址的数据无效（MESI）

     */
//    private volatile static boolean initFlag = false;

    public static void main(String[] args) throws Exception{
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(111111);
                while (!initFlag) {
                }
                System.out.println(233333);
            }
        }).start();

        Thread.sleep(2000);

        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(initFlag);
                changeFlag();
                System.out.println(initFlag);
            }
        }).start();

    }

    public static void changeFlag() {
        initFlag = true;
    }
}
