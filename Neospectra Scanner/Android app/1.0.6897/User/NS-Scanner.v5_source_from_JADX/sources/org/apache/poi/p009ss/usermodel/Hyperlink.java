package org.apache.poi.p009ss.usermodel;

/* renamed from: org.apache.poi.ss.usermodel.Hyperlink */
public interface Hyperlink extends org.apache.poi.common.usermodel.Hyperlink {
    int getFirstColumn();

    int getFirstRow();

    int getLastColumn();

    int getLastRow();

    void setFirstColumn(int i);

    void setFirstRow(int i);

    void setLastColumn(int i);

    void setLastRow(int i);
}
