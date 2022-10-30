package de.cmdjulian.playground.tag.adapter

import de.cmdjulian.playground.tag.domain.Tag

fun TagModel.toEntity(): Tag = Tag(name, color)

fun Tag.toModel(): TagModel = TagModel(name, color)
