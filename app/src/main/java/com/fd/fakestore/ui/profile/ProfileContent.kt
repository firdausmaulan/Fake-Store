import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.fd.fakestore.R
import com.fd.fakestore.data.model.User

@Composable
fun ProfileContent(user: User) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_question),
            contentDescription = "Profile Avatar",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(color = MaterialTheme.colorScheme.secondary)
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = "${user.name?.firstname} ${user.name?.lastname}",
            onValueChange = {},
            label = { Text("Name") },
            readOnly = true,
            enabled = false,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = TextFieldDefaults.colors(
                disabledTextColor = Color.Black,
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = user.username.toString(),
            onValueChange = {},
            label = { Text("Username") },
            readOnly = true,
            enabled = false,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = TextFieldDefaults.colors(
                disabledTextColor = Color.Black,
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = user.email.toString(),
            onValueChange = {},
            label = { Text("Email") },
            readOnly = true,
            enabled = false,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = TextFieldDefaults.colors(
                disabledTextColor = Color.Black,
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = user.phone.toString(),
            onValueChange = {},
            label = { Text("Phone") },
            readOnly = true,
            enabled = false,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = TextFieldDefaults.colors(
                disabledTextColor = Color.Black,
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = "${user.address?.number} ${user.address?.street}, ${user.address?.city}, ${user.address?.zipcode}",
            onValueChange = {},
            label = { Text("Address") },
            readOnly = true,
            enabled = false,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = TextFieldDefaults.colors(
                disabledTextColor = Color.Black,
            )
        )
    }
}