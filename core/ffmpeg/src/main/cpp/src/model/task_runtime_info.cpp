//
// Created by 萌新杰少 on 2026/1/4.
//

// task_runtime_info.cpp
#include "task_runtime_info.hpp"

namespace model::task_runtime_info {
    TaskRuntimeInfo parse(const std::string& s) {
        nlohmann::json j = nlohmann::json::parse(s);
        return j.get<TaskRuntimeInfo>();
    }
}