package de.cmdjulian.playground.notebook.adapter

import com.turkraft.springfilter.boot.Filter
import de.cmdjulian.playground.notebook.application.NotebookService
import de.cmdjulian.playground.notebook.domain.Notebook
import de.cmdjulian.playground.shared.NOTEBOOK_ENDPOINT
import jakarta.validation.Valid
import org.springframework.data.jpa.domain.Specification
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping(NOTEBOOK_ENDPOINT)
class NotebookController(private val service: NotebookService) {

    @PostMapping
    fun createNotebook(
        @Valid @RequestBody
        command: NotebookCommand
    ): NotebookDto {
        return service.createNotebook(command.toNotebook(), command.tags).toDto()
    }

    @GetMapping
    fun retrieveNotebooks(filters: NotebookFilters): List<NotebookDto> {
        return service.findAllNotebooks(filters.tagNames, filters.content).map(Notebook::toDto)
    }

    @GetMapping(params = ["!filter"])
    fun retrieveNotebooksFiltered(filters: NotebookFilters): List<NotebookDto> {
        return service.findAllNotebooks(filters.tagNames, filters.content).map(Notebook::toDto)
    }

    @GetMapping(params = ["filter"])
    fun search(@Filter specification: Specification<Notebook>): List<NotebookDto> {
        return service.findAllNotebooks(specification).map(Notebook::toDto)
    }

    @GetMapping("{id}")
    fun retrieveNotebook(@PathVariable id: UUID): NotebookDto {
        return service.findNotebook(id).toDto()
    }

    @PutMapping("{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    fun updateNotebook(@PathVariable id: UUID, @Valid @RequestBody command: NotebookCommand) {
        service.updateNotebook(id, command.title, command.description, command.tags)
    }

    @DeleteMapping("{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    fun deleteNotebook(@PathVariable id: UUID) {
        service.deleteNotebook(id)
    }
}

data class NotebookFilters(val tagNames: List<String>?, val content: String?)
