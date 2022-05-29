package jp.kaleidot725.sample.model

import jp.kaleidot725.sample.model.FooService
import javax.inject.Inject

class FooUsecase @Inject constructor(private val service: FooService) {
    fun print(tag: String, message: String) {
        service.print(tag, message)
    }
}
