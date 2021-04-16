package me.andy5.segment_forkjointask.exception;

import me.andy5.segment_forkjointask.TaskHandler;

/**
 * {@link TaskHandler}接口抛出的异常
 *
 * @author andy(Andy)
 * @datetime 2019-09-16 08:46 GMT+8
 * @email 411086563@qq.com
 */
public class TaskHandleException extends Exception {

    public TaskHandleException() {
    }

    public TaskHandleException(String message) {
        super(message);
    }

    public TaskHandleException(String message, Throwable cause) {
        super(message, cause);
    }

    public TaskHandleException(Throwable cause) {
        super(cause);
    }
}
