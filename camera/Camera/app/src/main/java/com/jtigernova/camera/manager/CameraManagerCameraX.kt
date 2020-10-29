package com.jtigernova.camera.manager

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CameraMetadata
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraManagerCameraX(private val context: Context?,
                           private val cameraHandler: ICameraHandler,
                           private val lifecycleOwner: LifecycleOwner) : ICameraManager {

    companion object {
        const val TAG = "CameraManagerCameraX"
    }

    private var cameraExecutor: ExecutorService = Executors.newSingleThreadExecutor()

    /**
     * Camera handler required for camera manager
     */
    interface ICameraHandler {

        /**
         * When permission to use the camera is needed
         */
        fun onCameraPermissionNeeded()

        fun getPreviewView(): PreviewView?
    }

    override fun hasAnyCamera(): Boolean {
        return context?.packageManager?.hasSystemFeature(PackageManager.FEATURE_CAMERA) == true
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun hasFrontFacingCamera(): Boolean {
        return getFrontFacingCameraId() != null
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun useFrontFacingCamera() {
        getFrontFacingCameraId()?.let { cameraId ->
            context?.let {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    Log.i(
                        TAG, "Permission not granted to camera. Doing nothing for now. " +
                            "Ask for permission and try again once granted")
                    cameraHandler.onCameraPermissionNeeded()
                    return
                }

                val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

                cameraProviderFuture.addListener(Runnable {
                    // Used to bind the lifecycle of cameras to the lifecycle owner
                    val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

                    // Preview
                    val preview = Preview.Builder()
                            .build()
                            .also {
                                it.setSurfaceProvider(cameraHandler.getPreviewView()?.surfaceProvider)
                            }

                    val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

                    try {
                        // Unbind use cases before rebinding
                        cameraProvider.unbindAll()

                        // Bind use cases to camera
                        cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, preview)

                    } catch (exc: Exception) {
                        Log.e(TAG, "Use case binding failed", exc)
                    }

                }, ContextCompat.getMainExecutor(context))
            }
        }
    }

    override fun release() {
        //TODO do something
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun getFrontFacingCameraId(): String? {
        getCameraManager()?.let { manager ->
            try {
                for (cameraId in manager.cameraIdList) {
                    val chars = manager.getCameraCharacteristics(cameraId)

                    if (chars.get(CameraCharacteristics.LENS_FACING) == CameraMetadata.LENS_FACING_FRONT) {
                        return cameraId
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Camera error ${e.message}")
            }
        }

        return null
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun getCameraManager(): CameraManager? {
        return context?.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    }
}