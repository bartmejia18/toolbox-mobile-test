@file:Suppress("unused")

package com.test.toolboxmobile.core.adapters

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.test.toolboxmobile.R


@Suppress("UNCHECKED_CAST")
fun <T> RecyclerView.initAdapter(layout: Int): RecyclerViewAdapter<T> {
    adapter = RecyclerViewAdapter<T>(context, layout)
    return adapter as RecyclerViewAdapter<T>
}

@Suppress("UNCHECKED_CAST")
fun <T> ViewPager2.initAdapter(layout: Int): RecyclerViewAdapter<T> {
    adapter = RecyclerViewAdapter<T>(context, layout)
    return adapter as RecyclerViewAdapter<T>
}

@Suppress("UNCHECKED_CAST")
fun <T> RecyclerView.getTypedAdapter(): RecyclerViewAdapter<T> {
    return adapter as RecyclerViewAdapter<T>
}

@Suppress("UNCHECKED_CAST")
fun <T> ViewPager2.getTypedAdapter(): RecyclerViewAdapter<T> {
    return adapter as RecyclerViewAdapter<T>
}

open class RecyclerViewAdapter<T>(private val context: Context, private val layout: Int) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    internal var isLoading: Boolean = false

    private var items: MutableList<Item<T>> = mutableListOf()
    private var size: Int = 20
    private var progressBarLayoutId = R.layout.item_loader
    private var handler = Handler(Looper.getMainLooper())
    private var bindView: ((view: View, item: T?, position: Int) -> Unit)? = null
    private var click: ((view: View, item: T?, position: Int) -> Unit)? = null
    private var longClick: ((view: View, item: T?, position: Int) -> Unit)? = null
    private var loadMore: ((page: Int) -> Unit)? = null

    enum class ViewType(val type: Int) {
        ITEM(1), PROGRESS_BAR(2)
    }

    inner class Item<T>(var progress: Boolean = false, var item: T? = null)

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener, View.OnLongClickListener {
        init {
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
        }

        fun bindView(position: Int) {
            bindView?.invoke(itemView, items[position].item, position)
        }

        override fun onClick(view: View) {
            click?.invoke(view, items[adapterPosition].item, adapterPosition)
        }

        override fun onLongClick(view: View): Boolean {
            longClick?.invoke(view, items[adapterPosition].item, adapterPosition)
            return true
        }
    }

    inner class ProgressViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): RecyclerView.ViewHolder = when (viewType) {
        ViewType.PROGRESS_BAR.type -> ProgressViewHolder(LayoutInflater.from(p0.context).inflate(this.progressBarLayoutId, p0, false))
        else -> ViewHolder(LayoutInflater.from(p0.context).inflate(layout, p0, false))
    }

    override fun getItemCount(): Int = items.size

    @Suppress("RemoveRedundantQualifierName")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) = (holder as RecyclerViewAdapter<*>.ViewHolder).bindView(position)

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) = recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            when {
                dy > 0 -> {
                    val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val visibleItemCount = recyclerView.childCount
                    val totalItemCount = linearLayoutManager.itemCount
                    val firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition()
                    when {
                        !isLoading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + 2) -> {
                            loadMore?.invoke((totalItemCount / this@RecyclerViewAdapter.size))
                            isLoading = true
                        }
                    }
                }
            }
        }
    })

    override fun getItemViewType(position: Int): Int = when {
        this.items[position].progress -> ViewType.PROGRESS_BAR.type
        else -> ViewType.ITEM.type
    }

    fun setItems(items: MutableList<T>?): RecyclerViewAdapter<T> {
        this.items = items?.map { Item(false, it) }?.toMutableList() ?: mutableListOf()
        notifyDataSetChanged()
        return this
    }

    fun addItem(item: T): RecyclerViewAdapter<T> {
        this.items.add(Item(false, item))
        notifyItemInserted(this.items.size)
        return this
    }

    fun addItems(items: ArrayList<T>): RecyclerViewAdapter<T> {
        this.items.addAll(items.map { Item(false, it) })
        notifyItemRangeInserted(this.items.size - 1, items.size)
        return this
    }

    fun clear(): RecyclerViewAdapter<T> {
        this.items.clear()
        notifyItemRangeRemoved(0, this.items.size)
        return this
    }

    fun onBindView(listener: (view: View, item: T?, position: Int) -> Unit): RecyclerViewAdapter<T> {
        bindView = listener
        return this
    }

    fun onClick(listener: (view: View, item: T?, position: Int) -> Unit): RecyclerViewAdapter<T> {
        click = listener
        return this
    }

    fun onLongClick(listener: (view: View?, item: T?, position: Int) -> Unit): RecyclerViewAdapter<T> {
        longClick = listener
        return this
    }

    fun onLoadMore(listener: (page: Int) -> Unit): RecyclerViewAdapter<T> {
        loadMore = listener
        return this
    }

    fun loading(isLoading: Boolean = true): RecyclerViewAdapter<T> {
        handler.post {
            when {
                isLoading -> {
                    this.items.add(Item(true))
                    notifyItemInserted(when (itemCount) {
                        0 -> itemCount
                        else -> itemCount - 1
                    })
                    this.isLoading = true
                }
                else -> for (i in 0 until itemCount) {
                    when {
                        i != itemCount && (items[i]).progress -> {
                            items.removeAt(i)
                            notifyItemRemoved(i)
                            this.isLoading = false
                        }
                    }
                }
            }

        }
        return this
    }

    fun isLoading(): Boolean {
        return isLoading
    }
}
