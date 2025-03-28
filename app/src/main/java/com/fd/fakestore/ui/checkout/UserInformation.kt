package com.fd.fakestore.ui.checkout

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fd.fakestore.R
import com.fd.fakestore.data.model.User

@Composable
fun UserInformation(user: User) {
    val name = "${user.name?.firstname} ${user.name?.lastname}"
    val phone = user.phone ?: ""
    val address = "${user.address?.street} ${user.address?.number} ${user.address?.city} ${user.address?.zipcode}"
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                stringResource(R.string.label_user_information),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "${stringResource(R.string.label_name)}\n${name.uppercase()}",
                modifier = Modifier.padding(vertical = 4.dp)
            )
            HorizontalDivider(Modifier.height(1.dp))
            Text(
                "${stringResource(R.string.label_phone)}\n$phone",
                modifier = Modifier.padding(vertical = 4.dp)
            )
            HorizontalDivider(Modifier.height(1.dp))
            Text(
                "${stringResource(R.string.label_address)}\n$address",
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }
    }
}