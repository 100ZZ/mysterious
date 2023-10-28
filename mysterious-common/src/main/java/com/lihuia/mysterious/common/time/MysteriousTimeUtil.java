package com.lihuia.mysterious.common.time;

import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author maple@lihuia.com
 * @date 2023/4/10 5:01 PM
 */

@Component
public class MysteriousTimeUtil {


    public String getCurrentTime() {
        return new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss").format(new Date());
    }

    public String getCurrentTime(Long currentTime) {
        return new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss").format(new Date(currentTime));
    }

    public static void main(String[] args) {
        System.out.println(new MysteriousTimeUtil().getCurrentTime());
        System.out.println(new MysteriousTimeUtil().getCurrentTime(System.currentTimeMillis()));
    }
}
