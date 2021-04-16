package me.andy5.segment_forkjointask.exception;

import me.andy5.segment_forkjointask.SegmentGroup;

/**
 * {@link SegmentGroup}接口抛出的异常
 *
 * @author andy(Andy)
 * @datetime 2019-09-16 08:48 GMT+8
 * @email 411086563@qq.com
 */
public class SegmentGroupException extends Exception {

    public SegmentGroupException() {
    }

    public SegmentGroupException(String message) {
        super(message);
    }

    public SegmentGroupException(String message, Throwable cause) {
        super(message, cause);
    }

    public SegmentGroupException(Throwable cause) {
        super(cause);
    }
}
