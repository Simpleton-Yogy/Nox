package com.b1u3_22.nox.ui.home

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.b1u3_22.nox.R
import com.b1u3_22.nox.databinding.FragmentHomeBinding
import com.b1u3_22.nox.db.internalDB
import kotlinx.coroutines.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import kotlin.concurrent.timer

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    @InternalCoroutinesApi
    fun timeUpdater(timeInterval: Long, timeTextView: TextView): Job {
        return CoroutineScope(Dispatchers.Default).launch {
            while (NonCancellable.isActive) {
                timeTextView.text = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))
                delay(timeInterval)
            }
        }
    }

    @InternalCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val database: internalDB = internalDB.getDatabaseInstance(requireContext())

        val currentDateTime = LocalDateTime.now()
        val formatterTime = DateTimeFormatter.ofPattern("HH:mm:ss")
        val formatterDate = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)

        val time: TextView = root.findViewById(R.id.time)
        time.text = LocalDateTime.now().format(formatterTime)

        val date: TextView = root.findViewById(R.id.date)
        date.text = currentDateTime.format(formatterDate)

        val status: TextView = root.findViewById(R.id.status)
        if (database.statusDAO().getStatus().status!!){
            status.text = getString(R.string.home_nox_status_on)
        }else{
            status.text = getString(R.string.home_nox_status_off)
        }

        timeUpdater(1000, time)

        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}