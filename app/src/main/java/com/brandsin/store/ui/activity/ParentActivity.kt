package com.brandsin.store.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.brandsin.store.utils.LocalUtil

@Suppress("DEPRECATION")
open class ParentActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?) {
        LocalUtil.changeLanguage(this)
        super.onCreate(savedInstanceState)
        LocalUtil.changeLanguage(this)
    }

    fun showMessage(message: Any)
    {
        Toast.makeText(this, "" + message, Toast.LENGTH_SHORT).show()
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocalUtil.onAttach(newBase))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        for (fragment in supportFragmentManager.primaryNavigationFragment!!
                .childFragmentManager.fragments) {
            fragment.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        for (fragment in supportFragmentManager.primaryNavigationFragment!!
                .childFragmentManager.fragments) {
            fragment.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }
}