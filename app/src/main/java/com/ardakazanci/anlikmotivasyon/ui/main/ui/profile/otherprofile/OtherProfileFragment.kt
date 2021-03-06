package com.ardakazanci.anlikmotivasyon.ui.main.ui.profile.otherprofile

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer

import com.ardakazanci.anlikmotivasyon.R
import com.ardakazanci.anlikmotivasyon.databinding.OtherprofileFragmentBinding

class OtherProfileFragment : Fragment() {

    companion object {
        fun newInstance() =
            OtherProfileFragment()
    }

    private lateinit var viewModel: OtherProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<OtherprofileFragmentBinding>(
            inflater,
            R.layout.otherprofile_fragment,
            container,
            false
        )

        viewModel = ViewModelProviders.of(this).get(OtherProfileViewModel::class.java)

        arguments!!.let { arguments ->
            val args = OtherProfileFragmentArgs.fromBundle(arguments)

            viewModel._otherUserId.value = args.userid

        }

        viewModel.otherUserIsFollow.observe(this, Observer {

            if (it) {
                binding.btnFollow.visibility = View.INVISIBLE
                binding.btnUnfollow.visibility = View.VISIBLE
            } else {
                binding.btnFollow.visibility = View.VISIBLE
                binding.btnUnfollow.visibility = View.INVISIBLE
            }

        })



        binding.otherprofileviewmodel = viewModel

        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.onClearedCoroutines()
    }

}
