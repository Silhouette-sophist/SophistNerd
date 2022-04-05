package com.example.extend.ui.notifications

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.extend.databinding.FragmentNotificationsBinding
import com.example.extend.utils.showPositionLog

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showPositionLog()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textNotifications
        notificationsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        showPositionLog()
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        showPositionLog()
    }

    override fun onStart() {
        super.onStart()
        showPositionLog()
    }

    override fun onResume() {
        super.onResume()
        showPositionLog()
    }

    override fun onPause() {
        super.onPause()
        showPositionLog()
    }

    override fun onStop() {
        super.onStop()
        showPositionLog()
    }

    override fun onDestroy() {
        super.onDestroy()
        showPositionLog()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        showPositionLog()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        showPositionLog()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showPositionLog()
    }
}