package de.cmdjulian.playground.tag.application

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class TagNotFoundException(name: String) : Exception("Tag with name '$name' not found")
