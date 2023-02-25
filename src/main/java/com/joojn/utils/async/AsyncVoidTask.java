package com.joojn.utils.async;

import java.util.function.Consumer;

public class AsyncVoidTask {

    private Runnable promise = null;
    private Consumer<Exception> errorPromise;
    private boolean called = false;

    private Thread thread;
    private Exception error;

    public AsyncVoidTask then(Runnable promise) {

        if(this.called)
        {
            promise.run();
            this.called = false;
        }
        else
        {
            this.promise = promise;
        }

        return this;
    }

    public AsyncVoidTask except(Consumer<Exception> errorPromise){
        if(this.error != null)
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

    public AsyncVoidTask call() {
        if(this.thread == null) this.thread = Thread.currentThread();

        if(this.promise != null)
        {
            this.promise.run();
        }
        else
        {
            this.called = true;
        }

        return this;
    }

    public AsyncVoidTask callError(Exception error) {
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

    public AsyncVoidTask callAsync(VoidPromise callable) {

        this.thread = new Thread(() -> {
            try
            {
                callable.call();
                call();
            }
            catch (Exception e)
            {
                callError(e);
            }
        });

        this.thread.start();

        return this;
    }

    public void await()
    {
        try
        {
            this.thread.join();
        }
        catch (InterruptedException e)
        {
            this.callError(e);
        }
    }

    public interface VoidPromise {
        void call() throws Exception;
    }
}

/*
package com.joojn.jidelnaapi.async;

public class AsyncTask<T> {

    private VoidPromise<T> taskPromise = null;
    private VoidPromise<T> voidPromise = null;

    private T value = null;
    private Thread thread;
    private Exception error;

    public <S> AsyncTask<S> then(Promise<T, S> promise) {
        AsyncTask<S> task = new AsyncTask<>();

        // if value has been already returned
        if(this.value == null)
        {
            this.taskPromise = (result) -> task.call(promise.then(result));
        }
        else
        {
            task.call(promise.then(this.value));
            this.value = null;
        }

        return task;
    }

    public void then(VoidPromise<T> promise) {
        if(value != null)
        {
            promise.then(this.value);
            this.value = null;
        }
        else
        {
            this.voidPromise = promise;
        }
    }

//    private VoidAsyncCallable callable;
//    public void then(VoidAsyncCallable callable)
//    {
//        this.callable = callable;
//    }
//    public AsyncTask<T> callAsync(VoidAsyncCallable callable)
//    {
//        new Thread(() -> {
//            try
//            {
//                callable.call();
//                if(this.callable != null)
//                {
//                    this.callable.call();
//                }
//            }
//            catch (Exception e)
//            {
//                e.printStackTrace();
//            }
//        }).start();
//
//        return this;
//    }

    public void call(T result) {
        this.value = result;
        if(this.thread == null) this.thread = Thread.currentThread();

        if (this.voidPromise != null)
        {
            this.voidPromise.then(result);
        }
        else if (this.taskPromise != null)
        {
            this.taskPromise.then(result);
        }
    }

    public AsyncTask<T> callAsync(AsyncCallable<T> callable) {

        this.thread = new Thread(() -> {
            try
            {
                T result = callable.call();
                call(result);
            }
            catch (Exception e)
            {
                this.error = e;
                e.printStackTrace();
            }
        });

        this.thread.start();

        return this;
    }

    public T await()
    {
        try{
            this.thread.join();
        }
        catch (InterruptedException e)
        {
            this.error = e;
            e.printStackTrace();
            return null;
        }

        return this.value;
    }

    public boolean hashError()
    {
        return this.error != null;
    }

    public Exception getError()
    {
        return this.error;
    }

    public interface AsyncCallable<T> {
        T call() throws Exception;
    }

    public interface VoidAsyncCallable {
        void call() throws Exception;
    }

    public interface Promise<T, S> {
        S then(T result);
    }

    public interface VoidPromise<T> {
        void then(T result);
    }
}
 */
