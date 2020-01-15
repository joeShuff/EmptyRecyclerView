package com.joeshuff.emptyrecyclerviewapp

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.text.bold
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.joeshuff.emptyrecyclerview.EmptyViewCreatedListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.example_empty_view.view.*
import kotlinx.android.synthetic.main.example_item.view.*
import kotlin.random.Random
import androidx.recyclerview.widget.DividerItemDecoration



class MainActivity : AppCompatActivity(), EmptyViewCreatedListener {
    var adapter: TestAdapter? = null

    val firstNames = listOf("Bob", "Mark", "Sarah", "Gary", "Helen", "Sparky", "Adrian", "Alan", "Casey", "Rela", "Eli", "Tom", "Bernie", "Herbert")
    val lastNames = listOf("Braunstein", "Templeton-Savage", "Headingsly", "Klompermouth", "Jones", "Restworth-Arthingular", "Brown", "Horridge-Romeleywood")

    var recentSearch: String = ""

    var existingUsers = arrayListOf<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        repeat(10) { i -> addOne(false)}
        adapter = TestAdapter(this, existingUsers)

        val dividerItemDecoration = DividerItemDecoration(this, RecyclerView.VERTICAL)
        mainRecyclerView.getRecyclerView()?.addItemDecoration(dividerItemDecoration)
        mainRecyclerView.setLayoutManager(LinearLayoutManager(this, RecyclerView.VERTICAL, false))
        mainRecyclerView.setAdapter(adapter)
        mainRecyclerView.setOnEmptyViewCreatedListener(this)

        example_searchField.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                setAdapterItemsBasedOnSearch(s.toString())
            }
        })
    }

    fun setAdapterItemsBasedOnSearch(search: String) {
        Log.i("SEARCH", "Search for $search")
        Log.i("SEARCH", "Total users: ${existingUsers.size}")

        recentSearch = search
        adapter?.setSearch(recentSearch)
    }

    override fun onCreated(view: View) {
        Log.i("EmptyRecyclerViewMain", "The empty view has been created and this is a reference to it if you'd like to modify anything within it")
    }

    override fun onShown(view: View?) {
        val translatedMessage = getString(R.string.no_results).split("%s")
        val s = SpannableStringBuilder()
            .append(translatedMessage[0])
            .bold { append(recentSearch) }
            .append(translatedMessage[1])

        view?.emptyText?.text = s

        if (existingUsers.size == 0) {
            view?.emptyText?.text = "No Users"
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_add -> addOne()
            R.id.menu_remove -> removeOne()
            R.id.menu_add_empty -> mainRecyclerView.setEmptyLayout(R.layout.example_empty_view)
            R.id.menu_remove_empty -> mainRecyclerView.removeEmptyLayout()
            R.id.menu_set_empty_adapter -> {
                existingUsers.clear()
                adapter = TestAdapter(this, existingUsers)
                mainRecyclerView.setAdapter(adapter)
            }
        }

        return true
    }

    private fun addOne(show: Boolean = true) {
        val newUser = User("${firstNames.shuffled()[0]} ${lastNames.shuffled()[0]}")
        if (show) Toast.makeText(this, "Adding ${newUser.name}", Toast.LENGTH_LONG).show()
        existingUsers.add(newUser)
        setAdapterItemsBasedOnSearch(recentSearch)
    }

    private fun removeOne() {
        if (existingUsers.isNotEmpty()) {
            val removed = existingUsers.removeAt(Random.nextInt(existingUsers.size))
            Toast.makeText(this, "Removed ${removed.name}", Toast.LENGTH_LONG).show()
            setAdapterItemsBasedOnSearch(recentSearch)
        }
    }

    inner class TestAdapter(val context: Context, var items: ArrayList<User>, var searchQuery: String = ""): RecyclerView.Adapter<TestViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = TestViewHolder(LayoutInflater.from(context).inflate(R.layout.example_item, parent, false))

        override fun getItemCount() = filteredItems().size

        override fun onBindViewHolder(holder: TestViewHolder, position: Int) {
            holder.bind(filteredItems()[position])
        }

        fun filteredItems() = items.filter { it.name.toLowerCase().contains(searchQuery.toLowerCase()) }

        fun setSearch(search: String) {
            searchQuery = search
            notifyDataSetChanged()
        }

        fun removeUser() {
            if (items.isNotEmpty()) items.removeAt(Random.nextInt(items.size))
        }

        fun addUser(user: User) {
            items.add(user)
            notifyDataSetChanged()
        }
    }

    inner class User(val name: String)

    inner class TestViewHolder(view: View): RecyclerView.ViewHolder(view) {
        fun bind(user: User) {
            itemView.example_text.text = user.name
        }
    }
}
