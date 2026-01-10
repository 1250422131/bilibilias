//
// Created by 萌新杰少 on 2026/1/4.
//

#ifndef BILIBILIAS_TASK_RUNTIME_INFO_HPP
#define BILIBILIAS_TASK_RUNTIME_INFO_HPP

#pragma once

#include <string>
#include <vector>
#include <optional>
#include <nlohmann/json.hpp>
#include "task_runtime_info.hpp"

namespace model::task_runtime_info {

    struct LocalSubtitle {
        std::string lang{};
        std::string path{};
    };

    struct LocalMediaResource {
        std::string path{};
        std::string fileType{};
        std::string trackName{};
    };

    struct TaskRuntimeInfo {
        std::vector<LocalSubtitle> subtitles{};
        std::string coverPath{};
        std::optional<LocalMediaResource> videoMedia{};
        std::vector<LocalMediaResource> audioMediaList{};
        std::string outMediaPath{};
    };

    NLOHMANN_DEFINE_TYPE_NON_INTRUSIVE(LocalSubtitle, lang, path)

    NLOHMANN_DEFINE_TYPE_NON_INTRUSIVE(TaskRuntimeInfo, subtitles, coverPath, outMediaPath,
                                       audioMediaList, videoMedia)

    NLOHMANN_DEFINE_TYPE_NON_INTRUSIVE(LocalMediaResource, path, fileType, trackName)
    TaskRuntimeInfo parse(const std::string& s);
} // model

#endif //BILIBILIAS_TASK_RUNTIME_INFO_HPP
