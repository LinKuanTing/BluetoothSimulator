package com.example.blesimulator;

import java.util.UUID;

public class define {
    public static final int UNIT_CELSIUS = 1;
    public static final int UNIT_FAHRENHEIT = 2;
    public static final int TYPE_EAR = 3;
    public static final int TYPE_FOREHEAD = 4;


    //service uuid
    /*Generic Access Service UUID*/
    public static final UUID GENERIC_ACCESS = UUID.fromString("00001800-0000-1000-8000-00805F9B34FB");
    /*Characteristic*/
    public static final UUID DEVICE_NAME = UUID.fromString("00002A00-0000-1000-8000-00805F9B34FB");
    public static final UUID APPEARANCE = UUID.fromString("00002A01-0000-1000-8000-00805F9B34FB");
    public static final UUID PERIPHERAL_PRIVACY_FLAG = UUID.fromString("00002A02-0000-1000-8000-00805F9B34FB");
    public static final UUID RECONNECTION_ADDRESS = UUID.fromString("00002A03-0000-1000-8000-00805F9B34FB");
    public static final UUID PPCP = UUID.fromString("00002A04-0000-1000-8000-00805F9B34FB");

    /*Generic Attribute Service uuid*/
    public static final UUID GENERIC_ATTRIBUTE = UUID.fromString("00001801-0000-1000-8000-00805F9B34FB");

    /*Device Information Service UUID*/
    public static final UUID DEVICE_INFORMATION = UUID.fromString("0000180A-0000-1000-8000-00805F9B34FB");
    /*Characteristic*/
    public static final UUID SYSTEM_ID = UUID.fromString("00002A23-0000-1000-8000-00805F9B34FB");
    public static final UUID MODEL_NUMBER_STRING = UUID.fromString("00002A24-0000-1000-8000-00805F9B34FB");
    public static final UUID SERIAL_NUMBER_STRING = UUID.fromString("00002A25-0000-1000-8000-00805F9B34FB");
    public static final UUID FIRMWARE_REVISION_STRING = UUID.fromString("00002A26-0000-1000-8000-00805F9B34FB");
    public static final UUID HARDWARE_REVISION_STRING = UUID.fromString("00002A27-0000-1000-8000-00805F9B34FB");
    public static final UUID SOFTWARE_REVISION_STRING = UUID.fromString("00002A28-0000-1000-8000-00805F9B34FB");
    public static final UUID MANUFACTURER_NAME_STRING = UUID.fromString("00002A29-0000-1000-8000-00805F9B34FB");
    public static final UUID IEEE_REGULATORY = UUID.fromString("00002A2A-0000-1000-8000-00805F9B34FB");
    public static final UUID PNP_ID = UUID.fromString("00002A50-0000-1000-8000-00805F9B34FB");

    /*Health Thermometer Service UUID*/
    public static final UUID HEALTH_THERMOMETER = UUID.fromString("00001809-0000-1000-8000-00805F9B34FB");
    /*Characteristic*/
    public static final UUID TEMPERATURE_MEASUREMENT = UUID.fromString("00002A1C-0000-1000-8000-00805F9B34FB");
    public static final UUID TEMPERATURE_TYPE = UUID.fromString("00002A1D-0000-1000-8000-00805F9B34FB");
    public static final UUID INTERMEDIATE_TEMPERATURE = UUID.fromString("00002A1E-0000-1000-8000-00805F9B34FB");
    public static final UUID MEASUREMENT_INTERVAL = UUID.fromString("00002A21-0000-1000-8000-00805F9B34FB");

    /*Battery Service UUID*/
    public static final UUID BATTERY_SERVICE = UUID.fromString("0000180F-0000-1000-8000-00805F9B34FB");
    /*Characteristic*/
    public static final UUID BATTERY_LEVEL = UUID.fromString("00002A19-0000-1000-8000-00805F9B34FB");


    /*Blood Pressure Service UUID*/
    public static final UUID BLOOD_PRESSURE = UUID.fromString("00001810-0000-1000-8000-00805F9B34FB");
    /*Characteristic*/
    public static final UUID BLOOD_PRESSURE_FEATURE = UUID.fromString("00002A49-0000-1000-8000-00805F9B34FB");
    public static final UUID BLOOD_PRESSURE_MEASUREMENT = UUID.fromString("00002A35-0000-1000-8000-00805F9B34FB");
    public static final UUID INTERMEDIATE_CUFF_PRESSURE = UUID.fromString("00002A36-0000-1000-8000-00805F9B34FB");

    /*Blood Glucose Service UUID*/
    public static final UUID uuidFFF0 = UUID.fromString("0000FFF0-0000-1000-8000-00805F9B34FB");
    /*Characteristic*/
    public static final UUID uuidFFF4 = UUID.fromString("0000FFF4-0000-1000-8000-00805F9B34FB");
    public static final UUID uuidFFF3 = UUID.fromString("0000FFF3-0000-1000-8000-00805F9B34FB");

    /*Weight Service UUID*/
    public static final UUID uuid78A2 = UUID.fromString("000078A2-0000-1000-8000-00805F9B34FB");
    /*Characteristic*/
    public static final UUID uuid8A21 = UUID.fromString("00008A21-0000-1000-8000-00805F9B34FB");
    public static final UUID uuid8A22 = UUID.fromString("00008A22-0000-1000-8000-00805F9B34FB");
    public static final UUID uuid8A20 = UUID.fromString("00008A20-0000-1000-8000-00805F9B34FB");
    public static final UUID uuid8A81 = UUID.fromString("00008A81-0000-1000-8000-00805F9B34FB");
    public static final UUID uuid8A82 = UUID.fromString("00008A82-0000-1000-8000-00805F9B34FB");



    /*ECG(Sympathetic Indexes) Service UUID*/
    //public static final UUID uuidFFF0 = UUID.fromString("0000FFF0-0000-1000-8000-00805F9B34FB");
    /*Characteristic*/
    //public static final UUID uuidFFF3 = UUID.fromString("0000FFF3-0000-1000-8000-00805F9B34FB");
    //public static final UUID uuidFFF4 = UUID.fromString("0000FFF4-0000-1000-8000-00805F9B34FB");
    public static final UUID uuidFFF5 = UUID.fromString("0000FFF5-0000-1000-8000-00805F9B34FB");

    /*Other*/
    /*Descriptor setting*/
    protected static final UUID CLIENT_CHARACTERISTIC_CONFIGURATION = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");


    /*HCARE paired */
    /*配對用 當HCARE找到此UUID 配對時 不用再去看device uuid*/
    /*原因: 每次開始藍牙 device uuid隨機產生無法固定 因此需重新配對*/
    public static final UUID uuid0000 = UUID.fromString("00000000-0000-1000-8000-00805F9B34FB");
    /*Characteristic*/
    public static final UUID uuid0003 = UUID.fromString("00000003-0000-1000-8000-00805F9B34FB");

}
