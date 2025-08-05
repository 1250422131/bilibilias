cmake_minimum_required(VERSION 3.22.1)
project("bilibilias")


file(GLOB_RECURSE BILI_AS_SOURCES "src/*.cpp")

add_library(${CMAKE_PROJECT_NAME} SHARED ${BILI_AS_SOURCES})

target_include_directories(${CMAKE_PROJECT_NAME} PUBLIC include)

target_link_libraries(${CMAKE_PROJECT_NAME} PUBLIC
    android
    log
    GLESv3
    EGL
)