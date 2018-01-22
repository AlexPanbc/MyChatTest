package com.pbc.bio;

import java.util.concurrent.*;

/**
 * 线程池
 * Created by Alex on 2018/1/22.
 */
public class HanderExecutorPool {
    public ExecutorService executor = null;
    /**
     * 活动线程数
     */
    public int activeCount = 0;
    public ArrayBlockingQueue wqueue = null;

    public HanderExecutorPool(int maxSize, int quereSize) {

        wqueue = new ArrayBlockingQueue<Runnable>(quereSize);
        /**
         * Runtime.getRuntime().availableProcessors() cpu自动分配线程
         * maxSize最大线程数
         * 120L秒
         *TimeUnit.SECONDS单位秒
         * ArrayBlockingQueue阻塞试队列
         * */
        this.executor = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(), maxSize, 120L, TimeUnit.SECONDS, wqueue);
    }

    public void execute(Runnable task) {
        executor.execute(task);
        ThreadPoolExecutor t = (ThreadPoolExecutor) executor;
        activeCount = t.getActiveCount();
    }
}
