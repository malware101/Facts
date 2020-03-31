package com.telstra.facts.model

data class Facts(
    val title: String,
    val rows: List<Fact>
)