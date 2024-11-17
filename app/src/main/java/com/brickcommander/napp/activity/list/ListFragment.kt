package com.brickcommander.napp.activity.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.brickcommander.napp.R
import com.brickcommander.napp.model.Enums
import com.brickcommander.napp.model.Item

class ListFragment : Fragment() {

    companion object {
        private const val TAG = "ListFragment"

        private const val ARG_LIST_TYPE = "list_type"

        fun newInstance(listType: Enums): ListFragment {
            val fragment = ListFragment()
            val args = Bundle().apply {
                putString(ARG_LIST_TYPE, listType.name)
            }
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var listView: ListView
    private val items: MutableList<Item> = mutableListOf()

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
        listView = view.findViewById(R.id.list_view)

        configureList(arguments?.getString(ARG_LIST_TYPE) ?: Enums.ITEMS.name)

        listView.setOnItemClickListener { _, _, position, _ ->
            val item = items[position]
            Toast.makeText(requireContext(), "Clicked item: ${item.getName()} : ${position}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun configureList(listType: String) {

        items.add(Item())
        items.add(Item())
        items.add(Item())
        items.add(Item())
        items.add(Item())
        items.add(Item())
        items.add(Item())
        items.add(Item())
        items.add(Item())

        when (listType) {
            Enums.PURCHASES.name -> {
                val adapter = ListItemAdapter(requireContext(), items.toList())
                listView.adapter = adapter
            }
            Enums.CUSTOMERS.name -> {
                val adapter = ListItemAdapter(requireContext(), items.toList())
                listView.adapter = adapter
            }
            else -> {
                val adapter = ListItemAdapter(requireContext(), items.toList())
                listView.adapter = adapter
            }
        }
    }
}
