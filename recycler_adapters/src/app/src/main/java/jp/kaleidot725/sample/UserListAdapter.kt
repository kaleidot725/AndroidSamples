package jp.kaleidot725.sample

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import jp.kaleidot725.sample.databinding.LayoutUserItemBinding

/**
 * 生成した View を保持する、 bind で User を　View に反映させる
 */
class UserItemViewHolder(
    private val binding: LayoutUserItemBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(user: User) {
        binding.firstNameTextView.text = user.firstName
        binding.lastNameTextView.text = user.lastName
        binding.ageTextView.text = user.age.toString()
    }
}

/**
 * User の差分確認する
 */
val DIFF_UTIL_USER_ITEM_CALLBACK = object : DiffUtil.ItemCallback<User>() {
    // 渡されたデータが同じ値であるか確認をする
    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem == newItem
    }

    // 渡されたデータが同じ項目であるか確認する
    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem.id == newItem.id
    }
}

/**
 * ListAdapter<A: Object, B: RecyclerView.ViewHolder>(C: DiffUtils.ItemCallback<A>) を継承する
 * 今回は User を表示するので、User を表示するための View を保持した ViewHolder と User の差分を比較するための ItemCallback をセットしてやります。
 *
 * - A:Object には表示するデータを保持するクラスをセットする
 * - B:RecyclerView.ViewHolder には表示する View を保持する ViewHolder をセットする
 * - C:DiffUtils.ItemCallback<A> には A:Object の差分確認方法を実装した ItemCallback をセットする
 */
class UserListAdapter : ListAdapter<User, UserItemViewHolder>(DIFF_UTIL_USER_ITEM_CALLBACK) {
    // ここで View を生成する、生成した View は ViewHolder に格納して、戻り値として返す
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserItemViewHolder {
        val view = LayoutUserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserItemViewHolder(view)
    }

    // その位置の User を取得し、ViewHolder を通じて View に User 情報をセットする
    override fun onBindViewHolder(holderUser: UserItemViewHolder, position: Int) {
        holderUser.bind(getItem(position))
    }
}