package com.pierluigizagaria.totemo.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products_table")
class Product(
    var plu: Int,
    var name: String,
    var ingredients: Array<String>,
) {

    @PrimaryKey(autoGenerate = true)
    var id: Long? = null

    var imageUUID: String? = null
}