package com.pixelmon.utm.features.team

data class Species(
    val id: String,
    val name: String,
)

data class TeamMember(
    val species: Species,
    val nickname: String? = null,
)

data class Team(
    val name: String,
    val members: List<TeamMember> = emptyList()
)