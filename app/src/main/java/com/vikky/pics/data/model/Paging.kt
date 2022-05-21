package com.vikky.pics.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "paging")
data class Paging(
    @PrimaryKey val id: String = "0",
    val prev: Int?,
    val next: Int?
)