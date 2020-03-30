package com.telstra.facts.arch.viewmodel

import io.reactivex.schedulers.Schedulers

val testSchedulers = object : UseCaseSchedulers {
    override val subscribeScheduler = Schedulers.trampoline()
    override val observeScheduler = Schedulers.trampoline()
}
