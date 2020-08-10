package org.apache.poi.p009ss.usermodel;

/* renamed from: org.apache.poi.ss.usermodel.PictureData */
public interface PictureData {
    byte[] getData();

    String getMimeType();

    String suggestFileExtension();
}
