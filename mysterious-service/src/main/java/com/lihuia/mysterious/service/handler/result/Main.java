package com.lihuia.mysterious.service.handler.result;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;

import java.io.IOException;

/**
 * @author maple@lihuia.com
 * @date 2023/4/14 10:54 PM
 */
public class Main {

    public static void main(String[] args) throws IOException {
        TestResultHandler resultHandler = new TestResultHandler();
        DefaultExecutor executor = new DefaultExecutor();
        CommandLine commandLine = new CommandLine("ls");
        //commandLine.addArgument("/Users/lihui/.ssh");
        executor.execute(commandLine, resultHandler);
    }
}
