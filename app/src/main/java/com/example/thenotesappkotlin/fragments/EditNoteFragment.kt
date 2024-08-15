package com.example.thenotesappkotlin.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.thenotesappkotlin.MainActivity
import com.example.thenotesappkotlin.R
import com.example.thenotesappkotlin.databinding.FragmentEditNoteBinding
import com.example.thenotesappkotlin.model.Note
import com.example.thenotesappkotlin.viewmodel.NoteViewModel


class EditNoteFragment : Fragment(R.layout.fragment_add_note), MenuProvider {

    lateinit var currentNoteData: Note
    lateinit var noteViewModel: NoteViewModel
    private var editNoteBinding: FragmentEditNoteBinding? = null
    private val binding get() = editNoteBinding!!
    private lateinit var editNoteView: View

    private val args: EditNoteFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        editNoteBinding = FragmentEditNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
        noteViewModel = (activity as MainActivity).noteViewModel
        editNoteView = view

        currentNoteData = args.note!!

        binding.editNoteTitle.setText(currentNoteData.noteTitle)
        binding.editNoteDesc.setText(currentNoteData.noteDesc)


        binding.editNoteFab.setOnClickListener(View.OnClickListener { updateNote(view) })
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.menu_edit_note, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.editMenu -> {
                deleteNote()
                true
            }

            else -> false
        }
    }

    private fun deleteNote() {

        AlertDialog.Builder(activity).apply {
            setTitle("Delete Note")
            setMessage("Do you Want to delete this Note?")
            setPositiveButton("Delete") { _, _ ->
                noteViewModel.deleteNote(currentNoteData)
                Toast.makeText(editNoteView.context, "Note Deleted", Toast.LENGTH_SHORT).show()
                view?.findNavController()?.popBackStack(R.id.homeFragment, false)
            }
            setNegativeButton("Cancel", null)
        }.create().show()
    }

    private fun updateNote(view: View) {
        val noteTitle = binding.editNoteTitle.text.toString().trim()
        val noteDesc = binding.editNoteDesc.text.toString().trim()
        if (noteTitle.isNotEmpty()) {
            noteViewModel.editNote(Note(currentNoteData.id, noteTitle, noteDesc))
            Toast.makeText(editNoteView.context, "Note Saved", Toast.LENGTH_SHORT).show()
            view.findNavController().popBackStack(R.id.homeFragment, false)
        } else {
            Toast.makeText(editNoteView.context, "Please Enter Note Title", Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        editNoteBinding = null
    }


}