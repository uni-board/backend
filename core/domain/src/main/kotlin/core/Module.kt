package core

import core.crypt.CryptService
import core.crypt.impl.CryptServiceImpl
import org.koin.dsl.module

val coreModule = module {
    single<CryptService>{ CryptServiceImpl() }
}