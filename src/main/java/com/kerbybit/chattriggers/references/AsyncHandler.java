package com.kerbybit.chattriggers.references;

import com.kerbybit.chattriggers.globalvars.global;
import com.kerbybit.chattriggers.objects.ArrayHandler;
import com.kerbybit.chattriggers.triggers.EventsHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AsyncHandler {
    private static HashMap<Integer, String> asyncsStatus = new HashMap<>();
    private static List<Integer> toRemove = new ArrayList<>();
    private static HashMap<Integer, Thread> threads = new HashMap<>();

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
                preloadAsyncStrings();
            }
        }

        if (global.Async_string.size() > 0 && global.waitEvents.size()==0 && global.asyncMap.size()==0 && asyncsStatus.size()==0) {
            global.Async_string.clear();
            ArrayHandler.jsonURL.clear();
        }
    }

    private static void runAsync(Integer asyncID) {
        final Integer asyncIDfin = asyncID;
        asyncsStatus.put(asyncID, "0");

        //preloadAsyncStrings();

        threads.put(asyncID, new Thread(() -> {
            try {
                EventsHandler.doEvents(global.asyncMap.get(asyncIDfin), null, true);
                asyncsStatus.put(asyncIDfin, "finished");
            } catch (Exception e) {
                BugTracker.show(e, "async");
                timeoutAsync(asyncIDfin);
            }
        }));
        threads.get(asyncID).start();
    }

    public static void preloadAsyncStrings() {
        for (List<String> string : global.backupUSR_strings) {
            global.Async_string.put(string.get(0), string.get(1));
        }

        for (List<String> string : global.backupTMP_strings) {
            global.Async_string.put(string.get(0), string.get(1));
        }
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

    public static void clearAsyncs() {
        for (Integer asyncID : threads.keySet()) {
            timeoutAsync(asyncID);
        }
        threads.clear();
        global.asyncMap.clear();
        asyncsStatus.clear();
        global.Async_string.clear();
        global.backupAsync_string.clear();
    }
}
