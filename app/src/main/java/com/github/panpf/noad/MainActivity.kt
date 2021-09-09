package com.github.panpf.noad

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.panpf.noad.ui.SkipAdFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction()
            .replace(R.id.mainContainer, SkipAdFragment())
            .commit()
    }
}