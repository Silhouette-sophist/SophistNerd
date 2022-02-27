package com.example.extend.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.extend.MainActivity2
import com.example.extend.databinding.FragmentHomeBinding
import com.example.testlifecycle.utils.showPositionLog

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

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
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        binding.lauchSecondPage.setOnClickListener {
            Intent(requireActivity(), MainActivity2::class.java).also{
                startActivity(it)
            }
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