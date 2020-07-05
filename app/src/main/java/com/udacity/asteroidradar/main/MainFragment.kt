package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.adapters.AsteroidListAdapter
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.model.Asteroid
import com.udacity.asteroidradar.model.PictureOfDay

class MainFragment : Fragment(), AsteroidListAdapter.OnClickListener {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentMainBinding.inflate(inflater)
        val adapter = AsteroidListAdapter(this@MainFragment)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.pictureOfDay = null
        binding.asteroidRecycler.apply {
            this.adapter = adapter
            this.setHasFixedSize(true)
        }

        viewModel.asteroidsLiveData.observe(viewLifecycleOwner, Observer { asteroids ->
            adapter.submitList(asteroids)
        })

        viewModel.picOfTheDayLiveData.observe(viewLifecycleOwner, Observer {
            if (it is PictureOfDay) binding.pictureOfDay = it
        })


        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
    }

    override fun onClick(asteroid: Asteroid) {
        findNavController().navigate(MainFragmentDirections.actionShowDetail(asteroid))
    }
}
