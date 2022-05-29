package jp.kaleidot725.counter.shared

class MyCounter(val min: Int, val max: Int) {
    var value: Int = 0
        private set

    init {
        check(min < max)
    }

    fun plus() {
        if (max < (value + 1)) {
            return
        }

        value++
    }

    fun minus() {
        if ((value - 1) < min) {
            return
        }

        value--
    }
}