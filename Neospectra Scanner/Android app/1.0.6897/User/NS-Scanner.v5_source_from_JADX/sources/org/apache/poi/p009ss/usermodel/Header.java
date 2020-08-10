package org.apache.poi.p009ss.usermodel;

/* renamed from: org.apache.poi.ss.usermodel.Header */
public interface Header extends HeaderFooter {
    String getCenter();

    String getLeft();

    String getRight();

    void setCenter(String str);

    void setLeft(String str);

    void setRight(String str);
}
