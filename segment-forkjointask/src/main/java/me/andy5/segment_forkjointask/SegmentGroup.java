package me.andy5.segment_forkjointask;

import java.util.List;

import me.andy5.segment_forkjointask.exception.SegmentGroupException;

/**
 * 对分段进行分组处理，每组对应一个线程
 *
 * @author andy(Andy)
 * @datetime 2019-08-12 11:58 GMT+8
 * @email 411086563@qq.com
 */
public interface SegmentGroup {

    /**
     * 对分段进行分组处理，每组对应一个线程
     * <p>
     * 返回数据要求：List<List<Segment>>的size不能大于maxThreads，每个List<Segment>中所有Segment的总size不能大于threadMaxSize
     *
     * @param segments
     * @param maxThreads
     * @param threadMaxSize
     * @return
     * @throws SegmentGroupException
     */
    List<List<Segment>> group(List<Segment> segments, int maxThreads, long threadMaxSize) throws SegmentGroupException;
}
