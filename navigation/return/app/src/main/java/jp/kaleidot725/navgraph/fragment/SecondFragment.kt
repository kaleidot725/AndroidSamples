package jp.kaleidot725.navgraph.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import jp.kaleidot725.navgraph.databinding.FragmentSecondBinding
import jp.kaleidot725.navgraph.model.Counter
import jp.kaleidot725.navgraph.viewmodel.CounterViewModel

@AndroidEntryPoint
class SecondFragment  : Fragment() {
    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CounterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.v("SecondFragment", "onCreateView")
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.navigateButton.setOnClickListener {
            Log.v("SecondFragment", "POP BACK STACK")
            findNavController().popBackStack()
            viewModel.increment()
        }
    }

    override fun onResume() {
        Log.v("SecondFragment", "onResume")
        super.onResume()
    }

    override fun onPause() {
        Log.v("SecondFragment", "onPause")
        super.onPause()
    }

    override fun onDestroy() {
        Log.v("SecondFragment", "onDestroy")
        super.onDestroy()
        _binding = null
    }
}
