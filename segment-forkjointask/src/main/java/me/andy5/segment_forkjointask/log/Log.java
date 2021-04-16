package me.andy5.segment_forkjointask.log;

/**
 * 日志处理类
 * Log handling class
 *
 * @author andy(Andy)
 * @datetime 2018-08-27 16:46 GMT+8
 * @email 411086563@qq.com
 */
public class Log {

    /**
     * 获取写日志类
     * Get the write log class
     *
     * @param clazz
     * @return
     */
    public static final Log getLog(Class clazz) {
        Log log = new Log();
        if (clazz != null) {
            log.prefix = clazz.getName();
        }
        return log;
    }

    private String prefix = "";

    private Log() {
    }

    /**
     * 调试信息
     * The debug log
     *
     * @param msg
     */
    public void debug(String msg) {
        System.out.println(prefix + " - " + msg);
    }

    /**
     * 普通信息
     * The info log
     *
     * @param msg
     */
    public void info(String msg) {
        System.out.println(prefix + " - " + msg);
    }

    /**
     * 异常信息
     * The error log
     *
     * @param msg
     */
    public void error(String msg) {
        System.out.println(prefix + " - " + msg);
    }
}
