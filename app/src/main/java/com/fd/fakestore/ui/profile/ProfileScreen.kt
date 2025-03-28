package com.fd.fakestore.ui.profile

import ErrorScreen
import LoadingScreen
import ProfileContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.fd.fakestore.helper.AppColor

@Composable
fun ProfileScreen(
    onDismiss: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.getUserDetail()
    }

    val state by viewModel.state.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.backgroundReverse())
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Profile",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Filled.Close, contentDescription = "Close")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            when (state) {
                is ProfileState.Loading -> {
                    LoadingScreen()
                }

                is ProfileState.Success -> {
                    val user = (state as ProfileState.Success).user
                    ProfileContent(user = user)
                }

                is ProfileState.Error -> {
                    ErrorScreen(
                        message = (state as ProfileState.Error).message,
                        onRetry = { viewModel.getUserDetail() }
                    )
                }
            }
        }
    }
}