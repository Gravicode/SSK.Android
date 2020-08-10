package org.apache.poi.p009ss.usermodel;

/* renamed from: org.apache.poi.ss.usermodel.Picture */
public interface Picture {
    ClientAnchor getPreferredSize();

    void resize();

    void resize(double d);
}
