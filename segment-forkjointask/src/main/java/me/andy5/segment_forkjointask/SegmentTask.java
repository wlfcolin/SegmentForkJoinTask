package me.andy5.segment_forkjointask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.RecursiveTask;

import me.andy5.segment_forkjointask.exception.SegmentGroupException;
import me.andy5.segment_forkjointask.exception.TaskComputeException;
import me.andy5.segment_forkjointask.exception.TaskHandleException;
import me.andy5.segment_forkjointask.impl.MultiThreadSegmentGroup;
import me.andy5.segment_forkjointask.log.Log;
import me.andy5.segment_forkjointask.util.SegmentUtil;

/**
 * 分段任务Task类
 * <p>
 * 将大的任务分段成多个小任务，然后再将每个小任务的结果汇合到大任务中，并在大任务的{@link SegmentTask#compute()}方法返回所有任务信息，如果有异常，通过
 * {@link SegmentTask#exception()}获取
 * <p>
 * 1、具体分多少个小任务由参数{@link SegmentTask#threads}指定
 * <p>
 * 2、每个任务最大处理分段大小由参数{@link SegmentTask#threadSegmentSize}指定
 * <p>
 * 3、分段规则由{@link SegmentGroup}接口实现决定
 * <p>
 * 4、分段后任务执行内容由{@link TaskHandler}接口实现决定
 *
 * @author andy(Andy)
 * @datetime 2019-03-18 16:59 GMT+8
 * @email 411086563@qq.com
 */
public class SegmentTask extends RecursiveTask<List<SegmentTask>> {

    // --------- 构造传递信息 ---------
    // 需要处理的分段
    private List<Segment> segments;
    // 处理分段期望的线程数
    private int threads;
    // 每个线程期望处理的分段大小
    private long threadSegmentSize;
    // 任务执行处理器
    private TaskHandler taskHandler;
    // 处理数据的优先模式
    private PriorityMode priorityMode = PriorityMode.THREADS;
    // 分段规则处理器
    private SegmentGroup segmentGroup = new MultiThreadSegmentGroup();

    // --------- 本线程处理信息 ---------
    // 准备要处理的分段
    private List<Segment> preparedSegments = new ArrayList<>();
    // 已处理成功的分段
    private List<Segment> successHandledSegments = new ArrayList<>();
    // 处理失败的分段以及失败信息
    private Map<Segment, Exception> errorHandleSegments = new HashMap<>();
    // 正在处理的分段
    private Segment handlingSegment;
    // 处理过程中遇到的异常信息（task本身的异常信息），如果是segment异常信息则存在errorHandleSegments中
    private Exception exception;
    // 所有子任务
    private List<SegmentTask> subSegmentTasks = new ArrayList<>();
    // 是否已经取消了
    private boolean isCancelled = false;

    // --------- 其它信息 ---------
    // 日志写入类
    private Log log = Log.getLog(this.getClass());

    /**
     * 构建分段任务Task类
     *
     * @param segment 需要处理的分段
     * @param threads 处理分段期望的线程数
     * @param taskHandler 任务执行处理器
     */
    public SegmentTask(Segment segment, int threads, TaskHandler taskHandler) {
        this(new ArrayList<>(Arrays.asList(segment)), threads, taskHandler);
    }

    /**
     * 构建分段任务Task类
     *
     * @param segments    需要处理的分段
     * @param threads     处理分段期望的线程数
     * @param taskHandler 任务执行处理器
     */
    public SegmentTask(List<Segment> segments, int threads, TaskHandler taskHandler) {
        this.segments = segments;
        this.threads = threads;
        this.taskHandler = taskHandler;
        priorityMode = PriorityMode.THREADS;
    }

    /**
     * 构建分段任务Task类
     *
     * @param segment 需要处理的分段
     * @param threadSegmentSize 每个线程期望处理的分段大小
     * @param taskHandler 任务执行处理器
     */
    public SegmentTask(Segment segment, long threadSegmentSize, TaskHandler taskHandler) {
        this(new ArrayList<>(Arrays.asList(segment)), threadSegmentSize, taskHandler);
    }

    /**
     * 构建分段任务Task类
     *
     * @param segments          需要处理的分段
     * @param threadSegmentSize 每个线程期望处理的分段大小
     * @param taskHandler       任务执行处理器
     */
    public SegmentTask(List<Segment> segments, long threadSegmentSize, TaskHandler taskHandler) {
        this.segments = segments;
        this.threadSegmentSize = threadSegmentSize;
        this.taskHandler = taskHandler;
        priorityMode = PriorityMode.THREAD_SEGMENT_SIZE;
    }

    @Override
    protected List<SegmentTask> compute() {
        List<SegmentTask> taskResults = new ArrayList<>();
        taskResults.add(this);// 添加当前任务
        try {
            // 1、检查参数
            checkParam();
            if (priorityMode == PriorityMode.THREADS) {
                // 2、单线程，当前线程直接处理
                if (threads == 1) {
                    executeTask(segments);
                }
                // 3、多线程，需要使用分段规则构建子任务
                else {
                    computeTask(taskResults);
                }
            } else if (priorityMode == PriorityMode.THREAD_SEGMENT_SIZE) {
                // 2、单线程，当前线程直接处理
                if (SegmentUtil.getSegmentsSize(segments) <= threadSegmentSize) {
                    executeTask(segments);
                }
                // 3、多线程，需要使用分段规则构建子任务
                else {
                    computeTask(taskResults);
                }
            }
        } catch (Exception e) {
            exception = e;
        }
        return taskResults;
    }

    // 检查参数
    private void checkParam() throws Exception {
        if (segments == null || segments.isEmpty()) {
            throw new Exception("参数segments为空错误，segments=" + segments);
        }
        if (priorityMode == PriorityMode.THREADS) {
            if (threads <= 0) {// 范围是[1~无限大]
                throw new Exception("参数threads错误，threads=" + threads);
            }
        } else if (priorityMode == PriorityMode.THREAD_SEGMENT_SIZE) {
            if (threadSegmentSize <= 0) {// 范围是[1~无限大]
                throw new Exception("参数threadSegmentSize错误，threadSegmentSize=" + threadSegmentSize);
            }
        }
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        boolean result = super.cancel(mayInterruptIfRunning);
        if (result) {
            isCancelled = true;
            for (Segment segment : preparedSegments) {
                if (segment == null) {
                    continue;
                }
                segment.cancel();
            }
            if (handlingSegment != null) {
                handlingSegment.cancel();
            }
            for (SegmentTask task : subSegmentTasks) {
                if (task == null) {
                    continue;
                }
                if (task == this) {
                    continue;
                }
                task.isCancelled = true;
                for (Segment segment : task.preparedSegments) {
                    if (segment == null) {
                        continue;
                    }
                    segment.cancel();
                }
                if (task.handlingSegment != null) {
                    task.handlingSegment.cancel();
                }
            }
        }
        return result;
    }

    // 当前线程直接处理
    private void executeTask(List<Segment> segments) {
        preparedSegments = segments;
        log.debug("======线程" + Thread.currentThread().getName() + "开始======");
        for (Segment segment : preparedSegments) {
            log.debug("开始处理：" + segment);
            try {
                if (isCancelled) { // 如果取消了，那么没有执行的分段就不要执行了
                    continue;
                }
                handlingSegment = segment;// 正在处理
                if (taskHandler != null) {
                    taskHandler.handle(handlingSegment, preparedSegments);
                }
                successHandledSegments.add(segment);// 完成
            } catch (TaskHandleException e) {
                log.error("处理：" + segment + "遇到异常：" + e);
                errorHandleSegments.put(segment, e);
            } finally {
                log.debug("处理完成：" + segment);
            }
        }
        log.debug("======线程" + Thread.currentThread().getName() + "结束======");
    }

    // 需要使用分段规则构建子任务
    private void computeTask(List<SegmentTask> taskResults) throws TaskComputeException, SegmentGroupException {
        // 总共需要处理的大小
        long totalSize = SegmentUtil.getSegmentsSize(segments);
        if (priorityMode == PriorityMode.THREADS) {
            // 每个线程处理的最大分段大小（四舍五入取整）
            threadSegmentSize = Math.round((double) totalSize / threads);
        } else if (priorityMode == PriorityMode.THREAD_SEGMENT_SIZE) {
            // 处理分段期望的线程数（四舍五入取整）
            long threads = Math.round((double) totalSize / threadSegmentSize);
            if (threads > Integer.MAX_VALUE) {
                // 这种情况达到了上限，应该重新分配
                threads = Integer.MAX_VALUE;
                threadSegmentSize = Math.round((double) totalSize / threads);
            }
            this.threads = (int) threads;
        }
        log.info("threads=" + threads + "，threadSegmentSize=" + threadSegmentSize + "，totalSize=" + totalSize);

        if (totalSize <= 0 || threadSegmentSize <= 0) {
            throw new TaskComputeException("异常，totalSize=" + totalSize + "，threadSegmentSize=" + threadSegmentSize);
        }
        // 分割后的分组（每段符合不大于threadMaxSize要求，且线程数量不能超过maxThreads）
        List<List<Segment>> taskSizePosList = segmentGroup.group(segments, threads, threadSegmentSize);
        if (taskSizePosList.size() <= 0) {
            throw new TaskComputeException("异常，taskSizePosList <= 0，taskSizePosList=" + taskSizePosList);
        }
        if (taskSizePosList.size() > threads) {
            throw new TaskComputeException("异常，taskSizePosList.size > threads，taskSizePosList=" + taskSizePosList);
        }
        // 只有一个分段集合
        if (taskSizePosList.size() == 1) {
            List<Segment> firstThreadSegment = taskSizePosList.get(0);
            if (firstThreadSegment != null) {
                // 当前线程直接处理
                executeTask(firstThreadSegment);
            }
        }
        // 二个分段集合及以上，需要使用新线程
        else if (taskSizePosList.size() >= 2) {
            List<SegmentTask> subTasks = new ArrayList();
            // 从第二个分段集合开始用新线程
            for (int i = 1; i < taskSizePosList.size(); i++) {
                List<Segment> posList = taskSizePosList.get(i);
                if (posList == null || posList.isEmpty()) {
                    continue;
                }
                // 由于已经算好，只需要一条线程即可
                SegmentTask subTask = new SegmentTask(posList, 1, taskHandler);
                // 异步执行
                subTask.fork();
                subTasks.add(subTask);
            }
            subSegmentTasks = subTasks;
            // 第一个分段集合直接处理
            List<Segment> firstThreadSegment = taskSizePosList.get(0);
            if (firstThreadSegment != null) {
                // 当前线程直接处理
                executeTask(firstThreadSegment);
            }
            for (SegmentTask subTask : subTasks) {
                if (subTask == null) {
                    continue;
                }
                // 同步获取结果
                List<SegmentTask> subTaskResult = subTask.join();
                // 每个task执行完添加到taskResults中
                taskResults.addAll(subTaskResult);
            }
        }
    }

    @Override
    public String toString() {
        long size = SegmentUtil.getSegmentsSize(preparedSegments);
        return "SegmentTask{" + "preparedSegments=" + preparedSegments + ",size=" + size + ", exception=" + exception + ", errorHandleSegments=" + errorHandleSegments + '}';
    }

    /**
     * 获取异常信息，如果不为null则处理过程中产生了异常
     *
     * @return
     */
    public Exception exception() {
        return exception;
    }

    /**
     * 所有需要处理的分段
     *
     * @return
     */
    public List<Segment> segments() {
        return segments;
    }

    /**
     * 处理成功的分段
     *
     * @return
     */
    public List<Segment> successHandledSegments() {
        return successHandledSegments;
    }

    /**
     * 处理失败的分段（有失败的分段则没有完全成功）
     *
     * @return
     */
    public List<Segment> errorHandleSegments() {
        return new ArrayList<>(errorHandleSegments.keySet());
    }

    /**
     * 处理失败的分段异常信息（有失败的分段则没有完全成功）
     *
     * @return
     */
    public Map<Segment, Exception> errorHandleSegmentException() {
        return errorHandleSegments;
    }

    /**
     * 是否完全成功
     *
     * @return
     */
    public boolean isFullySuccess() {
        if (exception == null && errorHandleSegments.values().isEmpty() && successHandledSegments.size() == preparedSegments.size()) {
            return true;
        }
        return false;
    }

    // 处理数据的优先模式
    private enum PriorityMode {
        // 线程优先
        THREADS, // 分段大小优先
        THREAD_SEGMENT_SIZE
    }
}