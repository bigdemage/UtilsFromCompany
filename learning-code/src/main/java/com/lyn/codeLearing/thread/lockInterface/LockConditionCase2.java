package com.lyn.codeLearing.thread.lockInterface;


import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * lock和condition级联调用
 */
public class LockConditionCase2 {

    private static Lock myLock = new ReentrantLock(false);

    private static Condition myCondition = myLock.newCondition();

    /**
     * 实例场景：
     * 定义5个等待线程如下：
     * 线程0：通过await()进行等待。
     * 线程1：通过awaitNanos(long)进行等待，long=1000000000，即1秒钟。
     * 线程2：通过await(long,TimeUnit)进行等待，long=2，TimeUnit=TimeUnit.SECONDS，即2秒钟。
     * 线程3：通过awaitUntil(deadline)进行等待，System.currentTimeMillis() + 5000，即5秒之后的时刻。
     * 线程4：通过awaitUninterruptibly()进行等待。
     * 场景：
     * 在5个测试线程启动100毫秒之后，通过interrupt()中断所有线程。
     *
     * @param args
     */
    public static void main(String[] args) {
        Thread thread0 = new Thread(() -> {
            myLock.lock();
            System.out.println(Thread.currentThread().getName() + "获得锁");
            try {
                System.out.println(Thread.currentThread().getName() + "调用await");
                myCondition.await();
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + "->被中断");
            } finally {
                myLock.unlock();
                System.out.println(Thread.currentThread().getName() + "释放锁");
            }

        });
        Thread thread1 = new Thread(() -> {
            myLock.lock();
            System.out.println(Thread.currentThread().getName() + "获得锁");
            try {
                System.out.println(Thread.currentThread().getName() + "调用awaitNanos()1秒");
                myCondition.awaitNanos(1000000000);
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + "->被中断");
            } finally {
                myLock.unlock();
                System.out.println(Thread.currentThread().getName() + "释放锁");
            }
        });
        Thread thread2 = new Thread(() -> {
            myLock.lock();
            System.out.println(Thread.currentThread().getName() + "获得锁");
            try {
                System.out.println(Thread.currentThread().getName() + "调用await(time)2秒");
                myCondition.await(2, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + "->被中断");
            } finally {
                myLock.unlock();
                System.out.println(Thread.currentThread().getName() + "释放锁");
            }

        });
        Thread thread3 = new Thread(() -> {
            myLock.lock();
            System.out.println(Thread.currentThread().getName() + "获得锁");
            try {
                System.out.println(Thread.currentThread().getName() + "调用awaitUntil(System.currentTimeMillis()+5000)调用后的5秒");
                myCondition.awaitUntil(new Date(System.currentTimeMillis() + 5000));
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + "->被中断");
            } finally {
                System.out.println(Thread.currentThread().getName() + "释放锁");
                myLock.unlock();
            }

        });
        Thread thread4 = new Thread(() -> {
            try {
                myLock.lock();
                System.out.println(Thread.currentThread().getName() + "获得锁");
                System.out.println(Thread.currentThread().getName() + "调用awaitUninterruptibly调用");
                myCondition.awaitUninterruptibly();
                myLock.unlock();
                System.out.println(Thread.currentThread().getName() + "释放锁");
            } catch (Exception e) {
                System.out.println(Thread.currentThread().getName() + "->被中断");
            }

        });
        startAll(thread0, thread1, thread2, thread3, thread4);
        try {
            TimeUnit.MILLISECONDS.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        interuptAll(thread0, thread1, thread2, thread3, thread4);

    }

    private static void startAll(Thread thread0, Thread thread1, Thread thread2, Thread thread3, Thread thread4) {
        thread0.start();
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
    }

    private static void interuptAll(Thread thread0, Thread thread1, Thread thread2, Thread thread3, Thread thread4) {
        thread0.interrupt();
        thread1.interrupt();
        thread2.interrupt();
        thread3.interrupt();
        thread4.interrupt();
    }
}
