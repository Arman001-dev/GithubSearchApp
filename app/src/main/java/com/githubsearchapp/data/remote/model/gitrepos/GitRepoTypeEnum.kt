package com.githubsearchapp.data.remote.model.gitrepos

enum class GitRepoTypeEnum(val type: String) {
    ALL("all"),
    PUBLIC("public"),
    PRIVATE("private"),
    FORKS("forks"),
    SOURCES("sources"),
    MEMBER("member")
}