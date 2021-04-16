package me.andy5.segment_forkjointask;

import java.util.Objects;

/**
 * 分段信息
 *
 * @author andy(Andy)
 * @datetime 2019-08-09 15:46 GMT+8
 * @email 411086563@qq.com
 */
public class Segment {

    // 分段的开始位置
    private long start;
    // 分段的结束位置
    private long end;
    // 本分段线程要处理的数据
    private Object data;
    // 是否取消了
    private boolean isCancelled = false;

    public Segment(long start, long end, Object data) {
        this.start = start;
        this.end = end;
        this.data = data;
    }

    Segment cancel() {
        isCancelled = true;
        return this;
    }

    /**
     * 是否取消了
     *
     * @return
     */
    public boolean isCancelled() {
        return isCancelled;
    }

    /**
     * 分段的大小
     */
    public long size() {
        return end - start;
    }

    /**
     * 分段的开始位置
     *
     * @return
     */
    public long start() {
        return start;
    }

    /**
     * 分段的结束位置
     */
    public long end() {
        return end;
    }

    /**
     * 携带的数据
     *
     * @return
     */
    public Object data() {
        return data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Segment segment = (Segment) o;
        return start == segment.start && end == segment.end && Objects.equals(data, segment.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end, data);
    }

    @Override
    public String toString() {
        return "Segment{" + "start=" + start + ", end=" + end  + ", size=" + size() + ", isCancelled=" + isCancelled + ", data=" + data + '}';
    }
}
