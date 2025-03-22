cmake_minimum_required(VERSION 3.31.0)
project(linear_systems VERSION 1.0.0 LANGUAGES C CXX)

set(CMAKE_CXX_STANDARD 23)
set(CMAKE_CXX_STANDARD_REQUIRED ON)
set(CMAKE_EXPORT_COMPILE_COMMANDS ON)

find_package(Boost REQUIRED COMPONENTS system filesystem)
find_package(Armadillo REQUIRED)

function(add_example example_name)
    set(example_target ${example_name})
    add_executable(${example_target} src/${example_name}.cpp)
    set_target_properties(${example_target} PROPERTIES OUTPUT_NAME ${example_target})
    target_link_libraries(${example_target} Boost::system Boost::filesystem)

    target_link_libraries(${example_target} ${ARMADILLO_LIBRARIES})
    target_include_directories(${example_target} PRIVATE ${ARMADILLO_INCLUDE_DIRS})
endfunction()

add_example(example1)
add_example(example2)
add_example(example3)
