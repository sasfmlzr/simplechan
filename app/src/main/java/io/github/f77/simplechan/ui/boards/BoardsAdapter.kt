package io.github.f77.simplechan.ui.boards

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.github.f77.simplechan.R
import io.github.f77.simplechan.bloc_utils.EventsAwareInterface
import io.github.f77.simplechan.bloc_utils.event.Events
import io.github.f77.simplechan.entities.BoardEntity

class BoardsAdapter(private val eventsHandler: EventsAwareInterface) :
    RecyclerView.Adapter<BoardsAdapter.BoardsViewHolder>() {
    var dataset: List<BoardEntity> = mutableListOf()

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    class BoardsViewHolder(itemView: View, eventsHandler: EventsAwareInterface) : RecyclerView.ViewHolder(itemView) {
        var textViewTitle: TextView = itemView.findViewById(R.id.recycler_item_title);
        var textViewSubtitle: TextView = itemView.findViewById(R.id.recycler_item_subtitle);

        init {
            itemView.setOnClickListener(View.OnClickListener {
                eventsHandler.addEvent(Events.itemClicked(adapterPosition))
            })
            itemView.setOnLongClickListener(View.OnLongClickListener {
                eventsHandler.addEvent(Events.itemLongClicked(adapterPosition))
                true
            })
        }
    }


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BoardsViewHolder {
        // create a new view
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_row, parent, false)
        // set the view's size, margins, paddings and layout parameters
        // ...
        return BoardsViewHolder(view, eventsHandler)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: BoardsViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        val board = dataset[position]

        holder.textViewTitle.text = board.name
        holder.textViewSubtitle.text = "/" + board.id
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataset.size
}
