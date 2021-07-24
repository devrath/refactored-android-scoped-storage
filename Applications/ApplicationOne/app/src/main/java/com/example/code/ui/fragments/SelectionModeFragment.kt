package com.example.code.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import com.example.code.R
import com.example.code.databinding.FragmentSelectModeBinding


class SelectionModeFragment : DialogFragment() {

    private var _binding: FragmentSelectModeBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val TAG = "SelectionModeFragment"
        fun newInstance() : SelectionModeFragment {
            return SelectionModeFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.DialogTheme_transparent);
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSelectModeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    private fun setupClickListeners() = binding.apply {
        btnPrivateTrueId.setOnClickListener {
            dismiss()
        }

        btnPrivateFalseId.setOnClickListener {
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}