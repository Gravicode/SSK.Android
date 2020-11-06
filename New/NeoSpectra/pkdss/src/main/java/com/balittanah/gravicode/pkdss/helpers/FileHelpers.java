package com.balittanah.gravicode.pkdss.helpers;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class FileHelpers {

public static String ReadFromAsset(String AssetName, Context appContext) {


    String vals = "";
    try{
    InputStream inputStream = appContext.getAssets().open(AssetName);

    StringBuilder textBuilder = new StringBuilder();
    Reader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName(StandardCharsets.UTF_8.name())));
        int c = 0;
        while ((c = reader.read()) != -1) {
            textBuilder.append((char) c);
        }
        vals = textBuilder.toString();
    } catch (IOException e) {
        e.printStackTrace();
    }
    return vals;
}
    public static String readFile(String filename) throws IOException {
        String content = null;
        File file = new File(filename); // For example, foo.txt
        FileReader reader = null;
        try {
            reader = new FileReader(file);
            char[] chars = new char[(int) file.length()];
            reader.read(chars);
            content = new String(chars);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        return content;
    }
}
