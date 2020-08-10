package org.apache.poi.util;

import java.io.File;
import java.util.Random;

public final class TempFile {
    private static File dir;
    private static final Random rnd = new Random();

    public static File createTempFile(String prefix, String suffix) {
        if (dir == null) {
            dir = new File(System.getProperty("java.io.tmpdir"), "poifiles");
            dir.mkdir();
            if (System.getProperty("poi.keep.tmp.files") == null) {
                dir.deleteOnExit();
            }
        }
        File file = dir;
        StringBuilder sb = new StringBuilder();
        sb.append(prefix);
        sb.append(rnd.nextInt());
        sb.append(suffix);
        File newFile = new File(file, sb.toString());
        if (System.getProperty("poi.keep.tmp.files") == null) {
            newFile.deleteOnExit();
        }
        return newFile;
    }
}
