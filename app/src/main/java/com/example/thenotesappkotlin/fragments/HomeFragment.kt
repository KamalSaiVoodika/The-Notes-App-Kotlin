package com.example.thenotesappkotlin.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.thenotesappkotlin.MainActivity
import com.example.thenotesappkotlin.R
import com.example.thenotesappkotlin.adapter.NotesAdapter
import com.example.thenotesappkotlin.databinding.FragmentHomeBinding
import com.example.thenotesappkotlin.model.Note
import com.example.thenotesappkotlin.viewmodel.NoteViewModel


class HomeFragment : Fragment(R.layout.fragment_home), SearchView.OnQueryTextListener,
    MenuProvider {

    private var homeBinding: FragmentHomeBinding? = null
    private val binding get() = homeBinding!!

    private lateinit var notesViewModel: NoteViewModel
    private lateinit var notesAdapter: NotesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
        notesViewModel = (activity as MainActivity).noteViewModel
        setUpHomeRecyclerView()
        binding.addNoteFab.setOnClickListener(View.OnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_addNoteFragment2)
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        homeBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText != null) {
            searchNote(newText)
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        homeBinding = null
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.home_menu, menu)
        val menuSearch = menu.findItem(R.id.searchMenu).actionView as SearchView
        menuSearch.isSubmitButtonEnabled = false
        menuSearch.setOnQueryTextListener(this)
    }

    private fun searchNote(query: String?) {
        if (query?.isNotEmpty() == true) {
            notesViewModel.searchNote(query).observe(this) { list ->
                notesAdapter.differ.submitList(list)
                updateUI(list)
            }
        } else {
            getAllNotes()
        }
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        TODO("Not yet implemented")
    }

    private fun updateUI(noteList: List<Note>) {
        if (noteList.isNotEmpty()) {
            binding.ivEmptyState.visibility = View.GONE
            binding.homeRecyclerView.visibility = View.VISIBLE
        } else {
            binding.ivEmptyState.visibility = View.VISIBLE
            binding.homeRecyclerView.visibility = View.GONE
        }
    }

    private fun setUpHomeRecyclerView() {
        notesAdapter = NotesAdapter()
        binding.homeRecyclerView.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            setHasFixedSize(true)
            adapter = notesAdapter
        }
    }

    private fun getAllNotes() {
        activity?.let {
            notesViewModel.getAllNotes().observe(viewLifecycleOwner) { noteList ->
                notesAdapter.differ.submitList(noteList)
                updateUI(noteList)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        getAllNotes()
    }

}