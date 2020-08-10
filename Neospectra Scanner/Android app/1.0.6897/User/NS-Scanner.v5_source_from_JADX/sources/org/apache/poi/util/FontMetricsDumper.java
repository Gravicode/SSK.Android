package org.apache.poi.util;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class FontMetricsDumper {
    public static void main(String[] args) throws IOException {
        char c;
        Properties props = new Properties();
        Font[] allFonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
        for (Font fontName : allFonts) {
            String fontName2 = fontName.getFontName();
            FontMetrics fontMetrics = Toolkit.getDefaultToolkit().getFontMetrics(new Font(fontName2, 1, 10));
            int fontHeight = fontMetrics.getHeight();
            StringBuilder sb = new StringBuilder();
            sb.append("font.");
            sb.append(fontName2);
            sb.append(".height");
            String sb2 = sb.toString();
            StringBuilder sb3 = new StringBuilder();
            sb3.append(fontHeight);
            sb3.append("");
            props.setProperty(sb2, sb3.toString());
            StringBuffer characters = new StringBuffer();
            char c2 = 'a';
            while (true) {
                c = 'z';
                if (c2 > 'z') {
                    break;
                }
                StringBuilder sb4 = new StringBuilder();
                sb4.append(c2);
                sb4.append(", ");
                characters.append(sb4.toString());
                c2 = (char) (c2 + 1);
            }
            for (char c3 = 'A'; c3 <= 'Z'; c3 = (char) (c3 + 1)) {
                StringBuilder sb5 = new StringBuilder();
                sb5.append(c3);
                sb5.append(", ");
                characters.append(sb5.toString());
            }
            char c4 = '0';
            for (char c5 = '0'; c5 <= '9'; c5 = (char) (c5 + 1)) {
                StringBuilder sb6 = new StringBuilder();
                sb6.append(c5);
                sb6.append(", ");
                characters.append(sb6.toString());
            }
            StringBuffer widths = new StringBuffer();
            char c6 = 'a';
            while (true) {
                char c7 = c6;
                if (c7 > c) {
                    break;
                }
                StringBuilder sb7 = new StringBuilder();
                sb7.append(fontMetrics.getWidths()[c7]);
                sb7.append(", ");
                widths.append(sb7.toString());
                c6 = (char) (c7 + 1);
                c = 'z';
            }
            char c8 = 'A';
            while (true) {
                char c9 = c8;
                if (c9 > 'Z') {
                    break;
                }
                StringBuilder sb8 = new StringBuilder();
                sb8.append(fontMetrics.getWidths()[c9]);
                sb8.append(", ");
                widths.append(sb8.toString());
                c8 = (char) (c9 + 1);
            }
            while (true) {
                char c10 = c4;
                if (c10 > '9') {
                    break;
                }
                StringBuilder sb9 = new StringBuilder();
                sb9.append(fontMetrics.getWidths()[c10]);
                sb9.append(", ");
                widths.append(sb9.toString());
                c4 = (char) (c10 + 1);
            }
            StringBuilder sb10 = new StringBuilder();
            sb10.append("font.");
            sb10.append(fontName2);
            sb10.append(".characters");
            props.setProperty(sb10.toString(), characters.toString());
            StringBuilder sb11 = new StringBuilder();
            sb11.append("font.");
            sb11.append(fontName2);
            sb11.append(".widths");
            props.setProperty(sb11.toString(), widths.toString());
        }
        FileOutputStream fileOut = new FileOutputStream("font_metrics.properties");
        try {
            props.store(fileOut, "Font Metrics");
            fileOut.close();
        } catch (Throwable th) {
            Throwable th2 = th;
            fileOut.close();
            throw th2;
        }
    }
}
