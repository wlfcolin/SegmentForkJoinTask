package me.andy5.segment_forkjointask.util;

import java.util.List;

import me.andy5.segment_forkjointask.Segment;

/**
 * @author andy(Andy)
 * @datetime 2019-08-16 08:54 GMT+8
 * @email 411086563@qq.com
 */
public class SegmentUtil {

    /**
     * 获取分段总大小
     *
     * @param segments
     * @return
     */
    public static long getSegmentsSize(List<Segment> segments) {
        long size = 0;
        for (Segment segment : segments) {
            if (segment == null) {
                continue;
            }
            size += segment.size();
        }
        return size;
    }
}
