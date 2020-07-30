package com.example.dictionary.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.dictionary.R
import com.example.dictionary.domain.model.DefinitionModel
import org.jetbrains.anko.find

/**
 * Created by PraNeeTh on 4/7/20
 */
class DefinitionAdapter : RecyclerView.Adapter<DefinitionAdapter.ViewHolder>() {

    private var definitionsList = ArrayList<DefinitionModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.definition_view, parent,
                false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = definitionsList.size

    fun setData(newList: List<DefinitionModel>) {
        val diffCallback = DefinitionDiffCallBack(definitionsList, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        definitionsList.clear()
        definitionsList.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int): Unit =
        holder.bindData(definitionsList[position])

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val definitionTv: TextView = itemView.find(R.id.tv_definition)
        private val likeTv: TextView = itemView.find(R.id.tv_like)
        private val dislikeTv: TextView = itemView.find(R.id.tv_dislike)

        fun bindData(model: DefinitionModel) {
            definitionTv.text = model.definition
            likeTv.text = model.thumbsUp.toString()
            dislikeTv.text = model.thumbsDown.toString()
        }
    }

    inner class DefinitionDiffCallBack(
        private val oldList: List<DefinitionModel>,
        private val newList: List<DefinitionModel>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].definitionId == newList[newItemPosition].definitionId
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val (_, definitionId1) = oldList[oldItemPosition]
            val (_, definitionId2) = newList[newItemPosition]

            return definitionId1 == definitionId2
        }

        @Nullable
        override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
            return super.getChangePayload(oldItemPosition, newItemPosition)
        }
    }
}