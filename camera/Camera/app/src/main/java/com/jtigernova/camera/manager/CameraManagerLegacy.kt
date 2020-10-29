@file:Suppress("DEPRECATION")

package com.jtigernova.camera.manager

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Camera
import android.os.Build
import android.util.Log
import android.view.Surface
import android.view.SurfaceHolder
import android.view.SurfaceView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

/**
 * Legacy camera manager. Note: if this implementation of {@see ICameraManager} is used
 * on Android devices with Android Lollipop (5.0) (API 21) or newer, an exception is throw.
 * It's important to check the OS version before using this implementation
 *
 * @param context Context
 * @param coroutineScope Coroutine scope used to open cameras (potentially a long running process)
 */
class CameraManagerLegacy(private val context: Context?,
                          private val cameraHandler: ICameraHandler,
                          private val coroutineScope: CoroutineScope) : ICameraManager {
    private var camera: Camera? = null
    private var camPreview: CameraPreview? = null

    companion object {
        const val TAG = "CameraManagerLegacy"
    }

    /**
     * Camera handler required for legacy camera manager
     */
    interface ICameraHandler {
        /**
         * Gets activity needed for learning the current screen orientation
         *
         * @return Activity where the camera preview is shown
         */
        fun getCameraPreviewActivity(): Activity?

        /**
         * When the camera preview is ready to be displayed
         */
        fun onPreviewReady(preview: CameraPreview)

        /**
         * When the camera preview view is destroyed. Consider cleaning up view resources
         */
        fun onPreviewDestroyed()

        /**
         * When the using the camera failed for any reason
         */
        fun onCameraOpenFailed()
    }

    /**
     * @throws UnsupportedOperationException If OS is Android Lollipop (5.0) (API 21) or newer
     */
    override fun hasAnyCamera(): Boolean {
        checkAndroidVersion()

        return context?.packageManager?.hasSystemFeature(PackageManager.FEATURE_CAMERA) == true
    }

    /**
     * @throws UnsupportedOperationException If OS is Android Lollipop (5.0) (API 21) or newer
     */
    override fun hasFrontFacingCamera(): Boolean {
        checkAndroidVersion()

        return getFrontFacingCameraId() != -1
    }

    private fun getFrontFacingCameraId(): Int {
        with(Camera.CameraInfo()) {
            for (cam in 0 until Camera.getNumberOfCameras()) {
                Camera.getCameraInfo(cam, this)

                if (facing == Camera.CameraInfo.CAMERA_FACING_FRONT)
                    return cam
            }
        }

        return -1
    }

    /**
     * @throws UnsupportedOperationException If OS is Android Lollipop (5.0) (API 21) or newer
     */
    override fun useFrontFacingCamera() {
        checkAndroidVersion()

        //make sure camera is in good state
        release()

        val camId = getFrontFacingCameraId()

        if (camId == -1) {
            cameraHandler.onCameraOpenFailed()
            return
        }

        coroutineScope.launch {
            getCameraInstance(camId)?.let { cam ->
                matchCameraToCurrentOrientation(activity = cameraHandler.getCameraPreviewActivity(),
                        cameraId = camId, camera = cam)

                camPreview = context?.let { context ->
                    CameraPreview(context, cam, cameraHandler)
                }

                // Set the Preview view as the content of our activity.
                camPreview?.let {
                    cameraHandler.onPreviewReady(preview = it)
                }
            } ?: cameraHandler.onCameraOpenFailed()
        }
    }

    private fun matchCameraToCurrentOrientation(activity: Activity?,
                                                cameraId: Int, camera: Camera) {
        val info = Camera.CameraInfo()
        Camera.getCameraInfo(cameraId, info)

        activity?.let {
            val rotation: Int = activity.windowManager.defaultDisplay.rotation
            var degrees = 0
            when (rotation) {
                Surface.ROTATION_0 -> degrees = 0
                Surface.ROTATION_90 -> degrees = 90
                Surface.ROTATION_180 -> degrees = 180
                Surface.ROTATION_270 -> degrees = 270
            }
            var result: Int
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                result = (info.orientation + degrees) % 360
                result = (360 - result) % 360 // compensate the mirror
            } else {  // back-facing
                result = (info.orientation - degrees + 360) % 360
            }
            camera.setDisplayOrientation(result)
        }
    }

    @SuppressLint("ViewConstructor")
    class CameraPreview(
            context: Context,
            private val mCamera: Camera,
            private val mCameraHandler: ICameraHandler
    ) : SurfaceView(context), SurfaceHolder.Callback {

        private val mHolder: SurfaceHolder = holder.apply {
            // Install a SurfaceHolder.Callback so we get notified when the
            // underlying surface is created and destroyed.
            addCallback(this@CameraPreview)
            // deprecated setting, but required on Android versions prior to 3.0
            setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
        }

        override fun surfaceCreated(holder: SurfaceHolder) {
            // The Surface has been created, now tell the camera where to draw the preview.
            mCamera.apply {
                try {
                    setPreviewDisplay(holder)
                    startPreview()
                } catch (e: IOException) {
                    Log.e(TAG, "Error setting camera preview: ${e.message}")
                }
            }
        }

        override fun surfaceDestroyed(holder: SurfaceHolder) {
            Log.i(TAG, "Camera preview surface destroyed, cleaning up...")
            mCamera.release()
            mCameraHandler.onPreviewDestroyed()
        }

        override fun surfaceChanged(holder: SurfaceHolder, format: Int, w: Int, h: Int) {
            // If your preview can change or rotate, take care of those events here.
            // Make sure to stop the preview before resizing or reformatting it.
            if (mHolder.surface == null) {
                // preview surface does not exist
                return
            }

            // stop preview before making changes
            try {
                mCamera.stopPreview()
            } catch (e: Exception) {
                // ignore: tried to stop a non-existent preview
            }

            // set preview size and make any resize, rotate or
            // reformatting changes here

            // start preview with new settings
            mCamera.apply {
                try {
                    setPreviewDisplay(mHolder)
                    startPreview()
                } catch (e: Exception) {
                    Log.e(TAG, "Error starting camera preview: ${e.message}")
                }
            }
        }
    }

    /**
     * @throws UnsupportedOperationException If OS is Android Lollipop (5.0) (API 21) or newer
     */
    override fun release() {
        checkAndroidVersion()

        camera?.release()
        camPreview = null
        camera = null

        Log.i(TAG, "Camera released. Good job")
    }

    private fun checkAndroidVersion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            throw UnsupportedOperationException("$TAG is not supported on " +
                    "Android API ${Build.VERSION_CODES.LOLLIPOP} or newer")
        }
    }

    /**
     * Returns a camera. Using a suspend function because opening a camera is a long running
     * process on some devices
     */
    private suspend fun getCameraInstance(cameraId: Int): Camera? {
        if (camera != null)
            return camera

        //the Android doc states:
        //<q>On some devices, this method may take a long time to complete. It is best to call
        // this method from a worker thread (possibly using AsyncTask) to avoid blocking the
        // main application UI thread.</q>
        //.......
        //so we will use a background dispatcher
        withContext(Dispatchers.IO) {
            try {
                camera = Camera.open(cameraId)
            } catch (e: Exception) {
                Log.e(
                    TAG, "**Could not load camera. It's either in use or does not exist. " +
                        "${e.message}")
            }
        }

        return camera
    }
}