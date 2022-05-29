package jp.kaleidot725.navgraph.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import jp.kaleidot725.navgraph.R
import jp.kaleidot725.navgraph.databinding.FragmentFirstBinding
import jp.kaleidot725.navgraph.model.Counter
import jp.kaleidot725.navgraph.viewmodel.CounterViewModel

@AndroidEntryPoint
class FirstFragment : Fragment() {
    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CounterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.v("FirstFragment", "onCreateView")
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onResume() {
        Log.v("FirstFragment", "onResume")
        super.onResume()
    }

    override fun onPause() {
        Log.v("FirstFragment", "onPause")
        super.onPause()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.navigateButton.setOnClickListener {
            Log.v("FirstFragment", "NAVIGATE FRAGMENT")
            findNavController().navigate(R.id.action_firstFragment_to_secondFragment)
        }

        binding.navigateDialogButton.setOnClickListener {
            Log.v("FirstFragment", "NAVIGATE DIALOG")
            findNavController().navigate(R.id.action_firstFragment_to_thirdFragment)
        }


        val backstackEntry = findNavController().currentBackStackEntry ?: return
        backstackEntry.lifecycle.addObserver(object: LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
            fun onCreate() {
                Log.v("BackStackEntry", "onCreate")
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_START)
            fun onStart() {
                Log.v("BackStackEntry", "onStart")
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
            fun onResume() {
                Log.v("BackStackEntry", "onResume")
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
            fun onPause() {
                Log.v("BackStackEntry", "onPause")
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
            fun onStop() {
                Log.v("BackStackEntry", "onStop")
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroy() {
                Log.v("BackStackEntry", "onDestory")
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
            fun onAny() {
                Log.v("BackStackEntry", "onAny")
            }
        })

        viewModel.countLiveData.observe(backstackEntry) {
            Log.v("FirstFragment", "POPUP: ${it.toString()}")
            binding.countText.text = "POPUP:" + it.toString()
        }
    }

    override fun onDestroy() {
        Log.v("FirstFragment", "onDestroy")
        super.onDestroy()
        _binding = null
    }
}
