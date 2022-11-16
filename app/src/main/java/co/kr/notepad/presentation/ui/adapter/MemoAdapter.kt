package co.kr.notepad.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import co.kr.notepad.databinding.ItemMemoBinding
import co.kr.notepad.domain.entity.Memo
import co.kr.notepad.util.toDateString

class MemoAdapter(
    private val onItemClick: (Long) -> Unit,
    private val onItemLongClick: (Memo) -> Unit
) :
    RecyclerView.Adapter<MemoAdapter.MemoViewHolder>() {
    private val itemList = AsyncListDiffer(this, diffCallback)

    class MemoViewHolder(
        private val binding: ItemMemoBinding,
        private val onItemClick: (Long) -> Unit,
        private val onItemLongClick: (Memo) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(item: Memo) {
            binding.tvTitle.text = item.text
            binding.tvDate.text = item.date.toDateString()
            binding.root.setOnClickListener { onItemClick(item.id) }
            binding.root.setOnLongClickListener {
                onItemLongClick(item)
                true
            }
        }

        companion object {
            fun create(
                parent: ViewGroup,
                onItemClick: (Long) -> Unit,
                onItemLongClick: (Memo) -> Unit
            ): MemoViewHolder {
                val binding =
                    ItemMemoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return MemoViewHolder(binding, onItemClick, onItemLongClick)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoViewHolder {
        return MemoViewHolder.create(parent, onItemClick, onItemLongClick)
    }

    override fun onBindViewHolder(holder: MemoViewHolder, position: Int) {
        holder.onBind(itemList.currentList[position])
    }

    override fun getItemCount(): Int = itemList.currentList.size

    fun updateList(updatedList: List<Memo>) {
        itemList.submitList(updatedList)
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<Memo>() {
            override fun areItemsTheSame(oldItem: Memo, newItem: Memo): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Memo, newItem: Memo): Boolean {
                return oldItem == newItem
            }
        }
    }
}