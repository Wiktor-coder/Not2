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
        val notesExists = notes.any { it.nId == noteId }
        if (!notesExists) {
            throw NotesNotFoundException("Заметки под номером $noteId, не существует")
        }
        comment.add(Comment(noteId = noteId, message = message))
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
        val foundComment = comment.find { it.noteId == commentId }
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
        val foundComment = comment.find { it.noteId == commentId }
        if (foundComment != null) {
            foundComment.message = message
            return 1
        } else {
            throw NotesNotFoundException("Комментария под номером $commentId, не существует")
        }
    }

    //получение заметок
    fun <A, B> get(
        noteIds: A,
        userId: B,
        count: Int,
    ): Any {
        val objectsNote = notes.find { it.nId == noteIds }
        if (objectsNote != null) {
            return objectsNote
        } else if (count > 0) {
            return notes.subList(0, toIndex = count)
        } else {
            throw NotesNotFoundException("Заметок под номером $noteIds, не существует")
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
    fun getComment(
        noteId: Int,
        count: Int
    ): Any {
        val foundComment = comment.find { it.noteId == noteId }
        if (foundComment != null) {
            return foundComment
        } else if (count > 0) {
            return comment.subList(0, toIndex = count)
        } else {
            throw NotesNotFoundException("Заметок под номером $noteId, не существует")
        }
    }

    //восстановление комментария
    fun restoreComment(commentId: Int) {
        val foundDeletedComment = deleteComment.find { it.noteId == commentId }
        if (foundDeletedComment != null) {
            comment.add(foundDeletedComment)
        }
    }
}

class NotesNotFoundException(message: String) : RuntimeException(message)

fun main() {
}