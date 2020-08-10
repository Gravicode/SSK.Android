package org.apache.poi.hssf.usermodel;

import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

final class StaticFontMetrics {
    private static Map<String, FontDetails> fontDetailsMap = new HashMap();
    private static Properties fontMetricsProps;

    StaticFontMetrics() {
    }

    public static FontDetails getFontDetails(Font font) {
        InputStream metricsIn;
        if (fontMetricsProps == null) {
            String propFileName = null;
            try {
                fontMetricsProps = new Properties();
                try {
                    propFileName = System.getProperty("font.metrics.filename");
                } catch (SecurityException e) {
                }
                if (propFileName != null) {
                    File file = new File(propFileName);
                    if (!file.exists()) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("font_metrics.properties not found at path ");
                        sb.append(file.getAbsolutePath());
                        throw new FileNotFoundException(sb.toString());
                    }
                    metricsIn = new FileInputStream(file);
                } else {
                    metricsIn = FontDetails.class.getResourceAsStream("/font_metrics.properties");
                    if (metricsIn == null) {
                        throw new FileNotFoundException("font_metrics.properties not found in classpath");
                    }
                }
                fontMetricsProps.load(metricsIn);
                if (metricsIn != null) {
                    try {
                        metricsIn.close();
                    } catch (IOException e2) {
                    }
                }
            } catch (IOException e3) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Could not load font metrics: ");
                sb2.append(e3.getMessage());
                throw new RuntimeException(sb2.toString());
            } catch (Throwable th) {
                if (0 != 0) {
                    try {
                        null.close();
                    } catch (IOException e4) {
                    }
                }
                throw th;
            }
        }
        String fontName = font.getName();
        String fontStyle = "";
        if (font.isPlain()) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append(fontStyle);
            sb3.append("plain");
            fontStyle = sb3.toString();
        }
        if (font.isBold()) {
            StringBuilder sb4 = new StringBuilder();
            sb4.append(fontStyle);
            sb4.append("bold");
            fontStyle = sb4.toString();
        }
        if (font.isItalic()) {
            StringBuilder sb5 = new StringBuilder();
            sb5.append(fontStyle);
            sb5.append("italic");
            fontStyle = sb5.toString();
        }
        if (fontMetricsProps.get(FontDetails.buildFontHeightProperty(fontName)) == null) {
            Properties properties = fontMetricsProps;
            StringBuilder sb6 = new StringBuilder();
            sb6.append(fontName);
            sb6.append(".");
            sb6.append(fontStyle);
            if (properties.get(FontDetails.buildFontHeightProperty(sb6.toString())) != null) {
                StringBuilder sb7 = new StringBuilder();
                sb7.append(fontName);
                sb7.append(".");
                sb7.append(fontStyle);
                fontName = sb7.toString();
            }
        }
        if (fontDetailsMap.get(fontName) != null) {
            return (FontDetails) fontDetailsMap.get(fontName);
        }
        FontDetails fontDetails = FontDetails.create(fontName, fontMetricsProps);
        fontDetailsMap.put(fontName, fontDetails);
        return fontDetails;
    }
}
