package com.haste.yzx.system.config;

import com.p6spy.engine.spy.appender.StdoutLogger;

public class MpLog extends StdoutLogger {
    public void logText(String text) {
        System.err.println(text);
    }
}
