package me.andy5.segment_forkjointask.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import me.andy5.segment_forkjointask.Segment;
import me.andy5.segment_forkjointask.SegmentGroup;
import me.andy5.segment_forkjointask.exception.SegmentGroupException;
import me.andy5.segment_forkjointask.log.Log;

/**
 * 对分段多线程分组处理
 *
 * @author andy(Andy)
 * @datetime 2019-08-12 11:58 GMT+8
 * @email 411086563@qq.com
 */
public class MultiThreadSegmentGroup implements SegmentGroup {

    // 日志写入类
    private Log log = Log.getLog(this.getClass());

    @Override
    public List<List<Segment>> group(List<Segment> segments, int maxThreads, long threadMaxSize) throws SegmentGroupException {
        try {
            // 保证segments中每个Segment的大小不大于threadMaxSize
            segments = resetSegmentByThreadMaxSize(segments, threadMaxSize);
            // 保证segments中的分段能使最优线方式进行排列处理
            List<List<Segment>> handledSegments = handleSegments(segments, threadMaxSize);
            // 超出的线程数
            int overSize = handledSegments.size() - maxThreads;
            // 处理一下超过线程部分的分段
            if (overSize > 0) {
                List<Segment> lastSegments = handledSegments.get(maxThreads - 1);
                for (int i = maxThreads; i < handledSegments.size(); i++) {
                    List<Segment> seg = handledSegments.get(i);
                    if (seg == null) {
                        continue;
                    }
                    lastSegments.addAll(seg);
                }
                handledSegments = handledSegments.subList(0, maxThreads);
            }
            return handledSegments;
        } catch (Exception e) {
            throw new SegmentGroupException(e);
        }
    }

    // 保证segments中每个Segment的大小不大于threadMaxSize
    private List<Segment> resetSegmentByThreadMaxSize(List<Segment> segments, long threadMaxSize) {
        List<Segment> segmentsNew = new ArrayList<>();
        for (Segment segment : segments) {
            if (segment == null) {
                continue;
            }
            // 保证最大分片不大于threadMaxSize
            resetSegmentByThreadMaxSizeImpl(segmentsNew, segment, threadMaxSize);
        }
        return segmentsNew;
    }

    // 保证segments中每个Segment的大小不大于threadMaxSize
    private void resetSegmentByThreadMaxSizeImpl(List<Segment> segmentsNew, Segment segment, long threadMaxSize) {
        if (segment.size() <= threadMaxSize) {
            Segment p = new Segment(segment.start(), segment.end(), segment.data());
            segmentsNew.add(p);
        } else {
            Segment p = new Segment(segment.start(), segment.start() + threadMaxSize, segment.data());
            segmentsNew.add(p);
            // segment.start() + threadMaxSize  ~ end
            Segment p2 = new Segment(segment.start() + threadMaxSize, segment.end(), segment.data());
            resetSegmentByThreadMaxSizeImpl(segmentsNew, p2, threadMaxSize);
        }
    }

    // 保证segments中的分段能使最优线方式进行排列处理
    private List<List<Segment>> handleSegments(List<Segment> segments, long threadMaxSize) {

        log.debug("排序前，segments=" + segments);

        List<List<Segment>> handledSegmentsList = new ArrayList<>();
        // 降序排序，以便于后续方便处理
        Collections.sort(segments, new Comparator<Segment>() {
            @Override
            public int compare(Segment left, Segment right) {
                if (left.size() - right.size() > 0) {
                    return -1;
                } else if (left.size() - right.size() < 0) {
                    return 1;
                }
                return 0;
            }
        });

        log.debug("排序后，segments=" + segments);

        handleSegmentsImpl(handledSegmentsList, segments, threadMaxSize);
        return handledSegmentsList;
    }

    // 保证segments中的分段能使最优线方式进行排列处理
    private void handleSegmentsImpl(List<List<Segment>> handledSegmentsList, List<Segment> segments,
                                    long threadMaxSize) {
        List<Segment> everyThreadSegments = new ArrayList<>();
        long curSize = 0;// 当前线程需要处理的大小
        for (Segment segment : segments) {
            if (segment == null) {
                continue;
            }
            curSize += segment.size();
            if (curSize <= threadMaxSize) {
                everyThreadSegments.add(segment);
            } else {
                // 大于，则后面的都放到新task中
                break;
            }
        }
        handledSegmentsList.add(everyThreadSegments);// 当前线程可以需要处理的分段
        segments.removeAll(everyThreadSegments);// 移除当前线程处理的分段
        if (segments.size() > 0) {
            handleSegmentsImpl(handledSegmentsList, segments, threadMaxSize);
        }
    }
}