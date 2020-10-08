package com.hp.flutter_nativeplatform

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel

class MainActivity: FlutterActivity() {
    private val CHANNEL = "samples.flutter.dev/battery"
    var t=""

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        MethodChannel(flutterEngine.dartExecutor.binaryMessenger,CHANNEL).setMethodCallHandler{
            methodCall: MethodCall, result: MethodChannel.Result ->
            if(methodCall.method=="getBatteryLevel"){
//                val batteryLevel = getBatteryLevel()
//                if(batteryLevel !=-1){
//                    result.success(batteryLevel)
//                }else{
//                    result.error("UNAVAILABLE","Battery level not availabel",null)
//                }
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "file/*"
                startActivityForResult(intent, 111)
                result.success(t)



            }else{
                result.notImplemented()
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val Fpath = data!!.dataString
        t=Fpath

        super.onActivityResult(requestCode, resultCode, data)
    }
    private fun getBatteryLevel(): Int {
        val batteryLevel: Int
        if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
            val batteryManager = getSystemService(Context.BATTERY_SERVICE) as BatteryManager
            batteryLevel = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
        } else {
            val intent = ContextWrapper(applicationContext).registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
            batteryLevel = intent!!.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) * 100 / intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
        }

        return batteryLevel
    }
}
