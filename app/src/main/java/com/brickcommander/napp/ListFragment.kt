package com.brickcommander.napp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.brickcommander.napp.model.Item

class ListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get the ListView from the layout
        val listView: ListView = view.findViewById(R.id.list_view)

        // Sample data for the ListView
        val items = mutableListOf<Item>()

        items.add(Item())
        items.add(Item())
        items.add(Item())
        items.add(Item())
        items.add(Item())
        items.add(Item())
        items.add(Item())
        items.add(Item())
        items.add(Item())

        val adapter = ListItemAdapter(requireContext(), items.toList())

        // Set up an ArrayAdapter to display the items in the ListView
//        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, items)
        listView.adapter = adapter
    }
}
