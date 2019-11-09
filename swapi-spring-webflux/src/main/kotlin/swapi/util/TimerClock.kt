package swapi.util

class TimerClock {

    private var startTimeMillis: Long = 0
    private var stopTimeMillis: Long = 0

    fun start(): TimerClock {
        startTimeMillis = System.currentTimeMillis()
        return this
    }

    fun stop(): TimerClock {
        stopTimeMillis = System.currentTimeMillis()
        return this
    }

    fun reset() {
        startTimeMillis = 0
        stopTimeMillis = 0
    }

    fun durationMillis() = stopTimeMillis - startTimeMillis

    fun durationSeconds(): Float = durationMillis() / 1000f
}