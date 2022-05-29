package jp.kaleidot725.sample.model

class CounterRepository {
    var count = 0
        private set

    fun plus() {
        count++
    }

    fun minus() {
        count--
    }
}