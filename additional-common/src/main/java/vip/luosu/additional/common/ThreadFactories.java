package vip.luosu.additional.common;

import java.util.concurrent.ThreadFactory;

public class ThreadFactories {

    public static ThreadFactory build(String namePrefix) {
        return new LucThreadFactory(namePrefix, false, Thread.NORM_PRIORITY);
    }

    public static ThreadFactory daemon(String namePrefix) {
        return new LucThreadFactory(namePrefix, true, Thread.NORM_PRIORITY);
    }

    public static ThreadFactory highPriority(String namePrefix) {
        return new LucThreadFactory(namePrefix, false, Thread.MAX_PRIORITY);
    }
}
