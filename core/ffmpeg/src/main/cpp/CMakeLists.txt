cmake_minimum_required(VERSION 3.22.1)
project("ffmpeg")

set(ffmpeg_lib_dir ${CMAKE_SOURCE_DIR}/ffmpeg/${ANDROID_ABI}/lib)
set(ffmpeg_head_dir ${CMAKE_SOURCE_DIR}/ffmpeg/include)

include_directories(${ffmpeg_head_dir})

add_library(avcodec SHARED IMPORTED)
set_target_properties(avcodec PROPERTIES IMPORTED_LOCATION ${ffmpeg_lib_dir}/libavcodec.so)

add_library(avdevice SHARED IMPORTED)
set_target_properties(avdevice PROPERTIES IMPORTED_LOCATION ${ffmpeg_lib_dir}/libavdevice.so)

add_library(avfilter SHARED IMPORTED)
set_target_properties(avfilter PROPERTIES IMPORTED_LOCATION ${ffmpeg_lib_dir}/libavfilter.so)

add_library(avformat SHARED IMPORTED)
set_target_properties(avformat PROPERTIES IMPORTED_LOCATION ${ffmpeg_lib_dir}/libavformat.so)

add_library(avutil SHARED IMPORTED)
set_target_properties(avutil PROPERTIES IMPORTED_LOCATION ${ffmpeg_lib_dir}/libavutil.so)

add_library(swresample SHARED IMPORTED)
set_target_properties(swresample PROPERTIES IMPORTED_LOCATION ${ffmpeg_lib_dir}/libswresample.so)

add_library(swscale SHARED IMPORTED)
set_target_properties(swscale PROPERTIES IMPORTED_LOCATION ${ffmpeg_lib_dir}/libswscale.so)

add_library(dav1d SHARED IMPORTED)
set_target_properties(dav1d PROPERTIES IMPORTED_LOCATION ${ffmpeg_lib_dir}/libdav1d.so)

add_library(fftools SHARED IMPORTED)
set_target_properties(fftools PROPERTIES IMPORTED_LOCATION ${ffmpeg_lib_dir}/libfftools.so)

file(GLOB_RECURSE BILI_AS_FFMPEG_SOURCES "src/*.cpp")

add_library(${CMAKE_PROJECT_NAME} SHARED ${BILI_AS_FFMPEG_SOURCES})

#target_sources(${CMAKE_PROJECT_NAME} PRIVATE
#        FILE_SET CXX_MODULES
#        TYPE CXX_MODULES
#        FILES ${BILI_AS_FFMPEG_MODULES}
#)


# 5、链接静态库和安卓常用库
target_link_libraries(${CMAKE_PROJECT_NAME}
        avutil
        avcodec
        avdevice
        avfilter
        avformat
        swresample
        swscale
        dav1d
        fftools
        android
        log
        dl
        m
        atomic
)

set(ARCH_LINK_FLAGS "-Wl,-z,max-page-size=16384")
target_link_options(${CMAKE_PROJECT_NAME} PUBLIC ${ARCH_LINK_FLAGS})