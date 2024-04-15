package com.ehsankhormali.userprofileusingfirebase.screens

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ehsankhormali.userprofileusingfirebase.navigation.Screens
import com.ehsankhormali.userprofileusingfirebase.widgets.ProfileImage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

@Composable
fun Profile(navController: NavController) {
    var firstName: String by remember {
        mutableStateOf("")
    }
    var lastName: String by remember {
        mutableStateOf("")
    }

    var editFirstName: String by remember {
        mutableStateOf("")
    }
    var editLastName: String by remember {
        mutableStateOf("")
    }

    var picUrl: String by remember {
        mutableStateOf("")
    }
    var editMode by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        // Actions to perform when LaunchedEffect enters the Composition
        FirebaseFirestore.getInstance().collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid.toString())
            .get()
            .addOnSuccessListener {

                firstName = it.get("first_name").toString()
                lastName = it.get("last_name").toString()
                picUrl = it.get("profile_picture").toString()
                editFirstName=firstName
                editLastName=lastName

            }
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 4.dp, end = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val context = LocalContext.current
        Spacer(modifier = Modifier.padding(top = 25.dp))
        ProfileImage(Uri.parse(picUrl)) {
            updateProfilePicture(it)
        }


        Spacer(modifier = Modifier.padding(vertical = 25.dp))
        OutlinedTextField(
            value = editFirstName, onValueChange = {
                editFirstName = it
            },
            label = { Text(text = "First name") },
            readOnly = !editMode
        )
        Spacer(modifier = Modifier.padding(vertical = 5.dp))

        OutlinedTextField(
            value = editLastName, onValueChange = {
                editLastName = it
            },
            label = { Text(text = "Last name") },
            readOnly = !editMode
        )
        Spacer(modifier = Modifier.padding(vertical = 25.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {

                    if (editMode) {
                        FirebaseFirestore.getInstance().collection("users")
                            .document(FirebaseAuth.getInstance().currentUser?.uid.toString())
                            .update(
                                "first_name", editFirstName,
                                "last_name", editLastName
                            )
                            .addOnSuccessListener {
                                firstName=editFirstName
                                lastName=editLastName
                            }
                            .addOnFailureListener{
                                editFirstName=firstName
                                editLastName=lastName
                            }
                    }
                    editMode = !editMode

                },
                modifier = Modifier
                    .height(40.dp),
            ) {
                Text(
                    text = if (!editMode) "Edit Profile" else "Save"
                )
                Spacer(modifier = Modifier.padding(horizontal = 5.dp))
                Icon(
                    imageVector = if (!editMode) Icons.Default.Edit else Icons.Default.Save,
                    contentDescription = null
                )
            }
            Spacer(modifier = Modifier.padding(horizontal = 5.dp))
            // sign out button
            Button(
                onClick = {
                    FirebaseAuth.getInstance().signOut()
                    navController.navigate(Screens.Login.name)
                },
                modifier = Modifier
                    .height(40.dp),
            ) {
                Text(
                    text = "Log Out!!"
                )
                Spacer(modifier = Modifier.padding(horizontal = 5.dp))
                Icon(imageVector = Icons.Default.AccountCircle, contentDescription = null)
            }
        }

    }
}

private fun updateProfilePicture(uri: Uri) {
    val riversRef =
        FirebaseStorage.getInstance().getReference("profile_pictures/${uri.lastPathSegment}")
    val uploadTask = riversRef.putFile(uri)

// Register observers to listen for when the download is done or if it fails
    uploadTask.addOnFailureListener {
        // Handle unsuccessful uploads
    }.addOnSuccessListener { taskSnapshot ->
        // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
        taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
            FirebaseFirestore.getInstance().collection("users")
                .document(FirebaseAuth.getInstance().currentUser?.uid.toString())
                .update("profile_picture", uri.toString())
        }

    }

}

@Preview
@Composable
fun PreviewProfile(navController: NavController= rememberNavController()){
    Profile(navController = navController)
}