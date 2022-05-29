package jp.kaleidot725.sample

import android.os.Parcel
import android.os.Parcelable

data class Number(val position: Int, val value: String?) : Parcelable {
    constructor(source: Parcel) : this(
        source.readInt(),
        source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(position)
        writeString(value)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Number> = object : Parcelable.Creator<Number> {
            override fun createFromParcel(source: Parcel): Number = Number(source)
            override fun newArray(size: Int): Array<Number?> = arrayOfNulls(size)
        }
    }
}
