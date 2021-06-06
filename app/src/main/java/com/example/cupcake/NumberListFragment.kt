package com.example.cupcake

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.cupcake.databinding.FragmentNumberListBinding
import com.example.cupcake.multiplication.MultiplicationFunctions

class NumberListFragment : Fragment() {
    //Data binding with this class and UI
    private var binding: FragmentNumberListBinding? = null
    //"activityViewModels()" property delegation to call methods
    private val multView: MultiplicationFunctions by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentNumberListBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Bind data with start fragment outlook and its declared values (modified)
        binding?.numberListFragment = this
    }

    //What number will we practice multiplying
    fun practiceNumber(number: Int) {
        multView.pracNumber(number)
        findNavController().navigate(R.id.action_numberListFragment_to_multiplicationFragment)
    }

    //Destroy values and reset data binding
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}