package com.si_ware.neospectra.BluetoothSDK;

import android.support.annotation.NonNull;
import android.util.Log;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class SWS_P3PacketResponse {
    private double Xinit_quant = 0.0d;
    private double Xstep_quant = 0.0d;
    private double[] mConvertedDoubles;
    private boolean mFirstArray = true;
    private boolean mInterpolationFlag;
    private double[] mInterpretedPacketResponse;
    private int mOriginalDataLength;
    private byte[] mPacketResponse;
    private int mPacketSize = 20;
    private int mPacketsAdded = 0;
    private int mPacketsCount = 0;

    SWS_P3PacketResponse(int mNumberofRecPackets, int mNumberElements, boolean mInterpolation) {
        this.mPacketsCount = mNumberofRecPackets;
        this.mPacketResponse = new byte[(this.mPacketSize * mNumberofRecPackets)];
        this.mOriginalDataLength = mNumberElements;
        this.mInterpolationFlag = mInterpolation;
    }

    public int getmPacketsCount() {
        return this.mPacketsCount;
    }

    public void setmPacketsCount(int mPacketsCount2) {
        this.mPacketsCount = mPacketsCount2;
    }

    public byte[] getmPacketResponse() {
        return this.mPacketResponse;
    }

    public void setmPacketResponse(byte[] mPacketResponse2) {
        this.mPacketResponse = mPacketResponse2;
    }

    public double[] getmInterpretedPacketResponse() {
        return this.mInterpretedPacketResponse;
    }

    public void setmInterpretedPacketResponse(double[] mInterpretedPacketResponse2) {
        this.mInterpretedPacketResponse = mInterpretedPacketResponse2;
    }

    public void prepareArraySize() {
        this.mPacketResponse = new byte[(this.mPacketsCount * this.mPacketSize)];
    }

    public void addNewResponse(@NonNull byte[] mNewBytes) {
        if (mNewBytes.length == this.mPacketSize) {
            if (this.mFirstArray) {
                System.arraycopy(mNewBytes, 0, this.mPacketResponse, 0, this.mPacketSize);
                this.mFirstArray = false;
                this.mPacketsAdded = 0;
            } else {
                System.arraycopy(mNewBytes, 0, this.mPacketResponse, this.mPacketSize * this.mPacketsAdded, this.mPacketSize);
            }
            this.mPacketsAdded++;
            if (this.mPacketsAdded == this.mPacketsCount) {
                this.mFirstArray = true;
            }
        }
    }

    public void interpretByteStream() {
        if (this.mOriginalDataLength == 1) {
            this.mInterpretedPacketResponse = new double[1];
            this.mInterpretedPacketResponse[0] = 0.0d;
        } else if (this.mOriginalDataLength == 2) {
            this.mInterpretedPacketResponse = new double[1];
            this.mInterpretedPacketResponse[0] = (double) convertTwoBytesToShort();
        } else {
            convertByteArrayToDoubleArray();
            if (!this.mInterpolationFlag) {
                this.mInterpretedPacketResponse = this.mConvertedDoubles;
                return;
            }
            this.mInterpretedPacketResponse = new double[(this.mOriginalDataLength * 2)];
            getXValsInterpolationCase();
            int numPoints = this.mOriginalDataLength;
            System.arraycopy(this.mConvertedDoubles, 0, this.mInterpretedPacketResponse, 0, numPoints);
            this.mInterpretedPacketResponse[numPoints] = this.Xinit_quant;
            for (int i = numPoints + 1; i < numPoints * 2; i++) {
                this.mInterpretedPacketResponse[i] = this.mInterpretedPacketResponse[i - 1] + this.Xstep_quant;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Interpreted Packet Response Length = ");
            sb.append(this.mInterpretedPacketResponse.length);
            Log.i("Packet Response", sb.toString());
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Converted Packet Response Length = ");
            sb2.append(this.mConvertedDoubles.length);
            Log.i("Packet Response", sb2.toString());
        }
    }

    private void convertByteArrayToDoubleArray() {
        ByteBuffer bb = ByteBuffer.wrap(this.mPacketResponse);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        if (this.mInterpolationFlag) {
            this.mConvertedDoubles = new double[this.mOriginalDataLength];
        } else {
            this.mConvertedDoubles = new double[(this.mOriginalDataLength * 2)];
        }
        for (int i = 0; i < this.mConvertedDoubles.length; i++) {
            this.mConvertedDoubles[i] = bb.getDouble();
        }
    }

    public String convertByteArrayToString() {
        String delimiter = "";
        String packetText = "";
        for (byte b : this.mPacketResponse) {
            StringBuilder sb = new StringBuilder();
            sb.append(packetText);
            sb.append(delimiter);
            sb.append(String.format("%02X", new Object[]{Integer.valueOf(b & 255)}));
            packetText = sb.toString();
            delimiter = " ";
        }
        return packetText;
    }

    private short convertTwoBytesToShort() {
        ByteBuffer bb_temp = ByteBuffer.allocate(2);
        bb_temp.order(ByteOrder.LITTLE_ENDIAN);
        bb_temp.put(this.mPacketResponse[0]);
        bb_temp.put(this.mPacketResponse[1]);
        return bb_temp.getShort(0);
    }

    private void getXValsInterpolationCase() {
        int LogicalArrayEnd = (this.mOriginalDataLength + 2) * 8;
        byte[] tmp_init_arr = Arrays.copyOfRange(this.mPacketResponse, LogicalArrayEnd - 16, LogicalArrayEnd - 8);
        byte[] tmp_step_arr = Arrays.copyOfRange(this.mPacketResponse, LogicalArrayEnd - 8, LogicalArrayEnd);
        long XStep_tmp = (bytesToLong(tmp_step_arr) >> 3) * 10000;
        this.Xinit_quant = ((double) ((bytesToLong(tmp_init_arr) >> 3) * 10000)) / 1.073741824E9d;
        this.Xstep_quant = ((double) XStep_tmp) / 1.073741824E9d;
    }

    private long bytesToLong(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.put(bytes);
        buffer.flip();
        return buffer.getLong();
    }
}
