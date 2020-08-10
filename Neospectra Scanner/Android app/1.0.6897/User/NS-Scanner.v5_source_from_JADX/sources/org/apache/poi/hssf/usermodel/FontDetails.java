package org.apache.poi.hssf.usermodel;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

public class FontDetails {
    private String _fontName;
    private int _height;
    private final Map<Character, Integer> charWidths = new HashMap();

    public FontDetails(String fontName, int height) {
        this._fontName = fontName;
        this._height = height;
    }

    public String getFontName() {
        return this._fontName;
    }

    public int getHeight() {
        return this._height;
    }

    public void addChar(char c, int width) {
        this.charWidths.put(new Character(c), Integer.valueOf(width));
    }

    public int getCharWidth(char c) {
        Integer widthInteger = (Integer) this.charWidths.get(new Character(c));
        if (widthInteger != null || c == 'W') {
            return widthInteger.intValue();
        }
        return getCharWidth('W');
    }

    public void addChars(char[] characters, int[] widths) {
        for (int i = 0; i < characters.length; i++) {
            this.charWidths.put(new Character(characters[i]), Integer.valueOf(widths[i]));
        }
    }

    protected static String buildFontHeightProperty(String fontName) {
        StringBuilder sb = new StringBuilder();
        sb.append("font.");
        sb.append(fontName);
        sb.append(".height");
        return sb.toString();
    }

    protected static String buildFontWidthsProperty(String fontName) {
        StringBuilder sb = new StringBuilder();
        sb.append("font.");
        sb.append(fontName);
        sb.append(".widths");
        return sb.toString();
    }

    protected static String buildFontCharactersProperty(String fontName) {
        StringBuilder sb = new StringBuilder();
        sb.append("font.");
        sb.append(fontName);
        sb.append(".characters");
        return sb.toString();
    }

    public static FontDetails create(String fontName, Properties fontMetricsProps) {
        String heightStr = fontMetricsProps.getProperty(buildFontHeightProperty(fontName));
        String widthsStr = fontMetricsProps.getProperty(buildFontWidthsProperty(fontName));
        String charactersStr = fontMetricsProps.getProperty(buildFontCharactersProperty(fontName));
        if (heightStr == null || widthsStr == null || charactersStr == null) {
            StringBuilder sb = new StringBuilder();
            sb.append("The supplied FontMetrics doesn't know about the font '");
            sb.append(fontName);
            sb.append("', so we can't use it. Please add it to your font metrics file (see StaticFontMetrics.getFontDetails");
            throw new IllegalArgumentException(sb.toString());
        }
        FontDetails d = new FontDetails(fontName, Integer.parseInt(heightStr));
        String[] charactersStrArray = split(charactersStr, ",", -1);
        String[] widthsStrArray = split(widthsStr, ",", -1);
        if (charactersStrArray.length != widthsStrArray.length) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Number of characters does not number of widths for font ");
            sb2.append(fontName);
            throw new RuntimeException(sb2.toString());
        }
        for (int i = 0; i < widthsStrArray.length; i++) {
            if (charactersStrArray[i].length() != 0) {
                d.addChar(charactersStrArray[i].charAt(0), Integer.parseInt(widthsStrArray[i]));
            }
        }
        return d;
    }

    public int getStringWidth(String str) {
        int width = 0;
        for (int i = 0; i < str.length(); i++) {
            width += getCharWidth(str.charAt(i));
        }
        return width;
    }

    private static String[] split(String text, String separator, int max) {
        StringTokenizer tok = new StringTokenizer(text, separator);
        int listSize = tok.countTokens();
        if (max != -1 && listSize > max) {
            listSize = max;
        }
        String[] list = new String[listSize];
        int i = 0;
        while (true) {
            if (!tok.hasMoreTokens()) {
                break;
            } else if (max == -1 || i != listSize - 1) {
                list[i] = tok.nextToken().trim();
                i++;
            } else {
                StringBuffer buf = new StringBuffer((text.length() * (listSize - i)) / listSize);
                while (tok.hasMoreTokens()) {
                    buf.append(tok.nextToken());
                    if (tok.hasMoreTokens()) {
                        buf.append(separator);
                    }
                }
                list[i] = buf.toString().trim();
            }
        }
        return list;
    }
}
