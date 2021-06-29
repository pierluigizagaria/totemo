package com.pierluigizagaria.totemo.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import com.pierluigizagaria.totemo.R
import com.pierluigizagaria.totemo.WebClient


class MainActivity : AppCompatActivity() {
    companion object {
        private val TAG = MainActivity::class.java
    }

    private var barCode: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        WebClient.getInstance(applicationContext).checkForUpdates(applicationContext)
    }

    override fun dispatchKeyEvent(event: KeyEvent?): Boolean {
        if (event?.action == KeyEvent.ACTION_DOWN && event.keyCode != KeyEvent.KEYCODE_BACK) {
            if (event.keyCode == KeyEvent.KEYCODE_ENTER) {
                openScannedProduct()
                barCode = ""
            } else if (event.displayLabel.isDigit()) {
                barCode += event.displayLabel
            }
            return true
        }
        return super.dispatchKeyEvent(event)
    }

    fun openScannedProduct() {
        val intent = Intent(baseContext, ScannedProductActivity::class.java)
        intent.putExtra(ScannedProductActivity.BARCODE_EXTRA, barCode)
        startActivity(intent)
    }
}