package org.apache.poi.p009ss.usermodel;

/* renamed from: org.apache.poi.ss.usermodel.Comment */
public interface Comment {
    String getAuthor();

    int getColumn();

    int getRow();

    RichTextString getString();

    boolean isVisible();

    void setAuthor(String str);

    void setColumn(int i);

    void setRow(int i);

    void setString(RichTextString richTextString);

    void setVisible(boolean z);
}
