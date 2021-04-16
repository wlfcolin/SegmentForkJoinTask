package me.andy5.segment_forkjointask;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;

import me.andy5.segment_forkjointask.exception.TaskHandleException;
import me.andy5.segment_forkjointask.log.Log;
import me.andy5.segment_forkjointask.util.SegmentUtil;

/**
 * @author andy(Andy)
 * @datetime 2019-10-16 09:58 GMT+8
 * @email 411086563@qq.com
 */
public class SegmentTaskTest {

    private static Log log = Log.getLog(SegmentTaskTest.class);

    // 测试SegmentTask使用单个线程执行
    @Test
    public void testSegmentTaskInOneThread() {
        int threads = 1;
        List<Segment> segments = getTestSegments();
        SegmentTask task = new SegmentTask(segments, threads, new SleepTaskHandler());
        // Segment segment = getTestSegment();
        // SegmentTask task = new SegmentTask(segment, threads, new SleepTaskHandler());
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        long start = System.currentTimeMillis();
        log.debug("--------testSegmentTaskInOneThread 开始执行--------");
        Future<List<SegmentTask>> result = forkJoinPool.submit(task);
        try {
            List<SegmentTask> tasks = result.get();
            for (SegmentTask t : tasks) {
                long end = System.currentTimeMillis();
                log.debug("task：" + t + "执行完成，耗时=" + (end - start));
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("异常：" + e);
        }
        long end = System.currentTimeMillis();
        log.debug("--------testSegmentTaskInOneThread 结束执行，耗时=" + (end - start) + "--------");
    }

    // 测试SegmentTask使用多个线程执行
    @Test
    public void testSegmentTaskInMultiThread() {
        int threads = 15;
        // List<Segment> segments = getTestSegments();
        // SegmentTask task = new SegmentTask(segments, threads, new SleepTaskHandler());
        Segment segment = getTestSegment();
        SegmentTask task = new SegmentTask(segment, threads, new SleepTaskHandler());
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        long start = System.currentTimeMillis();
        log.debug("--------testSegmentTaskInMultiThread 开始执行--------");
        Future<List<SegmentTask>> result = forkJoinPool.submit(task);
        try {
            List<SegmentTask> tasks = result.get();
            for (SegmentTask t : tasks) {
                long end = System.currentTimeMillis();
                log.debug("task：" + t + "执行完成，耗时=" + (end - start));
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("异常：" + e);
        }
        long end = System.currentTimeMillis();
        log.debug("--------testSegmentTaskInMultiThread 结束执行，耗时=" + (end - start) + "--------");
    }

    // 测试SegmentTask使用指定threadSegmentSize为segments的大小执行(相当于单个线程)
    @Test
    public void testSegmentTaskInWithSegmentsSize() {
        List<Segment> segments = getTestSegments();
        long threadSegmentSize = SegmentUtil.getSegmentsSize(segments);
        SegmentTask task = new SegmentTask(segments, threadSegmentSize, new SleepTaskHandler());
        // Segment segment = getTestSegment();
        // long threadSegmentSize = segment.size();
        // SegmentTask task = new SegmentTask(segment, threadSegmentSize, new SleepTaskHandler());
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        long start = System.currentTimeMillis();
        log.debug("--------testSegmentTaskInWithSegmentsSize 开始执行--------");
        Future<List<SegmentTask>> result = forkJoinPool.submit(task);
        try {
            List<SegmentTask> tasks = result.get();
            for (SegmentTask t : tasks) {
                long end = System.currentTimeMillis();
                log.debug("task：" + t + "执行完成，耗时=" + (end - start));
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("异常：" + e);
        }
        long end = System.currentTimeMillis();
        log.debug("--------testSegmentTaskInWithSegmentsSize 结束执行，耗时=" + (end - start) + "--------");
    }

    // 测试SegmentTask使用指定threadSegmentSize执行(相当于多个线程)
    @Test
    public void testSegmentTaskInWithCustomSegmentSize() {
        List<Segment> segments = getTestSegments();
        long threadSegmentSize = 1600;
        SegmentTask task = new SegmentTask(segments, threadSegmentSize, new SleepTaskHandler());
        // Segment segment = getTestSegment();
        // long threadSegmentSize = segment.size() / 3;
        // SegmentTask task = new SegmentTask(segment, threadSegmentSize, new SleepTaskHandler());
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        long start = System.currentTimeMillis();
        log.debug("--------testSegmentTaskInWithCustomSegmentSize 开始执行--------");
        Future<List<SegmentTask>> result = forkJoinPool.submit(task);
        try {
            List<SegmentTask> tasks = result.get();
            for (SegmentTask t : tasks) {
                long end = System.currentTimeMillis();
                log.debug("task：" + t + "执行完成，耗时=" + (end - start));
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("异常：" + e);
        }
        long end = System.currentTimeMillis();
        log.debug("--------testSegmentTaskInWithCustomSegmentSize 结束执行，耗时=" + (end - start) + "--------");
    }

    // 测试辅助方法
    private static List<Segment> getTestSegments() {
        List<Segment> segments = new ArrayList<>();
        Segment segment1 = new Segment(0, 1432, null);
        segments.add(segment1);
        Segment segment2 = new Segment(2048, 3100, null);
        segments.add(segment2);
        Segment segment3 = new Segment(3249, 14576, null);
        segments.add(segment3);
        Segment segment4 = new Segment(54577, 54579, null);
        segments.add(segment4);
        Segment segment5 = new Segment(20000, 30000, null);
        segments.add(segment5);
        return segments;
    }
    private static Segment getTestSegment() {
        return new Segment(0, 100000, null);
    }

    // 模拟处理任务实现
    private static class SleepTaskHandler implements TaskHandler {

        @Override
        public Segment handle(Segment segment, List<Segment> segments) throws TaskHandleException {
            // 模拟处理任务，休眠一会儿
            try {
                Thread.sleep(segment.size());
            } catch (InterruptedException e) {
                throw new TaskHandleException(e);
            }
            return segment;
        }
    }

}
