package com.artlens.view.composables

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.artlens.data.models.CommentResponse

@Composable
fun CommentsScreen(
    comments: List<CommentResponse>,
    onBackClick: () -> Unit,
    onPostComment: (String) -> Unit,
    isLoggedIn: Boolean
) {
    var newComment by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Título y botón de retroceso
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Text(text = "Comments", style = MaterialTheme.typography.h5)
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Lista de comentarios
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            if (comments.isEmpty()) {
                item {
                    Text(
                        text = "No comments available, do you want to be the first to comment?",
                        style = MaterialTheme.typography.body1,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            } else {
                items(comments) { comment ->
                    ForumEntry(
                        userName = "User ${comment.fields.user}",
                        comment = comment.fields.content
                    )
                    Divider()
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Campo de texto para nuevo comentario
        if (isLoggedIn) {
            // Campo de texto para nuevo comentario
            TextField(
                value = newComment,
                onValueChange = { newComment = it },
                placeholder = { Text("Write a comment...") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Botón para publicar comentario
            Button(
                onClick = {
                    onPostComment(newComment)
                    newComment = "" // Limpiar el campo tras publicar
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = newComment.isNotBlank()
            ) {
                Text("Post Comment")
            }
        } else {
            // Mensaje si el usuario no está logueado
            Text(
                text = "You must be logged in to post a comment.",
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(8.dp),
                color = MaterialTheme.colors.error
            )
        }
    }
}


@Composable
fun ForumEntry(userName: String, comment: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = userName,
            style = MaterialTheme.typography.subtitle1,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = comment,
            style = MaterialTheme.typography.body2,
            modifier = Modifier.padding(start = 8.dp, top = 4.dp)
        )
    }
}

