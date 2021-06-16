package com.example.blesimulator;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button btnScanPeripheral,btnWorkAsPeripheral;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //取得元件
        btnScanPeripheral = (Button) findViewById(R.id.ScanPeripheral);
        btnWorkAsPeripheral = (Button) findViewById(R.id.WorkAsPeripheral);


        //按鈕監聽
        btnScanPeripheral.setOnClickListener(btnListener);
        btnWorkAsPeripheral.setOnClickListener(btnListener);

    }


    //觸發監聽執行方法
    private Button.OnClickListener btnListener =
            new Button.OnClickListener(){
                Intent intent = new Intent();
                @Override
                public void onClick(View view) {
                    switch (view.getId()){
                        case R.id.ScanPeripheral:
                            intent.setClass(MainActivity.this,ScanPeripheralPage.class);
                            startActivity(intent);
                            break;
                        case R.id.WorkAsPeripheral:
                            intent.setClass(MainActivity.this,WorkAsPeripheralPage.class);
                            startActivity(intent);
                            break;
                    }
                }
            };
}
