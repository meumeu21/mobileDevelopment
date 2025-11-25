package com.example.rumireaborlykovacalendarapp.data.local;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DatabaseExecutor {
    private static final Executor diskIO = Executors.newSingleThreadExecutor();
    private static final Handler mainThreadHandler = new Handler(Looper.getMainLooper());

    public interface DatabaseOperation<T> {
        T execute();
    }

    public interface Callback<T> {
        void onComplete(T result);
    }

    public static <T> void execute(DatabaseOperation<T> operation, Callback<T> callback) {
        diskIO.execute(() -> {
            T result = operation.execute();
            mainThreadHandler.post(() -> callback.onComplete(result));
        });
    }
}
