package me.andy5.segment_forkjointask.exception;

import me.andy5.segment_forkjointask.SegmentTask;

/**
 * {@link SegmentTask}处理数据抛出的异常
 *
 * @author andy(Andy)
 * @datetime 2019-09-16 08:48 GMT+8
 * @email 411086563@qq.com
 */
public class TaskComputeException extends Exception {

    public TaskComputeException() {
    }

    public TaskComputeException(String message) {
        super(message);
    }

    public TaskComputeException(String message, Throwable cause) {
        super(message, cause);
    }

    public TaskComputeException(Throwable cause) {
        super(cause);
    }
}
