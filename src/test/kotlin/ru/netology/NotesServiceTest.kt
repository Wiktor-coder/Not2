package ru.netology

import org.junit.Test
import org.junit.Assert.*
import java.lang.RuntimeException

class NotesServiceTest {


    // ТЕСТЫ НА ДОБАВЛЕНИЕ ЗАМЕТОК
    @Test
    fun testAddNote() {
        val newNoteId = NotesService()
        newNoteId.add(1, "Заголовок", "Text", 0, 0)
        //assertEquals(newNoteId, 1)
        assertTrue(newNoteId.notes.size == 1)
    }

    // ТЕСТЫ НА РЕДАКТИРОВАНИЕ ЗАМЕТКИ
    @Test
    fun testEditNote() {
        val initialNoteId = NotesService()
        initialNoteId.add(0, "Первоначальная заметка", "Текст первой заметки", 0, 0)
        initialNoteId.edit(1, "Отредактированная заметка", "Новый текст заметки", 1, 1)
        val editedNote = initialNoteId.getByld(1)
        assertEquals(editedNote.title, "Отредактированная заметка")
        assertEquals(editedNote.text, "Новый текст заметки")
        assertEquals(editedNote.privacy, 1)
        assertEquals(editedNote.commentPrivacy, 1)
    }

    //  ТЕСТЫ НА ПОИСК ЗАМЕТОК
    @Test
    fun testGetNoteById() {
        val noteId = NotesService()
        noteId.add(0, "Тестовая заметка", "Текст заметки", 0, 0)
        val retrievedNote = noteId.getByld(noteId = 1)
        assertEquals(retrievedNote.title, "Тестовая заметка")
        assertEquals(retrievedNote.text, "Текст заметки")
    }

    // =ТЕСТЫ НА УДАЛЕНИЕ ЗАМЕТОК
    @Test(expected = NotesNotFoundException::class)
    fun testDeleteNonExistingNote() {
        val del = NotesService()
        del.delete(-1)
    }

    @Test
    fun testDeleteNote() {
        val noteId = NotesService()
        noteId.add(0, "Тестовая заметка", "Текст заметки", 0, 0)
        noteId.delete(noteId = 1)
        assertFalse(noteId.notes.any { it.nId.equals(noteId) })
    }

    // ТЕСТЫ НА СОЗДАНИЕ КОММЕНТАРИЕВ
    @Test
    fun testCreateComment() {
        val notesService = NotesService()
        val noteId = notesService.add(
            title = "Тестовая заметка", text = "Текст заметки", privacy = 0, commentPrivacy = 0,
            nId = 0
        )
        val commentId = notesService.createComment(noteId, "Первый комментарий")
        assertTrue(notesService.comment.any { it.noteId == noteId })
    }

    // ТЕСТЫ НА УДАЛЕНИЕ КОММЕНТАРИЯ
    @Test(expected = NotesNotFoundException::class)
    fun testDeleteNonExistingComment() {
        val del = NotesService()
        del.deleteComment(-1)
    }

    @Test
    fun testDeleteComment() {
        val noteId = NotesService()
        noteId.add(0, "Тестовая заметка", "Текст заметки", 0, 0)
        val commentId = noteId.createComment(noteId = 1, "Первый комментарий")
        noteId.deleteComment(commentId)
        assertFalse(noteId.comment.any { it.noteId.equals(noteId) })
    }

    //  ТЕСТЫ НА ВОССТАНОВЛЕНИЕ КОММЕНТАРИЯ
    @Test
    fun testRestoreComment() {
        val notesService = NotesService()
        val noteId = notesService.add(0,"Тестовая заметка", "Текст заметки", 0, 0)
        val commentId = notesService.createComment(noteId, "Первый комментарий")
        notesService.deleteComment(commentId)
        notesService.restoreComment(commentId)
        assertTrue(notesService.comment.any { it.noteId == noteId })
    }

    //  ТЕСТЫ НА ОБРАБОТКУ ОШИБОК
    @Test(expected = NotesNotFoundException::class)
    fun testErrorHandlingForNonExistentNote() {
        val gB = NotesService()
        gB.getByld(-1)
    }

    @Test(expected = NotesNotFoundException::class)
    fun testErrorHandlingForNonExistentComment() {
        val eC = NotesService()
        eC.editComment(-1, "Редактируемый комментарий")
    }

}