package com.samsungimaging.connectionmanager.util;

import android.annotation.TargetApi;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.util.Log;
import java.util.ArrayDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class AsyncTask<Params, Progress, Result> {
  private static final int CORE_POOL_SIZE = 5;
  
  public static final Executor DUAL_THREAD_EXECUTOR;
  
  private static final int KEEP_ALIVE = 1;
  
  private static final String LOG_TAG = "AsyncTask";
  
  private static final int MAXIMUM_POOL_SIZE = 128;
  
  private static final int MESSAGE_POST_PROGRESS = 2;
  
  private static final int MESSAGE_POST_RESULT = 1;
  
  public static final Executor SERIAL_EXECUTOR;
  
  public static final Executor THREAD_POOL_EXECUTOR;
  
  private static volatile Executor sDefaultExecutor;
  
  private static final InternalHandler sHandler;
  
  private static final BlockingQueue<Runnable> sPoolWorkQueue;
  
  private static final ThreadFactory sThreadFactory = new ThreadFactory() {
      private final AtomicInteger mCount = new AtomicInteger(1);
      
      public Thread newThread(Runnable param1Runnable) {
        return new Thread(param1Runnable, "AsyncTask #" + this.mCount.getAndIncrement());
      }
    };
  
  private final AtomicBoolean mCancelled = new AtomicBoolean();
  
  private final FutureTask<Result> mFuture = new FutureTask<Result>(this.mWorker) {
      protected void done() {
        try {
          AsyncTask.this.postResultIfNotInvoked(get());
          return;
        } catch (InterruptedException interruptedException) {
          Log.w("AsyncTask", interruptedException);
          return;
        } catch (ExecutionException executionException) {
          throw new RuntimeException("An error occured while executing doInBackground()", executionException.getCause());
        } catch (CancellationException cancellationException) {
          AsyncTask.this.postResultIfNotInvoked(null);
          return;
        } 
      }
    };
  
  private volatile Status mStatus = Status.PENDING;
  
  private final AtomicBoolean mTaskInvoked = new AtomicBoolean();
  
  private final WorkerRunnable<Params, Result> mWorker = new WorkerRunnable<Params, Result>() {
      public Result call() throws Exception {
        AsyncTask.this.mTaskInvoked.set(true);
        Process.setThreadPriority(10);
        return AsyncTask.this.postResult((Result)AsyncTask.this.doInBackground((Object[])this.mParams));
      }
    };
  
  static {
    sPoolWorkQueue = new LinkedBlockingQueue<Runnable>(10);
    THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(5, 128, 1L, TimeUnit.SECONDS, sPoolWorkQueue, sThreadFactory, new ThreadPoolExecutor.DiscardOldestPolicy());
    SERIAL_EXECUTOR = new SerialExecutor(null);
    DUAL_THREAD_EXECUTOR = Executors.newFixedThreadPool(2, sThreadFactory);
    sHandler = new InternalHandler(null);
    sDefaultExecutor = SERIAL_EXECUTOR;
  }
  
  public static void execute(Runnable paramRunnable) {
    sDefaultExecutor.execute(paramRunnable);
  }
  
  private void finish(Result paramResult) {
    if (isCancelled()) {
      onCancelled(paramResult);
    } else {
      onPostExecute(paramResult);
    } 
    this.mStatus = Status.FINISHED;
  }
  
  public static void init() {
    sHandler.getLooper();
  }
  
  private Result postResult(Result paramResult) {
    sHandler.obtainMessage(1, new AsyncTaskResult(this, new Object[] { paramResult })).sendToTarget();
    return paramResult;
  }
  
  private void postResultIfNotInvoked(Result paramResult) {
    if (!this.mTaskInvoked.get())
      postResult(paramResult); 
  }
  
  public static void setDefaultExecutor(Executor paramExecutor) {
    sDefaultExecutor = paramExecutor;
  }
  
  public final boolean cancel(boolean paramBoolean) {
    this.mCancelled.set(true);
    return this.mFuture.cancel(paramBoolean);
  }
  
  protected abstract Result doInBackground(Params... paramVarArgs);
  
  public final AsyncTask<Params, Progress, Result> execute(Params... paramVarArgs) {
    return executeOnExecutor(sDefaultExecutor, paramVarArgs);
  }
  
  public final AsyncTask<Params, Progress, Result> executeOnExecutor(Executor paramExecutor, Params... paramVarArgs) {
    if (this.mStatus != Status.PENDING) {
      switch (this.mStatus) {
        default:
          this.mStatus = Status.RUNNING;
          onPreExecute();
          this.mWorker.mParams = paramVarArgs;
          paramExecutor.execute(this.mFuture);
          return this;
        case RUNNING:
          throw new IllegalStateException("Cannot execute task: the task is already running.");
        case FINISHED:
          break;
      } 
      throw new IllegalStateException("Cannot execute task: the task has already been executed (a task can be executed only once)");
    } 
  }
  
  public final Result get() throws InterruptedException, ExecutionException {
    return this.mFuture.get();
  }
  
  public final Result get(long paramLong, TimeUnit paramTimeUnit) throws InterruptedException, ExecutionException, TimeoutException {
    return this.mFuture.get(paramLong, paramTimeUnit);
  }
  
  public final Status getStatus() {
    return this.mStatus;
  }
  
  public final boolean isCancelled() {
    return this.mCancelled.get();
  }
  
  protected void onCancelled() {}
  
  protected void onCancelled(Result paramResult) {
    onCancelled();
  }
  
  protected void onPostExecute(Result paramResult) {}
  
  protected void onPreExecute() {}
  
  protected void onProgressUpdate(Progress... paramVarArgs) {}
  
  protected final void publishProgress(Progress... paramVarArgs) {
    if (!isCancelled())
      sHandler.obtainMessage(2, new AsyncTaskResult<Progress>(this, paramVarArgs)).sendToTarget(); 
  }
  
  private static class AsyncTaskResult<Data> {
    final Data[] mData;
    
    final AsyncTask mTask;
    
    AsyncTaskResult(AsyncTask param1AsyncTask, Data... param1VarArgs) {
      this.mTask = param1AsyncTask;
      this.mData = param1VarArgs;
    }
  }
  
  private static class InternalHandler extends Handler {
    private InternalHandler() {}
    
    public void handleMessage(Message param1Message) {
      AsyncTask.AsyncTaskResult asyncTaskResult = (AsyncTask.AsyncTaskResult)param1Message.obj;
      switch (param1Message.what) {
        default:
          return;
        case 1:
          asyncTaskResult.mTask.finish((Result)asyncTaskResult.mData[0]);
          return;
        case 2:
          break;
      } 
      asyncTaskResult.mTask.onProgressUpdate((Object[])asyncTaskResult.mData);
    }
  }
  
  @TargetApi(11)
  private static class SerialExecutor implements Executor {
    Runnable mActive;
    
    final ArrayDeque<Runnable> mTasks = new ArrayDeque<Runnable>();
    
    private SerialExecutor() {}
    
    public void execute(Runnable param1Runnable) {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_0
      //   3: getfield mTasks : Ljava/util/ArrayDeque;
      //   6: new com/samsungimaging/connectionmanager/util/AsyncTask$SerialExecutor$1
      //   9: dup
      //   10: aload_0
      //   11: aload_1
      //   12: invokespecial <init> : (Lcom/samsungimaging/connectionmanager/util/AsyncTask$SerialExecutor;Ljava/lang/Runnable;)V
      //   15: invokevirtual offer : (Ljava/lang/Object;)Z
      //   18: pop
      //   19: aload_0
      //   20: getfield mActive : Ljava/lang/Runnable;
      //   23: ifnonnull -> 30
      //   26: aload_0
      //   27: invokevirtual scheduleNext : ()V
      //   30: aload_0
      //   31: monitorexit
      //   32: return
      //   33: astore_1
      //   34: aload_0
      //   35: monitorexit
      //   36: aload_1
      //   37: athrow
      // Exception table:
      //   from	to	target	type
      //   2	30	33	finally
    }
    
    protected void scheduleNext() {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_0
      //   3: getfield mTasks : Ljava/util/ArrayDeque;
      //   6: invokevirtual poll : ()Ljava/lang/Object;
      //   9: checkcast java/lang/Runnable
      //   12: astore_1
      //   13: aload_0
      //   14: aload_1
      //   15: putfield mActive : Ljava/lang/Runnable;
      //   18: aload_1
      //   19: ifnull -> 34
      //   22: getstatic com/samsungimaging/connectionmanager/util/AsyncTask.THREAD_POOL_EXECUTOR : Ljava/util/concurrent/Executor;
      //   25: aload_0
      //   26: getfield mActive : Ljava/lang/Runnable;
      //   29: invokeinterface execute : (Ljava/lang/Runnable;)V
      //   34: aload_0
      //   35: monitorexit
      //   36: return
      //   37: astore_1
      //   38: aload_0
      //   39: monitorexit
      //   40: aload_1
      //   41: athrow
      // Exception table:
      //   from	to	target	type
      //   2	18	37	finally
      //   22	34	37	finally
    }
  }
  
  class null implements Runnable {
    public void run() {
      try {
        r.run();
        return;
      } finally {
        this.this$1.scheduleNext();
      } 
    }
  }
  
  public enum Status {
    FINISHED, PENDING, RUNNING;
    
    static {
      ENUM$VALUES = new Status[] { PENDING, RUNNING, FINISHED };
    }
  }
  
  private static abstract class WorkerRunnable<Params, Result> implements Callable<Result> {
    Params[] mParams;
    
    private WorkerRunnable() {}
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanage\\util\AsyncTask.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */