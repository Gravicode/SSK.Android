package org.apache.poi.poifs.dev;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Iterator;
import org.apache.poi.poifs.filesystem.DirectoryNode;
import org.apache.poi.poifs.filesystem.DocumentNode;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class POIFSLister {
    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.err.println("Must specify at least one file to view");
            System.exit(1);
        }
        boolean withSizes = false;
        for (int j = 0; j < args.length; j++) {
            if (args[j].equalsIgnoreCase("-size") || args[j].equalsIgnoreCase("-sizes")) {
                withSizes = true;
            } else {
                viewFile(args[j], withSizes);
            }
        }
    }

    public static void viewFile(String filename, boolean withSizes) throws IOException {
        displayDirectory(new POIFSFileSystem(new FileInputStream(filename)).getRoot(), "", withSizes);
    }

    public static void displayDirectory(DirectoryNode dir, String indent, boolean withSizes) {
        PrintStream printStream = System.out;
        StringBuilder sb = new StringBuilder();
        sb.append(indent);
        sb.append(dir.getName());
        sb.append(" -");
        printStream.println(sb.toString());
        StringBuilder sb2 = new StringBuilder();
        sb2.append(indent);
        sb2.append("  ");
        String newIndent = sb2.toString();
        boolean hadChildren = false;
        Iterator it = dir.getEntries();
        while (it.hasNext()) {
            hadChildren = true;
            Object entry = it.next();
            if (entry instanceof DirectoryNode) {
                displayDirectory((DirectoryNode) entry, newIndent, withSizes);
            } else {
                DocumentNode doc = (DocumentNode) entry;
                String name = doc.getName();
                String size = "";
                if (name.charAt(0) < 10) {
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("(0x0");
                    sb3.append(name.charAt(0));
                    sb3.append(")");
                    sb3.append(name.substring(1));
                    String altname = sb3.toString();
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append(name.substring(1));
                    sb4.append(" <");
                    sb4.append(altname);
                    sb4.append(">");
                    name = sb4.toString();
                }
                if (withSizes) {
                    StringBuilder sb5 = new StringBuilder();
                    sb5.append(" [");
                    sb5.append(doc.getSize());
                    sb5.append(" / 0x");
                    sb5.append(Integer.toHexString(doc.getSize()));
                    sb5.append("]");
                    size = sb5.toString();
                }
                PrintStream printStream2 = System.out;
                StringBuilder sb6 = new StringBuilder();
                sb6.append(newIndent);
                sb6.append(name);
                sb6.append(size);
                printStream2.println(sb6.toString());
            }
        }
        if (!hadChildren) {
            PrintStream printStream3 = System.out;
            StringBuilder sb7 = new StringBuilder();
            sb7.append(newIndent);
            sb7.append("(no children)");
            printStream3.println(sb7.toString());
        }
    }
}
