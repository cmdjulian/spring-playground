package de.cmdjulian.playground.notebook.adapter

import jakarta.validation.constraints.Size

data class NotebookCommand(

    @get:Size(min = 3, max = 128)
    val title: String,

    @get:Size(max = 1024)
    val description: String?,

    val tags: List<String> = emptyList()

)
