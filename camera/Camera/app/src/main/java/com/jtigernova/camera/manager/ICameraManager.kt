package com.jtigernova.camera.manager

/**
 * Camera manager functionality
 */
interface ICameraManager {
    /**
     * Whether the device has a camera
     *
     * @return True if there is a camera, false otherwise
     */
    fun hasAnyCamera(): Boolean

    /**
     * Whether the device has a front facing camera
     *
     * @return True if there is a front facing camera, false otherwise
     */
    fun hasFrontFacingCamera(): Boolean

    /**
     * Uses the front facing camera
     */
    fun useFrontFacingCamera()

    /**
     * Releases and closes camera resources. Ideally called from onDestroy() functions
     */
    fun release()
}