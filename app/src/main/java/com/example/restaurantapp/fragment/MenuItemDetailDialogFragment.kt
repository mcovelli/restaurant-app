package com.example.restaurantapp.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.restaurantapp.R
import com.example.restaurantapp.model.MenuItem
import com.example.restaurantapp.model.Modification

class MenuItemDetailDialogFragment(
    private val menuItem: MenuItem,
    private val onItemConfigured: (quantity: Int, modifications: List<Modification>) -> Unit
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.dialog_menu_item_detail, null)


        val spinnerQuantity = view.findViewById<Spinner>(R.id.spinnerQuantity)
        val quantities = (1..10).toList()
        val quantityAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, quantities)
        quantityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerQuantity.adapter = quantityAdapter


        val listViewModifications = view.findViewById<ListView>(R.id.listViewModifications)
        val availableMods = menuItem.availableModifications
        val modNames = availableMods.map { it.name }
        val listAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_multiple_choice, modNames)
        listViewModifications.choiceMode = ListView.CHOICE_MODE_MULTIPLE
        listViewModifications.adapter = listAdapter

        return AlertDialog.Builder(requireContext())
            .setTitle(menuItem.name)
            .setView(view)
            .setPositiveButton("Add to Order") { dialog, _ ->
                val selectedQuantity = quantities[spinnerQuantity.selectedItemPosition]

                val selectedMods = mutableListOf<Modification>()
                for (i in 0 until listViewModifications.count) {
                    if (listViewModifications.isItemChecked(i)) {
                        selectedMods.add(availableMods[i])
                    }
                }
                onItemConfigured(selectedQuantity, selectedMods)
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
    }
}