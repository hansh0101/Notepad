package co.kr.notepad.presentation.ui.adapter

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
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
        fun onBind(item: Memo, isSelected: Boolean) {
            binding.tvTitle.text = item.text
            binding.tvDate.text = item.date.toDateString()
            initBackground(isSelected)
            binding.root.setOnClickListener {
                val isSelectMode = onItemClick(item)
                if (isSelectMode) {
                    updateBackground()
                }
            }
            binding.root.setOnLongClickListener {
                updateBackground()
                onItemSelect(item)
                true
            }
        }

        private fun initBackground(isSelected: Boolean) {
            if (isSelected) {
                binding.root.setBackgroundResource(R.color.gray)
            } else {
                binding.root.background = null
            }
        }

        private fun updateBackground() {
            if ((binding.root.background as? ColorDrawable)?.color == null) {
                binding.root.setBackgroundResource(R.color.gray)
            } else {
                binding.root.background = null
            }
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
        with(items.currentList[position]) {
            holder.onBind(this, selectedItems.contains(this))
        }
    }

    override fun getItemCount(): Int = items.currentList.size

    fun getSelectedItemCount(): Int = selectedItems.size

    fun updateList(updatedList: List<Memo>) {
        items.submitList(updatedList)
    }

    fun updateSelectedItems(selectedMemos: List<Memo>) {
        selectedItems = selectedMemos
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