package com.wen.test;

import com.wen.tools.log.utils.LogUtil;

public class Log2 {
    public static void main(String[] args) {
        LogUtil.getCoreLog().info("hahahahahhah");
        LogUtil.getCoreLog().error("hahahahahhah");
        throw new RuntimeException();
    }
}
