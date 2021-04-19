package me.andy5.segment_forkjointask;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;

import me.andy5.segment_forkjointask.exception.TaskHandleException;
import me.andy5.segment_forkjointask.log.Log;
import me.andy5.segment_forkjointask.util.SegmentTaskUtil;
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
        printDetail(result, start);
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
        printDetail(result, start);
        long end = System.currentTimeMillis();
        log.debug("--------testSegmentTaskInMultiThread 结束执行，耗时=" + (end - start) + "--------");
    }

    // 测试SegmentTask使用指定threadSegmentSize为segments的大小执行(相当于单个线程)
    @Test
    public void testSegmentTaskWithSegmentsSize() {
        List<Segment> segments = getTestSegments();
        long threadSegmentSize = SegmentUtil.getSegmentsSize(segments);
        SegmentTask task = new SegmentTask(segments, threadSegmentSize, new SleepTaskHandler());
        // Segment segment = getTestSegment();
        // long threadSegmentSize = segment.size();
        // SegmentTask task = new SegmentTask(segment, threadSegmentSize, new SleepTaskHandler());
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        long start = System.currentTimeMillis();
        log.debug("--------testSegmentTaskWithSegmentsSize 开始执行--------");
        Future<List<SegmentTask>> result = forkJoinPool.submit(task);
        printDetail(result, start);
        long end = System.currentTimeMillis();
        log.debug("--------testSegmentTaskWithSegmentsSize 结束执行，耗时=" + (end - start) + "--------");
    }

    // 测试SegmentTask使用指定threadSegmentSize执行(相当于多个线程)
    @Test
    public void testSegmentTaskWithCustomSegmentSize() {
        List<Segment> segments = getTestSegments();
        long threadSegmentSize = 1600;
        SegmentTask task = new SegmentTask(segments, threadSegmentSize, new SleepTaskHandler());
        // Segment segment = getTestSegment();
        // long threadSegmentSize = segment.size() / 3;
        // SegmentTask task = new SegmentTask(segment, threadSegmentSize, new SleepTaskHandler());
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        long start = System.currentTimeMillis();
        log.debug("--------testSegmentTaskWithCustomSegmentSize 开始执行--------");
        Future<List<SegmentTask>> result = forkJoinPool.submit(task);
        printDetail(result, start);
        long end = System.currentTimeMillis();
        log.debug("--------testSegmentTaskWithCustomSegmentSize 结束执行，耗时=" + (end - start) + "--------");
    }

    // 测试SegmentTask执行过程出现部分异常
    @Test
    public void testSegmentTaskWithPartError() {
        List<Segment> segments = getTestSegments();
        long threadSegmentSize = SegmentUtil.getSegmentsSize(segments);
        SegmentTask task = new SegmentTask(segments, threadSegmentSize, new RandomErrorTaskHandler());
        // Segment segment = getTestSegment();
        // long threadSegmentSize = segment.size();
        // SegmentTask task = new SegmentTask(segment, threadSegmentSize, new SleepTaskHandler());
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        long start = System.currentTimeMillis();
        log.debug("--------testSegmentTaskWithPartError 开始执行--------");
        Future<List<SegmentTask>> result = forkJoinPool.submit(task);
        printDetail(result, start);
        long end = System.currentTimeMillis();
        log.debug("--------testSegmentTaskWithPartError 结束执行，耗时=" + (end - start) + "--------");
    }

    // ------------ 测试辅助方法 ------------
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

    private void printDetail(Future<List<SegmentTask>> result, long start) {
        try {
            List<SegmentTask> tasks = result.get();
            for (SegmentTask t : tasks) {
                long end = System.currentTimeMillis();
                log.debug("task：" + t + "执行完成，耗时=" + (end - start));
            }
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
        } catch (Exception e) {
            // e.printStackTrace();
            log.error("printDetail存在异常：" + e);
        }
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

    // 模拟随机出现异常的处理任务实现
    private static class RandomErrorTaskHandler implements TaskHandler {

        private static Log log = Log.getLog(RandomErrorTaskHandler.class);

        @Override
        public Segment handle(Segment segment, List<Segment> segments) throws TaskHandleException {
            try {
                Random random = new Random();
                int number = random.nextInt(2);
                log.debug("产生随机数分母，number=" + number + "--------");
                int result = 100 / number;
            } catch (Exception e) {
                throw new TaskHandleException(e);
            }
            return segment;
        }
    }

}
