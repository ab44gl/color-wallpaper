package com.abhishek.colorwallpaper

import android.Manifest
import android.app.WallpaperManager
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.view.MenuItem
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.abhishek.colorwallpaper.databinding.ActivityMainBinding
import java.io.FileOutputStream
import java.io.IOException

class MainActivity : AppCompatActivity(), PopupMenu.OnMenuItemClickListener {
    private lateinit var binding: ActivityMainBinding
    private val REQUEST_CODE_SAVE_PNG = 100
    private var color = MutableLiveData(Color.rgb(200, 200, 200))
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fullscreen(true)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            colorView.showText(false)
            imgMore.setOnClickListener {
                showMoreMenu()
            }
        }
        color.observe(this) {
            binding.colorView.setColor(it)
        }
    }

    private fun showColorDialog() {
        val dialog = ColorDialogFragment()
        color.value?.let { it1 -> dialog.setColor(it1) }
        dialog.setOnClickListener { isOk, rgb ->
            if (isOk) {
                color.value = rgb
            }
        }
        dialog.show(supportFragmentManager, null)
    }

    private fun savePng() {
        if (checkPermission()) {
            val filename = if (color.value != null) {
                "${Color.red((color.value!!))}_${Color.green((color.value!!))}_${Color.blue((color.value!!))}"
            } else
                color.toString()
            val dir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + "/$filename.png")
            dir.createNewFile()
            try {
                val out = FileOutputStream(dir)
                val bitmap = binding.colorView.getBitmap()
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                showMessage("file save at ${dir.absolutePath}")
            } catch (e: IOException) {
                Utils.log("can't save file", e)
                showMessage("some error while saving file")
            }
        }
    }

    private fun checkPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_CODE_SAVE_PNG
            )
        } else {
            return true
        }
        return false
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE_SAVE_PNG -> {
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    //granted
                    savePng()
                } else {
                    //explain the user
                }
                return
            }
            else -> {

            }
        }
    }

    private fun showMoreMenu() {
        PopupMenu(this, binding.imgMore).apply {
            setOnMenuItemClickListener(this@MainActivity)
            inflate(R.menu.menu_more)
            show()
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        fullscreen(true)
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.color -> {
                showColorDialog()
                true
            }
            R.id.save_png -> {
                savePng()
                true
            }
            R.id.set_wallpaper -> {
                setColorAsWallpaper()
                true
            }
            else -> false
        }
    }

    private fun setColorAsWallpaper() {
        val wm = WallpaperManager.getInstance(this)
        Utils.log("$")
        try {
            wm.setBitmap(binding.colorView.getBitmap())
            showMessage("wallpaper added")
        } catch (e: IOException) {
            Utils.log("error", e)
        }
    }
}