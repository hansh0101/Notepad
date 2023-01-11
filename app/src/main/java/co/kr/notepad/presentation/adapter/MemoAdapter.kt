package co.kr.notepad.presentation.adapter

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import co.kr.notepad.R
import co.kr.notepad.databinding.ItemMemoBinding
import co.kr.notepad.domain.entity.Memo
import co.kr.notepad.util.toDateString

class MemoAdapter(
    private val onItemClick: (Memo) -> Boolean,
    private val onItemSelect: (Memo) -> Unit
) :
    RecyclerView.Adapter<MemoAdapter.MemoViewHolder>() {
    private val items = AsyncListDiffer(this, diffCallback)
    private var selectedItems = listOf<Memo>()

    class MemoViewHolder(
        private val binding: ItemMemoBinding,
        private val onItemClick: (Memo) -> Boolean,
        private val onItemSelect: (Memo) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(item: Memo, isSelected: Boolean, isSelectMode: Boolean) {
            binding.tvTitle.text = item.title
            binding.tvDate.text = item.date.toDateString()
            initViewHolderByIsSelected(isSelected)
            initViewHolderByIsSelectMode(isSelectMode)
            binding.root.setOnClickListener {
                if (onItemClick(item)) {
                    updateViewHolderBySelectMode()
                }
            }
            binding.root.setOnLongClickListener {
                updateViewHolderBySelectMode()
                onItemSelect(item)
                true
            }
        }

        private fun initViewHolderByIsSelected(isSelected: Boolean) {
            if (isSelected) {
                binding.root.setBackgroundResource(R.color.gray)
                binding.ivCheck.isVisible = true
            } else {
                binding.root.background = null
                binding.ivCheck.isVisible = false
            }
        }

        private fun updateViewHolderBySelectMode() {
            if ((binding.root.background as? ColorDrawable)?.color == null) {
                binding.root.setBackgroundResource(R.color.gray)
                binding.ivCheck.isVisible = true
            } else {
                binding.root.background = null
                binding.ivCheck.isVisible = false
            }
        }

        fun initViewHolderByIsSelectMode(isSelectMode: Boolean) {
            binding.layoutCheckBox.isVisible = isSelectMode
        }

        companion object {
            fun create(
                parent: ViewGroup,
                onItemClick: (Memo) -> Boolean,
                onItemSelect: (Memo) -> Unit
            ): MemoViewHolder {
                val binding =
                    ItemMemoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return MemoViewHolder(binding, onItemClick, onItemSelect)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoViewHolder {
        return MemoViewHolder.create(parent, onItemClick, onItemSelect)
    }

    override fun onBindViewHolder(holder: MemoViewHolder, position: Int) {
        items.currentList[position].run {
            holder.onBind(this, selectedItems.contains(this), selectedItems.isNotEmpty())
        }
    }

    override fun onBindViewHolder(
        holder: MemoViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        onBindViewHolder(holder, position)
        if (payloads.isNotEmpty()) {
            for (payload in payloads) {
                if (payload is Boolean) {
                    holder.initViewHolderByIsSelectMode(payload)
                }
            }
        }
    }

    override fun getItemCount(): Int = items.currentList.size

    fun getSelectedItemCount(): Int = selectedItems.size

    fun updateList(updatedList: List<Memo>) {
        items.submitList(updatedList)
    }

    fun updateSelectedItems(selectedMemos: List<Memo>) {
        selectedItems = selectedMemos
        notifyItemRangeChanged(0, itemCount, selectedMemos.isNotEmpty())
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