package com.example.blesimulator;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.blesimulator.define.*;
import static java.lang.Thread.sleep;


public class WorkAsPeripheralPage extends Activity {
    private Spinner spnPeripheral;
    private TextView tvData1, tvData2, tvData3, tvData4, tvData5, tvData6;
    private RadioGroup radGroupUnit, radGroupType;
    private EditText edText1, edText2, edText3;

    private int intUnit, intType;
    private String strTextValue1,strTextValue2,strTextValue3;

    private Button btnGattServer,btnAdvertise,btnCloseServer;
    AdvertiseActivity advertiseActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.work_as_peripheral);

        //藍牙開啟
        advertiseActivity = new AdvertiseActivity(WorkAsPeripheralPage.this);
        if (advertiseActivity.IsSupportBluetooth()){
            if (!advertiseActivity.IsEnableBluetooth()){
                Intent bleEnable = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(bleEnable,100);
            }
        }

        //取得元件
        btnGattServer = (Button) findViewById(R.id.button1);
        btnAdvertise = (Button) findViewById(R.id.button2);
        btnCloseServer = (Button) findViewById(R.id.button3);
        tvData1 = findViewById(R.id.textView6);
        tvData2 = findViewById(R.id.textView7);
        tvData3 = findViewById(R.id.textView8);
        tvData4 = findViewById(R.id.textView9);
        tvData5 = findViewById(R.id.textView10);
        tvData6 = findViewById(R.id.textView11);
        edText1 = findViewById(R.id.editText1);
        edText2 = findViewById(R.id.editText2);
        edText3 = findViewById(R.id.editText3);

        //按鈕監聽
        btnGattServer.setOnClickListener(btnListener);
        btnAdvertise.setOnClickListener(btnListener);
        btnCloseServer.setOnClickListener(btnListener);

        //spinner 宣告
        setPeripheralSpinner();
        spnPeripheral.setOnItemSelectedListener(spnPeripheralListener);
    }

    protected void onDestroy() {
        if (advertiseActivity.checkServerStatus()){
            Log.i("TAG","Stop Server.");
            advertiseActivity.stopServer();
        }

        super.onDestroy();
    }

    private void setInitialization(){
        intUnit = 0;
        intType = 0;
        strTextValue1 = "";
        strTextValue2 = "";
        strTextValue3 = "";
    }

    private Spinner.OnItemSelectedListener spnPeripheralListener = new Spinner.OnItemSelectedListener(){

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String sel = parent.getSelectedItem().toString();
            edText1.setText(null);
            edText2.setText(null);
            edText3.setText(null);
            switch (sel){
                case "Temperature":
                    setInitialization();
                    tvData1.setVisibility(View.VISIBLE);
                    tvData2.setVisibility(View.VISIBLE);
                    tvData3.setVisibility(View.VISIBLE);
                    tvData4.setVisibility(View.GONE);
                    tvData5.setVisibility(View.GONE);
                    tvData6.setVisibility(View.VISIBLE);
                    tvData1.setText("Unit : ");
                    tvData2.setText("Type : ");
                    tvData3.setText("Data : ");
                    tvData6.setText("");
                    radGroupUnit = findViewById(R.id.radioGroup1);
                    radGroupType = findViewById(R.id.radioGroup2);
                    radGroupUnit.setVisibility(View.VISIBLE);
                    radGroupType.setVisibility(View.VISIBLE);
                    radGroupUnit.setOnCheckedChangeListener(radGroupUnitListener);
                    radGroupType.setOnCheckedChangeListener(radGroupTypeListener);
                    edText1.setVisibility(View.GONE);
                    edText2.setVisibility(View.GONE);
                    edText3.setVisibility(View.VISIBLE);
                    edText3.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

                    break;
                case "Blood Pressure":
                    setInitialization();
                    tvData1.setVisibility(View.VISIBLE);
                    tvData2.setVisibility(View.VISIBLE);
                    tvData3.setVisibility(View.VISIBLE);
                    tvData4.setVisibility(View.VISIBLE);
                    tvData5.setVisibility(View.VISIBLE);
                    tvData6.setVisibility(View.VISIBLE);
                    radGroupUnit.setVisibility(View.GONE);
                    radGroupType.setVisibility(View.GONE);
                    tvData1.setText("SYS : ");
                    tvData2.setText("DIA : ");
                    tvData3.setText("Pulse : ");
                    tvData4.setText("mmHg");
                    tvData5.setText("mmHg");
                    tvData6.setText("bpm");
                    edText1.setVisibility(View.VISIBLE);
                    edText2.setVisibility(View.VISIBLE);
                    edText3.setVisibility(View.VISIBLE);
                    edText1.setInputType( InputType.TYPE_CLASS_NUMBER);
                    edText2.setInputType( InputType.TYPE_CLASS_NUMBER);
                    edText3.setInputType( InputType.TYPE_CLASS_NUMBER);

                    break;
                case "Blood Glucose":
                    setInitialization();
                    tvData1.setVisibility(View.VISIBLE);
                    tvData2.setVisibility(View.GONE);
                    tvData3.setVisibility(View.GONE);
                    tvData4.setVisibility(View.VISIBLE);
                    tvData5.setVisibility(View.GONE);
                    tvData6.setVisibility(View.GONE);
                    radGroupUnit.setVisibility(View.GONE);
                    radGroupType.setVisibility(View.GONE);
                    tvData1.setText("Data : ");
                    tvData4.setText("mg/dL");
                    edText1.setVisibility(View.VISIBLE);
                    edText2.setVisibility(View.GONE);
                    edText3.setVisibility(View.GONE);
                    edText1.setInputType( InputType.TYPE_CLASS_NUMBER);
                    break;
                case "Body Fat":
                    setInitialization();
                    tvData1.setVisibility(View.VISIBLE);
                    tvData2.setVisibility(View.VISIBLE);
                    tvData3.setVisibility(View.GONE);
                    tvData4.setVisibility(View.VISIBLE);
                    tvData5.setVisibility(View.VISIBLE);
                    tvData6.setVisibility(View.GONE);
                    radGroupUnit.setVisibility(View.GONE);
                    radGroupType.setVisibility(View.GONE);
                    tvData1.setText("Weight : ");
                    tvData2.setText("Resistance : ");
                    tvData4.setText("Kg");
                    tvData5.setText("(800>x>300)");
                    edText1.setVisibility(View.VISIBLE);
                    edText2.setVisibility(View.VISIBLE);
                    edText3.setVisibility(View.GONE);
                    edText1.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    edText2.setInputType( InputType.TYPE_CLASS_NUMBER);

                    break;
                case "Sympathetic Indexes":
                    setInitialization();
                    tvData1.setVisibility(View.GONE);
                    tvData2.setVisibility(View.GONE);
                    tvData3.setVisibility(View.GONE);
                    tvData4.setVisibility(View.GONE);
                    tvData5.setVisibility(View.GONE);
                    tvData6.setVisibility(View.GONE);
                    radGroupUnit.setVisibility(View.GONE);
                    radGroupType.setVisibility(View.GONE);
                    edText1.setVisibility(View.GONE);
                    edText2.setVisibility(View.GONE);
                    edText3.setVisibility(View.GONE);
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private RadioGroup.OnCheckedChangeListener radGroupUnitListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId == R.id.radioButton1){
                tvData6.setText("°C");
                intUnit = UNIT_CELSIUS;
            }else if (checkedId == R.id.radioButton2){
                tvData6.setText("°F");
                intUnit = UNIT_FAHRENHEIT;
            }
        }
    };
    private RadioGroup.OnCheckedChangeListener radGroupTypeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId == R.id.radioButton3){
                intType = TYPE_EAR;
            }else if (checkedId == R.id.radioButton4){
                intType = TYPE_FOREHEAD;
            }
        }
    };


    private Button.OnClickListener btnListener = new Button.OnClickListener() {
        @Override
        public void onClick(View view) {
            advertiseActivity.setStrPeripheral(spnPeripheral.getSelectedItem().toString());
            switch (view.getId()){
                case R.id.button1:

                    Log.i("TAG", "Creating Gatt Server. . .");
                    advertiseActivity.stopServer();  //初始化
                    advertiseActivity.createGattServer();
                    if (advertiseActivity.checkServerStatus()){
                        Toast.makeText(WorkAsPeripheralPage.this,"Success to open GattServer",Toast.LENGTH_SHORT).show();
                        Log.i("TAG","Create GattServer success");
                    }else {
                        Toast.makeText(WorkAsPeripheralPage.this,"Fail to open GattServer",Toast.LENGTH_SHORT).show();
                        Log.i("TAG","Create GattServer fail");
                    }
                    break;

                case R.id.button2:

                    Log.i("TAG","Starting Advertising. . .");

                    //跳出訊框 顯示loading
                    final ProgressDialog dlg = new ProgressDialog(WorkAsPeripheralPage.this);
                    dlg.setMessage("Loading . . .");
                    dlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    dlg.show();

                    //儲存EditText中的值
                    if (intUnit != 0){
                        if (intUnit == UNIT_CELSIUS){
                            strTextValue1 = "Celsius";
                        }else if (intUnit == UNIT_FAHRENHEIT){
                            strTextValue1 = "Fahrenheit";
                        }else {
                            Log.w("TAG","Unknown unit");
                        }
                    }else {
                        strTextValue1 = edText1.getText().toString();
                    }
                    if (intType != 0){
                        if (intType == TYPE_EAR){
                            strTextValue2 = "Ear";
                        }else if (intType == TYPE_FOREHEAD){
                            strTextValue2 = "Forehead";
                        }else {
                            Log.w("TAG","Unknown unit");
                        }
                    }else {
                        strTextValue2 = edText2.getText().toString();
                    }
                    strTextValue3 = edText3.getText().toString();
                    advertiseActivity.getEditTextData(strTextValue1,strTextValue2,strTextValue3);



                    //廣播初始化且開始廣播
                    advertiseActivity.stopAdvertiser();  //初始化
                    advertiseActivity.startAdvertising();

                    //持續檢查藍牙名稱是否被修改正確
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (!advertiseActivity.checkBluetoothName()){
                                handler.postDelayed(this,1000);
                            }else {
                                dlg.dismiss();
                            }
                        }
                    },1000);


                    /****/


                    if (advertiseActivity.checkAdvertiser() ){
                        Toast.makeText(WorkAsPeripheralPage.this,"Success to start Advertiser",Toast.LENGTH_SHORT).show();
                        advertiseActivity.updateCharacteristicsData();


                        Log.i("TAG","Start advertising success");
                    }else {
                        Toast.makeText(WorkAsPeripheralPage.this,"Fail to start Advertiser",Toast.LENGTH_SHORT).show();
                        Log.i("TAG","Start advertising fail");
                    }
                    break;

                case R.id.button3:
                    Log.i("TAG","Stop Server.");
                    advertiseActivity.stopServer();
                    Toast.makeText(WorkAsPeripheralPage.this,"Close Server",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };


    private void setPeripheralSpinner(){
        spnPeripheral = (Spinner)findViewById(R.id.spinner);
        final String[] lunch = {"Temperature", "Blood Pressure", "Blood Glucose", "Body Fat", "Sympathetic Indexes"};
        ArrayAdapter<String> lunchList = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, lunch);
        spnPeripheral.setAdapter(lunchList);
    }
}
