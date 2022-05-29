package jp.kaleidot725.sample.setting

import android.os.Bundle
import android.widget.SeekBar
import androidx.preference.*
import jp.kaleidot725.sample.R

class ParentPreferenceFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.parent_preferences, rootKey)


        val dropDownPreference = findPreference<DropDownPreference>("dropDownPreference")
        dropDownPreference?.summaryProvider = Preference.SummaryProvider<DropDownPreference> {
            "entry :" + it.entry + " value : " + it.value
        }

        val listPreference = findPreference<ListPreference>("listPreference")
        listPreference?.summaryProvider = Preference.SummaryProvider<ListPreference> {
            "entry :" + it.entry + " value :" + it.value
        }

        val multiSelectListPreference = findPreference<MultiSelectListPreference>("multiSelectListPreference")
        multiSelectListPreference?.summaryProvider = Preference.SummaryProvider<MultiSelectListPreference> {
             "values :" + it.values
        }

        val editTextPreference = findPreference<EditTextPreference>("editTextPreference")
        editTextPreference?.summaryProvider = Preference.SummaryProvider<EditTextPreference> {
            "value :" + it.text
        }
    }
}
