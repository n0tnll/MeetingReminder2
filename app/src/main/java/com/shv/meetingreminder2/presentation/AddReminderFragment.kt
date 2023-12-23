package com.shv.meetingreminder2.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.shv.meetingreminder2.R
import com.shv.meetingreminder2.databinding.FragmentAddReminderBinding

class AddReminderFragment : Fragment() {

    private var _binding: FragmentAddReminderBinding? = null
    private val binding: FragmentAddReminderBinding
        get() = _binding ?: throw RuntimeException("FragmentAddReminderBinding is null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddReminderBinding.inflate(
            layoutInflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.etClient.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.main_fragment_container, ClientsListFragment.newInstance())
                .commit()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {

        fun newInstance(): AddReminderFragment {
            return AddReminderFragment()
        }
    }
}