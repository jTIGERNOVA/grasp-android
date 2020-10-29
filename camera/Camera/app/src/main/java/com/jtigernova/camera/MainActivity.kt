package com.jtigernova.camera

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.lifecycle.*
import com.jtigernova.camera.manager.CameraManagerCameraX
import com.jtigernova.camera.manager.CameraManagerLegacy
import com.jtigernova.camera.manager.CameraManagerPostLollipop
import com.jtigernova.camera.manager.ICameraManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CameraManagerLegacy.ICameraHandler,
    CameraManagerPostLollipop.ICameraHandler, CameraManagerCameraX.ICameraHandler, CoroutineScope {
    private var cameraManager: ICameraManager? = null

    private var cameraPermissionAsked = false

    private lateinit var viewModel: Fake

    class Fake : ViewModel() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this).get(Fake::class.java)
    }

    private fun isAtLeastLollipop(): Boolean {
        return Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP
    }

    override fun onResume() {
        super.onResume()

        useCamera()
    }

    override fun onDestroy() {
        cameraManager?.release()

        try {
            coroutineContext.cancel()
        } catch (e: Exception) {
        }

        super.onDestroy()
    }

    private fun useCamera() {
        if (cameraManager == null) {
            cameraManager = if (isAtLeastLollipop()) {
//            CameraManagerPostLollipop(context = context,
//                    handler = this)
                val lc = lifecycle

                CameraManagerCameraX(context = this, cameraHandler = this) { lc }
            } else {
                CameraManagerLegacy(
                    context = this,
                    cameraHandler = this,
                    coroutineScope = this
                )
            }
        }

        cameraManager?.useFrontFacingCamera()
    }

    override fun onPause() {
        cameraManager?.release()

        super.onPause()
    }

    override fun getCameraPreviewActivity(): Activity? {
        return this
    }

    override fun onPreviewReady(preview: CameraManagerLegacy.CameraPreview) {
        findViewById<FrameLayout>(R.id.camera_preview)?.addView(preview)
    }

    override fun onPreviewReady(preview: CameraManagerPostLollipop.CameraPreview) {
        findViewById<FrameLayout>(R.id.camera_preview)?.addView(preview)
    }

    override fun onPreviewDestroyed() {
        findViewById<FrameLayout>(R.id.camera_preview)?.removeAllViews()
    }

    override fun onCameraPermissionNeeded() {
        if (cameraPermissionAsked)
            return

        ActivityCompat.requestPermissions(
            this, arrayOf(Manifest.permission.CAMERA),
            REQUEST_CAMERA
        )

        cameraPermissionAsked = true
    }

    override fun getPreviewView(): PreviewView? {
        return findViewById(R.id.camera_preview)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode != REQUEST_CAMERA) {
            return
        }

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            useCamera()
        } else {
            Log.e("JoinSession", "User denied camera permission :(. ")
        }
    }

    override fun onCameraOpenFailed() {
    }

    override fun onCameraDisconnected() {

    }

    override fun onPreviewFailed() {
    }

    companion object {
        const val REQUEST_CAMERA = 1
    }

    override val coroutineContext: CoroutineContext = Dispatchers.Main
}