package com.example.android.tobefocused;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;

public class AppExecutors {

    private final Executor mDiskIO;
    private final Executor mNetworkIO;
    private final Executor mMainThread;
    private static AppExecutors INCTANCE;


    private AppExecutors(Executor diskIO, Executor networkIO, Executor mainThread) {
        this.mDiskIO = diskIO;
        this.mNetworkIO = networkIO;
        this.mMainThread = mainThread;
    }

    public static AppExecutors getInstance() {
        if (INCTANCE == null) {
            synchronized (AppExecutors.class) {
                if (INCTANCE == null) {
                    INCTANCE = new AppExecutors(
                            Executors.newSingleThreadExecutor(),
                            Executors.newFixedThreadPool(3),
                            new MainThreadExecutor());
                }
            }
        }
        return INCTANCE;
    }

    public Executor diskIO() {
        return mDiskIO;
    }

    public Executor mainThread() {
        return mMainThread;
    }

    public Executor networkIO() {
        return mNetworkIO;
    }

    private static class MainThreadExecutor implements Executor {
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command);
        }
    }
}
