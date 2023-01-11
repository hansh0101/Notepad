package co.kr.notepad.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import co.kr.notepad.domain.entity.Memo

@Entity(tableName = "memo")
data class MemoDto(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo val title: String,
    @ColumnInfo val text: String,
    @ColumnInfo val image: String?,
    @ColumnInfo val date: Long
) {
    fun toMemo(): Memo = Memo(this.id, this.title, this.text, this.image, this.date)
}
