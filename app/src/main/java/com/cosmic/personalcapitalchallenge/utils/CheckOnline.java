package com.cosmic.personalcapitalchallenge.utils;

import java.io.IOException;

/**
 * Created by anushree on 19/10/2017.
 * Util class to check if device is online or not
 */

public class CheckOnline {

    public static boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }
        return false;
    }
}
