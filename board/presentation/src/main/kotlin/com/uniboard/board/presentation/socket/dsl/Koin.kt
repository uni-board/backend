package com.uniboard.board.presentation.socket.dsl

import org.koin.core.context.GlobalContext
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.mp.KoinPlatformTools

@SocketIODSL
inline fun <reified T : Any> SocketIO.inject(
    qualifier: Qualifier? = null,
    mode: LazyThreadSafetyMode = KoinPlatformTools.defaultLazyMode(),
    noinline parameters: ParametersDefinition? = null
): Lazy<T> {
    return GlobalContext.get().inject<T>(
        qualifier = qualifier,
        mode = mode,
        parameters = parameters
    )
}