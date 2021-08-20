package com.b1u3_22.nox.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.b1u3_22.nox.R
import com.b1u3_22.nox.databinding.FragmentSettingsBinding
import com.b1u3_22.nox.db.internalDB
import com.b1u3_22.nox.db.settings.setting
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsFragment : Fragment() {

    private lateinit var settingsViewModel: SettingsViewModel
    private var _binding: FragmentSettingsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        settingsViewModel =
            ViewModelProvider(this).get(SettingsViewModel::class.java)

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val database: internalDB = internalDB.getDatabaseInstance(requireContext())

        //Load setting for switches from database
        var allowAlarmsSetting: setting = database.settingsDAO().getSetting("allowAlarms")
        var allowOffsetSetting: setting = database.settingsDAO().getSetting("allowOffset")
        var allowWakeTimeSetting: setting = database.settingsDAO().getSetting("allowWakeTime")

        //Load setting for timePickers from database
        var bedTimeSetting: setting = database.settingsDAO().getSetting("bedTime")
        var sleepTimeSetting: setting = database.settingsDAO().getSetting("sleepTime")
        var offsetTimeSetting: setting = database.settingsDAO().getSetting("offsetTime")
        var wakeTimeSetting: setting = database.settingsDAO().getSetting("wakeTime")

        //Find switches by their id
        val allowAlarms: SwitchMaterial = root.findViewById(R.id.allowAlarms)
        val allowOffset: SwitchMaterial = root.findViewById(R.id.allowOffset)
        val allowWakeTime: SwitchMaterial = root.findViewById(R.id.allowWakeTime)

        //Find timePickers by their id
        val bedTime: TimePicker = root.findViewById(R.id.bedTime)
        val sleepTime: TimePicker = root.findViewById(R.id.sleepTime)
        val offsetTime: TimePicker = root.findViewById(R.id.bedTimeOffset)
        val wakeTime: TimePicker = root.findViewById(R.id.wakeTime)

        //Set switches checked state according to values from db
        allowAlarms.isChecked = allowAlarmsSetting.value.toBoolean()
        allowOffset.isChecked = allowOffsetSetting.value.toBoolean()
        allowWakeTime.isChecked = allowWakeTimeSetting.value.toBoolean()

        //Update values in db when switches change value
        allowAlarms.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                allowAlarmsSetting = setting(allowAlarmsSetting.settingID, allowAlarmsSetting.name, "true")
                database.settingsDAO().updateSetting(allowAlarmsSetting)
            }else{
                allowAlarmsSetting = setting(allowAlarmsSetting.settingID, allowAlarmsSetting.name, "false")
                database.settingsDAO().updateSetting(allowAlarmsSetting)
            }
        }

        allowOffset.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                allowOffsetSetting = setting(allowOffsetSetting.settingID, allowOffsetSetting.name, "true")
                database.settingsDAO().updateSetting(allowOffsetSetting)
            }else{
                allowOffsetSetting = setting(allowOffsetSetting.settingID, allowOffsetSetting.name, "false")
                database.settingsDAO().updateSetting(allowOffsetSetting)
            }
        }

        allowWakeTime.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                allowWakeTimeSetting = setting(allowWakeTimeSetting.settingID, allowWakeTimeSetting.name, "true")
                database.settingsDAO().updateSetting(allowAlarmsSetting)
            }else{
                allowWakeTimeSetting = setting(allowWakeTimeSetting.settingID, allowWakeTimeSetting.name, "false")
                database.settingsDAO().updateSetting(allowWakeTimeSetting)
            }
        }

        // Set timePickers to 24h view. Why can't it be done in .xml for bloody sake
        bedTime.setIs24HourView(true)
        sleepTime.setIs24HourView(true)
        offsetTime.setIs24HourView(true)
        wakeTime.setIs24HourView(true)

        //Set default values in timePickers according to values in db
        var time = bedTimeSetting.value?.split(":")
        var hour: Int = time?.get(0)?.toInt()!!
        var minute: Int = time?.get(1)?.toInt()!!
        bedTime.hour = hour
        bedTime.minute = minute

        time = sleepTimeSetting.value?.split(":")
        hour = time?.get(0)?.toInt()!!
        minute = time?.get(1)?.toInt()!!
        sleepTime.hour = hour
        sleepTime.minute = minute

        time = offsetTimeSetting.value?.split(":")
        hour = time?.get(0)?.toInt()!!
        minute = time?.get(1)?.toInt()!!
        offsetTime.hour = hour
        offsetTime.minute = minute

        time = wakeTimeSetting.value?.split(":")
        hour = time?.get(0)?.toInt()!!
        minute = time?.get(1)?.toInt()!!
        wakeTime.hour = hour
        wakeTime.minute = minute

        // update on every time change == not very wise and probably should be reworked or updated in onDestroy TODO()
        bedTime.setOnTimeChangedListener{_, hour, minute, ->
            database.settingsDAO().updateSetting(setting(bedTimeSetting.settingID, bedTimeSetting.name, "$hour:$minute"))
        }

        sleepTime.setOnTimeChangedListener{_, hour, minute, ->
            database.settingsDAO().updateSetting(setting(sleepTimeSetting.settingID, sleepTimeSetting.name, "$hour:$minute"))
        }

        offsetTime.setOnTimeChangedListener{_, hour, minute, ->
            database.settingsDAO().updateSetting(setting(offsetTimeSetting.settingID, offsetTimeSetting.name, "$hour:$minute"))
        }

        wakeTime.setOnTimeChangedListener{_, hour, minute, ->
            database.settingsDAO().updateSetting(setting(wakeTimeSetting.settingID, wakeTimeSetting.name, "$hour:$minute"))
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}