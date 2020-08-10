package org.apache.poi.p009ss.usermodel;

/* renamed from: org.apache.poi.ss.usermodel.Textbox */
public interface Textbox {
    public static final short OBJECT_TYPE_TEXT = 6;

    int getMarginBottom();

    int getMarginLeft();

    int getMarginRight();

    int getMarginTop();

    RichTextString getString();

    void setMarginBottom(int i);

    void setMarginLeft(int i);

    void setMarginRight(int i);

    void setMarginTop(int i);

    void setString(RichTextString richTextString);
}
