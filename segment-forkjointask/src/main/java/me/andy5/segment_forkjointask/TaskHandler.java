package me.andy5.segment_forkjointask;

import java.util.List;

import me.andy5.segment_forkjointask.exception.TaskHandleException;

/**
 * 任务执行处理器
 *
 * @author andy(Andy)
 * @datetime 2019-08-27 16:13 GMT+8
 * @email 411086563@qq.com
 */
public interface TaskHandler {

    /**
     * 处理每个线程中的每个分段
     *
     * @param segment  当前正在处理的分段
     * @param segments 当前线程要处理的所有分段
     * @return
     * @throws TaskHandleException 异常信息
     */
    Segment handle(Segment segment, List<Segment> segments) throws TaskHandleException;
}
