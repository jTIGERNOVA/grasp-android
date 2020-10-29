package com.jtigernova.camera.manager

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.camera2.*
import android.hardware.camera2.CameraMetadata.LENS_FACING_FRONT
import android.os.Build
import android.util.Log
import android.util.Range
import android.view.Surface
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import java.util.concurrent.Semaphore

class CameraManagerPostLollipop(private val context: Context?,
                                private val handler: ICameraHandler
) : ICameraManager {
    companion object {
        const val TAG = "CameraManagerLollipop"
    }

    private var cameraDevice: CameraDevice? = null
    private var cameraSession: CameraCaptureSession? = null

    /**
     * A [Semaphore] to prevent the app from exiting before closing the camera.
     */
    private val mCameraOpenCloseLock: Semaphore = Semaphore(1)

    /**
     * Camera handler required for camera manager
     */
    interface ICameraHandler {

        /**
         * When permission to use the camera is needed
         */
        fun onCameraPermissionNeeded()

        /**
         * When the using the camera failed for any reason
         */
        fun onCameraOpenFailed()

        /**
         * When the camera is disconnected
         */
        fun onCameraDisconnected()

        /**
         * When the camera preview is ready to be displayed
         */
        fun onPreviewReady(preview: CameraPreview)

        /**
         * When the creating the camera preview fails
         */
        fun onPreviewFailed()

        /**
         * When the camera preview view is destroyed. Consider cleaning up view resources
         */
        fun onPreviewDestroyed()
    }

    override fun hasAnyCamera(): Boolean {
        return context?.packageManager?.hasSystemFeature(PackageManager.FEATURE_CAMERA) == true
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun hasFrontFacingCamera(): Boolean {
        return getFrontFacingCameraId() != null
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun getFrontFacingCameraId(): String? {
        getCameraManager()?.let { manager ->
            try {
                for (cameraId in manager.cameraIdList) {
                    val chars = manager.getCameraCharacteristics(cameraId)

                    if (chars.get(CameraCharacteristics.LENS_FACING) == LENS_FACING_FRONT) {
                        return cameraId
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Camera error ${e.message}")
            }
        }

        return null
    }

    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun useFrontFacingCamera() {

        getFrontFacingCameraId()?.let { cameraId ->
            context?.let {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    Log.i(
                        TAG, "Permission not granted to camera. Doing nothing for now. " +
                            "Ask for permission and try again once granted")
                    handler.onCameraPermissionNeeded()
                    return
                }

                //mCameraOpenCloseLock.acquire()

                getCameraManager()?.openCamera(cameraId, object : CameraDevice.StateCallback() {
                    override fun onOpened(openedCamera: CameraDevice) {
                        release()

                        cameraDevice = openedCamera

                        //update camera preview

                        fun setupPreview(pCamera: CameraDevice, pPreviewSurface: Surface) {
                            // Create a capture session using the predefined targets; this also involves defining the
                            // session state callback to be notified of when the session is ready
                            //TODO check SDK
                            pCamera.createCaptureSession(listOf(pPreviewSurface),
                                    object : CameraCaptureSession.StateCallback() {
                                        @RequiresApi(Build.VERSION_CODES.M)
                                        override fun onConfigured(session: CameraCaptureSession) {
                                            cameraSession = session
                                            Log.i(TAG, "Camera session configured")
                                            val captureRequest = session.device.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
                                            captureRequest.addTarget(pPreviewSurface)

                                            fun getRange(): Range<Int> {
                                                val chars = getCameraManager()?.getCameraCharacteristics(cameraId)

                                                val ranges = chars!!.get(CameraCharacteristics.CONTROL_AE_AVAILABLE_TARGET_FPS_RANGES);
                                                var result: Range<Int>? = null
                                                for (range in ranges!!) {
                                                    val upper = range.upper;

                                                    // 10 - min range upper for my needs
                                                    if (upper >= 10) {
                                                        if (result == null || upper < result.upper) {
                                                            result = range;
                                                        }
                                                    }
                                                }
                                                //return result!!//15 on my device
                                                return Range(30,30)
                                            }

                                            captureRequest.set(CaptureRequest.CONTROL_AE_TARGET_FPS_RANGE, getRange());//This line of code is used for adjusting the fps range and fixing the dark preview
                                            captureRequest.set(CaptureRequest.CONTROL_AE_LOCK, false);
                                            captureRequest.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER_START);

                                            // Auto focus should be continuous for camera preview.
                                            captureRequest.set(CaptureRequest.CONTROL_AF_MODE,
                                                    CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE)
                                            captureRequest.set(CaptureRequest.CONTROL_AE_MODE,
                                                    CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH)
                                            captureRequest.set(CaptureRequest.CONTROL_SCENE_MODE, CaptureRequest.CONTROL_SCENE_MODE_NIGHT)

                                            // The first null argument corresponds to the capture callback, which you should
                                            // provide if you want to retrieve frame metadata or keep track of failed capture
                                            // requests that could indicate dropped frames; the second null argument
                                            // corresponds to the Handler used by the asynchronous callback, which will fall
                                            // back to the current thread's looper if null
                                            // This will keep sending the capture request as frequently as possible until the
                                            // session is torn down or session.stopRepeating() is called
                                            session.setRepeatingRequest(captureRequest.build(), null, null)
                                        }

                                        override fun onConfigureFailed(session: CameraCaptureSession) {
                                            Log.e(TAG, "Error configuring camera session")
                                            handler.onCameraOpenFailed()
                                        }

                                        override fun onClosed(session: CameraCaptureSession) {
                                            super.onClosed(session)

                                            Log.i(TAG, "Camera session closed")
                                        }
                                    }, null)
                        }

                        // configure camera with all the surfaces to be ever used
                        try {
                            val tCameraPreview = CameraPreview(context = context,
                                    onSurfaceCreated = {
                                        setupPreview(pCamera = openedCamera, pPreviewSurface = it.surface)
                                    },
                                    onSurfaceChanged = {
                                        stopPreview()
                                    },
                                    onSurfaceDestroyed = {
                                        stopPreview()
                                    }
                            )

                            handler.onPreviewReady(tCameraPreview)
                        } catch (e: Exception) {
                            Log.e(TAG, "Error setting up preview: ${e.message}")
                            handler.onPreviewFailed()
                        }
                    }

                    private fun stopPreview() {
                        // stop preview before making changes
                        try {
                            cameraSession?.stopRepeating()
                        } catch (e: Exception) {
                            Log.e(TAG, "Failed to stop camera session: ${e.message}")
                        }
                    }

                    override fun onDisconnected(camera: CameraDevice) {
                        release()

                        handler.onCameraDisconnected()
                    }

                    override fun onError(camera: CameraDevice, error: Int) {
                        release()

                        handler.onCameraOpenFailed()
                    }

                }, null)
            }
        }
    }

    @SuppressLint("ViewConstructor")
    class CameraPreview(
            context: Context,
            private val onSurfaceCreated: (holder: SurfaceHolder) -> Unit,
            private val onSurfaceChanged: (holder: SurfaceHolder) -> Unit,
            private val onSurfaceDestroyed: (holder: SurfaceHolder) -> Unit
    ) : SurfaceView(context), SurfaceHolder.Callback {

        private val mHolder: SurfaceHolder = holder.apply {
            // Install a SurfaceHolder.Callback so we get notified when the
            // underlying surface is created and destroyed.
            addCallback(this@CameraPreview)
        }

        override fun surfaceCreated(holder: SurfaceHolder) {
            // The Surface has been created, now tell the camera where to draw the preview.
            Log.i(TAG, "Camera preview surface created")
            onSurfaceCreated(holder)
        }

        override fun surfaceDestroyed(holder: SurfaceHolder) {
            Log.i(TAG, "Camera preview surface destroyed, cleaning up...")
            onSurfaceDestroyed(holder)
        }

        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        override fun surfaceChanged(holder: SurfaceHolder, format: Int, w: Int, h: Int) {
            Log.i(TAG, "Camera preview surface changed")
            // If your preview can change or rotate, take care of those events here.
            // Make sure to stop the preview before resizing or reformatting it.
            if (mHolder.surface == null) {
                // preview surface does not exist
                return
            }

            onSurfaceChanged(holder)
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun release() {
        Log.i(TAG, "Releasing camera...")
        try {
            mCameraOpenCloseLock.acquire()
            cameraDevice?.close()
            cameraDevice = null
        } finally {
            mCameraOpenCloseLock.release()
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun getCameraManager(): CameraManager? {
        return context?.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    }
}