# SegmentForkJoinTask

[English](https://gitee.com/wlfcolin_admin/SegmentForkJoinTask/blob/master/README.en.md)

#### 介绍
具备指定分段大小自动处理分段的ForkJoinTask
例如有一个数据：[0, 1000]，现要求，使用4个线程处理分段处理，那么使用此库后会自动的创建4个线程分别处理[0, 250]、[250, 500]、[500, 750]、[750, 1000]，你只需要指定每个线程中需要做什么事情即可。
当然还支持更复杂的处理比如一次处理这样的不连续数据：[[0, 1432], [2048, 3100], [3249, 14576]]自动均衡分段等。

以下是SegmentTask的使用：
``` java
// 指定线程数（不指定每个线程的大小）
Object myData = new Object();
Segment segment = new Segment(0, 1000, myData);
int threads = 4;
SegmentTask task = new SegmentTask(segment, threads, new MyDataTaskHandler());
ForkJoinPool forkJoinPool = new ForkJoinPool();
Future<List<SegmentTask>> result = forkJoinPool.submit(task);
List<SegmentTask> tasks = result.get();
boolean isFullySuccess = SegmentTaskUtil.isFullySuccess(tasks);
log.debug("--------所有任务是否完全成功，isFullySuccess=" + isFullySuccess + "--------");
List<Segment> successHandledSegments = SegmentTaskUtil.successHandledSegments(tasks);
log.debug("--------所有任务成功的分段，successHandledSegments=" + successHandledSegments + "--------");
List<Segment> errorHandledSegments = SegmentTaskUtil.errorHandledSegments(tasks);
log.debug("--------所有任务失败的分段，errorHandledSegments=" + errorHandledSegments + "--------");
List<Exception> exceptions = SegmentTaskUtil.exceptions(tasks);
log.debug("--------所有任务出现的异常，exceptions=" + exceptions + "--------");
Map<Segment, Exception> errorHandleSegmentExceptions = SegmentTaskUtil.errorHandleSegmentExceptions(tasks);
log.debug("--------所有任务失败的分段详情，errorHandleSegmentExceptions=" + errorHandleSegmentExceptions + "--------");

// 指定每个线程的大小（不指定线程数）
Object myData = new Object();
Segment segment = new Segment(0, 1000, myData);
long threadSegmentSize = 1600;
SegmentTask task = new SegmentTask(segment, threadSegmentSize, new MyDataTaskHandler());
ForkJoinPool forkJoinPool = new ForkJoinPool();
Future<List<SegmentTask>> result = forkJoinPool.submit(task);
List<SegmentTask> tasks = result.get();
boolean isFullySuccess = SegmentTaskUtil.isFullySuccess(tasks);
log.debug("--------所有任务是否完全成功，isFullySuccess=" + isFullySuccess + "--------");
List<Segment> successHandledSegments = SegmentTaskUtil.successHandledSegments(tasks);
log.debug("--------所有任务成功的分段，successHandledSegments=" + successHandledSegments + "--------");
List<Segment> errorHandledSegments = SegmentTaskUtil.errorHandledSegments(tasks);
log.debug("--------所有任务失败的分段，errorHandledSegments=" + errorHandledSegments + "--------");
List<Exception> exceptions = SegmentTaskUtil.exceptions(tasks);
log.debug("--------所有任务出现的异常，exceptions=" + exceptions + "--------");
Map<Segment, Exception> errorHandleSegmentExceptions = SegmentTaskUtil.errorHandleSegmentExceptions(tasks);
log.debug("--------所有任务失败的分段详情，errorHandleSegmentExceptions=" + errorHandleSegmentExceptions + "--------");

// 多段不连续处理
Object myData = new Object();
List<Segment> segments = new ArrayList<>();
Segment segment1 = new Segment(0, 1000, myData);
segments.add(segment1);
Segment segment2 = new Segment(1300, 2000, myData);
segments.add(segment2);
int threads = 4;
SegmentTask task = new SegmentTask(segments, threads, new MyDataTaskHandler());
ForkJoinPool forkJoinPool = new ForkJoinPool();
Future<List<SegmentTask>> result = forkJoinPool.submit(task);
List<SegmentTask> tasks = result.get();
boolean isFullySuccess = SegmentTaskUtil.isFullySuccess(tasks);
log.debug("--------所有任务是否完全成功，isFullySuccess=" + isFullySuccess + "--------");
List<Segment> successHandledSegments = SegmentTaskUtil.successHandledSegments(tasks);
log.debug("--------所有任务成功的分段，successHandledSegments=" + successHandledSegments + "--------");
List<Segment> errorHandledSegments = SegmentTaskUtil.errorHandledSegments(tasks);
log.debug("--------所有任务失败的分段，errorHandledSegments=" + errorHandledSegments + "--------");
List<Exception> exceptions = SegmentTaskUtil.exceptions(tasks);
log.debug("--------所有任务出现的异常，exceptions=" + exceptions + "--------");
Map<Segment, Exception> errorHandleSegmentExceptions = SegmentTaskUtil.errorHandleSegmentExceptions(tasks);
log.debug("--------所有任务失败的分段详情，errorHandleSegmentExceptions=" + errorHandleSegmentExceptions + "--------");
```

详细使用见：[SegmentTaskTest.java](https://gitee.com/wlfcolin_admin/SegmentForkJoinTask/blob/master/segment-forkjointask/src/test/java/me/andy5/segment_forkjointask/SegmentTaskTest.java)