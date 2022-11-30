package co.kr.notepad.domain.entity

data class Memo(
    val id: Long = 0,
    val text: String,
    val date: Long
) {
    fun isContentsTheSame(memo: Memo?): Boolean =
        this.id == memo?.id && this.text == memo.text
}
