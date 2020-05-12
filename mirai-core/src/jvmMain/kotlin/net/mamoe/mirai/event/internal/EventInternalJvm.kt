/*
 * Copyright 2020 Mamoe Technologies and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/mamoe/mirai/blob/master/LICENSE
 */

@file:Suppress("unused")

package net.mamoe.mirai.event.internal

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.mamoe.mirai.event.Event
import net.mamoe.mirai.event.Listener
import net.mamoe.mirai.event.ListeningStatus
import net.mamoe.mirai.utils.MiraiInternalAPI
import java.util.function.Consumer
import java.util.function.Function
import kotlin.coroutines.EmptyCoroutineContext

@Suppress("FunctionName")
internal fun <E : Event> Class<E>._subscribeEventForJaptOnly(
    scope: CoroutineScope,
    onEvent: Function<E, ListeningStatus>
): Listener<E> {
    return this.kotlin.subscribeInternal(
        scope.Handler(
            scope.coroutineContext,
            Listener.ConcurrencyKind.LOCKED
        ) { withContext(Dispatchers.IO) { onEvent.apply(it) } })
}

@Suppress("FunctionName")
internal fun <E : Event> Class<E>._subscribeEventForJaptOnly(scope: CoroutineScope, onEvent: Consumer<E>): Listener<E> {
    return this.kotlin.subscribeInternal(
        scope.Handler(
            EmptyCoroutineContext,
            Listener.ConcurrencyKind.LOCKED
        ) { withContext(Dispatchers.IO) { onEvent.accept(it) }; ListeningStatus.LISTENING; })
}