package com.lihuia.mysterious.service.handler.result;

import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.ExecuteException;

/**
 * @author maple@lihuia.com
 * @date 2023/4/14 10:08 PM
 */

public class TestResultHandler extends DefaultExecuteResultHandler {


    private int a;
    private int flag;

    @Override
    public void onProcessComplete(final int exitValue) {
        super.onProcessComplete(exitValue);
        a = 0;
        System.out.println("onProcessComplete, a=" + a);
        flag = 0;
        int i = 1 / 0;
        System.out.println("end");
    }

    @Override
    public void onProcessFailed(final ExecuteException e) {
        super.onProcessFailed(e);
        e.printStackTrace();
        a = 1;
        System.out.println("onProcessFailed, a=" + a);
    }
}
