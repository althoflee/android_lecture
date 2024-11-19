package com.example.bleexam01

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.bluetooth.le.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.*
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.UUID

class MainActivity : AppCompatActivity() {

    companion object {
        const val SERVICE_UUID = "c6f8b088-2af8-4388-8364-ca2a907bdeb8"
        const val CHARACTERISTIC_UUID = "f6aa83ca-de53-46b4-bdea-28a7cb57942e"
    }


    // UI 선언
    private lateinit var tvInfoMsg: TextView
    private lateinit var btnScan: Button
    private lateinit var btnConnect: Button

//    private lateinit var list_ble_device: ListView
    private lateinit var listView: ListView
    private lateinit var arrayAdapter: ArrayAdapter<String>
    private val deviceList = HashSet<String>()

    private lateinit var bluetoothAdapter: BluetoothAdapter
    private lateinit var bluetoothLeScanner: BluetoothLeScanner
    private val handlerBleScan = Handler(Looper.getMainLooper()) // 메인 스레드에서 실행

    private var scanning = false
    private var isConnected = false
    private var currentSelectedDevice: String? = null
    private var bluetoothGatt: BluetoothGatt? = null

    // 콜백 함수를 저장할 변수 선언
    private var onPermissionGranted: (() -> Unit)? = null
    // 권한 요청 런처 선언
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>

    // 블루투스 활성화 요청 런처
    private val bluetoothActivityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // 블루투스가 활성화되었습니다.
                Log.d("MainActivity", "Bluetooth enabled")
            } else {
                // 사용자가 블루투스 활성화를 거부했습니다.
                Log.d("MainActivity", "Bluetooth denied")
            }
        }

    // 스캔 콜백
    private val scanCallback = object : ScanCallback() {
        @SuppressLint("MissingPermission")
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            val device = result.device
            val rssi = result.rssi
            val serviceUuids = result.scanRecord?.serviceUuids

            Log.d(
                "MainActivity",
                "Device found: ${device.name}, Address: ${device.address}, Service UUIDs: $serviceUuids, RSSI: $rssi"
            )

            // 필요한 추가 처리
            //list_ble_device.adapter 에 중복되지않게 추가
            val deviceName = device.name ?: return // 이름이 없으면 무시

            val deviceInfo = "$deviceName - ${device.address}"
            if (deviceList.add(deviceInfo)) { // 중복 확인
                runOnUiThread {
                    arrayAdapter.add(deviceInfo)
                    arrayAdapter.notifyDataSetChanged()
                }
            }
        }

        override fun onScanFailed(errorCode: Int) {
            val errorMessage = when (errorCode) {
                ScanCallback.SCAN_FAILED_ALREADY_STARTED -> "Scan already started"
                ScanCallback.SCAN_FAILED_APPLICATION_REGISTRATION_FAILED -> "Application registration failed"
                ScanCallback.SCAN_FAILED_FEATURE_UNSUPPORTED -> "Feature unsupported"
                ScanCallback.SCAN_FAILED_INTERNAL_ERROR -> "Internal error"
                else -> "Unknown error code: $errorCode"
            }
            Log.e("MainActivity", "BLE scan failed: $errorMessage")
        }
    }


    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

// 권한 요청 런처 초기화
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
            }

            // 콜백 함수 초기화
            onPermissionGranted = null
        }

        btnScan = findViewById(R.id.btnScan)
        btnConnect = findViewById(R.id.btnConnect)
        tvInfoMsg = findViewById(R.id.InfoMsg)

        listView = findViewById(R.id.list_ble_device)
        arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1)
        listView.adapter = arrayAdapter

        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter
        bluetoothLeScanner = bluetoothAdapter.bluetoothLeScanner

        btnScan.setOnClickListener {
            if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled) {
                // 블루투스 활성화 요청
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                bluetoothActivityResultLauncher.launch(enableBtIntent)
            } else {
                if (!scanning) {
                    // 권한 체크 및 스캔 시작
                    checkPermissions {
                        startScan()
                    }
                } else {
                    // 스캔 중지
                    checkPermissions {
                        stopScan()
                    }
//                    bluetoothLeScanner.stopScan(scanCallback)
//                    scanning = false
                    btnScan.text = "Start scan"
                    tvInfoMsg.text = "Bluetooth scan stopped"
                }
            }
        }

        btnConnect.setOnClickListener {
            //currentSelectedDevice 로 연결시키기

            //연결된 상태라면 연결종료
            if (isConnected) {
                // 연결된 상태라면 연결 종료
                bluetoothGatt?.disconnect()
                bluetoothGatt = null
                return@setOnClickListener
            }

            //선택된 디바이스가 없으면 경고메쎄지 출력
            if (currentSelectedDevice == null) {
                Log.d("MainActivity", "no selected device")
                tvInfoMsg.setText("no selected device")

            } else {
                //연결을 시도한다.
                Log.d("MainActivity", "try connect to ${currentSelectedDevice}")

                // 연결을 시도하는 코드
                val device = bluetoothAdapter.getRemoteDevice(currentSelectedDevice)

                bluetoothGatt = device.connectGatt(this, false, object : BluetoothGattCallback() {

                    override fun onDescriptorWrite(
                        gatt: BluetoothGatt?,
                        descriptor: BluetoothGattDescriptor?,
                        status: Int
                    ) {
                        if (status == BluetoothGatt.GATT_SUCCESS) {
                            Log.d("MainActivity", "Descriptor write successful")
                        } else {
                            Log.e("MainActivity", "Descriptor write failed: $status")
                        }
                    }


                    //서버측 알림을 받음
                    override fun onCharacteristicChanged(
                        gatt: BluetoothGatt?,
                        characteristic: BluetoothGattCharacteristic?
                    ) {
                        val data = characteristic?.value // 변경된 데이터
                        Log.d("MainActivity", "onCharacteristicChanged")
                        // 데이터 처리 로직
                        if (data != null) {
                            val dataLength = data.size
                            Log.d("MainActivity", "Changed data length: $dataLength")
                            Log.d("MainActivity", "Changed data: ${data.joinToString(", ")}")

                        }
                    }

                    //읽기 요청에 대한 답
                    override fun onCharacteristicRead(
                        gatt: BluetoothGatt?,
                        characteristic: BluetoothGattCharacteristic?,
                        status: Int
                    ) {

                        Log.d("MainActivity", "onCharacteristicRead")

                        val data: ByteArray? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                            try {
                                val readCharacteristicMethod = gatt?.javaClass?.getMethod(
                                    "readCharacteristic",
                                    BluetoothGattCharacteristic::class.java
                                )
                                readCharacteristicMethod?.invoke(gatt, characteristic) as? ByteArray
                            } catch (e: Exception) {
                                // 메서드 호출에 실패한 경우, 예외 처리
                                null
                            }
                        } else {
                            characteristic?.value
                        }

                        if (data != null) {

                            //데이터 길이
                            val dataLength = data.size
                            Log.d("MainActivity", "Read data length: $dataLength")
                            Log.d("MainActivity", "Read data: ${data.joinToString(", ")}")

                        }
                    }

                    override fun onMtuChanged(gatt: BluetoothGatt?, mtu: Int, status: Int) {
                        super.onMtuChanged(gatt, mtu, status)
                        if (status == BluetoothGatt.GATT_SUCCESS) {
                            Log.d("MainActivity", "MTU changed to $mtu")
                        } else {
                            Log.e("MainActivity", "MTU change failed")
                        }
                    }

                    @SuppressLint("MissingPermission")
                    override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
                        super.onServicesDiscovered(gatt, status)

                        if (status == BluetoothGatt.GATT_SUCCESS) {
                            Log.d("MainActivity", "Services discovered.")
                            // 원하는 서비스 찾기
                            val service = gatt.getService(UUID.fromString(SERVICE_UUID))
                            if (service != null) {
                                Log.d("MainActivity", "Desired service found: $SERVICE_UUID")

                                // 원하는 특성 찾기
                                val characteristic = service.getCharacteristic(UUID.fromString(CHARACTERISTIC_UUID))
                                if (characteristic != null) {
                                    Log.d("MainActivity", "Desired characteristic found: $CHARACTERISTIC_UUID")
                                    // 특성에 대한 작업 수행 (예: 읽기, 쓰기, 알림 설정 등)
                                    // 예: 특성 읽기
                                    gatt.readCharacteristic(characteristic)
                                } else {
                                    Log.e("MainActivity", "Desired characteristic not found.")
                                }
                            } else {
                                Log.e("MainActivity", "Desired service not found.")
                            }
                        } else {
                            Log.w("MainActivity", "onServicesDiscovered received: $status")
                        }
                    }

                    @SuppressLint("MissingPermission")
                    override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
                        super.onConnectionStateChange(gatt, status, newState)

                        if (newState == BluetoothProfile.STATE_CONNECTED) {
                            Log.d("MainActivity", "Connected to GATT server.")
                            isConnected = true
                            runOnUiThread {
                                tvInfoMsg.text = "Connected to ${gatt.device.name}"
                            }
                            // 서비스 검색 시작
                            gatt.discoverServices()
                        } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                            Log.d("MainActivity", "Disconnected from GATT server.")
                            isConnected = false
                            runOnUiThread {
                                tvInfoMsg.text = "Disconnected from ${gatt.device.name}"
                            }
                        }
                    }


                })


            }
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedDevice = arrayAdapter.getItem(position)
            // TODO: 여기에서 선택한 디바이스로 무언가를 할 수 있습니다.
            Log.d("MainActivity", "selected device: $selectedDevice")
            // 문자열을 " - "로 분리하고 두 번째 부분(주소)을 currentSelectedDevice에 저장
            val deviceAddress = selectedDevice?.split(" - ")?.get(1)
            if (deviceAddress != null) {
                currentSelectedDevice = deviceAddress
                Log.d("MainActivity", "currentSelectedDevice address: $currentSelectedDevice")
                tvInfoMsg.text = "selected device: $selectedDevice"
            }
        }

    }

    private fun checkPermissions(onPermissionGranted: () -> Unit) {
        val permissionsToRequest = mutableListOf<String>()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (checkSelfPermission(Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(Manifest.permission.BLUETOOTH_SCAN)
            }

            if (checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(Manifest.permission.BLUETOOTH_CONNECT)
            }
        } else {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }

        if (permissionsToRequest.isNotEmpty()) {
            // 권한 요청 실행
            this.onPermissionGranted = onPermissionGranted // 콜백 저장
            permissionLauncher.launch(permissionsToRequest.toTypedArray())
            Log.d("MainActivity", "Requesting permissions: $permissionsToRequest")
        } else {
            // 모든 권한이 이미 허용된 경우
            Log.d("MainActivity", "All permissions are already granted")
            onPermissionGranted() // 콜백 함수 실행
        }
    }



    @SuppressLint("MissingPermission")
    private fun startScan() {
        if (!scanning) {
            bluetoothLeScanner.startScan(scanCallback)
            scanning = true

            Log.d("MainActivity", "Bluetooth scan started")

            btnScan.text = "Stop scan"
            tvInfoMsg.text = "Bluetooth scan started"

            // 일정 시간 후 스캔 중지
            handlerBleScan.postDelayed({
                if (scanning) {
                    bluetoothLeScanner.stopScan(scanCallback)
                    scanning = false
                    btnScan.text = "Start scan"
                    tvInfoMsg.text = "Bluetooth scan stopped"
                }
            }, 5000)
        }
    }

    @SuppressLint("MissingPermission")
    private fun stopScan() {
        if (scanning) {
            bluetoothLeScanner.stopScan(scanCallback)
            scanning = false
            btnScan.text = "Start scan"
            tvInfoMsg.text = "Bluetooth scan stopped"
        }
    }



    override fun onDestroy() {
        super.onDestroy()

        checkPermissions {
            stopScan()
        }


//        if (scanning) {
//            checkPermissions {
////                bluetoothLeScanner.stopScan(scanCallback)
//            }
//            scanning = false
//        }
        handlerBleScan.removeCallbacksAndMessages(null)
    }
}
