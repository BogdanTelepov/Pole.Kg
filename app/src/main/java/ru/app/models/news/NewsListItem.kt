package ru.app.models.news

data class NewsListItem(
        val id: Int,
        val title: String,
        val body: String,
        val preview: String,
        val created_at: String
)