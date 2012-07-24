package com.goldblastgames.simonsays

import reactive.EventSource

class HandlerSource(
    val startValue: Long,
    val interval: Long,
    val until: (Long) => Boolean) extends EventSource {
}
