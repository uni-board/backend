package core.util.impl

import core.util.Time

class TimeImpl : Time {
    override fun millis(): Long {
        return System.currentTimeMillis()
    }
}