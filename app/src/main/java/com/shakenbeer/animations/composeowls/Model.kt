package com.shakenbeer.animations.composeowls

import androidx.annotation.DrawableRes
import com.shakenbeer.animations.R

interface Bird {
    val image: Int
    val name: String
}

class Owl(override val image: Int, override val name: String): Bird

val owls = listOf<Bird>(Owl(R.drawable.owl,"Pretty Owl"),
    Owl(R.drawable.owl_asleep, "Sleeping Owl"),
    Owl(R.drawable.owl_drunk, "Drunk Owl"),
    Owl(R.drawable.owl_flower, "Spring Owl"),
    Owl(R.drawable.owl_glasses, "Myopia Owl"),
    Owl(R.drawable.owl_tablet, "Internet Owl"),
    Owl(R.drawable.owl_witch, "Witch Owl"))