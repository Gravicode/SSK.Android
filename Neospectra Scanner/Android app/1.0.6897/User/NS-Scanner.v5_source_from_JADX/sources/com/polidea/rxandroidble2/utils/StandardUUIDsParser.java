package com.polidea.rxandroidble2.utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class StandardUUIDsParser {
    private static final Map<String, String> CHARACTERISTIC_UUIDS;
    private static final Map<String, String> DESCRIPTOR_UUIDS;
    private static final Map<String, String> SERVICE_UUIDS;

    static {
        Map<String, String> aMap = new HashMap<>();
        aMap.put("1811", "Alert Notification Service");
        aMap.put("180F", "Battery Service");
        aMap.put("1810", "Blood Pressure");
        aMap.put("181B", "Body Composition");
        aMap.put("181E", "Bond Management");
        aMap.put("181F", "Continuous Glucose Monitoring");
        aMap.put("1805", "Current Time Service");
        aMap.put("1818", "Cycling Power");
        aMap.put("1816", "Cycling Speed and Cadence");
        aMap.put("180A", "Device Information");
        aMap.put("181A", "Environmental Sensing");
        aMap.put("1800", "Generic Access");
        aMap.put("1801", "Generic Attribute");
        aMap.put("1808", "Glucose");
        aMap.put("1809", "Health Thermometer");
        aMap.put("180D", "Heart Rate");
        aMap.put("1812", "Human Interface Device");
        aMap.put("1802", "Immediate Alert");
        aMap.put("1803", "Link Loss");
        aMap.put("1819", "Location and Navigation");
        aMap.put("1820", "Internet Protocol Support");
        aMap.put("1807", "Next DST Change Service");
        aMap.put("180E", "Phone Alert Status Service");
        aMap.put("1806", "Reference Time Update Service");
        aMap.put("1814", "Running Speed and Cadence");
        aMap.put("1813", "Scan Parameters");
        aMap.put("1804", "Tx Power");
        aMap.put("181C", "User Data");
        aMap.put("181D", "Weight Scale");
        aMap.put("1815", "Automation IO");
        aMap.put("1802", "Immediate Alert Service 1.1");
        SERVICE_UUIDS = Collections.unmodifiableMap(aMap);
        Map<String, String> aMap2 = new HashMap<>();
        aMap2.put("2A7E", "Aerobic Heart Rate Lower Limit");
        aMap2.put("2A84", "Aerobic Heart Rate Upper Limit");
        aMap2.put("2A7F", "Aerobic Threshold");
        aMap2.put("2A80", "Age");
        aMap2.put("2A43", "Alert Category ID");
        aMap2.put("2A42", "Alert Category ID Bit Mask");
        aMap2.put("2A06", "Alert Level");
        aMap2.put("2A44", "Alert Notification Control Point");
        aMap2.put("2A3F", "Alert Status");
        aMap2.put("2A81", "Anaerobic Heart Rate Lower Limit");
        aMap2.put("2A82", "Anaerobic Heart Rate Upper Limit");
        aMap2.put("2A83", "Anaerobic Threshold");
        aMap2.put("2A73", "Apparent Wind Direction");
        aMap2.put("2A72", "Apparent Wind Speed");
        aMap2.put("2A01", "Appearance");
        aMap2.put("2AA3", "Barometric Pressure Trend");
        aMap2.put("2A19", "Battery Level");
        aMap2.put("2A49", "Blood Pressure Feature");
        aMap2.put("2A35", "Blood Pressure Measurement");
        aMap2.put("2A9B", "Body Composition Feature");
        aMap2.put("2A9C", "Body Composition Measurement");
        aMap2.put("2A38", "Body Sensor Location");
        aMap2.put("2AA4", "Bond Management Control Point");
        aMap2.put("2AA5", "Bond Management Feature");
        aMap2.put("2A22", "Boot Keyboard Input Report");
        aMap2.put("2A32", "Boot Keyboard Output Report");
        aMap2.put("2A33", "Boot Mouse Input Report");
        aMap2.put("2AA6", "Central Address Resolution");
        aMap2.put("2AA8", "CGM Feature");
        aMap2.put("2AA7", "CGM Measurement");
        aMap2.put("2AAB", "CGM Session Run Time");
        aMap2.put("2AAA", "CGM Session Start Time");
        aMap2.put("2AAC", "CGM Specific Ops Control Point");
        aMap2.put("2AA9", "CGM Status");
        aMap2.put("2A5C", "CSC Feature");
        aMap2.put("2A5B", "CSC Measurement");
        aMap2.put("2A2B", "Current Time");
        aMap2.put("2A66", "Cycling Power Control Point");
        aMap2.put("2A65", "Cycling Power Feature");
        aMap2.put("2A63", "Cycling Power Measurement");
        aMap2.put("2A64", "Cycling Power Vector");
        aMap2.put("2A99", "Database Change Increment");
        aMap2.put("2A85", "Date of Birth");
        aMap2.put("2A86", "Date of Threshold Assessment ");
        aMap2.put("2A08", "Date Time");
        aMap2.put("2A0A", "Day Date Time");
        aMap2.put("2A09", "Day of Week");
        aMap2.put("2A7D", "Descriptor Value Changed");
        aMap2.put("2A00", "Device Name");
        aMap2.put("2A7B", "Dew Point");
        aMap2.put("2A0D", "DST Offset");
        aMap2.put("2A6C", "Elevation");
        aMap2.put("2A87", "Email Address");
        aMap2.put("2A0C", "Exact Time 256");
        aMap2.put("2A88", "Fat Burn Heart Rate Lower Limit");
        aMap2.put("2A89", "Fat Burn Heart Rate Upper Limit");
        aMap2.put("2A26", "Firmware Revision String");
        aMap2.put("2A8A", "First Name");
        aMap2.put("2A8B", "Five Zone Heart Rate Limits");
        aMap2.put("2A8C", "Gender");
        aMap2.put("2A51", "Glucose Feature");
        aMap2.put("2A18", "Glucose Measurement");
        aMap2.put("2A34", "Glucose Measurement Context");
        aMap2.put("2A74", "Gust Factor");
        aMap2.put("2A27", "Hardware Revision String");
        aMap2.put("2A39", "Heart Rate Control Point");
        aMap2.put("2A8D", "Heart Rate Max");
        aMap2.put("2A37", "Heart Rate Measurement");
        aMap2.put("2A7A", "Heat Index");
        aMap2.put("2A8E", "Height");
        aMap2.put("2A4C", "HID Control Point");
        aMap2.put("2A4A", "HID Information");
        aMap2.put("2A8F", "Hip Circumference");
        aMap2.put("2A6F", "Humidity");
        aMap2.put("2A2A", "IEEE 11073-20601 Regulatory Certification Data List");
        aMap2.put("2A36", "Intermediate Cuff Pressure");
        aMap2.put("2A1E", "Intermediate Temperature");
        aMap2.put("2A77", "Irradiance");
        aMap2.put("2AA2", "Language");
        aMap2.put("2A90", "Last Name");
        aMap2.put("2A6B", "LN Control Point");
        aMap2.put("2A6A", "LN Feature");
        aMap2.put("2A0F", "Local Time Information");
        aMap2.put("2A67", "Location and Speed");
        aMap2.put("2A2C", "Magnetic Declination");
        aMap2.put("2AA0", "Magnetic Flux Density - 2D");
        aMap2.put("2AA1", "Magnetic Flux Density - 3D");
        aMap2.put("2A29", "Manufacturer Name String");
        aMap2.put("2A91", "Maximum Recommended Heart Rate");
        aMap2.put("2A21", "Measurement Interval");
        aMap2.put("2A24", "Model Number String");
        aMap2.put("2A68", "Navigation");
        aMap2.put("2A46", "New Alert");
        aMap2.put("2A04", "Peripheral Preferred Connection Parameters");
        aMap2.put("2A02", "Peripheral Privacy Flag");
        aMap2.put("2A50", "PnP ID");
        aMap2.put("2A75", "Pollen Concentration");
        aMap2.put("2A69", "Position Quality");
        aMap2.put("2A6D", "Pressure");
        aMap2.put("2A4E", "Protocol Mode");
        aMap2.put("2A78", "Rainfall");
        aMap2.put("2A03", "Reconnection Address");
        aMap2.put("2A52", "Record Access Control Point");
        aMap2.put("2A14", "Reference Time Information");
        aMap2.put("2A4D", "Report");
        aMap2.put("2A4B", "Report Map");
        aMap2.put("2A92", "Resting Heart Rate");
        aMap2.put("2A40", "Ringer Control Point");
        aMap2.put("2A41", "Ringer Setting");
        aMap2.put("2A54", "RSC Feature");
        aMap2.put("2A53", "RSC Measurement");
        aMap2.put("2A55", "SC Control Point");
        aMap2.put("2A4F", "Scan Interval Window");
        aMap2.put("2A31", "Scan Refresh");
        aMap2.put("2A5D", "Sensor Location");
        aMap2.put("2A25", "Serial Number String");
        aMap2.put("2A05", "Service Changed");
        aMap2.put("2A28", "Software Revision String");
        aMap2.put("2A93", "Sport Type for Aerobic and Anaerobic Thresholds");
        aMap2.put("2A47", "Supported New Alert Category");
        aMap2.put("2A48", "Supported Unread Alert Category");
        aMap2.put("2A23", "System ID");
        aMap2.put("2A6E", "Temperature");
        aMap2.put("2A1C", "Temperature Measurement");
        aMap2.put("2A1D", "Temperature Type");
        aMap2.put("2A94", "Three Zone Heart Rate Limits");
        aMap2.put("2A12", "Time Accuracy");
        aMap2.put("2A13", "Time Source");
        aMap2.put("2A16", "Time Update Control Point");
        aMap2.put("2A17", "Time Update State");
        aMap2.put("2A11", "Time with DST");
        aMap2.put("2A0E", "Time Zone");
        aMap2.put("2A71", "True Wind Direction");
        aMap2.put("2A70", "True Wind Speed");
        aMap2.put("2A95", "Two Zone Heart Rate Limit");
        aMap2.put("2A07", "Tx Power Level");
        aMap2.put("2A45", "Unread Alert Status");
        aMap2.put("2A9F", "User Control Point");
        aMap2.put("2A9A", "User Index");
        aMap2.put("2A76", "UV Index");
        aMap2.put("2A96", "VO2 Max");
        aMap2.put("2A97", "Waist Circumference");
        aMap2.put("2A98", "Weight");
        aMap2.put("2A9D", "Weight Measurement");
        aMap2.put("2A9E", "Weight Scale Feature");
        aMap2.put("2A79", "Wind Chill");
        aMap2.put("2A5A", "Aggregate");
        aMap2.put("2A58", "Analog");
        aMap2.put("2A56", "Digital");
        CHARACTERISTIC_UUIDS = Collections.unmodifiableMap(aMap2);
        Map<String, String> aMap3 = new HashMap<>();
        aMap3.put("2900", "Characteristic Extended Properties");
        aMap3.put("2901", "Characteristic User Description");
        aMap3.put("2902", "Client Characteristic Configuration");
        aMap3.put("2903", "Server Characteristic Configuration");
        aMap3.put("2904", "Characteristic Presentation Format");
        aMap3.put("2905", "Characteristic Aggregate Format");
        aMap3.put("2906", "Valid Range");
        aMap3.put("2907", "External Report Reference");
        aMap3.put("2908", "Report Reference");
        aMap3.put("290B", "Environmental Sensing Configuration");
        aMap3.put("290C", "Environmental Sensing Measurement");
        aMap3.put("290D", "Environmental Sensing Trigger Setting");
        aMap3.put("2909", "Number of Digitals");
        aMap3.put("290A", "Value Trigger Setting");
        aMap3.put("290E", "Time Trigger Setting");
        DESCRIPTOR_UUIDS = Collections.unmodifiableMap(aMap3);
    }

    private StandardUUIDsParser() {
    }

    public static String getServiceName(UUID uuid) {
        String uuid16bit = getStandardizedUUIDComponent(uuid);
        if (uuid16bit != null) {
            return (String) SERVICE_UUIDS.get(uuid16bit);
        }
        return null;
    }

    public static String getCharacteristicName(UUID uuid) {
        String uuid16bit = getStandardizedUUIDComponent(uuid);
        if (uuid16bit != null) {
            return (String) CHARACTERISTIC_UUIDS.get(uuid16bit);
        }
        return null;
    }

    public static String getDescriptorName(UUID uuid) {
        String uuid16bit = getStandardizedUUIDComponent(uuid);
        if (uuid16bit != null) {
            return (String) DESCRIPTOR_UUIDS.get(uuid16bit);
        }
        return null;
    }

    private static String getStandardizedUUIDComponent(UUID uuid) {
        String stringUUIDRepresentation = uuid.toString().toUpperCase();
        if (isStandardizedUUID(stringUUIDRepresentation)) {
            return stringUUIDRepresentation.substring(4, 8);
        }
        return null;
    }

    private static boolean isStandardizedUUID(String stringUUIDRepresentation) {
        return stringUUIDRepresentation.startsWith("0000") && stringUUIDRepresentation.endsWith("-0000-1000-8000-00805F9B34FB");
    }
}
