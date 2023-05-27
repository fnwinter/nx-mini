package android.support.v4.content;

import android.os.Build;
import java.util.concurrent.Executor;

public class ParallelExecutorCompat {
  public static Executor getParallelExecutor() {
    return (Build.VERSION.SDK_INT >= 11) ? ExecutorCompatHoneycomb.getParallelExecutor() : ModernAsyncTask.THREAD_POOL_EXECUTOR;
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v4\content\ParallelExecutorCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */