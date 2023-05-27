package com.samsungimaging.connectionmanager.util;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public final class DiskLruCache implements Closeable {
  static final long ANY_SEQUENCE_NUMBER = -1L;
  
  private static final String CLEAN = "CLEAN";
  
  private static final String DIRTY = "DIRTY";
  
  private static final int IO_BUFFER_SIZE = 8192;
  
  static final String JOURNAL_FILE = "journal";
  
  static final String JOURNAL_FILE_TMP = "journal.tmp";
  
  static final String MAGIC = "libcore.io.DiskLruCache";
  
  private static final String READ = "READ";
  
  private static final String REMOVE = "REMOVE";
  
  private static final Charset UTF_8 = Charset.forName("UTF-8");
  
  static final String VERSION_1 = "1";
  
  private final int appVersion;
  
  private final Callable<Void> cleanupCallable = new Callable<Void>() {
      public Void call() throws Exception {
        synchronized (DiskLruCache.this) {
          if (DiskLruCache.this.journalWriter == null)
            return null; 
          DiskLruCache.this.trimToSize();
          if (DiskLruCache.this.journalRebuildRequired()) {
            DiskLruCache.this.rebuildJournal();
            DiskLruCache.this.redundantOpCount = 0;
          } 
          return null;
        } 
      }
    };
  
  private final File directory;
  
  private final ExecutorService executorService = new ThreadPoolExecutor(0, 1, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
  
  private final File journalFile;
  
  private final File journalFileTmp;
  
  private Writer journalWriter;
  
  private final LinkedHashMap<String, Entry> lruEntries = new LinkedHashMap<String, Entry>(0, 0.75F, true);
  
  private final long maxSize;
  
  private long nextSequenceNumber = 0L;
  
  private int redundantOpCount;
  
  private long size = 0L;
  
  private final int valueCount;
  
  private DiskLruCache(File paramFile, int paramInt1, int paramInt2, long paramLong) {
    this.directory = paramFile;
    this.appVersion = paramInt1;
    this.journalFile = new File(paramFile, "journal");
    this.journalFileTmp = new File(paramFile, "journal.tmp");
    this.valueCount = paramInt2;
    this.maxSize = paramLong;
  }
  
  private void checkNotClosed() {
    if (this.journalWriter == null)
      throw new IllegalStateException("cache is closed"); 
  }
  
  public static void closeQuietly(Closeable paramCloseable) {
    if (paramCloseable != null)
      try {
        paramCloseable.close();
        return;
      } catch (RuntimeException runtimeException) {
        throw runtimeException;
      } catch (Exception exception) {
        return;
      }  
  }
  
  private void completeEdit(Editor paramEditor, boolean paramBoolean) throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_1
    //   3: invokestatic access$2 : (Lcom/samsungimaging/connectionmanager/util/DiskLruCache$Editor;)Lcom/samsungimaging/connectionmanager/util/DiskLruCache$Entry;
    //   6: astore #8
    //   8: aload #8
    //   10: invokestatic access$0 : (Lcom/samsungimaging/connectionmanager/util/DiskLruCache$Entry;)Lcom/samsungimaging/connectionmanager/util/DiskLruCache$Editor;
    //   13: aload_1
    //   14: if_acmpeq -> 30
    //   17: new java/lang/IllegalStateException
    //   20: dup
    //   21: invokespecial <init> : ()V
    //   24: athrow
    //   25: astore_1
    //   26: aload_0
    //   27: monitorexit
    //   28: aload_1
    //   29: athrow
    //   30: iload_2
    //   31: ifeq -> 368
    //   34: aload #8
    //   36: invokestatic access$1 : (Lcom/samsungimaging/connectionmanager/util/DiskLruCache$Entry;)Z
    //   39: ifne -> 368
    //   42: iconst_0
    //   43: istore_3
    //   44: iload_3
    //   45: aload_0
    //   46: getfield valueCount : I
    //   49: if_icmplt -> 196
    //   52: goto -> 368
    //   55: iload_3
    //   56: aload_0
    //   57: getfield valueCount : I
    //   60: if_icmplt -> 237
    //   63: aload_0
    //   64: aload_0
    //   65: getfield redundantOpCount : I
    //   68: iconst_1
    //   69: iadd
    //   70: putfield redundantOpCount : I
    //   73: aload #8
    //   75: aconst_null
    //   76: invokestatic access$5 : (Lcom/samsungimaging/connectionmanager/util/DiskLruCache$Entry;Lcom/samsungimaging/connectionmanager/util/DiskLruCache$Editor;)V
    //   79: aload #8
    //   81: invokestatic access$1 : (Lcom/samsungimaging/connectionmanager/util/DiskLruCache$Entry;)Z
    //   84: iload_2
    //   85: ior
    //   86: ifeq -> 319
    //   89: aload #8
    //   91: iconst_1
    //   92: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/util/DiskLruCache$Entry;Z)V
    //   95: aload_0
    //   96: getfield journalWriter : Ljava/io/Writer;
    //   99: new java/lang/StringBuilder
    //   102: dup
    //   103: ldc 'CLEAN '
    //   105: invokespecial <init> : (Ljava/lang/String;)V
    //   108: aload #8
    //   110: invokestatic access$2 : (Lcom/samsungimaging/connectionmanager/util/DiskLruCache$Entry;)Ljava/lang/String;
    //   113: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   116: aload #8
    //   118: invokevirtual getLengths : ()Ljava/lang/String;
    //   121: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   124: bipush #10
    //   126: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   129: invokevirtual toString : ()Ljava/lang/String;
    //   132: invokevirtual write : (Ljava/lang/String;)V
    //   135: iload_2
    //   136: ifeq -> 160
    //   139: aload_0
    //   140: getfield nextSequenceNumber : J
    //   143: lstore #4
    //   145: aload_0
    //   146: lconst_1
    //   147: lload #4
    //   149: ladd
    //   150: putfield nextSequenceNumber : J
    //   153: aload #8
    //   155: lload #4
    //   157: invokestatic access$9 : (Lcom/samsungimaging/connectionmanager/util/DiskLruCache$Entry;J)V
    //   160: aload_0
    //   161: getfield size : J
    //   164: aload_0
    //   165: getfield maxSize : J
    //   168: lcmp
    //   169: ifgt -> 179
    //   172: aload_0
    //   173: invokespecial journalRebuildRequired : ()Z
    //   176: ifeq -> 193
    //   179: aload_0
    //   180: getfield executorService : Ljava/util/concurrent/ExecutorService;
    //   183: aload_0
    //   184: getfield cleanupCallable : Ljava/util/concurrent/Callable;
    //   187: invokeinterface submit : (Ljava/util/concurrent/Callable;)Ljava/util/concurrent/Future;
    //   192: pop
    //   193: aload_0
    //   194: monitorexit
    //   195: return
    //   196: aload #8
    //   198: iload_3
    //   199: invokevirtual getDirtyFile : (I)Ljava/io/File;
    //   202: invokevirtual exists : ()Z
    //   205: ifne -> 373
    //   208: aload_1
    //   209: invokevirtual abort : ()V
    //   212: new java/lang/IllegalStateException
    //   215: dup
    //   216: new java/lang/StringBuilder
    //   219: dup
    //   220: ldc_w 'edit didn't create file '
    //   223: invokespecial <init> : (Ljava/lang/String;)V
    //   226: iload_3
    //   227: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   230: invokevirtual toString : ()Ljava/lang/String;
    //   233: invokespecial <init> : (Ljava/lang/String;)V
    //   236: athrow
    //   237: aload #8
    //   239: iload_3
    //   240: invokevirtual getDirtyFile : (I)Ljava/io/File;
    //   243: astore_1
    //   244: iload_2
    //   245: ifeq -> 312
    //   248: aload_1
    //   249: invokevirtual exists : ()Z
    //   252: ifeq -> 380
    //   255: aload #8
    //   257: iload_3
    //   258: invokevirtual getCleanFile : (I)Ljava/io/File;
    //   261: astore #9
    //   263: aload_1
    //   264: aload #9
    //   266: invokevirtual renameTo : (Ljava/io/File;)Z
    //   269: pop
    //   270: aload #8
    //   272: invokestatic access$7 : (Lcom/samsungimaging/connectionmanager/util/DiskLruCache$Entry;)[J
    //   275: iload_3
    //   276: laload
    //   277: lstore #4
    //   279: aload #9
    //   281: invokevirtual length : ()J
    //   284: lstore #6
    //   286: aload #8
    //   288: invokestatic access$7 : (Lcom/samsungimaging/connectionmanager/util/DiskLruCache$Entry;)[J
    //   291: iload_3
    //   292: lload #6
    //   294: lastore
    //   295: aload_0
    //   296: aload_0
    //   297: getfield size : J
    //   300: lload #4
    //   302: lsub
    //   303: lload #6
    //   305: ladd
    //   306: putfield size : J
    //   309: goto -> 380
    //   312: aload_1
    //   313: invokestatic deleteIfExists : (Ljava/io/File;)V
    //   316: goto -> 380
    //   319: aload_0
    //   320: getfield lruEntries : Ljava/util/LinkedHashMap;
    //   323: aload #8
    //   325: invokestatic access$2 : (Lcom/samsungimaging/connectionmanager/util/DiskLruCache$Entry;)Ljava/lang/String;
    //   328: invokevirtual remove : (Ljava/lang/Object;)Ljava/lang/Object;
    //   331: pop
    //   332: aload_0
    //   333: getfield journalWriter : Ljava/io/Writer;
    //   336: new java/lang/StringBuilder
    //   339: dup
    //   340: ldc_w 'REMOVE '
    //   343: invokespecial <init> : (Ljava/lang/String;)V
    //   346: aload #8
    //   348: invokestatic access$2 : (Lcom/samsungimaging/connectionmanager/util/DiskLruCache$Entry;)Ljava/lang/String;
    //   351: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   354: bipush #10
    //   356: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   359: invokevirtual toString : ()Ljava/lang/String;
    //   362: invokevirtual write : (Ljava/lang/String;)V
    //   365: goto -> 160
    //   368: iconst_0
    //   369: istore_3
    //   370: goto -> 55
    //   373: iload_3
    //   374: iconst_1
    //   375: iadd
    //   376: istore_3
    //   377: goto -> 44
    //   380: iload_3
    //   381: iconst_1
    //   382: iadd
    //   383: istore_3
    //   384: goto -> 55
    // Exception table:
    //   from	to	target	type
    //   2	25	25	finally
    //   34	42	25	finally
    //   44	52	25	finally
    //   55	135	25	finally
    //   139	160	25	finally
    //   160	179	25	finally
    //   179	193	25	finally
    //   196	237	25	finally
    //   237	244	25	finally
    //   248	309	25	finally
    //   312	316	25	finally
    //   319	365	25	finally
  }
  
  private static <T> T[] copyOfRange(T[] paramArrayOfT, int paramInt1, int paramInt2) {
    int i = paramArrayOfT.length;
    if (paramInt1 > paramInt2)
      throw new IllegalArgumentException(); 
    if (paramInt1 < 0 || paramInt1 > i)
      throw new ArrayIndexOutOfBoundsException(); 
    paramInt2 -= paramInt1;
    i = Math.min(paramInt2, i - paramInt1);
    Object[] arrayOfObject = (Object[])Array.newInstance(paramArrayOfT.getClass().getComponentType(), paramInt2);
    System.arraycopy(paramArrayOfT, paramInt1, arrayOfObject, 0, i);
    return (T[])arrayOfObject;
  }
  
  public static void deleteContents(File paramFile) throws IOException {
    File[] arrayOfFile = paramFile.listFiles();
    if (arrayOfFile == null)
      throw new IllegalArgumentException("not a directory: " + paramFile); 
    int j = arrayOfFile.length;
    for (int i = 0;; i++) {
      if (i >= j)
        return; 
      paramFile = arrayOfFile[i];
      if (paramFile.isDirectory())
        deleteContents(paramFile); 
      if (!paramFile.delete())
        throw new IOException("failed to delete file: " + paramFile); 
    } 
  }
  
  private static void deleteIfExists(File paramFile) throws IOException {
    if (paramFile.exists() && !paramFile.delete())
      throw new IOException(); 
  }
  
  private Editor edit(String paramString, long paramLong) throws IOException {
    // Byte code:
    //   0: aconst_null
    //   1: astore #7
    //   3: aload_0
    //   4: monitorenter
    //   5: aload_0
    //   6: invokespecial checkNotClosed : ()V
    //   9: aload_0
    //   10: aload_1
    //   11: invokespecial validateKey : (Ljava/lang/String;)V
    //   14: aload_0
    //   15: getfield lruEntries : Ljava/util/LinkedHashMap;
    //   18: aload_1
    //   19: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   22: checkcast com/samsungimaging/connectionmanager/util/DiskLruCache$Entry
    //   25: astore #8
    //   27: lload_2
    //   28: ldc2_w -1
    //   31: lcmp
    //   32: ifeq -> 67
    //   35: aload #7
    //   37: astore #6
    //   39: aload #8
    //   41: ifnull -> 62
    //   44: aload #8
    //   46: invokestatic access$8 : (Lcom/samsungimaging/connectionmanager/util/DiskLruCache$Entry;)J
    //   49: lstore #4
    //   51: lload #4
    //   53: lload_2
    //   54: lcmp
    //   55: ifeq -> 67
    //   58: aload #7
    //   60: astore #6
    //   62: aload_0
    //   63: monitorexit
    //   64: aload #6
    //   66: areturn
    //   67: aload #8
    //   69: ifnonnull -> 163
    //   72: new com/samsungimaging/connectionmanager/util/DiskLruCache$Entry
    //   75: dup
    //   76: aload_0
    //   77: aload_1
    //   78: aconst_null
    //   79: invokespecial <init> : (Lcom/samsungimaging/connectionmanager/util/DiskLruCache;Ljava/lang/String;Lcom/samsungimaging/connectionmanager/util/DiskLruCache$Entry;)V
    //   82: astore #6
    //   84: aload_0
    //   85: getfield lruEntries : Ljava/util/LinkedHashMap;
    //   88: aload_1
    //   89: aload #6
    //   91: invokevirtual put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   94: pop
    //   95: new com/samsungimaging/connectionmanager/util/DiskLruCache$Editor
    //   98: dup
    //   99: aload_0
    //   100: aload #6
    //   102: aconst_null
    //   103: invokespecial <init> : (Lcom/samsungimaging/connectionmanager/util/DiskLruCache;Lcom/samsungimaging/connectionmanager/util/DiskLruCache$Entry;Lcom/samsungimaging/connectionmanager/util/DiskLruCache$Editor;)V
    //   106: astore #7
    //   108: aload #6
    //   110: aload #7
    //   112: invokestatic access$5 : (Lcom/samsungimaging/connectionmanager/util/DiskLruCache$Entry;Lcom/samsungimaging/connectionmanager/util/DiskLruCache$Editor;)V
    //   115: aload_0
    //   116: getfield journalWriter : Ljava/io/Writer;
    //   119: new java/lang/StringBuilder
    //   122: dup
    //   123: ldc_w 'DIRTY '
    //   126: invokespecial <init> : (Ljava/lang/String;)V
    //   129: aload_1
    //   130: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   133: bipush #10
    //   135: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   138: invokevirtual toString : ()Ljava/lang/String;
    //   141: invokevirtual write : (Ljava/lang/String;)V
    //   144: aload_0
    //   145: getfield journalWriter : Ljava/io/Writer;
    //   148: invokevirtual flush : ()V
    //   151: aload #7
    //   153: astore #6
    //   155: goto -> 62
    //   158: astore_1
    //   159: aload_0
    //   160: monitorexit
    //   161: aload_1
    //   162: athrow
    //   163: aload #8
    //   165: invokestatic access$0 : (Lcom/samsungimaging/connectionmanager/util/DiskLruCache$Entry;)Lcom/samsungimaging/connectionmanager/util/DiskLruCache$Editor;
    //   168: astore #9
    //   170: aload #8
    //   172: astore #6
    //   174: aload #9
    //   176: ifnull -> 95
    //   179: aload #7
    //   181: astore #6
    //   183: goto -> 62
    // Exception table:
    //   from	to	target	type
    //   5	27	158	finally
    //   44	51	158	finally
    //   72	95	158	finally
    //   95	151	158	finally
    //   163	170	158	finally
  }
  
  private static String inputStreamToString(InputStream paramInputStream) throws IOException {
    return readFully(new InputStreamReader(paramInputStream, UTF_8));
  }
  
  private boolean journalRebuildRequired() {
    return (this.redundantOpCount >= 2000 && this.redundantOpCount >= this.lruEntries.size());
  }
  
  public static DiskLruCache open(File paramFile, int paramInt1, int paramInt2, long paramLong) throws IOException {
    if (paramLong <= 0L)
      throw new IllegalArgumentException("maxSize <= 0"); 
    if (paramInt2 <= 0)
      throw new IllegalArgumentException("valueCount <= 0"); 
    DiskLruCache diskLruCache2 = new DiskLruCache(paramFile, paramInt1, paramInt2, paramLong);
    if (diskLruCache2.journalFile.exists())
      try {
        diskLruCache2.readJournal();
        diskLruCache2.processJournal();
        diskLruCache2.journalWriter = new BufferedWriter(new FileWriter(diskLruCache2.journalFile, true), 8192);
        return diskLruCache2;
      } catch (IOException iOException) {
        diskLruCache2.delete();
      }  
    paramFile.mkdirs();
    DiskLruCache diskLruCache1 = new DiskLruCache(paramFile, paramInt1, paramInt2, paramLong);
    diskLruCache1.rebuildJournal();
    return diskLruCache1;
  }
  
  private void processJournal() throws IOException {
    deleteIfExists(this.journalFileTmp);
    Iterator<Entry> iterator = this.lruEntries.values().iterator();
    label19: while (true) {
      if (!iterator.hasNext())
        return; 
      Entry entry = iterator.next();
      if (entry.currentEditor == null) {
        for (int j = 0; j < this.valueCount; j++)
          this.size += entry.lengths[j]; 
        continue;
      } 
      entry.currentEditor = null;
      for (int i = 0;; i++) {
        if (i >= this.valueCount) {
          iterator.remove();
          continue label19;
        } 
        deleteIfExists(entry.getCleanFile(i));
        deleteIfExists(entry.getDirtyFile(i));
      } 
      break;
    } 
  }
  
  public static String readAsciiLine(InputStream paramInputStream) throws IOException {
    StringBuilder stringBuilder = new StringBuilder(80);
    while (true) {
      int i = paramInputStream.read();
      if (i == -1)
        throw new EOFException(); 
      if (i == 10) {
        i = stringBuilder.length();
        if (i > 0 && stringBuilder.charAt(i - 1) == '\r')
          stringBuilder.setLength(i - 1); 
        return stringBuilder.toString();
      } 
      stringBuilder.append((char)i);
    } 
  }
  
  public static String readFully(Reader paramReader) throws IOException {
    try {
      StringWriter stringWriter = new StringWriter();
      char[] arrayOfChar = new char[1024];
      while (true) {
        String str;
        int i = paramReader.read(arrayOfChar);
        if (i == -1) {
          str = stringWriter.toString();
          return str;
        } 
        str.write(arrayOfChar, 0, i);
      } 
    } finally {
      paramReader.close();
    } 
  }
  
  private void readJournal() throws IOException {
    BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(this.journalFile), 8192);
    try {
      String str1 = readAsciiLine(bufferedInputStream);
      String str2 = readAsciiLine(bufferedInputStream);
      String str3 = readAsciiLine(bufferedInputStream);
      String str4 = readAsciiLine(bufferedInputStream);
      String str5 = readAsciiLine(bufferedInputStream);
    } finally {
      closeQuietly(bufferedInputStream);
    } 
    try {
      while (true)
        readJournalLine(readAsciiLine(bufferedInputStream)); 
    } catch (EOFException eOFException) {
      closeQuietly(bufferedInputStream);
      return;
    } 
  }
  
  private void readJournalLine(String paramString) throws IOException {
    String[] arrayOfString = paramString.split(" ");
    if (arrayOfString.length < 2)
      throw new IOException("unexpected journal line: " + paramString); 
    String str = arrayOfString[1];
    if (arrayOfString[0].equals("REMOVE") && arrayOfString.length == 2) {
      this.lruEntries.remove(str);
      return;
    } 
    Entry entry2 = this.lruEntries.get(str);
    Entry entry1 = entry2;
    if (entry2 == null) {
      entry1 = new Entry(str, null);
      this.lruEntries.put(str, entry1);
    } 
    if (arrayOfString[0].equals("CLEAN") && arrayOfString.length == this.valueCount + 2) {
      entry1.readable = true;
      entry1.currentEditor = null;
      entry1.setLengths(copyOfRange(arrayOfString, 2, arrayOfString.length));
      return;
    } 
    if (arrayOfString[0].equals("DIRTY") && arrayOfString.length == 2) {
      entry1.currentEditor = new Editor(entry1, null);
      return;
    } 
    if (!arrayOfString[0].equals("READ") || arrayOfString.length != 2)
      throw new IOException("unexpected journal line: " + paramString); 
  }
  
  private void rebuildJournal() throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield journalWriter : Ljava/io/Writer;
    //   6: ifnull -> 16
    //   9: aload_0
    //   10: getfield journalWriter : Ljava/io/Writer;
    //   13: invokevirtual close : ()V
    //   16: new java/io/BufferedWriter
    //   19: dup
    //   20: new java/io/FileWriter
    //   23: dup
    //   24: aload_0
    //   25: getfield journalFileTmp : Ljava/io/File;
    //   28: invokespecial <init> : (Ljava/io/File;)V
    //   31: sipush #8192
    //   34: invokespecial <init> : (Ljava/io/Writer;I)V
    //   37: astore_1
    //   38: aload_1
    //   39: ldc 'libcore.io.DiskLruCache'
    //   41: invokevirtual write : (Ljava/lang/String;)V
    //   44: aload_1
    //   45: ldc_w '\\n'
    //   48: invokevirtual write : (Ljava/lang/String;)V
    //   51: aload_1
    //   52: ldc '1'
    //   54: invokevirtual write : (Ljava/lang/String;)V
    //   57: aload_1
    //   58: ldc_w '\\n'
    //   61: invokevirtual write : (Ljava/lang/String;)V
    //   64: aload_1
    //   65: aload_0
    //   66: getfield appVersion : I
    //   69: invokestatic toString : (I)Ljava/lang/String;
    //   72: invokevirtual write : (Ljava/lang/String;)V
    //   75: aload_1
    //   76: ldc_w '\\n'
    //   79: invokevirtual write : (Ljava/lang/String;)V
    //   82: aload_1
    //   83: aload_0
    //   84: getfield valueCount : I
    //   87: invokestatic toString : (I)Ljava/lang/String;
    //   90: invokevirtual write : (Ljava/lang/String;)V
    //   93: aload_1
    //   94: ldc_w '\\n'
    //   97: invokevirtual write : (Ljava/lang/String;)V
    //   100: aload_1
    //   101: ldc_w '\\n'
    //   104: invokevirtual write : (Ljava/lang/String;)V
    //   107: aload_0
    //   108: getfield lruEntries : Ljava/util/LinkedHashMap;
    //   111: invokevirtual values : ()Ljava/util/Collection;
    //   114: invokeinterface iterator : ()Ljava/util/Iterator;
    //   119: astore_2
    //   120: aload_2
    //   121: invokeinterface hasNext : ()Z
    //   126: ifne -> 174
    //   129: aload_1
    //   130: invokevirtual close : ()V
    //   133: aload_0
    //   134: getfield journalFileTmp : Ljava/io/File;
    //   137: aload_0
    //   138: getfield journalFile : Ljava/io/File;
    //   141: invokevirtual renameTo : (Ljava/io/File;)Z
    //   144: pop
    //   145: aload_0
    //   146: new java/io/BufferedWriter
    //   149: dup
    //   150: new java/io/FileWriter
    //   153: dup
    //   154: aload_0
    //   155: getfield journalFile : Ljava/io/File;
    //   158: iconst_1
    //   159: invokespecial <init> : (Ljava/io/File;Z)V
    //   162: sipush #8192
    //   165: invokespecial <init> : (Ljava/io/Writer;I)V
    //   168: putfield journalWriter : Ljava/io/Writer;
    //   171: aload_0
    //   172: monitorexit
    //   173: return
    //   174: aload_2
    //   175: invokeinterface next : ()Ljava/lang/Object;
    //   180: checkcast com/samsungimaging/connectionmanager/util/DiskLruCache$Entry
    //   183: astore_3
    //   184: aload_3
    //   185: invokestatic access$0 : (Lcom/samsungimaging/connectionmanager/util/DiskLruCache$Entry;)Lcom/samsungimaging/connectionmanager/util/DiskLruCache$Editor;
    //   188: ifnull -> 228
    //   191: aload_1
    //   192: new java/lang/StringBuilder
    //   195: dup
    //   196: ldc_w 'DIRTY '
    //   199: invokespecial <init> : (Ljava/lang/String;)V
    //   202: aload_3
    //   203: invokestatic access$2 : (Lcom/samsungimaging/connectionmanager/util/DiskLruCache$Entry;)Ljava/lang/String;
    //   206: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   209: bipush #10
    //   211: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   214: invokevirtual toString : ()Ljava/lang/String;
    //   217: invokevirtual write : (Ljava/lang/String;)V
    //   220: goto -> 120
    //   223: astore_1
    //   224: aload_0
    //   225: monitorexit
    //   226: aload_1
    //   227: athrow
    //   228: aload_1
    //   229: new java/lang/StringBuilder
    //   232: dup
    //   233: ldc 'CLEAN '
    //   235: invokespecial <init> : (Ljava/lang/String;)V
    //   238: aload_3
    //   239: invokestatic access$2 : (Lcom/samsungimaging/connectionmanager/util/DiskLruCache$Entry;)Ljava/lang/String;
    //   242: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   245: aload_3
    //   246: invokevirtual getLengths : ()Ljava/lang/String;
    //   249: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   252: bipush #10
    //   254: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   257: invokevirtual toString : ()Ljava/lang/String;
    //   260: invokevirtual write : (Ljava/lang/String;)V
    //   263: goto -> 120
    // Exception table:
    //   from	to	target	type
    //   2	16	223	finally
    //   16	120	223	finally
    //   120	171	223	finally
    //   174	220	223	finally
    //   228	263	223	finally
  }
  
  private void trimToSize() throws IOException {
    while (true) {
      if (this.size <= this.maxSize)
        return; 
      remove((String)((Map.Entry)this.lruEntries.entrySet().iterator().next()).getKey());
    } 
  }
  
  private void validateKey(String paramString) {
    if (paramString.contains(" ") || paramString.contains("\n") || paramString.contains("\r"))
      throw new IllegalArgumentException("keys must not contain spaces or newlines: \"" + paramString + "\""); 
  }
  
  public void close() throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield journalWriter : Ljava/io/Writer;
    //   6: astore_1
    //   7: aload_1
    //   8: ifnonnull -> 14
    //   11: aload_0
    //   12: monitorexit
    //   13: return
    //   14: new java/util/ArrayList
    //   17: dup
    //   18: aload_0
    //   19: getfield lruEntries : Ljava/util/LinkedHashMap;
    //   22: invokevirtual values : ()Ljava/util/Collection;
    //   25: invokespecial <init> : (Ljava/util/Collection;)V
    //   28: invokevirtual iterator : ()Ljava/util/Iterator;
    //   31: astore_1
    //   32: aload_1
    //   33: invokeinterface hasNext : ()Z
    //   38: ifne -> 65
    //   41: aload_0
    //   42: invokespecial trimToSize : ()V
    //   45: aload_0
    //   46: getfield journalWriter : Ljava/io/Writer;
    //   49: invokevirtual close : ()V
    //   52: aload_0
    //   53: aconst_null
    //   54: putfield journalWriter : Ljava/io/Writer;
    //   57: goto -> 11
    //   60: astore_1
    //   61: aload_0
    //   62: monitorexit
    //   63: aload_1
    //   64: athrow
    //   65: aload_1
    //   66: invokeinterface next : ()Ljava/lang/Object;
    //   71: checkcast com/samsungimaging/connectionmanager/util/DiskLruCache$Entry
    //   74: astore_2
    //   75: aload_2
    //   76: invokestatic access$0 : (Lcom/samsungimaging/connectionmanager/util/DiskLruCache$Entry;)Lcom/samsungimaging/connectionmanager/util/DiskLruCache$Editor;
    //   79: ifnull -> 32
    //   82: aload_2
    //   83: invokestatic access$0 : (Lcom/samsungimaging/connectionmanager/util/DiskLruCache$Entry;)Lcom/samsungimaging/connectionmanager/util/DiskLruCache$Editor;
    //   86: invokevirtual abort : ()V
    //   89: goto -> 32
    // Exception table:
    //   from	to	target	type
    //   2	7	60	finally
    //   14	32	60	finally
    //   32	57	60	finally
    //   65	89	60	finally
  }
  
  public void delete() throws IOException {
    close();
    deleteContents(this.directory);
  }
  
  public Editor edit(String paramString) throws IOException {
    return edit(paramString, -1L);
  }
  
  public void flush() throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokespecial checkNotClosed : ()V
    //   6: aload_0
    //   7: invokespecial trimToSize : ()V
    //   10: aload_0
    //   11: getfield journalWriter : Ljava/io/Writer;
    //   14: invokevirtual flush : ()V
    //   17: aload_0
    //   18: monitorexit
    //   19: return
    //   20: astore_1
    //   21: aload_0
    //   22: monitorexit
    //   23: aload_1
    //   24: athrow
    // Exception table:
    //   from	to	target	type
    //   2	17	20	finally
  }
  
  public Snapshot get(String paramString) throws IOException {
    // Byte code:
    //   0: aconst_null
    //   1: astore #5
    //   3: aload_0
    //   4: monitorenter
    //   5: aload_0
    //   6: invokespecial checkNotClosed : ()V
    //   9: aload_0
    //   10: aload_1
    //   11: invokespecial validateKey : (Ljava/lang/String;)V
    //   14: aload_0
    //   15: getfield lruEntries : Ljava/util/LinkedHashMap;
    //   18: aload_1
    //   19: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   22: checkcast com/samsungimaging/connectionmanager/util/DiskLruCache$Entry
    //   25: astore #6
    //   27: aload #6
    //   29: ifnonnull -> 41
    //   32: aload #5
    //   34: astore #4
    //   36: aload_0
    //   37: monitorexit
    //   38: aload #4
    //   40: areturn
    //   41: aload #5
    //   43: astore #4
    //   45: aload #6
    //   47: invokestatic access$1 : (Lcom/samsungimaging/connectionmanager/util/DiskLruCache$Entry;)Z
    //   50: ifeq -> 36
    //   53: aload_0
    //   54: getfield valueCount : I
    //   57: anewarray java/io/InputStream
    //   60: astore #4
    //   62: iconst_0
    //   63: istore_2
    //   64: aload_0
    //   65: getfield valueCount : I
    //   68: istore_3
    //   69: iload_2
    //   70: iload_3
    //   71: if_icmplt -> 162
    //   74: aload_0
    //   75: aload_0
    //   76: getfield redundantOpCount : I
    //   79: iconst_1
    //   80: iadd
    //   81: putfield redundantOpCount : I
    //   84: aload_0
    //   85: getfield journalWriter : Ljava/io/Writer;
    //   88: new java/lang/StringBuilder
    //   91: dup
    //   92: ldc_w 'READ '
    //   95: invokespecial <init> : (Ljava/lang/String;)V
    //   98: aload_1
    //   99: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   102: bipush #10
    //   104: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   107: invokevirtual toString : ()Ljava/lang/String;
    //   110: invokevirtual append : (Ljava/lang/CharSequence;)Ljava/io/Writer;
    //   113: pop
    //   114: aload_0
    //   115: invokespecial journalRebuildRequired : ()Z
    //   118: ifeq -> 135
    //   121: aload_0
    //   122: getfield executorService : Ljava/util/concurrent/ExecutorService;
    //   125: aload_0
    //   126: getfield cleanupCallable : Ljava/util/concurrent/Callable;
    //   129: invokeinterface submit : (Ljava/util/concurrent/Callable;)Ljava/util/concurrent/Future;
    //   134: pop
    //   135: new com/samsungimaging/connectionmanager/util/DiskLruCache$Snapshot
    //   138: dup
    //   139: aload_0
    //   140: aload_1
    //   141: aload #6
    //   143: invokestatic access$8 : (Lcom/samsungimaging/connectionmanager/util/DiskLruCache$Entry;)J
    //   146: aload #4
    //   148: aconst_null
    //   149: invokespecial <init> : (Lcom/samsungimaging/connectionmanager/util/DiskLruCache;Ljava/lang/String;J[Ljava/io/InputStream;Lcom/samsungimaging/connectionmanager/util/DiskLruCache$Snapshot;)V
    //   152: astore #4
    //   154: goto -> 36
    //   157: astore_1
    //   158: aload_0
    //   159: monitorexit
    //   160: aload_1
    //   161: athrow
    //   162: aload #4
    //   164: iload_2
    //   165: new java/io/FileInputStream
    //   168: dup
    //   169: aload #6
    //   171: iload_2
    //   172: invokevirtual getCleanFile : (I)Ljava/io/File;
    //   175: invokespecial <init> : (Ljava/io/File;)V
    //   178: aastore
    //   179: iload_2
    //   180: iconst_1
    //   181: iadd
    //   182: istore_2
    //   183: goto -> 64
    //   186: astore_1
    //   187: aload #5
    //   189: astore #4
    //   191: goto -> 36
    // Exception table:
    //   from	to	target	type
    //   5	27	157	finally
    //   45	62	157	finally
    //   64	69	186	java/io/FileNotFoundException
    //   64	69	157	finally
    //   74	135	157	finally
    //   135	154	157	finally
    //   162	179	186	java/io/FileNotFoundException
    //   162	179	157	finally
  }
  
  public File getDirectory() {
    return this.directory;
  }
  
  public boolean isClosed() {
    return (this.journalWriter == null);
  }
  
  public long maxSize() {
    return this.maxSize;
  }
  
  public boolean remove(String paramString) throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokespecial checkNotClosed : ()V
    //   6: aload_0
    //   7: aload_1
    //   8: invokespecial validateKey : (Ljava/lang/String;)V
    //   11: aload_0
    //   12: getfield lruEntries : Ljava/util/LinkedHashMap;
    //   15: aload_1
    //   16: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   19: checkcast com/samsungimaging/connectionmanager/util/DiskLruCache$Entry
    //   22: astore #4
    //   24: aload #4
    //   26: ifnull -> 41
    //   29: aload #4
    //   31: invokestatic access$0 : (Lcom/samsungimaging/connectionmanager/util/DiskLruCache$Entry;)Lcom/samsungimaging/connectionmanager/util/DiskLruCache$Editor;
    //   34: astore #5
    //   36: aload #5
    //   38: ifnull -> 47
    //   41: iconst_0
    //   42: istore_3
    //   43: aload_0
    //   44: monitorexit
    //   45: iload_3
    //   46: ireturn
    //   47: iconst_0
    //   48: istore_2
    //   49: iload_2
    //   50: aload_0
    //   51: getfield valueCount : I
    //   54: if_icmplt -> 130
    //   57: aload_0
    //   58: aload_0
    //   59: getfield redundantOpCount : I
    //   62: iconst_1
    //   63: iadd
    //   64: putfield redundantOpCount : I
    //   67: aload_0
    //   68: getfield journalWriter : Ljava/io/Writer;
    //   71: new java/lang/StringBuilder
    //   74: dup
    //   75: ldc_w 'REMOVE '
    //   78: invokespecial <init> : (Ljava/lang/String;)V
    //   81: aload_1
    //   82: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   85: bipush #10
    //   87: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   90: invokevirtual toString : ()Ljava/lang/String;
    //   93: invokevirtual append : (Ljava/lang/CharSequence;)Ljava/io/Writer;
    //   96: pop
    //   97: aload_0
    //   98: getfield lruEntries : Ljava/util/LinkedHashMap;
    //   101: aload_1
    //   102: invokevirtual remove : (Ljava/lang/Object;)Ljava/lang/Object;
    //   105: pop
    //   106: aload_0
    //   107: invokespecial journalRebuildRequired : ()Z
    //   110: ifeq -> 208
    //   113: aload_0
    //   114: getfield executorService : Ljava/util/concurrent/ExecutorService;
    //   117: aload_0
    //   118: getfield cleanupCallable : Ljava/util/concurrent/Callable;
    //   121: invokeinterface submit : (Ljava/util/concurrent/Callable;)Ljava/util/concurrent/Future;
    //   126: pop
    //   127: goto -> 208
    //   130: aload #4
    //   132: iload_2
    //   133: invokevirtual getCleanFile : (I)Ljava/io/File;
    //   136: astore #5
    //   138: aload #5
    //   140: invokevirtual delete : ()Z
    //   143: ifne -> 177
    //   146: new java/io/IOException
    //   149: dup
    //   150: new java/lang/StringBuilder
    //   153: dup
    //   154: ldc_w 'failed to delete '
    //   157: invokespecial <init> : (Ljava/lang/String;)V
    //   160: aload #5
    //   162: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   165: invokevirtual toString : ()Ljava/lang/String;
    //   168: invokespecial <init> : (Ljava/lang/String;)V
    //   171: athrow
    //   172: astore_1
    //   173: aload_0
    //   174: monitorexit
    //   175: aload_1
    //   176: athrow
    //   177: aload_0
    //   178: aload_0
    //   179: getfield size : J
    //   182: aload #4
    //   184: invokestatic access$7 : (Lcom/samsungimaging/connectionmanager/util/DiskLruCache$Entry;)[J
    //   187: iload_2
    //   188: laload
    //   189: lsub
    //   190: putfield size : J
    //   193: aload #4
    //   195: invokestatic access$7 : (Lcom/samsungimaging/connectionmanager/util/DiskLruCache$Entry;)[J
    //   198: iload_2
    //   199: lconst_0
    //   200: lastore
    //   201: iload_2
    //   202: iconst_1
    //   203: iadd
    //   204: istore_2
    //   205: goto -> 49
    //   208: iconst_1
    //   209: istore_3
    //   210: goto -> 43
    // Exception table:
    //   from	to	target	type
    //   2	24	172	finally
    //   29	36	172	finally
    //   49	127	172	finally
    //   130	172	172	finally
    //   177	201	172	finally
  }
  
  public long size() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield size : J
    //   6: lstore_1
    //   7: aload_0
    //   8: monitorexit
    //   9: lload_1
    //   10: lreturn
    //   11: astore_3
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_3
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	11	finally
  }
  
  public final class Editor {
    private final DiskLruCache.Entry entry;
    
    private boolean hasErrors;
    
    private Editor(DiskLruCache.Entry param1Entry) {
      this.entry = param1Entry;
    }
    
    public void abort() throws IOException {
      DiskLruCache.this.completeEdit(this, false);
    }
    
    public void commit() throws IOException {
      if (this.hasErrors) {
        DiskLruCache.this.completeEdit(this, false);
        DiskLruCache.this.remove(this.entry.key);
        return;
      } 
      DiskLruCache.this.completeEdit(this, true);
    }
    
    public String getString(int param1Int) throws IOException {
      InputStream inputStream = newInputStream(param1Int);
      return (inputStream != null) ? DiskLruCache.inputStreamToString(inputStream) : null;
    }
    
    public InputStream newInputStream(int param1Int) throws IOException {
      synchronized (DiskLruCache.this) {
        if (this.entry.currentEditor != this)
          throw new IllegalStateException(); 
      } 
      if (!this.entry.readable) {
        /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=SYNTHETIC_LOCAL_VARIABLE_2} */
        return null;
      } 
      FileInputStream fileInputStream = new FileInputStream(this.entry.getCleanFile(param1Int));
      /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=SYNTHETIC_LOCAL_VARIABLE_2} */
      return fileInputStream;
    }
    
    public OutputStream newOutputStream(int param1Int) throws IOException {
      synchronized (DiskLruCache.this) {
        if (this.entry.currentEditor != this)
          throw new IllegalStateException(); 
      } 
      FaultHidingOutputStream faultHidingOutputStream = new FaultHidingOutputStream(new FileOutputStream(this.entry.getDirtyFile(param1Int)), null);
      /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=SYNTHETIC_LOCAL_VARIABLE_2} */
      return faultHidingOutputStream;
    }
    
    public void set(int param1Int, String param1String) throws IOException {
      Exception exception1;
      Exception exception2;
      Exception exception3 = null;
      try {
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(newOutputStream(param1Int), DiskLruCache.UTF_8);
      } finally {
        exception2 = null;
      } 
      DiskLruCache.closeQuietly((Closeable)exception1);
      throw exception2;
    }
    
    private class FaultHidingOutputStream extends FilterOutputStream {
      private FaultHidingOutputStream(OutputStream param2OutputStream) {
        super(param2OutputStream);
      }
      
      public void close() {
        try {
          this.out.close();
          return;
        } catch (IOException iOException) {
          DiskLruCache.Editor.this.hasErrors = true;
          return;
        } 
      }
      
      public void flush() {
        try {
          this.out.flush();
          return;
        } catch (IOException iOException) {
          DiskLruCache.Editor.this.hasErrors = true;
          return;
        } 
      }
      
      public void write(int param2Int) {
        try {
          this.out.write(param2Int);
          return;
        } catch (IOException iOException) {
          DiskLruCache.Editor.this.hasErrors = true;
          return;
        } 
      }
      
      public void write(byte[] param2ArrayOfbyte, int param2Int1, int param2Int2) {
        try {
          this.out.write(param2ArrayOfbyte, param2Int1, param2Int2);
          return;
        } catch (IOException iOException) {
          DiskLruCache.Editor.this.hasErrors = true;
          return;
        } 
      }
    }
  }
  
  private class FaultHidingOutputStream extends FilterOutputStream {
    private FaultHidingOutputStream(OutputStream param1OutputStream) {
      super(param1OutputStream);
    }
    
    public void close() {
      try {
        this.out.close();
        return;
      } catch (IOException iOException) {
        DiskLruCache.Editor.this.hasErrors = true;
        return;
      } 
    }
    
    public void flush() {
      try {
        this.out.flush();
        return;
      } catch (IOException iOException) {
        DiskLruCache.Editor.this.hasErrors = true;
        return;
      } 
    }
    
    public void write(int param1Int) {
      try {
        this.out.write(param1Int);
        return;
      } catch (IOException iOException) {
        DiskLruCache.Editor.this.hasErrors = true;
        return;
      } 
    }
    
    public void write(byte[] param1ArrayOfbyte, int param1Int1, int param1Int2) {
      try {
        this.out.write(param1ArrayOfbyte, param1Int1, param1Int2);
        return;
      } catch (IOException iOException) {
        DiskLruCache.Editor.this.hasErrors = true;
        return;
      } 
    }
  }
  
  private final class Entry {
    private DiskLruCache.Editor currentEditor;
    
    private final String key;
    
    private final long[] lengths;
    
    private boolean readable;
    
    private long sequenceNumber;
    
    private Entry(String param1String) {
      this.key = param1String;
      this.lengths = new long[DiskLruCache.this.valueCount];
    }
    
    private IOException invalidLengths(String[] param1ArrayOfString) throws IOException {
      throw new IOException("unexpected journal line: " + Arrays.toString(param1ArrayOfString));
    }
    
    private void setLengths(String[] param1ArrayOfString) throws IOException {
      if (param1ArrayOfString.length != DiskLruCache.this.valueCount)
        throw invalidLengths(param1ArrayOfString); 
      int i = 0;
      try {
        while (true) {
          if (i >= param1ArrayOfString.length)
            return; 
          this.lengths[i] = Long.parseLong(param1ArrayOfString[i]);
          i++;
        } 
      } catch (NumberFormatException numberFormatException) {
        throw invalidLengths(param1ArrayOfString);
      } 
    }
    
    public File getCleanFile(int param1Int) {
      return new File(DiskLruCache.this.directory, String.valueOf(this.key) + "." + param1Int);
    }
    
    public File getDirtyFile(int param1Int) {
      return new File(DiskLruCache.this.directory, String.valueOf(this.key) + "." + param1Int + ".tmp");
    }
    
    public String getLengths() throws IOException {
      StringBuilder stringBuilder = new StringBuilder();
      long[] arrayOfLong = this.lengths;
      int j = arrayOfLong.length;
      for (int i = 0;; i++) {
        if (i >= j)
          return stringBuilder.toString(); 
        long l = arrayOfLong[i];
        stringBuilder.append(' ').append(l);
      } 
    }
  }
  
  public final class Snapshot implements Closeable {
    private final InputStream[] ins;
    
    private final String key;
    
    private final long sequenceNumber;
    
    private Snapshot(String param1String, long param1Long, InputStream[] param1ArrayOfInputStream) {
      this.key = param1String;
      this.sequenceNumber = param1Long;
      this.ins = param1ArrayOfInputStream;
    }
    
    public void close() {
      InputStream[] arrayOfInputStream = this.ins;
      int j = arrayOfInputStream.length;
      for (int i = 0;; i++) {
        if (i >= j)
          return; 
        DiskLruCache.closeQuietly(arrayOfInputStream[i]);
      } 
    }
    
    public DiskLruCache.Editor edit() throws IOException {
      return DiskLruCache.this.edit(this.key, this.sequenceNumber);
    }
    
    public InputStream getInputStream(int param1Int) {
      return this.ins[param1Int];
    }
    
    public String getString(int param1Int) throws IOException {
      return DiskLruCache.inputStreamToString(getInputStream(param1Int));
    }
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanage\\util\DiskLruCache.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */