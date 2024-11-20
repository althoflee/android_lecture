package com.example.bleexam02

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.registerForActivityResult
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var btnScan: Button


    private lateinit var bluetoothAdapter : BluetoothAdapter
    private lateinit var bluetoothLeScanner : BluetoothLeScanner
    private val handlerBleScan = Handler(Looper.getMainLooper())

    private var isScanning = false

    private var onPermissionGranted:( () -> Unit  )?= null
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>


    private val scanCallback = object : ScanCallback() {

        override fun onScanResult(callbackType: Int, result: ScanResult) {
//            Log.d("MainActivity", "onScanResult: $result")
            val device = result.device
            val rssi = result.rssi
            val name = if (ActivityCompat.checkSelfPermission(
                    this@MainActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            } else
                device.name

            Log.d("MainActivity", "Device: $device, RSSI: $rssi , Name: $name")
        }
        override fun onScanFailed(errorCode: Int) {
            Log.d("MainActivity", "onScanFailed: $errorCode")
        }
    }


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
            var allPermissionsGranted = true
            permissions.entries.forEach { entry ->
                val permissionName = entry.key
                val isGranted = entry.value

                if (!isGranted) {
                    allPermissionsGranted = false
                    Log.d("MainActivity", "$permissionName denied")
                    // 권한이 거부된 경우 사용자에게 안내
                }
            }

            if (allPermissionsGranted) {
                // 모든 권한이 허용되었으므로 콜백 함수 실행
                onPermissionGranted?.invoke()
            } else {
                // 권한이 거부된 경우 안내 또는 처리
                Log.d("MainActivity", "Required permissions not granted. Cannot proceed.")

                // 콜백 함수 초기화
                onPermissionGranted = null
            }
        }

        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter
        bluetoothLeScanner = bluetoothAdapter.bluetoothLeScanner

        btnScan = findViewById(R.id.btnScan)
        btnScan.setOnClickListener {
            if(bluetoothAdapter == null || !bluetoothAdapter.isEnabled)
            {
                val enableBtIntent =Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)

                registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                    result ->
                    if (result.resultCode == RESULT_OK) {
                        Log.d("MainActivity", "Bluetooth is activated")
                        Toast.makeText(this, "Bluetooth is activated", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        Log.d("MainActivity", "Bluetooth is not activated")
                        Toast.makeText(this, "Bluetooth is not activated", Toast.LENGTH_SHORT).show()
                    }
                }

            }

            if (isScanning) {

                val permissionToRequest = mutableListOf<String>()

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) { // Android 13 이상일 때

                    if (checkSelfPermission(android.Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                        permissionToRequest.add(android.Manifest.permission.BLUETOOTH_SCAN)
                    }
                    if (checkSelfPermission(android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                        permissionToRequest.add(android.Manifest.permission.BLUETOOTH_CONNECT)
                    }
                }
                else { // Android 13 이하일 때
                    if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        permissionToRequest.add(android.Manifest.permission.ACCESS_FINE_LOCATION)
                    }
                }

                if (permissionToRequest.isNotEmpty()) {
                    permissionLauncher.launch(permissionToRequest.toTypedArray())
                    return@setOnClickListener
                }
                else {
                    bluetoothLeScanner.stopScan(scanCallback)
                    isScanning = false
                    btnScan.text = "Scan"
                }

            } else {
                val permissionToRequest = mutableListOf<String>()

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) { // Android 13 이상일 때

                    if (checkSelfPermission(android.Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                        permissionToRequest.add(android.Manifest.permission.BLUETOOTH_SCAN)
                    }
                    if (checkSelfPermission(android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                        permissionToRequest.add(android.Manifest.permission.BLUETOOTH_CONNECT)
                    }
                }
                else { // Android 13 이하일 때
                    if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        permissionToRequest.add(android.Manifest.permission.ACCESS_FINE_LOCATION)
                    }
                }

                if (permissionToRequest.isNotEmpty()) {
                    permissionLauncher.launch(permissionToRequest.toTypedArray())
                    return@setOnClickListener
                }
                else {
                    bluetoothLeScanner.startScan(scanCallback)
                    isScanning = true
                    btnScan.text = "Stop"

                    handlerBleScan.postDelayed({
                        bluetoothLeScanner.stopScan(scanCallback)
                        isScanning = false
                        btnScan.text = "Scan"
                    }, 5000)
                }

            }
        }



    }
}