package com.joojn.utils.async;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class AsyncTask<T> {

    private Consumer<T> promise = null;
    private Consumer<Exception> errorPromise = null;

    private T value = null;
    private Thread thread;
    private Exception error;

    public AsyncTask<T> then(Consumer<T> promise) {
        if(value != null)
        {
            promise.accept(this.value);
            this.value = null;
        }
        else
        {
            this.promise = promise;
        }

        return this;
    }

    public AsyncTask<T> except(Consumer<Exception> errorPromise) {

        if(error != null)
        {
            errorPromise.accept(this.error);
            this.error = null;
        }
        else
        {
            this.errorPromise = errorPromise;
        }

        return this;
    }

    public AsyncTask<T> call(T result) {
        if(this.thread == null) this.thread = Thread.currentThread();

        if(this.promise != null)
        {
            this.promise.accept(result);
        }
        else
        {
            this.value = result;
        }

        return this;
    }

    public AsyncTask<T> callError(Exception error) {
        if(this.thread == null) this.thread = Thread.currentThread();

        if(this.errorPromise != null)
        {
            this.errorPromise.accept(error);
        }
        else
        {
            this.error = error;
        }

        return this;
    }

    public AsyncTask<T> callAsync(AsyncCallable<T> callable) {

        this.thread = new Thread(() -> {
            try
            {
                call(callable.call());
            }
            catch (Exception e)
            {
                callError(e);
            }
        });

        this.thread.start();

        return this;
    }

    public T await()
    {
        try
        {
            this.thread.join();
        }
        catch (InterruptedException e)
        {
            this.callError(e);
            return null;
        }

        return this.value;
    }

    interface AsyncCallable<T> {
        T call() throws Exception;
    }
}
