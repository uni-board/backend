package core

import core.crypt.CryptService
import core.crypt.impl.CryptServiceImpl
import core.util.Time
import core.util.impl.TimeImpl
import org.koin.dsl.module

val coreModule = module {
    single<Time> { TimeImpl() }
    single<CryptService> { CryptServiceImpl() }
}