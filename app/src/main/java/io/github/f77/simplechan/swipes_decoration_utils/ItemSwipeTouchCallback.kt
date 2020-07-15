package io.github.f77.simplechan.swipes_decoration_utils

import android.graphics.Canvas
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import io.github.f77.simplechan.bloc_utils.EventsAwareInterface
import io.github.f77.simplechan.bloc_utils.event.ItemMovedEventInterface
import io.github.f77.simplechan.bloc_utils.event.ItemSwipedLeftEventInterface
import io.github.f77.simplechan.bloc_utils.event.ItemSwipedRightEventInterface
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator

/**
 * @see <a href="https://github.com/xabaras/RecyclerViewSwipeDecorator">RecyclerViewSwipeDecorator</a>
 */
class ItemSwipeTouchCallback(private val eventsHandler: EventsAwareInterface) : ItemTouchHelper.Callback() {
    var leftLabel: String? = null
    var rightLabel: String? = null
    var leftLabelColor: Int? = null
    var rightLabelColor: Int? = null
    var leftBackgroundColor: Int? = null
    var rightBackgroundColor: Int? = null
    var leftIcon: Int? = null
    var rightIcon: Int? = null

    var isLongPressDrag: Boolean = true
    var isItemViewSwipe: Boolean = true

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
        return makeMovementFlags(dragFlags, swipeFlags)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        eventsHandler.addEvent(object : ItemMovedEventInterface {
            override val fromPosition: Int
                get() = viewHolder.adapterPosition
            override val toPosition: Int
                get() = target.adapterPosition
        })
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        if (direction == ItemTouchHelper.START) {
            eventsHandler.addEvent(object : ItemSwipedLeftEventInterface {
                override val position: Int
                    get() = viewHolder.adapterPosition
            })
        } else {
            eventsHandler.addEvent(object : ItemSwipedRightEventInterface {
                override val position: Int
                    get() = viewHolder.adapterPosition
            })
        }
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val builder = RecyclerViewSwipeDecorator
            .Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

        leftLabel?.let { builder.addSwipeLeftLabel(leftLabel!!) }
        rightLabel?.let { builder.addSwipeRightLabel(rightLabel!!) }
        leftLabelColor?.let { builder.setSwipeLeftLabelColor(leftLabelColor!!) }
        rightLabelColor?.let { builder.setSwipeRightLabelColor(rightLabelColor!!) }
        leftBackgroundColor?.let { builder.addSwipeLeftBackgroundColor(leftBackgroundColor!!) }
        rightBackgroundColor?.let { builder.addSwipeRightBackgroundColor(rightBackgroundColor!!) }
        leftIcon?.let { builder.addSwipeLeftActionIcon(leftIcon!!) }
        rightIcon?.let { builder.addSwipeRightActionIcon(rightIcon!!) }

        builder.create().decorate()
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    override fun isLongPressDragEnabled(): Boolean {
        return isLongPressDrag
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return isItemViewSwipe
    }
}
