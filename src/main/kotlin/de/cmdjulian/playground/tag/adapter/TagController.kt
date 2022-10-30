package de.cmdjulian.playground.tag.adapter

import de.cmdjulian.playground.shared.TAG_ENDPOINT
import de.cmdjulian.playground.tag.application.TagService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(TAG_ENDPOINT)
class TagController(private val service: TagService) {

    @PostMapping
    fun createTag(@RequestBody command: TagModel): TagModel {
        return command.toEntity()
            .let(service::createTag)
            .toModel()
    }

    @GetMapping
    fun findTags(): List<TagModel> {
        return service.findTags().map { tag -> tag.toModel() }
    }

    @GetMapping("/{name}")
    fun findTag(@PathVariable name: String): TagModel {
        return service.findTag(name).toModel()
    }

    @PutMapping("/{name}")
    fun updateTag(@PathVariable name: String, command: TagUpdateCommand) = service.updateTag(name, command.color)

    @DeleteMapping("/{name}")
    fun deleteTag(@PathVariable name: String) = service.deleteTag(name)
}
