package com.example.restaurantapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantapp.R
import com.example.restaurantapp.adapter.MenuAdapter
import com.example.restaurantapp.model.MenuItem
import com.example.restaurantapp.model.Modification
import com.example.restaurantapp.viewmodel.SharedViewModel

class MenuFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var menuAdapter: MenuAdapter
    private var allMenuItems: List<MenuItem> = listOf()

    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_menu, container, false)

        recyclerView = view.findViewById(R.id.recycler_view_menu)
        searchView = view.findViewById(R.id.searchView)

        recyclerView.layoutManager = LinearLayoutManager(activity)

        allMenuItems = loadMenuFromResources()

        menuAdapter = MenuAdapter(allMenuItems) { menuItem ->
            val dialog = MenuItemDetailDialogFragment(menuItem) { quantity, selectedModifications ->
                sharedViewModel.addItem(menuItem, selectedModifications, quantity)
            }
            dialog.show(childFragmentManager, "MenuItemDetailDialog")
        }
        recyclerView.adapter = menuAdapter

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                val filtered = if (newText.isNullOrEmpty()) allMenuItems
                else allMenuItems.filter {
                    it.name.contains(newText, true) || it.description.contains(newText, true)
                }
                menuAdapter.updateList(filtered)
                return true
            }
        })

        return view
    }

    private fun loadMenuFromResources(): List<MenuItem> {
        val ctx = requireContext()
        val names = ctx.resources.getStringArray(R.array.menu_item_names)
        val descriptions = ctx.resources.getStringArray(R.array.menu_item_descriptions)
        val prices = ctx.resources.getStringArray(R.array.menu_item_prices)

        return names.mapIndexed { index, name ->
            val description = descriptions.getOrNull(index) ?: ""
            val price = prices.getOrNull(index)?.toDoubleOrNull() ?: 0.0
            val mods = loadModifications(name)

            val imageResId = when (name.lowercase()) {
                "pizza"                -> R.drawable.pizza
                "calzone"              -> R.drawable.calzone
                "pizza by the slice"   -> R.drawable.slice
                "heroes"               -> R.drawable.hero
                "pasta"               -> R.drawable.spaghetti
                else                   -> R.drawable.citypizza
            }

            MenuItem(
                id = index + 1,
                name = name,
                description = description,
                price = price,
                availableModifications = mods,
                imageResId = imageResId
            )
        }
    }

    private fun loadModifications(itemName: String): List<Modification> {
        val context = requireContext()
        val modArrayId = when (itemName.lowercase()) {
            "pizza" -> R.array.pizza_modifications
            "calzone" -> R.array.Calzone_modifications
            "pasta" -> R.array.pasta_modifications
            "pizza by the slice" -> R.array.slice_modifications
            "heroes" -> R.array.hero_modifications
            "drinks" -> R.array.drink_modifications
            "desserts" -> R.array.dessert_modifications
            else -> null
        }

        return modArrayId?.let { id ->
            context.resources.getStringArray(id).mapNotNull { line ->
                val parts = line.split(":")
                if (parts.size == 2) {
                    val label = parts[0].trim()
                    val price = parts[1].trim().toDoubleOrNull() ?: 0.0
                    Modification(label, price)
                } else null
            }
        } ?: emptyList()
    }
}