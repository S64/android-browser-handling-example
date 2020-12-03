package jp.s64.android.example.browserhandling

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.SimpleAdapter

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val browsers by lazy { findViewById<ListView>(R.id.detected_browsers) }
    private val open by lazy { findViewById<Button>(R.id.open) }

    private val queryFlag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        PackageManager.MATCH_ALL
    } else {
        PackageManager.MATCH_DEFAULT_ONLY
    }

    private lateinit var browseIntent: Intent

    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        browseIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://example.com"))

        browsers.adapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_list_item_1,
            mutableListOf()
        ).also {
            this.adapter = it
        }

        open.setOnClickListener {
            startActivity(browseIntent)
        }
    }

    override fun onPostResume() {
        super.onPostResume()
        resetBrowserList()
    }

    private fun resetBrowserList() {
        adapter.apply {
            clear()
            addAll(
                packageManager
                    .queryIntentActivities(browseIntent, queryFlag)
                    .map {
                        it.activityInfo?.name ?: it.toString()
                    }
            )
        }
    }

}
