package me.andy5.segment_forkjointask.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.andy5.segment_forkjointask.Segment;
import me.andy5.segment_forkjointask.SegmentTask;

/**
 * @author andy(Andy)
 * @datetime 2019-08-16 08:54 GMT+8
 * @email 411086563@qq.com
 */
public class SegmentTaskUtil {

    /**
     * 所有任务是否完全成功
     *
     * @param tasks
     * @return
     */
    public static boolean isFullySuccess(List<SegmentTask> tasks) {
        boolean isFullySuccess = true;
        for (SegmentTask task : tasks) {
            if (task == null) {
                continue;
            }
            isFullySuccess = isFullySuccess && task.isFullySuccess();
        }
        return isFullySuccess;
    }

    /**
     * 所有任务出现的异常
     *
     * @param tasks
     * @return
     */
    public static List<Exception> exceptions(List<SegmentTask> tasks) {
        List<Exception> exceptions = new ArrayList<>();
        for (SegmentTask task : tasks) {
            if (task == null) {
                continue;
            }
            // task本身异常
            Exception exception = task.exception();
            if (exception != null) {
                exceptions.add(exception);
            }
            // segment异常
            Map<Segment, Exception> errorHandleSegmentException = task.errorHandleSegmentException();
            if (errorHandleSegmentException != null && !errorHandleSegmentException.isEmpty()) {
                exceptions.addAll(errorHandleSegmentException.values());
            }
        }
        return exceptions;
    }

    /**
     * 所有任务成功的分段
     *
     * @param tasks
     * @return
     */
    public static List<Segment> successHandledSegments(List<SegmentTask> tasks) {
        List<Segment> successHandledSegments = new ArrayList<>();
        for (SegmentTask task : tasks) {
            if (task == null) {
                continue;
            }
            List<Segment> successSegments = task.successHandledSegments();
            if (successSegments != null && !successSegments.isEmpty()) {
                successHandledSegments.addAll(successSegments);
            }
        }
        return successHandledSegments;
    }

    /**
     * 所有任务失败的分段
     *
     * @param tasks
     * @return
     */
    public static List<Segment> errorHandledSegments(List<SegmentTask> tasks) {
        List<Segment> errorHandledSegments = new ArrayList<>();
        for (SegmentTask task : tasks) {
            if (task == null) {
                continue;
            }
            List<Segment> errorSegments = task.errorHandleSegments();
            if (errorSegments != null && !errorSegments.isEmpty()) {
                errorHandledSegments.addAll(errorSegments);
            }
        }
        return errorHandledSegments;
    }

    /**
     * 所有任务失败的分段详情
     *
     * @param tasks
     * @return
     */
    public static Map<Segment, Exception> errorHandleSegmentExceptions(List<SegmentTask> tasks) {
        Map<Segment, Exception> errorHandleSegmentExceptionMap = new HashMap<>();
        for (SegmentTask task : tasks) {
            if (task == null) {
                continue;
            }
            Map<Segment, Exception> errorHandleSegmentException = task.errorHandleSegmentException();
            if (errorHandleSegmentException != null && !errorHandleSegmentException.isEmpty()) {
                errorHandleSegmentExceptionMap.putAll(errorHandleSegmentException);
            }
        }
        return errorHandleSegmentExceptionMap;
    }
}
