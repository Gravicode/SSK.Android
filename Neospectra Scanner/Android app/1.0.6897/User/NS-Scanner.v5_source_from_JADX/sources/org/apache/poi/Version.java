package org.apache.poi;

import java.io.PrintStream;

public class Version {
    private static final String RELEASE_DATE = "20101029";
    private static final String VERSION_STRING = "3.7";

    public static String getVersion() {
        return VERSION_STRING;
    }

    public static String getReleaseDate() {
        return RELEASE_DATE;
    }

    public static String getProduct() {
        return "POI";
    }

    public static String getImplementationLanguage() {
        return "Java";
    }

    public static void main(String[] args) {
        PrintStream printStream = System.out;
        StringBuilder sb = new StringBuilder();
        sb.append("Apache ");
        sb.append(getProduct());
        sb.append(" ");
        sb.append(getVersion());
        sb.append(" (");
        sb.append(getReleaseDate());
        sb.append(")");
        printStream.println(sb.toString());
    }
}
