package com.balittanah.gravicode.pkdss;

import android.content.Context;

import com.balittanah.gravicode.pkdss.helpers.FileHelpers;
import com.balittanah.gravicode.pkdss.model.*;
import java.util.*;
import java.io.*;
import java.nio.file.*;

import java.util.Scanner; // Import the Scanner class to read text files

public class Resources {
    public static String PathToData = "src/main/assets/";
    public static String GetResources(Context context, String Name) {
        String data = "";
        switch (Name) {
            case "Data":
                data = FileHelpers.ReadFromAsset("Data.txt",context);//ReadFromFile(PathToData+"Data.txt");
                return data;
                //break;
            case "NPK":
                data = FileHelpers.ReadFromAsset("NPK.csv",context);//ReadFromFile(PathToData+"NPK.csv");
                return data;
            //break;
            case "DataRekomendasi":
                data = FileHelpers.ReadFromAsset("DataRekomendasi.txt",context);//ReadFromFile(PathToData+"DataRekomendasi.txt");
                return data;
            //break;
            case "Lokasi":
                data = FileHelpers.ReadFromAsset("Lokasi.txt",context);//ReadFromFile(PathToData+"Lokasi.txt");
                return data;
            //break;
            default:
                return null;

        }
    }
    public static String GetResources(String Name) {
        String data = "";
        switch (Name) {
            case "Data":
                data = ReadFromFile(PathToData+"Data.txt");
                return data;
            //break;
            case "NPK":
                data = ReadFromFile(PathToData+"NPK.csv");
                return data;
            //break;
            case "DataRekomendasi":
                data = ReadFromFile(PathToData+"DataRekomendasi.txt");
                return data;
            //break;
            case "Lokasi":
                data = ReadFromFile(PathToData+"Lokasi.txt");
                return data;
            //break;
            default:
                return null;

        }
    }
    public static String ReadFromFile(String FileName){
        String AllText = "";
        try {

            File myObj = new File(FileName);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                //System.out.println(data);
                AllText+=data+System.lineSeparator();
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return AllText;
    }

}
