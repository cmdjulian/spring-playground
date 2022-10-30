package de.cmdjulian.playground.tag.application

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.CONFLICT)
class TagAlreadyExistsException(name: String) : RuntimeException("Tag with name '$name' already exists")
