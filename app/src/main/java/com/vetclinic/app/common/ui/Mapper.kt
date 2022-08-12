package com.vetclinic.app.common.ui

interface Mapper<I, O> {
    fun map(data: I): O
}