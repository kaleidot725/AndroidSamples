package jp.kaleidot725.sample.model

import android.util.Log
import jp.kaleidot725.sample.model.FooService
import javax.inject.Inject

class FooServiceImpl @Inject constructor():
    FooService {
    override fun print(tag: String, message: String) {
        Log.v(tag, message)
    }
}
