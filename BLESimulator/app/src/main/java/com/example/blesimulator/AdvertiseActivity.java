package com.example.blesimulator;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattServer;
import android.bluetooth.BluetoothGattServerCallback;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.ParcelUuid;
import android.util.Log;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.example.blesimulator.define.*;
import static android.content.Context.BLUETOOTH_SERVICE;
import static java.lang.Thread.sleep;

public class AdvertiseActivity {
    private static String strPeripheral = "";
    private String strData1, strData2, strData3;
    private static boolean devicePassword = false;
    private static boolean sampleRaw = false;



    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothManager mBluetoothManager;
    private BluetoothGattServer mBluetoothGattServer;
    private BluetoothLeAdvertiser mBluetoothLeAdvertiser;
    private Set<BluetoothDevice> mRegisteredDevices = new HashSet<>();



    public Context c;

    public AdvertiseActivity(Context context){
        this.c = context;

        //藍牙初始化
        mBluetoothManager = (BluetoothManager) c.getSystemService(BLUETOOTH_SERVICE);
        mBluetoothAdapter = mBluetoothManager.getAdapter();


    }

    private void resetDevicePassword(){
        devicePassword = false;
    }

    private void resetSampleRaw(){
        sampleRaw = false;
    }

    private void setBluetoothAdapterName(){
        String strExpectName = "";
        if (mBluetoothAdapter != null) {
            switch (strPeripheral){
                case "Temperature":
                    strExpectName = "GSH BH";
                    mBluetoothAdapter.setName(strExpectName);
                    break;
                case "Blood Pressure":
                     strExpectName = "0802A0";
                     mBluetoothAdapter.setName(strExpectName);
                     break;
                case "Blood Glucose":
                    strExpectName = "GSH-GL";
                    mBluetoothAdapter.setName(strExpectName);
                    break;
                case "Body Fat":
                     strExpectName = "0202B-0001";
                     mBluetoothAdapter.setName(strExpectName);
                     break;
                case "Sympathetic Indexes":
                    strExpectName = "GSH_ECG";
                    mBluetoothAdapter.setName(strExpectName);
                    break;
                default:
                    Log.w("TAG","Unknown peripheral device");
            }

            while (true){
                if (mBluetoothAdapter.getName().equalsIgnoreCase(strExpectName)){
                    break;
                }else {
                    try {
                        sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public boolean checkBluetoothName(){
        String strExpectName = "";
        switch (strPeripheral){
            case "Temperature":
                strExpectName = "GSH BH";
                break;
            case "Blood Pressure":
                strExpectName = "0802A0";
                break;
            case "Blood Glucose":
                strExpectName = "GSH-GL";
                break;
            case "Body Fat":
                strExpectName = "0202B-0001";
                break;
            case "Sympathetic Indexes":
                strExpectName = "GSH_ECG";
                break;
        }
        String strReadName = mBluetoothAdapter.getName();
        if (strExpectName.equalsIgnoreCase(strReadName)){
            return true;
        }else {
            return false;
        }
    }


    public void setStrPeripheral(String strPeripheral){
        this.strPeripheral = strPeripheral;
    }

    //1.1 檢查是否支援藍牙
    public boolean IsSupportBluetooth(){
        if (mBluetoothAdapter == null){
            return false; //裝置不支援藍牙
        }
        if (!c.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)){
            return false; //裝置不支援BLE
        }
        return true;
    }

    //1.2 檢查是否開始藍牙
    public boolean IsEnableBluetooth(){
        if (mBluetoothAdapter.enable()){
            return true;
        }else {
            return false;
        }
    }


    //2.創建GATT服務
    protected void createGattServer(){

        //2.1 創建Service
        //2.2 建立Characteristic (UUID,特徵屬性,權限屬性)
        //2.3 配置特徵描述(非必要 略)
        //2.4 將Characteristic配置Service

        BluetoothGattService deviceService = new BluetoothGattService(DEVICE_INFORMATION,BluetoothGattService.SERVICE_TYPE_PRIMARY);
        BluetoothGattCharacteristic systemID = new BluetoothGattCharacteristic(SYSTEM_ID,
                BluetoothGattCharacteristic.PROPERTY_READ | BluetoothGattCharacteristic.PROPERTY_NOTIFY,
                BluetoothGattCharacteristic.PERMISSION_READ);
        deviceService.addCharacteristic(systemID);


        BluetoothGattCharacteristic serialNumber = new BluetoothGattCharacteristic(SERIAL_NUMBER_STRING,
                BluetoothGattCharacteristic.PROPERTY_READ | BluetoothGattCharacteristic.PROPERTY_NOTIFY,
                BluetoothGattCharacteristic.PERMISSION_READ);
        deviceService.addCharacteristic(serialNumber);

        BluetoothGattCharacteristic hardwareRevision = new BluetoothGattCharacteristic(HARDWARE_REVISION_STRING,
                BluetoothGattCharacteristic.PROPERTY_READ | BluetoothGattCharacteristic.PROPERTY_NOTIFY,
                BluetoothGattCharacteristic.PERMISSION_READ);
        deviceService.addCharacteristic(hardwareRevision);

        BluetoothGattCharacteristic firmwareRevision = new BluetoothGattCharacteristic(FIRMWARE_REVISION_STRING,
                BluetoothGattCharacteristic.PROPERTY_READ | BluetoothGattCharacteristic.PROPERTY_NOTIFY,
                BluetoothGattCharacteristic.PERMISSION_READ);
        deviceService.addCharacteristic(firmwareRevision);

        BluetoothGattCharacteristic softwareRevision = new BluetoothGattCharacteristic(SOFTWARE_REVISION_STRING,
                BluetoothGattCharacteristic.PROPERTY_READ | BluetoothGattCharacteristic.PROPERTY_NOTIFY,
                BluetoothGattCharacteristic.PERMISSION_READ);
        deviceService.addCharacteristic(softwareRevision);

        BluetoothGattCharacteristic manufacturerName = new BluetoothGattCharacteristic(MANUFACTURER_NAME_STRING,
                BluetoothGattCharacteristic.PROPERTY_READ | BluetoothGattCharacteristic.PROPERTY_NOTIFY,
                BluetoothGattCharacteristic.PERMISSION_READ);
        deviceService.addCharacteristic(manufacturerName);



        //2.5 打開外圍設備
        if (mBluetoothManager != null){
            mBluetoothGattServer = mBluetoothManager.openGattServer(this.c, mGattServerCallback);;
        }


        //2.6 檢查Gatt順利加入Service
        mBluetoothGattServer.addService(deviceService);

    }


    //開啟藍牙廣播服務
    protected void startAdvertising(){
        setBluetoothAdapterName();

        mBluetoothLeAdvertiser= mBluetoothAdapter.getBluetoothLeAdvertiser();
        if (mBluetoothLeAdvertiser == null) {
            Log.w("TAG", "Failed to create advertiser");
            return;
        }

        AdvertiseSettings settings = new AdvertiseSettings.Builder()
                .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_BALANCED)
                .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_MEDIUM)
                .setTimeout(0)
                .setConnectable(true)
                .build();

        AdvertiseData data = null;
        switch (strPeripheral){
            case "Temperature":
                 data = new AdvertiseData.Builder()
                         .setIncludeDeviceName(true)
                         .setIncludeTxPowerLevel(false)
                         .addServiceUuid(new ParcelUuid(HEALTH_THERMOMETER))
                         .addServiceUuid(new ParcelUuid(uuid0000))
                         .build();
                break;
            case "Blood Pressure":
                data = new AdvertiseData.Builder()
                        .setIncludeDeviceName(true)
                        .setIncludeTxPowerLevel(false)
                        .addServiceUuid(new ParcelUuid(BLOOD_PRESSURE))
                        .addServiceUuid(new ParcelUuid(uuid0000))
                        .build();
                break;
            case "Blood Glucose":
                data = new AdvertiseData.Builder()
                        .setIncludeDeviceName(true)
                        .setIncludeTxPowerLevel(false)
                        .addServiceUuid(new ParcelUuid(uuidFFF0))
                        .addServiceUuid(new ParcelUuid(uuid0000))
                        .build();
                break;
            case "Sympathetic Indexes":
                byte[] ManufacturerData = new byte[]{(byte) 0xb0,0x20,0x18, (byte) 0x8c, (byte) 0xf5, (byte) 0xd1};
                data = new AdvertiseData.Builder()
                        .setIncludeDeviceName(true)
                        .setIncludeTxPowerLevel(false)
                        .addServiceUuid(new ParcelUuid(DEVICE_INFORMATION))
                        .addServiceUuid(new ParcelUuid(uuid0000))
                        .addManufacturerData(0x59, ManufacturerData)
                        .build();
                break;
            case "Body Fat":
                data = new AdvertiseData.Builder()
                        .setIncludeDeviceName(true)
                        .setIncludeTxPowerLevel(false)
                        .addServiceUuid(new ParcelUuid(uuid78A2))
                        .addServiceUuid(new ParcelUuid(uuid0000))
                        .build();
                break;

            default:
                Log.w("TAG","Unknown peripheral device");
        }

        if (data == null){
            mBluetoothLeAdvertiser = null;
            return;
        }
        mBluetoothLeAdvertiser.startAdvertising(settings, data, mAdvertiseCallback);


    }


    /**
     * Stop Bluetooth advertisements.
     * Shut down the GATT server.
     */
    public boolean checkServerStatus(){
        if (mBluetoothGattServer != null){
            return true;
        }else {
            return false;
        }
    }

    public boolean checkAdvertiser(){
        if (mBluetoothLeAdvertiser != null){
            return true;
        }
        return false;
    }


    public void stopServer() {
        if (checkAdvertiser()) {
            mBluetoothLeAdvertiser.stopAdvertising(mAdvertiseCallback);
            mBluetoothLeAdvertiser = null;
        }
        if(checkServerStatus()) {
            mBluetoothGattServer.close();
            mBluetoothGattServer = null;
        }

        resetDevicePassword();
        resetSampleRaw();

    }

    public void stopAdvertiser(){
        if (checkAdvertiser()) {
            mBluetoothLeAdvertiser.stopAdvertising(mAdvertiseCallback);
            mBluetoothLeAdvertiser = null;
        }
    }



    /**
     * Callback handles events from the framework describing
     * if we were successful in starting the advertisement requests.
     */
    private AdvertiseCallback mAdvertiseCallback = new AdvertiseCallback() {
        @Override
        public void onStartSuccess(AdvertiseSettings settingsInEffect) {
            super.onStartSuccess(settingsInEffect);
            Log.i("TAG", "LE Advertise Started.");

        }
        @Override
        public void onStartFailure(int errorCode) {
            super.onStartFailure(errorCode);
            Log.w("TAG", "LE Advertise Failed: "+errorCode);

        }
    };


    /**
     * CallBack to handle incoming request to the GATT server.
     * Handle all characteristics and descriptors' read and write request
     */

    private BluetoothGattServerCallback mGattServerCallback = new BluetoothGattServerCallback() {
        //設備連接/斷開連接回調
        @Override
        public void onConnectionStateChange(BluetoothDevice device, int status, int newState) {
            super.onConnectionStateChange(device, status, newState);
            Log.i("TAG", "ConnectionState: " + status );
            switch (newState){
                case BluetoothProfile.STATE_CONNECTED:
                    Log.i("TAG","GattCallBack: " + "STATE_CONNECTED");
                    mRegisteredDevices.add(device);
                    break;
                case BluetoothProfile.STATE_DISCONNECTED:
                    Log.w("TAG","GattCallBack: " + "STATE_DISCONNECTED");
                    mRegisteredDevices.remove(device);
                    break;
                default:
                    Log.w("TAG","GattCallBack: " + "STATE_OTHER");
            }

        }
        //添加本地服務回調
        @Override
        public void onServiceAdded(int status, BluetoothGattService service) {
            super.onServiceAdded(status, service);
            Log.i("TAG", "service: " + service.getUuid());
            if (service.getUuid() == DEVICE_INFORMATION){
                BluetoothGattService uuidPaired = new BluetoothGattService(uuid0000, BluetoothGattService.SERVICE_TYPE_PRIMARY);
                BluetoothGattCharacteristic uuidPairedUsed = new BluetoothGattCharacteristic(uuid0003,
                        BluetoothGattCharacteristic.PROPERTY_READ | BluetoothGattCharacteristic.PROPERTY_NOTIFY,
                        BluetoothGattCharacteristic.PERMISSION_READ);
                uuidPaired.addCharacteristic(uuidPairedUsed);

                mBluetoothGattServer.addService(uuidPaired);
            }

            if (service.getUuid() == uuid0000) {

                switch (strPeripheral){
                    case "Temperature":
                        BluetoothGattService temp = new BluetoothGattService(HEALTH_THERMOMETER,BluetoothGattService.SERVICE_TYPE_PRIMARY);

                        BluetoothGattCharacteristic temperatureMeasurement = new BluetoothGattCharacteristic(TEMPERATURE_MEASUREMENT,
                                BluetoothGattCharacteristic.PROPERTY_NOTIFY,
                                BluetoothGattCharacteristic.PERMISSION_READ);
                        BluetoothGattDescriptor tempDescriptor = new BluetoothGattDescriptor(CLIENT_CHARACTERISTIC_CONFIGURATION,
                                BluetoothGattDescriptor.PERMISSION_READ | BluetoothGattDescriptor.PERMISSION_WRITE);
                        temperatureMeasurement.addDescriptor(tempDescriptor);
                        temp.addCharacteristic(temperatureMeasurement);

                        BluetoothGattCharacteristic temperatureType = new BluetoothGattCharacteristic(TEMPERATURE_TYPE,
                                BluetoothGattCharacteristic.PROPERTY_READ,
                                BluetoothGattCharacteristic.PERMISSION_READ);
                        temp.addCharacteristic(temperatureType);

                        BluetoothGattCharacteristic intermediateTemp = new BluetoothGattCharacteristic(INTERMEDIATE_TEMPERATURE,
                                BluetoothGattCharacteristic.PROPERTY_NOTIFY,
                                BluetoothGattCharacteristic.PERMISSION_READ);
                        temp.addCharacteristic(intermediateTemp);

                        BluetoothGattCharacteristic measurementInterval = new BluetoothGattCharacteristic(MEASUREMENT_INTERVAL,
                                BluetoothGattCharacteristic.PROPERTY_READ | BluetoothGattCharacteristic.PROPERTY_WRITE | BluetoothGattCharacteristic.PROPERTY_INDICATE,
                                BluetoothGattCharacteristic.PERMISSION_READ);
                        temp.addCharacteristic(measurementInterval);

                        mBluetoothGattServer.addService(temp);
                        break;
                    case "Blood Pressure":
                        BluetoothGattService bp = new BluetoothGattService(BLOOD_PRESSURE,BluetoothGattService.SERVICE_TYPE_PRIMARY);

                        BluetoothGattCharacteristic bpFeature = new BluetoothGattCharacteristic(BLOOD_PRESSURE_FEATURE,
                                BluetoothGattCharacteristic.PROPERTY_READ,
                                BluetoothGattCharacteristic.PERMISSION_READ);
                        bp.addCharacteristic(bpFeature);

                        BluetoothGattCharacteristic bpMeasurement = new BluetoothGattCharacteristic(BLOOD_PRESSURE_MEASUREMENT,
                                BluetoothGattCharacteristic.PROPERTY_NOTIFY,
                                BluetoothGattCharacteristic.PERMISSION_READ);
                        BluetoothGattDescriptor bpDescriptor1 = new BluetoothGattDescriptor(CLIENT_CHARACTERISTIC_CONFIGURATION,
                                BluetoothGattDescriptor.PERMISSION_READ | BluetoothGattDescriptor.PERMISSION_WRITE);
                        bpMeasurement.addDescriptor(bpDescriptor1);
                        bp.addCharacteristic(bpMeasurement);

                        BluetoothGattCharacteristic intermediateCuffPressure = new BluetoothGattCharacteristic(INTERMEDIATE_CUFF_PRESSURE,
                                BluetoothGattCharacteristic.PROPERTY_NOTIFY,
                                BluetoothGattCharacteristic.PERMISSION_READ);
                        BluetoothGattDescriptor bpDescriptor2 = new BluetoothGattDescriptor(CLIENT_CHARACTERISTIC_CONFIGURATION,
                                BluetoothGattDescriptor.PERMISSION_READ | BluetoothGattDescriptor.PERMISSION_WRITE);
                        intermediateCuffPressure.addDescriptor(bpDescriptor2);
                        bp.addCharacteristic(intermediateCuffPressure);

                        mBluetoothGattServer.addService(bp);
                        break;
                    case "Blood Glucose":
                        BluetoothGattService bg = new BluetoothGattService(uuidFFF0,BluetoothGattService.SERVICE_TYPE_PRIMARY);

                        BluetoothGattCharacteristic bgUuidFFF4 = new BluetoothGattCharacteristic(uuidFFF4,
                                BluetoothGattCharacteristic.PROPERTY_NOTIFY,
                                BluetoothGattCharacteristic.PERMISSION_READ);
                        BluetoothGattDescriptor bgDescriptor = new BluetoothGattDescriptor(CLIENT_CHARACTERISTIC_CONFIGURATION,
                                BluetoothGattDescriptor.PERMISSION_READ | BluetoothGattDescriptor.PERMISSION_WRITE);
                        bgUuidFFF4.addDescriptor(bgDescriptor);
                        bg.addCharacteristic(bgUuidFFF4);

                        BluetoothGattCharacteristic bgUuidFFF3 = new BluetoothGattCharacteristic(uuidFFF3,
                                BluetoothGattCharacteristic.PROPERTY_NOTIFY,
                                BluetoothGattCharacteristic.PERMISSION_READ);
                        bg.addCharacteristic(bgUuidFFF3);

                        mBluetoothGattServer.addService(bg);
                        break;
                    case "Body Fat":
                        BluetoothGattService bf = new BluetoothGattService(uuid78A2,BluetoothGattService.SERVICE_TYPE_PRIMARY);

                        BluetoothGattCharacteristic bfUuid8A21 = new BluetoothGattCharacteristic(uuid8A21,
                                BluetoothGattCharacteristic.PROPERTY_NOTIFY,
                                BluetoothGattCharacteristic.PERMISSION_READ);
                        BluetoothGattDescriptor bfDescriptor = new BluetoothGattDescriptor(CLIENT_CHARACTERISTIC_CONFIGURATION,
                                BluetoothGattDescriptor.PERMISSION_READ | BluetoothGattDescriptor.PERMISSION_WRITE);
                        bfUuid8A21.addDescriptor(bfDescriptor);
                        bf.addCharacteristic(bfUuid8A21);

                        BluetoothGattCharacteristic bfUuid8A22 = new BluetoothGattCharacteristic(uuid8A22,
                                BluetoothGattCharacteristic.PROPERTY_INDICATE,
                                BluetoothGattCharacteristic.PERMISSION_READ);
                        bf.addCharacteristic(bfUuid8A22);

                        BluetoothGattCharacteristic bfUuid8A20 = new BluetoothGattCharacteristic(uuid8A20,
                                BluetoothGattCharacteristic.PROPERTY_READ,
                                BluetoothGattCharacteristic.PERMISSION_READ);
                        bf.addCharacteristic(bfUuid8A20);

                        BluetoothGattCharacteristic bfUuid8A81 = new BluetoothGattCharacteristic(uuid8A81,
                                BluetoothGattCharacteristic.PROPERTY_WRITE,
                                BluetoothGattCharacteristic.PERMISSION_READ);
                        bf.addCharacteristic(bfUuid8A81);

                        BluetoothGattCharacteristic bfUuid8A82 = new BluetoothGattCharacteristic(uuid8A82,
                                BluetoothGattCharacteristic.PROPERTY_INDICATE,
                                BluetoothGattCharacteristic.PERMISSION_READ);
                        bf.addCharacteristic(bfUuid8A82);

                        mBluetoothGattServer.addService(bf);
                        break;
                    case "Sympathetic Indexes":
                        BluetoothGattService ecg = new BluetoothGattService(uuidFFF0,BluetoothGattService.SERVICE_TYPE_PRIMARY);

                        BluetoothGattCharacteristic ecgUuidFFF3 = new BluetoothGattCharacteristic(uuidFFF3,
                                BluetoothGattCharacteristic.PROPERTY_NOTIFY,
                                BluetoothGattCharacteristic.PERMISSION_READ);
                        BluetoothGattDescriptor ecgDescriptor = new BluetoothGattDescriptor(CLIENT_CHARACTERISTIC_CONFIGURATION,
                                BluetoothGattDescriptor.PERMISSION_READ | BluetoothGattDescriptor.PERMISSION_WRITE);
                        ecgUuidFFF3.addDescriptor(ecgDescriptor);
                        ecg.addCharacteristic(ecgUuidFFF3);

                        BluetoothGattCharacteristic ecgUuidFFF4 = new BluetoothGattCharacteristic(uuidFFF4,
                                BluetoothGattCharacteristic.PROPERTY_WRITE | BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE,
                                BluetoothGattCharacteristic.PERMISSION_WRITE);
                        ecg.addCharacteristic(ecgUuidFFF4);

                        BluetoothGattCharacteristic ecgUuidFFF5 = new BluetoothGattCharacteristic(uuidFFF5,
                                BluetoothGattCharacteristic.PROPERTY_NOTIFY,
                                BluetoothGattCharacteristic.PERMISSION_READ);
                        BluetoothGattDescriptor ecgFFF5Descriptor = new BluetoothGattDescriptor(CLIENT_CHARACTERISTIC_CONFIGURATION,
                                BluetoothGattDescriptor.PERMISSION_READ | BluetoothGattDescriptor.PERMISSION_WRITE);
                        ecgUuidFFF5.addDescriptor(ecgFFF5Descriptor);

                        ecg.addCharacteristic(ecgUuidFFF5);

                        mBluetoothGattServer.addService(ecg);
                        break;
                    default:
                        Log.w("TAG","Unknown peripheral device");
                }

            }

        }
        //特徵值讀取回調
        @Override
        public void onCharacteristicReadRequest(BluetoothDevice device, int requestId, int offset, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicReadRequest(device,requestId,offset,characteristic);

            Log.i("TAG","characteristic uuid:" + characteristic.getUuid().toString());
            byte[] brSystemID = null;
            String strSerialNumber = "", strHardwareRevision = "", strFirmwareRevision = "", strSoftwareRevision = "", strManufactureName = "";


            switch (strPeripheral){
                case "Temperature":
                    brSystemID = new byte[]{0x7A, 0x05, 0x1A, 0x42, 0x48, (byte) 0xCF, (byte) 0xE5, (byte) 0xE0};
                    strSerialNumber = "00000001";
                    strHardwareRevision = "P133210A";
                    strFirmwareRevision = "16101301";
                    strSoftwareRevision = "530T0140";
                    strManufactureName = "Bough Tech";
                    break;
                case "Blood Pressure":
                    strSerialNumber = "FFFFFFFFFFFF";
                    strHardwareRevision = "B2";
                    strFirmwareRevision = "1.2";
                    strSoftwareRevision = "STD_BLP01";
                    strManufactureName = "GSH";
                    break;
                case "Blood Glucose":
                    strSerialNumber = "08038538AFF4";
                    strHardwareRevision = "GSH_HW_0_1";
                    strFirmwareRevision = "GSH_FW_0_5";
                    strSoftwareRevision = "GSH_SW_0_1";
                    strManufactureName = "GSH";
                    break;
                case "Body Fat":
                    strSerialNumber = "93EA4F8CECE6";
                    strHardwareRevision = "B2";
                    strFirmwareRevision = "B.9";
                    strSoftwareRevision = "BLE_BF";
                    strManufactureName = "GSH_TW";
                    break;
                case "Sympathetic Indexes":
                    strSerialNumber = "B020188CF5D1";
                    strHardwareRevision = "GSH_ECG_KO3_HW_V100";
                    strFirmwareRevision = "GSH_ECG_KO3_FW_V118";
                    strSoftwareRevision = "GSH_ECG_KO3_SW_V100";
                    strManufactureName = "GSH";
                    break;
                default:
                    Log.w("TAG","Unknown peripheral device");
            }

            /*Device information service characteristics*/
            if (characteristic.getUuid().equals(SYSTEM_ID)){
                mBluetoothGattServer.sendResponse(device,requestId,BluetoothGatt.GATT_SUCCESS,0,brSystemID);
            }
            if (characteristic.getUuid().equals(SERIAL_NUMBER_STRING)){
                mBluetoothGattServer.sendResponse(device,requestId,BluetoothGatt.GATT_SUCCESS,0,strSerialNumber.getBytes());
            }
            if (characteristic.getUuid().equals(HARDWARE_REVISION_STRING)){
                mBluetoothGattServer.sendResponse(device,requestId,BluetoothGatt.GATT_SUCCESS,0,strHardwareRevision.getBytes());
            }
            if (characteristic.getUuid().equals(FIRMWARE_REVISION_STRING)){
                mBluetoothGattServer.sendResponse(device,requestId,BluetoothGatt.GATT_SUCCESS,0,strFirmwareRevision.getBytes());
            }
            if (characteristic.getUuid().equals(SOFTWARE_REVISION_STRING)){
                mBluetoothGattServer.sendResponse(device,requestId,BluetoothGatt.GATT_SUCCESS,0,strSoftwareRevision.getBytes());
            }
            if (characteristic.getUuid().equals(MANUFACTURER_NAME_STRING)){
                mBluetoothGattServer.sendResponse(device,requestId,BluetoothGatt.GATT_SUCCESS,0,strManufactureName.getBytes());
            }

            /*Health thermometer service characteristic*/
            if (characteristic.getUuid().equals(TEMPERATURE_MEASUREMENT)){
                mBluetoothGattServer.sendResponse(device,requestId,BluetoothGatt.GATT_SUCCESS,0,null);
            }
            if (characteristic.getUuid().equals(TEMPERATURE_TYPE)){
                mBluetoothGattServer.sendResponse(device,requestId,BluetoothGatt.GATT_SUCCESS,0,null);
            }
            if (characteristic.getUuid().equals(INTERMEDIATE_TEMPERATURE)){
                mBluetoothGattServer.sendResponse(device,requestId,BluetoothGatt.GATT_SUCCESS,0,null);
            }
            if (characteristic.getUuid().equals(MEASUREMENT_INTERVAL)){
                mBluetoothGattServer.sendResponse(device,requestId,BluetoothGatt.GATT_SUCCESS,0,null);
            }

            /*Blood Pressure service characteristic*/
            if (characteristic.getUuid().equals(BLOOD_PRESSURE_FEATURE)){
                mBluetoothGattServer.sendResponse(device,requestId,BluetoothGatt.GATT_SUCCESS,0,null);
            }
            if (characteristic.getUuid().equals(BLOOD_PRESSURE_MEASUREMENT)){
                mBluetoothGattServer.sendResponse(device,requestId,BluetoothGatt.GATT_SUCCESS,0,null);
            }
            if (characteristic.getUuid().equals(INTERMEDIATE_CUFF_PRESSURE)){
                mBluetoothGattServer.sendResponse(device,requestId,BluetoothGatt.GATT_SUCCESS,0,null);
            }

            /*Blood Glucose and ECG service characteristic*/
            if (characteristic.getUuid().equals(uuidFFF4)){
                mBluetoothGattServer.sendResponse(device,requestId,BluetoothGatt.GATT_SUCCESS,0,null);
                if (strPeripheral == "Sympathetic Indexes"){
                    byte[] bytes = new byte[]{(byte) 0xFC, (byte) 0xFF, 0x10};
                    mBluetoothGattServer.sendResponse(device,requestId,BluetoothGatt.GATT_SUCCESS,0,bytes);
                }
            }

            if (characteristic.getUuid().equals(uuidFFF3)){
                mBluetoothGattServer.sendResponse(device,requestId,BluetoothGatt.GATT_SUCCESS,0,null);
            }
            if (characteristic.getUuid().equals(uuidFFF5)){
                mBluetoothGattServer.sendResponse(device,requestId,BluetoothGatt.GATT_SUCCESS,0,null);
            }

            /*Weight service characteristic*/
            if (characteristic.getUuid().equals(uuid8A21)){
                mBluetoothGattServer.sendResponse(device,requestId,BluetoothGatt.GATT_SUCCESS,0,null);
            }
            if (characteristic.getUuid().equals(uuid8A22)){
                mBluetoothGattServer.sendResponse(device,requestId,BluetoothGatt.GATT_SUCCESS,0,null);
            }
            if (characteristic.getUuid().equals(uuid8A20)){
                mBluetoothGattServer.sendResponse(device,requestId,BluetoothGatt.GATT_SUCCESS,0,null);
            }
            if (characteristic.getUuid().equals(uuid8A81)){
                if (strPeripheral == "Body Fat"){
                    byte[] bytes = new byte[]{(byte) 0x02};
                    mBluetoothGattServer.sendResponse(device,requestId,BluetoothGatt.GATT_SUCCESS,0,bytes);
                }
            }
            if (characteristic.getUuid().equals(uuid8A82)){
                mBluetoothGattServer.sendResponse(device,requestId,BluetoothGatt.GATT_SUCCESS,0,null);
            }

            mBluetoothGattServer.sendResponse(device, requestId,
                    BluetoothGatt.GATT_FAILURE, 0, null);
        }
        //特徵值寫入回調
        @Override
        public void onCharacteristicWriteRequest(BluetoothDevice device, int requestId, BluetoothGattCharacteristic characteristic, boolean preparedWrite, boolean responseNeeded, int offset, byte[] value) {
            super.onCharacteristicWriteRequest(device, requestId, characteristic, preparedWrite, responseNeeded, offset, value);
            /*身心指標連接時 需寫入FFF4某特定值 表示正確連接且等待peripheral中FFF5回傳確認資料*/
            Log.i("TAG" ,"onCharacteristicWriteRequest: "+characteristic.getUuid() +" ,value: " + byteArrayToHexStr(value));
            if (characteristic.getUuid().equals(uuidFFF4)){
                if (byteArrayToHexStr(value).equals("FCFF10") || byteArrayToHexStr(value).equals("F18008")){
                    sampleRaw = true;
                }
                else {
                    devicePassword = true;
                }
            }
            if (characteristic.getUuid().equals(uuid8A81)){

                mBluetoothGattServer.sendResponse(device, requestId,
                        BluetoothGatt.GATT_SUCCESS, 0, null);
            }


        }
        //描述讀取回調
        @Override
        public void onDescriptorReadRequest(BluetoothDevice device, int requestId, int offset, BluetoothGattDescriptor descriptor) {
            super.onDescriptorReadRequest(device, requestId, offset, descriptor);
            if (CLIENT_CHARACTERISTIC_CONFIGURATION.equals(descriptor.getUuid())){
                Log.i("TAG","Get DescriptorRead");

                byte[] returnValue;
                if (mRegisteredDevices.contains(device)){
                    returnValue = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE;
                }else {
                    returnValue = BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE;
                }
                mBluetoothGattServer.sendResponse(device, requestId,
                        BluetoothGatt.GATT_SUCCESS, 0, returnValue);
            }else {
                Log.w("TAG","Unknown DescriptorRead Request");
                mBluetoothGattServer.sendResponse(device, requestId,
                        BluetoothGatt.GATT_FAILURE, 0, null);
            }

        }
        //描述寫入回調
        @Override
        public void onDescriptorWriteRequest(BluetoothDevice device, int requestId, BluetoothGattDescriptor descriptor, boolean preparedWrite, boolean responseNeeded, int offset, byte[] value) {
            super.onDescriptorWriteRequest(device, requestId, descriptor, preparedWrite, responseNeeded, offset, value);
            if (CLIENT_CHARACTERISTIC_CONFIGURATION.equals(descriptor.getUuid())) {
                if (Arrays.equals(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE,value)){
                    Log.i("TAG","SubScribe device to notifications: " + device);
                    mRegisteredDevices.add(device);
                }else if (Arrays.equals(BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE,value)){
                    Log.i("TAG","Unsubscribe device from notifications: " + device);
                    mRegisteredDevices.remove(device);
                }

                if (responseNeeded){
                    mBluetoothGattServer.sendResponse(device,requestId,
                            BluetoothGatt.GATT_SUCCESS,0,null);
                }
            }else {
                Log.w("TAG","Unknown DescriptorWrite Request");
                if (responseNeeded) {
                    mBluetoothGattServer.sendResponse(device,requestId,
                            BluetoothGatt.GATT_FAILURE,0,null);
                }
            }
        }

    };



    public void pairedSympatheticIndexesData(){


        if (devicePassword == true || sampleRaw == true){
            return;
        }


        final BluetoothGattCharacteristic newCharacteristic = mBluetoothGattServer
                .getService(uuidFFF0)
                .getCharacteristic(uuidFFF5);

        byte[] data = new byte[]{(byte) 0xcc,0x4f,0x4b};

        newCharacteristic.setValue(data);
        for (BluetoothDevice device : mRegisteredDevices) {
            mBluetoothGattServer.notifyCharacteristicChanged(device, newCharacteristic, false);
        }

 /*
        final BluetoothGattCharacteristic newCharacteristic = mBluetoothGattServer
                .getService(uuidFFF0)
                .getCharacteristic(uuidFFF3);
        byte[] data = new byte[]{(byte) 0x9A, (byte) 0x80, 0x1F, 0x1E, (byte) 0x24, 0x26, 0x21, 0x1E, 0x23, 0x26, 0x22, 0x1D, (byte) 0x21, (byte) 0x26, 0x23, 0x1E, (byte) 0x20, 0x25, 0x24, 0x1F};
        newCharacteristic.setValue(data);

        for (BluetoothDevice device : mRegisteredDevices) {
            mBluetoothGattServer.notifyCharacteristicChanged(device, newCharacteristic, false);
        }
*/
    }


    public void updateCharacteristicsData(){
        if (mBluetoothGattServer == null){
            return;
        }

        final Handler updateHandle = new Handler();
        Runnable runnable = new Runnable() {
            int time = 0;
            @Override
            public void run() {
                for (final BluetoothDevice device : mRegisteredDevices){
                    BluetoothGattCharacteristic newCharacteristic = null;
                    byte[] data = null;

                    try{
                        switch (strPeripheral){
                            case "Temperature":
                                newCharacteristic = mBluetoothGattServer
                                        .getService(HEALTH_THERMOMETER)
                                        .getCharacteristic(TEMPERATURE_MEASUREMENT);

                                data = new byte[]{0x06, (byte) 0x00, 0x00, 0x00, (byte) 0xFE, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x02};
                                //[0]->Unit(06/07)('C/'F)  [1][2]->溫度(顛倒) EX:0x74,0x0E = 0x0E74 = 3700
                                //[3]->0  [4]->0xFE = -2(精度) =10^-2    因此溫度=3700/10^-2 = 37.0
                                //[11]->count  [12]->09耳溫 02額溫

                                //處理量測單位
                                if (strData1 == "Celsius"){
                                    data[0] = 0x06;
                                }else if (strData1 == "Fahrenheit"){
                                    data[0] = 0x07;
                                }

                                //處理量測種類
                                if (strData2 == "Ear"){
                                    data[12] = 0x09;
                                }else if (strData2 == "Forehead"){
                                    data[12] = 0x02;
                                }

                                //處理量測數值
                                int temp = (int) (Double.parseDouble(strData3) * 100);
                                String tempToHex = Integer.toHexString(temp); //e74
                                tempToHex = ("0000" + tempToHex).substring(tempToHex.length()); // 0e74
                                byte[] newTemp = hexToBytes(tempToHex);
                                data[1] = newTemp[1];
                                data[2] = newTemp[0];

                                //count處理

                                if (time == 255){
                                    data[11] = 0x00;
                                    time = 0;
                                }else {
                                    data[11]+=time;
                                }
                                break;
                            case "Blood Pressure":
                                newCharacteristic = mBluetoothGattServer
                                        .getService(BLOOD_PRESSURE)
                                        .getCharacteristic(BLOOD_PRESSURE_MEASUREMENT);

                                data = new byte[]{0x3E, 0x00, 0x00, 0x00, 0x00, 0x59, 0x00, (byte) 0xDA, 0x07, 0x01, 0x10, 0x15, 0x05, 0x10, 0x00, 0x00, 0x00, 0x00, 0x00};
                                //                [00]  [01]  [02]  [03]  [04]  [05]  [06]         [07]  [08]  [09]  [10]  [11]  [12]  [13]  [14]  [15]  [16]  [17]  [18]
                                //[1]->SYS [3]->DIA [14]->Pulse

                                String sys = Integer.toHexString(Integer.parseInt(strData1));
                                sys = ("0000" + sys).substring(sys.length()); //0064
                                byte[] b = hexToBytes(sys); // b[0]->00 b[1]->64
                                data[1] = b[1];
                                data[2] = b[0];

                                String dia = Integer.toHexString(Integer.parseInt(strData2));
                                dia = ("0000" + dia).substring(dia.length());
                                b = hexToBytes(dia);
                                data[3] = b[1];
                                data[4] = b[0];

                                String pulsse = Integer.toHexString(Integer.parseInt(strData3));
                                pulsse = ("0000" + pulsse).substring(pulsse.length());
                                b = hexToBytes(pulsse);
                                data[14] = b[1];
                                data[15] = b[0];


                                break;
                            case "Blood Glucose":
                                newCharacteristic = mBluetoothGattServer
                                        .getService(uuidFFF0)
                                        .getCharacteristic(uuidFFF4);

                                data = new byte[]{0x00, 0x00, 0x00, 0x00, 0x00};
                                //[1][2]->血糖 EX:0064 = 100 mg/dl, 014F = 335 mg/dl

                                int bg = Integer.parseInt(strData1);
                                String bgToHex = Integer.toHexString(bg);
                                bgToHex = ("0000" + bgToHex).substring(bgToHex.length());
                                byte[] newBg = hexToBytes(bgToHex);

                                data[1] = newBg[0];
                                data[2] = newBg[1];
                                System.out.println(Arrays.toString(data));
                                break;
                            case "Body Fat":
                                newCharacteristic = mBluetoothGattServer
                                        .getService(uuid78A2)
                                        .getCharacteristic(uuid8A21);

                                data = new byte[]{(byte) 0x9F, (byte) 0x00, 0x00, 0x00, (byte) 0xFE, 0x06, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, (byte) 0xFF, (byte) 0x00, 0x00, 0x00, (byte) 0xFF, 0x00, 0x09, 0x00};
                                //                       [00]         [01]  [02]  [03]         [04]  [05]  [06]  [07]  [08]  [09]  [10]  [11]         [12]         [13]  [14]  [15]         [16]  [17]  [18]  [19]
                                //[1][2]->體重(顛倒) [3]->0 [4]->-2
                                //[13][14]->電阻(顛倒)(正常範圍:800>X>300) [15]->0 [16]->-2

                                int bf = (int) (Double.parseDouble(strData1)*100);
                                String bfToHex = Integer.toHexString(bf);
                                bfToHex = ("0000" + bfToHex).substring(bfToHex.length());
                                byte[] newBf = hexToBytes(bfToHex);
                                data[1] = newBf[1];
                                data[2] = newBf[0];

                                int resistance = Integer.parseInt(strData2)*10;
                                String bfrToHex = Integer.toHexString(resistance);
                                bfrToHex = ("0000" + bfrToHex).substring(bfrToHex.length());
                                byte[] newBfr = hexToBytes(bfrToHex);
                                data[13] = newBfr[1];
                                data[14] = newBfr[0];


                                break;
                            case "Sympathetic Indexes":

                                newCharacteristic = mBluetoothGattServer
                                        .getService(uuidFFF0)
                                        .getCharacteristic(uuidFFF3);
                                data = new byte[]{(byte) 0x9A, (byte) 0x80, 0x1F, 0x1E, (byte) 0x24, 0x26, 0x21, 0x1E, 0x23, 0x26, 0x22, 0x1D, (byte) 0x21, (byte) 0x26, 0x23, 0x1E, (byte) 0x20, 0x25, 0x24, 0x1F};
                                //                       [00]         [01]  [02]  [03]         [04]  [05]  [06]  [07]  [08]  [09]  [10]  [11]         [12]         [13]  [14]  [15]         [16]  [17]  [18]  [19]


                                if (isGetDevicePassword() || isGetSampleRaw()){
                                    pairedSympatheticIndexesData();
                                }

                                break;
                            default:
                                Log.w("TAG","Unknown peripheral device");
                        }

                        /*10秒後廣播*/
/*
                        final BluetoothGattCharacteristic finalNewCharacteristic = newCharacteristic;
                        final byte[] finalData = data;
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    if (finalNewCharacteristic != null){
                                        finalNewCharacteristic.setValue(finalData);
                                        mBluetoothGattServer.notifyCharacteristicChanged(device, finalNewCharacteristic, false);

                                    }else {
                                        Log.w("TAG","Can't keep notifying characteristic data");
                                        return;
                                    }
                                }catch (Exception e){

                                }

                            }

                            },5000);
*/



                        if (newCharacteristic != null){
                            newCharacteristic.setValue(data);
                            mBluetoothGattServer.notifyCharacteristicChanged(device, newCharacteristic, false);

                        }else {
                            Log.w("TAG","Can't keep notifying characteristic data");
                            return;
                        }
                        /**/


                    }catch (Exception e){
                        Log.d("TAG", "Stop to notify characteristic data");
                        return;
                    }

                }
                time++;
                if (strPeripheral == "Sympathetic Indexes" && isGetSampleRaw()){
                    updateHandle.postDelayed(this,2);
                }else {
                    updateHandle.postDelayed(this,5000);

                }
            }
        };
        updateHandle.postDelayed(runnable,1000);



    }

    public void getEditTextData(String strData1, String strData2, String strData3){
        this.strData1 = strData1;
        this.strData2 = strData2;
        this.strData3 = strData3;
    }

    public boolean isGetDevicePassword(){
        return devicePassword;
    }

    public boolean isGetSampleRaw(){
        return sampleRaw;
    }

    private static String byteArrayToHexStr(byte[] byteArray) {
        if (byteArray == null) {
            return null;
        }
        StringBuilder hex = new StringBuilder(byteArray.length * 2);
        for (byte aData : byteArray) {
            hex.append(String.format("%02X", aData));
        }
        String gethex = hex.toString();
        return gethex;

    }

    public static byte[] hexToBytes(String hexString) {

        char[] hex = hexString.toCharArray();
        //轉rawData長度減半
        int length = hex.length / 2;
        byte[] rawData = new byte[length];
        for (int i = 0; i < length; i++) {
            //先將hex資料轉10進位數值
            int high = Character.digit(hex[i * 2], 16);
            int low = Character.digit(hex[i * 2 + 1], 16);
            //將第一個值的二進位值左平移4位,ex: 00001000 => 10000000 (8=>128)
            //然後與第二個值的二進位值作聯集ex: 10000000 | 00001100 => 10001100 (137)
            int value = (high << 4) | low;
            //與FFFFFFFF作補集
            if (value > 127)
                value -= 256;
            //最後轉回byte就OK
            rawData [i] = (byte) value;
        }
        return rawData ;
    }

}
