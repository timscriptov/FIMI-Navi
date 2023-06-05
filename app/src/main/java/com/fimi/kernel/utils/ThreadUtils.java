package com.fimi.kernel.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ThreadUtils {
    private static ExecutorService executorService;

    public static synchronized ExecutorService getCachedThreadPool() {
        ExecutorService executorService2;
        synchronized (ThreadUtils.class) {
            if (executorService == null) {
                executorService = Executors.newCachedThreadPool();
            }
            executorService2 = executorService;
        }
        return executorService2;
    }

    public static void releaseThreadPool() {
        if (executorService != null) {
            executorService.shutdownNow();
            executorService = null;
        }
    }

    public static void runInThreadPool(Runnable runnable) {
        getCachedThreadPool().execute(runnable);
    }

    public static void execute(Runnable task) {
        getCachedThreadPool().execute(task);
    }
}
