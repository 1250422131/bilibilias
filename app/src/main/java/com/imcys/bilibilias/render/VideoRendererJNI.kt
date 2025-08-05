package com.imcys.bilibilias.render

typealias Ptr = Long

object VideoRendererJNI {

    init {
        System.loadLibrary("bilibilias")
    }

    external fun createRenderer(): Ptr
    external fun destroyRenderer(ptr: Ptr)
    external fun onSurfaceCreated(ptr: Ptr)
    external fun onSurfaceChanged(ptr: Ptr, width: Int, height: Int)
    external fun onDrawFrame(ptr: Ptr)

}