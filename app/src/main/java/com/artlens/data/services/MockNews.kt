package com.artlens.data.services


import com.artlens.data.models.NewsItem

object MockNews {
    val newsList = listOf(
        NewsItem(1, "The Mona Lisa in Digital Era", "Exploring how AI is recreating classic art.", "https://ichef.bbci.co.uk/ace/ws/640/cpsprodpb/ea99/live/a687f720-bec4-11ee-9972-4f9c2a756523.jpg.webp"),
        NewsItem(2, "Van Gogh's Starry Night Auctioned", "The iconic painting fetched $1 billion.", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTDuXRsUi_vW5fZKRvlB41OoexUjhckdOrURQ&s"),
        NewsItem(3, "New Age of Sculpture", "Modern sculptors redefining art.", "https://assets-global.website-files.com/5fa50f157f3d0938227083c2/658d9fe9dd2cec5744fed9f0_contemporary%20sculpture%20now%20from%20japan.webp")
    )
}
