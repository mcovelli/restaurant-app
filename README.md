Restaurant App – UI Prototype

Overview
This Android Studio prototype demonstrates the core user interface and navigation flow for the Restaurant App. 
It includes all primary screens connected via a Bottom Navigation bar. 
No backend logic or real data is integrated—mock data populates lists and placeholders are used where appropriate. 
Navigation between fragments is implemented, but business logic (e.g. checkout, order submission) remains stubbed.

Screens

1. Menu Screen
    - Shows a scrollable list of dishes.
    - Tapping an item opens a detail dialog for quantity and options.

2. Cart Screen
    - Displays items added from the Menu.
    - Allows adjusting quantity or removing items.
    - “Checkout” button is present and produces confirmation screen.
    - Displays Total price of order and pick up time after order is placed.
    - Displays Toast once order is placed.

4. Order History Screen
    - Lists previous mock orders (date, total, status, order time, items).
    - Up button to back to Cart.

5. Directions Screen
    - Contains an input field for your starting address and a Go button.
    - On tap, launches the Google Maps app via Intent to navigate from your entered address to SUNY Old Westbury.

Notes
- Commented code in CartFagment.kt that allows clearing of database (order history)
  - fun clearHistory() {
    val db = OrderHistoryDatabase.getDatabase(requireContext())
    val dao = db.orderHistoryDao()
    lifecycleScope.launch {
    dao.clearAllOrders()
    }
    }
    clearHistory()
  - 
- clearAllOrders() in OrderHistoryDao
  - @Query("DELETE FROM order_history")
    suspend fun clearAllOrders()

Notes on Incomplete Features
- Checkout Flow: No payment processing or order submission to restaurant.
- Customization Dialog: Selections in the MenuItem dialog do not persist into order history.
- Screen Responsiveness: Layouts use ConstraintLayout and ScrollView but may need adjustments on extreme device sizes.
- Input Validation: Directions input only checks for non-empty text; no further validation is performed.