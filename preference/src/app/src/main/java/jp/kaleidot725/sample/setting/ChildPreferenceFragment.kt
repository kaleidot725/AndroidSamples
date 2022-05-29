package jp.kaleidot725.sample.setting

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import jp.kaleidot725.sample.R

class ChildPreferenceFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.child_preferences, rootKey)
    }
}
