# SegmentForkJoinTask

#### Introduction
ForkJoinTask that automatically processes segments with a specified segment size
For example, there is a data: [0, 1000]. Now it is required to use 4 threads to process segmented processing. After using this library, 4 threads will be automatically created to process [0, 250], [250, 500], [ 500, 750], [750, 1000], you only need to specify what needs to be done in each thread.
Of course, it also supports more complex processing, such as processing discontinuous data at once: [[0, 1432], [2048, 3100], [3249, 14576]] automatic equalization and segmentation, etc.

The following is the use of SegmentTask:
``` java
// Specify the number of threads (do not specify the size of each thread)
Object myData = new Object();
Segment segment = new Segment(0, 1000, myData);
int threads = 4;
SegmentTask task = new SegmentTask(segment, threads, new MyDataTaskHandler());
ForkJoinPool forkJoinPool = new ForkJoinPool();
long start = System.currentTimeMillis();
Future<List<SegmentTask>> result = forkJoinPool.submit(task);

// Specify the size of each thread (do not specify the number of threads)
Object myData = new Object();
Segment segment = new Segment(0, 1000, myData);
long threadSegmentSize = 1600;
SegmentTask task = new SegmentTask(segment, threadSegmentSize, new MyDataTaskHandler());
ForkJoinPool forkJoinPool = new ForkJoinPool();
long start = System.currentTimeMillis();
Future<List<SegmentTask>> result = forkJoinPool.submit(task);

// Multiple segments processing
Object myData = new Object();
List<Segment> segments = new ArrayList<>();
Segment segment1 = new Segment(0, 1000, myData);
segments.add(segment1);
Segment segment2 = new Segment(1300, 2000, myData);
segments.add(segment2);
int threads = 4;
SegmentTask task = new SegmentTask(segments, threads, new MyDataTaskHandler());
ForkJoinPool forkJoinPool = new ForkJoinPool();
long start = System.currentTimeMillis();
Future<List<SegmentTask>> result = forkJoinPool.submit(task);
```

For detailed usage, see: [SegmentTaskTest.java](https://gitee.com/wlfcolin_admin/SegmentForkJoinTask/blob/master/segment-forkjointask/src/test/java/me/andy5/segment_forkjointask/SegmentTaskTest.java)