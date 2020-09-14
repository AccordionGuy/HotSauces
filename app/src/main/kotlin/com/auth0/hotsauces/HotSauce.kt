package com.auth0.hotsauces

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Lob


@Entity
data class HotSauce(

    // This property maps to the primary key in the database.
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0,

    // These properties are likely NOT to be longer than 256 characters.
    var brandName: String = "",
    var sauceName: String = "",

    // These properties might be longer than 256 characters.
    @Lob
    var description: String = "",
    @Lob
    var url: String = "",

    var heat: Int = 0

)