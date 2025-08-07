cmake_minimum_required(VERSION 3.22.1)
project("ffmpeg")

# 1、定义静态库和头文件所在目录
set(ffmpeg_lib_dir ${CMAKE_SOURCE_DIR}/ffmpeg/${ANDROID_ABI}/lib)
set(ffmpeg_head_dir ${CMAKE_SOURCE_DIR}/ffmpeg/include)

# 2、添加头文件目录
include_directories(${ffmpeg_head_dir})

# 3、导入FFmpeg所有静态库
add_library(avcodec STATIC IMPORTED)
set_target_properties(avcodec PROPERTIES IMPORTED_LOCATION ${ffmpeg_lib_dir}/libavcodec.so)

add_library(avdevice STATIC IMPORTED)
set_target_properties(avdevice PROPERTIES IMPORTED_LOCATION ${ffmpeg_lib_dir}/libavdevice.so)

add_library(avfilter STATIC IMPORTED)
set_target_properties(avfilter PROPERTIES IMPORTED_LOCATION ${ffmpeg_lib_dir}/libavfilter.so)

add_library(avformat STATIC IMPORTED)
set_target_properties(avformat PROPERTIES IMPORTED_LOCATION ${ffmpeg_lib_dir}/libavformat.so)

add_library(avutil STATIC IMPORTED)
set_target_properties(avutil PROPERTIES IMPORTED_LOCATION ${ffmpeg_lib_dir}/libavutil.so)

add_library(swresample STATIC IMPORTED)
set_target_properties(swresample PROPERTIES IMPORTED_LOCATION ${ffmpeg_lib_dir}/libswresample.so)

add_library(swscale STATIC IMPORTED)
set_target_properties(swscale PROPERTIES IMPORTED_LOCATION ${ffmpeg_lib_dir}/libswscale.so)

# 4、你的 native 库

file(GLOB_RECURSE BILI_AS_FFMPEG_SOURCES "src/*.cpp")
#file(GLOB_RECURSE BILI_AS_FFMPEG_MODULES "src/*.ixx")

add_library(${CMAKE_PROJECT_NAME} SHARED ${BILI_AS_FFMPEG_SOURCES})
target_include_directories(${CMAKE_PROJECT_NAME} PUBLIC include)

#target_sources(${CMAKE_PROJECT_NAME} PRIVATE
#        FILE_SET CXX_MODULES
#        TYPE CXX_MODULES
#        FILES ${BILI_AS_FFMPEG_MODULES}
#)


# 5、链接静态库和安卓常用库
target_link_libraries(${CMAKE_PROJECT_NAME} PUBLIC
        avutil
        avcodec
        avdevice
        avfilter
        avformat
        swresample
        swscale
        android
        log
        dl
        m
        atomic
        GLESv3
        EGL
)

set(ARCH_LINK_FLAGS "-Wl,-z,max-page-size=16384" )
target_link_options(${CMAKE_PROJECT_NAME} PUBLIC ${ARCH_LINK_FLAGS} )