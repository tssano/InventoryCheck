package com.mitsui.tkmid.inventorycheck

import android.content.pm.PackageManager
import android.graphics.Color
import android.hardware.Camera
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.util.SparseArray
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.util.isNotEmpty
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.mitsui.tkmid.inventorycheck.databinding.FragmentQrReaderBinding
import kotlinx.android.synthetic.main.fragment_qr_reader.*
import java.lang.Exception


class QRReaderFragment : Fragment() {

    private val requestCodeCameraPermission = 1001
    private lateinit var cameraSource: CameraSource
    private lateinit var detector: BarcodeDetector
    private val mHandler = Handler()
    private var flashmode = false
    private var mCamera: Camera? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i("CallCheck","onCreateView called")

        val binding = DataBindingUtil.inflate<FragmentQrReaderBinding>(
            inflater,
            R.layout.fragment_qr_reader, container, false
        )

        binding.textScanResult.setOnClickListener { view: View ->
            view.findNavController().navigate(
                QRReaderFragmentDirections.actionQRReaderFragmentToQRConfirmFragment(binding.textScanResult.text.toString()))
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        Log.i("CallCheck","onStart called")

        // Below setOnClickListener cannot be in onCreateView() because binding.ImageView is not supported.
        toggle_flash.setOnClickListener {
            if(!flashmode) {
                toggle_flash.setColorFilter(Color.argb(255, 255, 255, 255))
            } else{
                toggle_flash.setColorFilter(Color.parseColor("#FFDA44"))
            }
            switchOnOffFlash() }

        // Below cannot be in onCreateView() because cameraSurfaceView is not instantiated yet.
        if (getContext()?.let {
                ActivityCompat.checkSelfPermission(
                    it,
                    android.Manifest.permission.CAMERA
                )
            } != PackageManager.PERMISSION_GRANTED
        ) {
            askForCameraPermission()
        } else {
            setupControls()
        }

    }

    private fun askForCameraPermission() {
        getActivity()?.let {
            ActivityCompat.requestPermissions(
                it,
                arrayOf(android.Manifest.permission.CAMERA),
                requestCodeCameraPermission
            )
        }
    }

    private fun setupControls() {
        detector = BarcodeDetector.Builder(getContext()).build()
        cameraSource =
            CameraSource.Builder(getContext(), detector).setAutoFocusEnabled(true).build()
        cameraSurfaceView.holder.addCallback(surgaceCallBack)
        detector.setProcessor(processor)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == requestCodeCameraPermission && grantResults.isNotEmpty()) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setupControls()
            } else {
                val runnable = object : Runnable {
                    override fun run() {
                        Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT).show()
                    }
                }
                mHandler.post(runnable)
            }
        }
    }

    private val surgaceCallBack = object : SurfaceHolder.Callback {
        override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
        }

        override fun surfaceDestroyed(holder: SurfaceHolder?) {
            val runnable = object : Runnable {
                override fun run() {
                    cameraSource.stop()
                }
            }
            mHandler.post(runnable)
        }

        override fun surfaceCreated(holder: SurfaceHolder?) {
            try {
                cameraSource.start(holder)
                mCamera = getCamera(cameraSource)
            } catch (exception: Exception) {
                val runnable = object : Runnable {
                    override fun run() {
                        Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show()
                    }
                }
                mHandler.post(runnable)
            }
        }
    }

    private val processor = object : Detector.Processor<Barcode> {
        override fun release() {
        }

        override fun receiveDetections(detections: Detector.Detections<Barcode>?) {
            if (detections != null && detections.detectedItems.isNotEmpty()) {
                val qrCodes: SparseArray<Barcode> = detections.detectedItems
                val code = qrCodes.valueAt(0)
                val runnable = object : Runnable {
                    override fun run() {
                        if (textScanResult != null) {
                            textScanResult.text = code.displayValue
                        }
                    }
                }
                mHandler.post(runnable)
            }
        }
    }

    private fun getCamera(cameraSource: CameraSource): Camera? {
        val declaredFields = CameraSource::class.java.declaredFields

        for (field in declaredFields) {
            if (field.type == Camera::class.java) {
                field.isAccessible = true
                try {
                    val camera = field.get(cameraSource) as Camera
                    if (camera != null) {
                        return camera
                    }
                    return null
                } catch (e: IllegalAccessException) {
                    e.printStackTrace()
                }

                break
            }
        }
        return null
    }

    private fun switchOnOffFlash() {
        Log.i("switchOnOffFlash","Entered")
        if (mCamera != null) {
            try {
                Log.i("switchOnOffFlash","Try to change")
                val param = mCamera!!.parameters
                param.flashMode = if (!flashmode) Camera.Parameters.FLASH_MODE_TORCH else Camera.Parameters.FLASH_MODE_OFF
                mCamera!!.parameters = param
                flashmode = !flashmode
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}
