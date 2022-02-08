package com.jared;

import com.jared.app.Application;
import com.jared.app.TimestampWriter;

public class Main {
    


    public static void main(String[] args) {
        
        TimestampWriter.calcDaysBetweenMillis(1644213252572L - 86400000L, System.currentTimeMillis());
        System.out.println(System.currentTimeMillis() - (86400000L*18));
        
        Application.main(args);
    }
}

