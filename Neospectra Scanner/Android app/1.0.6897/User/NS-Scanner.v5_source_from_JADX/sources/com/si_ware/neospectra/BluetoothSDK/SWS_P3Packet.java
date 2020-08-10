package com.si_ware.neospectra.BluetoothSDK;

import android.support.annotation.NonNull;
import com.si_ware.neospectra.Global.GlobalVariables.apodization;
import com.si_ware.neospectra.Global.GlobalVariables.command;
import com.si_ware.neospectra.Global.GlobalVariables.opticalGain;
import com.si_ware.neospectra.Global.GlobalVariables.pointsCount;
import com.si_ware.neospectra.Global.GlobalVariables.runMode;
import com.si_ware.neospectra.Global.GlobalVariables.zeroPadding;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class SWS_P3Packet {
    private boolean InterpolationMode;
    private String SWS_P3Packet_Apodization;
    private String SWS_P3Packet_Command;
    private String SWS_P3Packet_OpticalGain;
    private String SWS_P3Packet_PointsCount;
    private String SWS_P3Packet_RunMode;
    private String SWS_P3Packet_ScanTime;
    private String SWS_P3Packet_ZeroPadding;
    private Map<String, Byte> apodizationMap;
    private Map<String, Byte> commandMap;
    private Map<String, Byte> opticalGainMap;
    private int packetSize;
    private byte[] packetStream;
    private Map<String, Byte> pointsCountMap;
    private Map<String, Byte> runModeMap;
    private Map<String, Byte> zeroPaddingMap;

    public SWS_P3Packet() {
        this.packetSize = 20;
        this.InterpolationMode = false;
        this.packetStream = new byte[this.packetSize];
        for (int i = 0; i < this.packetSize; i++) {
            this.packetStream[i] = 0;
        }
        this.commandMap = new HashMap();
        this.pointsCountMap = new HashMap();
        this.opticalGainMap = new HashMap();
        this.apodizationMap = new HashMap();
        this.zeroPaddingMap = new HashMap();
        this.runModeMap = new HashMap();
        initPacketMaps();
    }

    public SWS_P3Packet(int packetSize2) {
        this.packetSize = 20;
        this.InterpolationMode = false;
        this.packetStream = new byte[packetSize2];
        for (int i = 0; i < packetSize2; i++) {
            this.packetStream[i] = 0;
        }
    }

    /* access modifiers changed from: 0000 */
    public void initPacketMaps() {
        this.commandMap.put(command.Read_RunModule_ID.toString(), Byte.valueOf(1));
        this.commandMap.put(command.Run_PSD.toString(), Byte.valueOf(3));
        this.commandMap.put(command.Run_Background.toString(), Byte.valueOf(4));
        this.commandMap.put(command.Run_Absorbance.toString(), Byte.valueOf(5));
        this.commandMap.put(command.Run_GainAdjustment.toString(), Byte.valueOf(6));
        this.commandMap.put(command.Burn_Gain.toString(), Byte.valueOf(7));
        this.commandMap.put(command.Burn_Self.toString(), Byte.valueOf(8));
        this.commandMap.put(command.Burn_WLN.toString(), Byte.valueOf(9));
        this.commandMap.put(command.Run_SelfCorrection.toString(), Byte.valueOf(10));
        this.commandMap.put(command.Run_WavelengthCorrectionBackground.toString(), Byte.valueOf(11));
        this.commandMap.put(command.Run_WavelengthCorrection.toString(), Byte.valueOf(12));
        this.commandMap.put(command.Restore_Defaults.toString(), Byte.valueOf(13));
        this.commandMap.put(command.Set_Optical_Settings.toString(), Byte.valueOf(27));
        this.commandMap.put(command.Get_Calibration_Wells.toString(), Byte.valueOf(90));
        this.commandMap.put(command.Get_Calibration_Wells1.toString(), Byte.valueOf(91));
        this.pointsCountMap.put(pointsCount.disable.toString(), Byte.valueOf(0));
        this.pointsCountMap.put(pointsCount.points_65.toString(), Byte.valueOf(1));
        this.pointsCountMap.put(pointsCount.points_129.toString(), Byte.valueOf(2));
        this.pointsCountMap.put(pointsCount.points_257.toString(), Byte.valueOf(3));
        this.pointsCountMap.put(pointsCount.points_513.toString(), Byte.valueOf(4));
        this.pointsCountMap.put(pointsCount.points_1024.toString(), Byte.valueOf(5));
        this.pointsCountMap.put(pointsCount.points_2048.toString(), Byte.valueOf(6));
        this.pointsCountMap.put(pointsCount.points_4096.toString(), Byte.valueOf(7));
        this.opticalGainMap.put(opticalGain.UseSettingsSavedonDVK.toString(), Byte.valueOf(0));
        this.opticalGainMap.put(opticalGain.UseCalculatedSettings.toString(), Byte.valueOf(1));
        this.opticalGainMap.put(opticalGain.UseExternalSettings.toString(), Byte.valueOf(2));
        this.apodizationMap.put(apodization.Boxcar.toString(), Byte.valueOf(0));
        this.apodizationMap.put(apodization.Gaussian.toString(), Byte.valueOf(1));
        this.apodizationMap.put(apodization.HappGenzel.toString(), Byte.valueOf(2));
        this.apodizationMap.put(apodization.Lorenz.toString(), Byte.valueOf(3));
        this.zeroPaddingMap.put(zeroPadding.points_8k.toString(), Byte.valueOf(1));
        this.zeroPaddingMap.put(zeroPadding.points_16k.toString(), Byte.valueOf(2));
        this.zeroPaddingMap.put(zeroPadding.points_32k.toString(), Byte.valueOf(3));
        this.runModeMap.put(runMode.Single_Mode.toString(), Byte.valueOf(0));
        this.runModeMap.put(runMode.Continuous_Mode.toString(), Byte.valueOf(1));
    }

    public void preparePacketContent() {
        byte[] scanTimeArr = IntToThreeBytes(Integer.parseInt(this.SWS_P3Packet_ScanTime));
        boolean z = false;
        this.packetStream[0] = ((Byte) this.commandMap.get(this.SWS_P3Packet_Command)).byteValue();
        this.packetStream[1] = scanTimeArr[2];
        this.packetStream[2] = scanTimeArr[1];
        this.packetStream[3] = scanTimeArr[0];
        this.packetStream[4] = ((Byte) this.pointsCountMap.get(this.SWS_P3Packet_PointsCount)).byteValue();
        this.packetStream[5] = ((Byte) this.opticalGainMap.get(this.SWS_P3Packet_OpticalGain)).byteValue();
        this.packetStream[6] = ((Byte) this.apodizationMap.get(this.SWS_P3Packet_Apodization)).byteValue();
        this.packetStream[7] = ((Byte) this.zeroPaddingMap.get(this.SWS_P3Packet_ZeroPadding)).byteValue();
        this.packetStream[8] = ((Byte) this.runModeMap.get(this.SWS_P3Packet_RunMode)).byteValue();
        if (this.packetStream[4] != 0) {
            z = true;
        }
        this.InterpolationMode = z;
    }

    public void preparePacketContent(String command) {
        byte[] bytesToBeSent = hexStringToByteArray(command);
        for (int i = 0; i < bytesToBeSent.length; i++) {
            this.packetStream[i] = bytesToBeSent[i];
        }
    }

    public static byte[] hexStringToByteArray(String s) {
        String[] bytes = s.split(" ");
        byte[] b = new byte[bytes.length];
        for (int i = 0; i < b.length; i++) {
            b[i] = (byte) Integer.parseInt(bytes[i], 16);
        }
        return b;
    }

    @NonNull
    public String getPacketAsText() {
        new DecimalFormat("##");
        String packetText = "P:";
        for (int i = 0; i < 9; i++) {
            StringBuilder sb = new StringBuilder();
            sb.append(packetText);
            sb.append(" ");
            sb.append(String.format("%02X", new Object[]{Integer.valueOf(this.packetStream[i] & 255)}));
            packetText = sb.toString();
        }
        return packetText;
    }

    @NonNull
    public static byte[] IntToThreeBytes(int i) {
        return new byte[]{(byte) (i >> 16), (byte) (i >> 8), (byte) i};
    }

    public String getSWS_P3Packet_Command() {
        return this.SWS_P3Packet_Command;
    }

    public void setSWS_P3Packet_Command(String SWS_P3Packet_Command2) {
        this.SWS_P3Packet_Command = SWS_P3Packet_Command2;
    }

    public String getSWS_P3Packet_ScanTime() {
        return this.SWS_P3Packet_ScanTime;
    }

    public void setSWS_P3Packet_ScanTime(String SWS_P3Packet_ScanTime2) {
        this.SWS_P3Packet_ScanTime = SWS_P3Packet_ScanTime2;
    }

    public String getSWS_P3Packet_PointsCount() {
        return this.SWS_P3Packet_PointsCount;
    }

    public void setSWS_P3Packet_PointsCount(String SWS_P3Packet_PointsCount2) {
        this.SWS_P3Packet_PointsCount = SWS_P3Packet_PointsCount2;
    }

    public String getSWS_P3Packet_OpticalGain() {
        return this.SWS_P3Packet_OpticalGain;
    }

    public void setSWS_P3Packet_OpticalGain(String SWS_P3Packet_OpticalGain2) {
        this.SWS_P3Packet_OpticalGain = SWS_P3Packet_OpticalGain2;
    }

    public String getSWS_P3Packet_Apodization() {
        return this.SWS_P3Packet_Apodization;
    }

    public void setSWS_P3Packet_Apodization(String SWS_P3Packet_Apodization2) {
        this.SWS_P3Packet_Apodization = SWS_P3Packet_Apodization2;
    }

    public String getSWS_P3Packet_ZeroPadding() {
        return this.SWS_P3Packet_ZeroPadding;
    }

    public void setSWS_P3Packet_ZeroPadding(String SWS_P3Packet_ZeroPadding2) {
        this.SWS_P3Packet_ZeroPadding = SWS_P3Packet_ZeroPadding2;
    }

    public String getSWS_P3Packet_RunMode() {
        return this.SWS_P3Packet_RunMode;
    }

    public void setSWS_P3Packet_RunMode(String SWS_P3Packet_RunMode2) {
        this.SWS_P3Packet_RunMode = SWS_P3Packet_RunMode2;
    }

    public byte[] getPacketStream() {
        return this.packetStream;
    }

    public void setPacketStream(byte[] packetStream2) {
        System.arraycopy(packetStream2, 0, this.packetStream, 0, packetStream2.length);
    }

    public boolean isInterpolationMode() {
        return this.InterpolationMode;
    }

    public void setInterpolationMode(boolean interpolationMode) {
        this.InterpolationMode = interpolationMode;
    }
}
