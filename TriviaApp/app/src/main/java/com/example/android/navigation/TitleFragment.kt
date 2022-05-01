package com.example.android.navigation

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.android.navigation.databinding.FragmentTitleBinding

class TitleFragment : Fragment() {

    private lateinit var titleFragmentBind: FragmentTitleBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        titleFragmentBind =
            DataBindingUtil.inflate(inflater, R.layout.fragment_title, container, false)

        titleFragmentBind.playButton.setOnClickListener {
            it.findNavController().navigate(R.id.action_titleFragment_to_gameFragment)
            //Navigation.createNavigateOnClickListener(R.id.action_titleFragment_to_gameFragment)
        }

        setHasOptionsMenu(true)

        return titleFragmentBind.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.overflow_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item, view!!.findNavController()) || super.onOptionsItemSelected(item)
    }
}