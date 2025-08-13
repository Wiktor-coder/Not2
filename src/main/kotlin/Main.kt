package ru.netology

data class Notes<A, B>(
    val nId: A, //идентификатор заметки
    var title: B, //Заголовок
    var text: B, // Текст
    var privacy: A, // Уровень доступа к заметке
    var commentPrivacy: A, //Уровень доступа к комментированию заметки
)

val PRIVACY = listOf<Int>(
    0, //все пользователи
    1, //только друзья,
    2, //друзья и друзья друзей
    3, //только пользователь
)
val COMMENT_PRIVACY = listOf<Int>(
    0, //все пользователи
    1, //только друзья,
    2, //друзья и друзья друзей
    3, //только пользователь
)

data class Comment<A, B>(
    var noteId: A,
    var message: B,
    var commentId: A,
)

class NotesService {
    private var nextId = 1
    private var cId = 1
    var notes = mutableListOf<Notes<Int, String>>()
    var comment = mutableListOf<Comment<Int, String>>()
    private var deleteComment = mutableListOf<Comment<Int, String>>()

    //добавление заметки
    fun add(
        nId: Int,
        title: String,
        text: String,
        privacy: Int,
        commentPrivacy: Int
    ): Int {
        notes.add(
            Notes(
                nId = nextId++,
                title = title,
                text = text,
                privacy = privacy,
                commentPrivacy = commentPrivacy,
            )
        )
        return nextId - 1
    }

    //создание комментария
    fun createComment(noteId: Int, message: String): Int {
        if (!notes.any { it.nId == noteId }) {
            throw NotesNotFoundException("Заметки под номером $noteId не существует")
        }
        val newComment = Comment(noteId = noteId, message = message, commentId = cId)
        comment.add(newComment)
        return cId++
    }

    //удаление заметки
    fun delete(noteId: Int): Int {
        val foundNote = notes.find { it.nId == noteId }
        if (foundNote != null) {
            notes.remove(foundNote)
            return 1
        } else {
            throw NotesNotFoundException("Заметки под номером $noteId, не существует")
        }
    }

    //удаление комментария
    fun deleteComment(commentId: Int): Int {
        val foundComment = comment.find { it.commentId == commentId }
        if (foundComment != null) {
            deleteComment.add(foundComment)
            comment.remove(foundComment)
            return 1
        } else {
            throw NotesNotFoundException("Комментария под номером $commentId, не существует")
        }
    }

    //редактирование заметок
    fun edit(
        noteId: Int,
        title: String,
        text: String,
        privacy: Int,
        commentPrivacy: Int
    ): Int {
        val foundNote = notes.find { it.nId == noteId }
        if (foundNote != null) {
            foundNote.title = title
            foundNote.text = text
            foundNote.privacy = privacy
            foundNote.commentPrivacy = commentPrivacy
            return 1
        } else {
            throw NotesNotFoundException("Заметки под номером $noteId, не существует")
        }

    }

    //редактирование комментария
    fun editComment(
        commentId: Int,
        message: String,
    ): Int {
        val foundComment = comment.find { it.commentId == commentId }
        if (foundComment != null) {
            foundComment.message = message
            return 1
        } else {
            throw NotesNotFoundException("Комментария под номером $commentId, не существует")
        }
    }

    //получение заметок
    fun get(
        noteId: Int,
        count: Int,
    ): List<Notes<Int, String>> {
        val objectNote = notes.find { it.nId == noteId } ?: throw NotesNotFoundException("Заметок под номером $noteId, не существует")
        return if (count > 0) {
            notes.subList(0, toIndex = count)
        } else {
            listOf(objectNote)
        }
    }

    //получить заметку по Id
    fun getByld(noteId: Int): Notes<Int, String> {
        val objects = notes.find { it.nId == noteId }
        if (objects != null) {
            return objects
        } else {
            throw NotesNotFoundException("Заметок под номером $noteId, не существует")
        }
    }

    //получить комментарий
    fun getComments(
        noteId: Int,
        count: Int = 0 // 0 означает "все комментарии"
    ): List<Comment<Int, String>> {
        val commentsForNote = comment.filter { it.noteId == noteId }

        if (commentsForNote.isEmpty()) {
            throw NotesNotFoundException("Комментариев для заметки $noteId не найдено")
        }

        return if (count > 0) commentsForNote.take(count) else commentsForNote
    }

    //восстановление комментария
    fun restoreComment(commentId: Int) {
        val foundDeletedComment = deleteComment.find { it.commentId == commentId }
        if (foundDeletedComment != null) {
            comment.add(foundDeletedComment)
        }
    }
}

class NotesNotFoundException(message: String) : RuntimeException(message)

fun main() {
}