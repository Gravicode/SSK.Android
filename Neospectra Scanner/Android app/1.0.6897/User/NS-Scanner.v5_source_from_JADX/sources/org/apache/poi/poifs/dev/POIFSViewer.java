package org.apache.poi.poifs.dev;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class POIFSViewer {
    public static void main(String[] args) {
        boolean z = true;
        if (args.length < 0) {
            System.err.println("Must specify at least one file to view");
            System.exit(1);
        }
        int j = 0;
        if (args.length <= 1) {
            z = false;
        }
        boolean printNames = z;
        while (true) {
            int j2 = j;
            if (j2 < args.length) {
                viewFile(args[j2], printNames);
                j = j2 + 1;
            } else {
                return;
            }
        }
    }

    private static void viewFile(String filename, boolean printName) {
        if (printName) {
            StringBuffer flowerbox = new StringBuffer();
            flowerbox.append(".");
            for (int j = 0; j < filename.length(); j++) {
                flowerbox.append("-");
            }
            flowerbox.append(".");
            System.out.println(flowerbox);
            PrintStream printStream = System.out;
            StringBuilder sb = new StringBuilder();
            sb.append("|");
            sb.append(filename);
            sb.append("|");
            printStream.println(sb.toString());
            System.out.println(flowerbox);
        }
        try {
            for (Object print : POIFSViewEngine.inspectViewable(new POIFSFileSystem(new FileInputStream(filename)), true, 0, "  ")) {
                System.out.print(print);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
