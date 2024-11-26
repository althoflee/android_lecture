package com.example.day12_01

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class MainActivity : AppCompatActivity() {

    private lateinit var btnScan: Button

    private lateinit var bluetoothAdapter: BluetoothAdapter
    private lateinit var bluetoothScanner: BluetoothLeScanner

    private var scanning = false

    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>

    private val bluetoothActivityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result->
            if (result.resultCode == RESULT_OK) {
                Log.d("MainActivity", "Bluetooth enabled")
            } else {
                Log.d("MainActivity", "Bluetooth disabled")
            }
        }
    private val scanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            super.onScanResult(callbackType, result)
            val device= result?.device
            val rssi = result?.rssi
            Log.d("MainActivity", "Device: $device, RSSI: $rssi")
        }
        override fun onScanFailed(errorCode: Int) {
            super.onScanFailed(errorCode)
            Log.d("MainActivity", "Scan failed with error code: $errorCode")
        }
    }

    private var onPermissionGranted: (()->Unit)?=null
    private fun checkPermissions(onPermissionGranted:()->Unit) {
//        val permissionToRequest : mutableListOf<String>()
        val permissionToRequest = mutableListOf<String>()

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (checkSelfPermission(Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                permissionToRequest.add(Manifest.permission.BLUETOOTH_SCAN)
            }
            if (checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                permissionToRequest.add(Manifest.permission.BLUETOOTH_CONNECT)
            }
        }
        else {
            if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
                permissionToRequest.add(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
        if(permissionToRequest.isNotEmpty()) {
            this.onPermissionGranted = onPermissionGranted
            permissionLauncher.launch(permissionToRequest.toTypedArray())
        } else {
            onPermissionGranted()
        }
    }

    @SuppressLint("MissingInflatedId", "MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            if (permissions.all { it.value }) {
                Log.d("MainActivity", "All permissions granted")
            } else {
                Log.d("MainActivity", "Not all permissions granted")
            }
        }

        val bluetoothManager = getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter
        bluetoothScanner = bluetoothAdapter.bluetoothLeScanner

        btnScan = findViewById(R.id.btnScan)
        btnScan.setOnClickListener {
            Log.d("MainActivity", "Button clicked!")
            if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled) {
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                bluetoothActivityResultLauncher.launch(enableBtIntent)
            } else {
                checkPermissions {
                    //BLE scan
                    if (!scanning) {
                        scanning = true
                        bluetoothScanner.startScan(scanCallback)
                    } else {
                        scanning = false
                    }
                }
            }
        }
    }
}