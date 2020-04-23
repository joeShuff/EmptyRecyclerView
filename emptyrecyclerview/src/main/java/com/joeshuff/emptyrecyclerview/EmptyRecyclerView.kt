package com.joeshuff.emptyrecyclerview

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.base_emptyrecyclerview.view.*

class EmptyRecyclerView(context: Context, attrs: AttributeSet) : RelativeLayout(context, attrs) {

    private var TAG = "EmptyRecyclerView"

    /**
     * This is a reference to the layout Id that you want to show as the Empty Indicator
     */
    private var emptyLayoutId: Int? = null
    private var emptyView: View? = null

    /**
     * References to the elements that make up this View
     */
    private var recyclerView: RecyclerView? = null
    private var emptyContainer: RelativeLayout? = null

    /**
     * Boolean to decide whether to show the Empty Indicator or not.
     *
     * Set to false using @see<removeEmptyLayout> and to true using @see<showEmptyLayout>
     */
    private var shouldShowEmptyIndicator = true

    /**
     * The listener to notify when the Empty Indicator is created so that you can modify the view
     */
    private var onCreatedListener: EmptyViewCreatedListener? = null

    /**
     * Constructor to initialise the View and fetch attributes from the attributes
     */
    init {
        LayoutInflater.from(context).inflate(R.layout.base_emptyrecyclerview, this, true)

        recyclerView = emptyRecyclerView_recyclerView
        emptyContainer = emptyRecyclerView_emptyContainer

        context.theme.obtainStyledAttributes(attrs, R.styleable.EmptyRecyclerView, 0, 0).apply {
            setEmptyLayout(getResourceId(R.styleable.EmptyRecyclerView_empty_layout, R.layout.default_empty_layout))
        }
    }

    /**
     * Sets the @see<onCreatedListener> to the parameter.
     *
     * This parameter will get @see<onCreatedListener.onCreated> called when the view is created
     */
    fun setOnEmptyViewCreatedListener(listener: EmptyViewCreatedListener) {
        onCreatedListener = listener
    }

    /**
     * Sets @see<shouldShowEmptyIndicator> to false so that the empty indicator isn't shown. Effects visible immediately.
     */
    fun removeEmptyLayout() {
        shouldShowEmptyIndicator = false
        recyclerView?.adapter?.notifyDataSetChanged()
    }

    /**
     * Sets @see<shouldShowEmptyIndicator> to true so that the empty indicator is shown. Effects visible immediately.
     */
    fun showEmptyLayout() {
        shouldShowEmptyIndicator = true
        recyclerView?.adapter?.notifyDataSetChanged()
    }

    /**
     * Sets the @see<emptyLayoutId> to the value of the parameter if not null, then inflates that layout and places it inside
     * the empty indicator container. Effects visible immediately.
     */
    fun setEmptyLayout(@LayoutRes layoutId: Int? = null) {
        showEmptyLayout()
        layoutId?.let { emptyLayoutId = it }

        emptyLayoutId?.let {
            val newEmptyView = LayoutInflater.from(context).inflate(it, emptyContainer, false)
            newEmptyView.let {newView ->
                emptyView = newView
                emptyContainer?.removeAllViews()
                emptyContainer?.addView(newView)
                onCreatedListener?.onCreated(newView)
            }
        }

        recyclerView?.adapter?.notifyDataSetChanged()
    }

    /**
     * Private function called by the adapter change listener to toggle the visibility of the empty indicator
     */
    private fun updateVisibility(items: Int) {
        if (items == 0 && shouldShowEmptyIndicator) {
            emptyContainer?.visibility = View.VISIBLE
            recyclerView?.visibility = View.GONE
            onCreatedListener?.onShown(emptyView)
        } else {
            emptyContainer?.visibility = View.GONE
            recyclerView?.visibility = View.VISIBLE
        }
    }

    /**
     * Data observer to observe changes in the @see<RecyclerView> so that the empty indicator can be shown when the @see<RecyclerView> is empty.
     */
    private val dataObserver = object: RecyclerView.AdapterDataObserver() {
        override fun onChanged() {
            val adapter = recyclerView?.adapter
            if (adapter != null) {
                updateVisibility(adapter.itemCount)
            }
        }
    }

    /**
     * Getters for the 2 views used in this View
     */
    fun getRecyclerView() = recyclerView
    fun getEmptyLayout() = emptyContainer

    /**
     * Sets the layout manager of the recyclerview using @see<RecyclerView.setLayoutManager>
     */
    fun setLayoutManager(manager: RecyclerView.LayoutManager) {
        recyclerView?.layoutManager = manager
    }

    /**
     * Sets the adapter of the recycler and attaches the @see<dataObserver> to the adapter to be notified
     * of data changes.
     *
     * @param notifyChange : This parameter can be used to choose whether or not the data observer gets notified
     * of the change in data (which in this case is the setting of a new adapter). If you set to true and the new
     * adapter is empty, the EmptyView will be shown right away. So set to false if you want to load some data first
     * before the EmptyView is shown.
     *
     * NOTE: All subsequent changes to the dataset WILL notify the data observer, this just says whether the setting of
     * a new adapter should notify the observer.
     */
    fun setAdapter(adapter: RecyclerView.Adapter<*>?, notifyChange: Boolean = true) {
        Log.i(TAG, "Setting adapter")
        recyclerView?.adapter = adapter
        setEmptyLayout()
        adapter?.registerAdapterDataObserver(dataObserver)
        if (notifyChange) dataObserver.onChanged()
    }
}