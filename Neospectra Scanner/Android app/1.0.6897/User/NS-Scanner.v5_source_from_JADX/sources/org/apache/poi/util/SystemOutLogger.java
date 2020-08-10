package org.apache.poi.util;

import java.io.PrintStream;

public class SystemOutLogger extends POILogger {
    private String _cat;

    public void initialize(String cat) {
        this._cat = cat;
    }

    public void log(int level, Object obj1) {
        log(level, obj1, null);
    }

    public void log(int level, Object obj1, Throwable exception) {
        if (check(level)) {
            PrintStream printStream = System.out;
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            sb.append(this._cat);
            sb.append("] ");
            sb.append(obj1);
            printStream.println(sb.toString());
            if (exception != null) {
                exception.printStackTrace(System.out);
            }
        }
    }

    public boolean check(int level) {
        int currentLevel;
        String str = "poi.log.level";
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(WARN);
            sb.append("");
            currentLevel = Integer.parseInt(System.getProperty(str, sb.toString()));
        } catch (SecurityException e) {
            currentLevel = POILogger.DEBUG;
        }
        if (level >= currentLevel) {
            return true;
        }
        return false;
    }
}
