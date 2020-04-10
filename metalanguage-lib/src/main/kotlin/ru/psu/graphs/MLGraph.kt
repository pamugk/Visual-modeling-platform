package ru.psu.graphs

import java.util.*
import kotlin.collections.HashSet

class MLGraph(var parentId:UUID?) {
    val entities:MutableSet<UUID> = HashSet()
    val ports:MutableSet<UUID> = HashSet()
    val relations:MutableSet<UUID> = HashSet()
}