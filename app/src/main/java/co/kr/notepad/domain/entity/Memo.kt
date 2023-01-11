package co.kr.notepad.domain.entity

data class Memo(
    val id: Long = 0,
    val title: String,
    val text: String,
    val image: String?,
    val date: Long
) {
    fun isContentsTheSame(memo: Memo?): Boolean =
        this.id == memo?.id && this.title == memo.title && this.text == memo.text && this.image == memo.image
}
