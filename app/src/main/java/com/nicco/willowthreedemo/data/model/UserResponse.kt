package com.nicco.willowthreedemo.data.model


data class UserResponse(
    val bio: String = "",
    val firstName: String = "",
    val headshot: Headshot = Headshot(),
    val id: String = "",
    val jobTitle: String = "",
    val lastName: String = "",
    val slug: String = "",
    val socialLinks: List<SocialLink> = emptyList(),
    val type: String = ""
)