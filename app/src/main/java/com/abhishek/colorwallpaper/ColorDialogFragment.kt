package com.abhishek.colorwallpaper

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.DialogFragment
import com.abhishek.colorwallpaper.databinding.FragmentColorDialogBinding


class ColorDialogFragment : DialogFragment() {
    private var r: Int = 255
    private var g: Int = 255
    private var b: Int = 255
    lateinit var binding: FragmentColorDialogBinding
    private var onClickListener: ((isOk: Boolean, rgb: Int) -> Unit)? = null
    fun getColor() = Color.rgb(r, g, b)
    fun setColor(color: Int) {
        r = Color.red(color)
        g = Color.green(color)
        b = Color.blue(color)
        //update seekbar
        if (this::binding.isInitialized) {
            binding.apply {
                seekBarRed.progress = r
                seekBarGreen.progress = g
                seekBarBlue.progress = b
                colorView2.setColor(getColor())
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentColorDialogBinding.inflate(inflater, container, false)
        //update seekBar and colorView with default color
        setColor(getColor())
        binding.apply {
            setSeekListener(seekBarRed) {
                r = it
                onSeekBarDrag()
            }
            setSeekListener(seekBarGreen) {
                g = it
                onSeekBarDrag()
            }
            setSeekListener(seekBarBlue) {
                b = it
                onSeekBarDrag()
            }
            buttonCancel.setOnClickListener {
                click(false)
            }
            buttonOk.setOnClickListener {
                click(true)
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val metrics = resources.displayMetrics
        val width = metrics.widthPixels
        val height = metrics.heightPixels
        requireDialog().window?.setLayout((6 * width) / 7, height * 50 / 100)
    }

    private fun setSeekListener(seekBar: SeekBar, callbackChange: (value: Int) -> Unit) {
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                callbackChange(p1)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }
        })
    }

    private fun onSeekBarDrag() {
        binding.colorView2.setColor(getColor())
    }

    private fun click(isOk: Boolean) {
        onClickListener?.invoke(isOk, getColor())
        dismiss()
    }

    fun setOnClickListener(f: (isOk: Boolean, rgb: Int) -> Unit) {
        onClickListener = f
    }
}