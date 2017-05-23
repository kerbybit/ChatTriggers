package com.kerbybit.chattriggers.references;

import com.kerbybit.chattriggers.globalvars.global;
import com.kerbybit.chattriggers.triggers.EventsHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AsyncHandler {
    private static HashMap<Integer, String> asyncsStatus = new HashMap<Integer, String>();
    private static List<Integer> toRemove = new ArrayList<Integer>();
    private static HashMap<Integer, Thread> threads = new HashMap<Integer, Thread>();

    public static void asyncTick() {
        trimAsyncs();

        for (Integer asyncID : global.asyncMap.keySet()) {
            if (asyncsStatus.containsKey(asyncID)) {
                if (asyncsStatus.get(asyncID).equals("not running")) {
                    runAsync(asyncID);
                } else if (asyncsStatus.get(asyncID).equals("finished")) {
                    toRemove.add(asyncID);
                } else {
                    updateAsyncTimer(asyncID);
                }
            } else {
                asyncsStatus.put(asyncID, "not running");
            }
        }
    }

    private static void runAsync(Integer asyncID) {
        final Integer asyncIDfin = asyncID;
        asyncsStatus.put(asyncID, "0");
        threads.put(asyncID, new Thread(new Runnable() {
            public void run() {
                EventsHandler.doEvents(global.asyncMap.get(asyncIDfin), null);
                asyncsStatus.put(asyncIDfin, "finished");
            }
        }));
        threads.get(asyncID).start();
    }

    private static void trimAsyncs() {
        for (Integer remove : toRemove) {
            asyncsStatus.remove(remove);
            global.asyncMap.remove(remove);
        }
    }

    private static void updateAsyncTimer(Integer asyncID) {
        try {
            int timer = Integer.parseInt(asyncsStatus.get(asyncID)) + 1;
            if (timer >= 100) {
                timeoutAsync(asyncID);
            }
            asyncsStatus.put(asyncID, timer+"");
        } catch (NumberFormatException exception) {
            // do nothing //
        }
    }

    private static void timeoutAsync(Integer asyncID) {
        threads.get(asyncID).interrupt();
    }
}
