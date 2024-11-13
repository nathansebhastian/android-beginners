package com.codewithnathan.taskly.ui.screens

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.codewithnathan.taskly.R
import com.codewithnathan.taskly.data.Task
import com.codewithnathan.taskly.data.getFormattedDueDate
import com.codewithnathan.taskly.ui.navigation.NavigationDestination
import com.codewithnathan.taskly.ui.viewmodels.TaskFormViewModel
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

object TaskFormDestination : NavigationDestination {
    override val route = "form"
    override val titleRes = R.string.task_form_title
    const val TASK_ID = "taskId"
    val routeWithArgs = "$route/{$TASK_ID}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskFormScreen(
    navigateUp: () -> Unit,
    viewModel: TaskFormViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val taskFormUiState = viewModel.taskFormUiState
    val isNewTask = viewModel.isNewTask()
    var showDeleteConfirmation by remember { mutableStateOf(false) }

    val onSaveClick: () -> Unit = {
        coroutineScope.launch {
            if (isNewTask) {
                viewModel.saveTask()
            } else {
                viewModel.updateTask()
            }
            navigateUp()
        }
    }

    val onDeleteClick: () -> Unit = {
        coroutineScope.launch {
            viewModel.deleteTask()
            navigateUp()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text(stringResource(TaskFormDestination.titleRes)) },
                navigationIcon = {
                    IconButton(onClick = navigateUp) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back_button)
                        )
                    }
                })
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(dimensionResource(id = R.dimen.padding_medium)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_large))
        ) {
            TaskInputForm(
                taskDetails = taskFormUiState.task,
                onValueChange = viewModel::updateUiState,
                modifier = Modifier.fillMaxWidth()
            )
            if (!isNewTask) {
                Button(
                    onClick = { showDeleteConfirmation = true },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red,        // Background color
                        contentColor = Color.White         // Text color
                    ),
                ) {
                    Text(stringResource(R.string.delete_action))
                }
            }
            Button(
                onClick = onSaveClick,
                enabled = taskFormUiState.isEntryValid,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.save_task))
            }
        }
        if (showDeleteConfirmation) {
            AlertDialog(
                onDismissRequest = {
                    showDeleteConfirmation = false
                }, // Close dialog when clicking outside
                title = { Text("Confirm Delete") },
                text = { Text("Are you sure you want to delete this task?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showDeleteConfirmation = false
                            onDeleteClick()
                        }
                    ) {
                        Text("Confirm", color = Color.Red)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteConfirmation = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@Composable
fun TaskInputForm(
    taskDetails: Task,
    modifier: Modifier = Modifier,
    onValueChange: (Task) -> Unit = {},
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium)) // 16dp
    ) {
        OutlinedTextField(
            value = taskDetails.title,
            onValueChange = { onValueChange(taskDetails.copy(title = it)) },
            label = { Text(stringResource(R.string.task_title_req)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        OutlinedTextField(
            value = taskDetails.notes,
            onValueChange = { onValueChange(taskDetails.copy(notes = it)) },
            label = { Text(stringResource(R.string.task_notes)) },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 100.dp),  // Adjust height for multiline input
            singleLine = false,  // Allows multiple lines
            maxLines = 5
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = stringResource(R.string.task_is_completed))
            Spacer(modifier = Modifier.weight(1f)) // To push the switch to the end
            Switch(
                checked = taskDetails.isCompleted,
                onCheckedChange = { onValueChange(taskDetails.copy(isCompleted = it)) },
            )
        }
        TaskDatePickerFieldToModal(taskDetails, onValueChange)
        SelectImageField(taskDetails, onValueChange)
        Text(
            text = stringResource(R.string.required_fields), // *required field
            modifier = Modifier.padding(start = dimensionResource(id = R.dimen.padding_medium))
        )
    }
}

@Composable
fun TaskDatePickerFieldToModal(
    taskDetails: Task,
    onValueChange: (Task) -> Unit = {}
) {
    val selectedDate = taskDetails.dueDate
    var showModal by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = getFormattedDueDate(selectedDate),
        onValueChange = { },
        label = { Text("Due Date") },
        placeholder = { Text("MM/DD/YYYY") },
        trailingIcon = {
            Icon(Icons.Default.DateRange, contentDescription = "Select date")
        },
        modifier = Modifier
            .fillMaxWidth()
            .pointerInput(selectedDate) {
                awaitEachGesture {
                    awaitFirstDown(pass = PointerEventPass.Initial)
                    val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                    if (upEvent != null) {
                        showModal = true
                    }
                }
            }
    )

    if (showModal) {
        DatePickerModal(
            onDateSelected = { onValueChange(taskDetails.copy(dueDate = it)) },
            onDismiss = { showModal = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Long) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                datePickerState.selectedDateMillis?.let { onDateSelected(it) }
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

@Composable
fun SelectImageField(
    taskDetails: Task,
    onValueChange: (Task) -> Unit = {}
) {
    val context = LocalContext.current
    val selectedImageUri: Uri? = taskDetails.imageUri?.let { Uri.parse(it) }

    val pickMedia = rememberLauncherForActivityResult(PickVisualMedia()) { uri ->
        // Callback is invoked after the user selects a media item
        // or closes the photo picker.
        uri?.let {
            val imageUri = saveImageToScopedStorage(context, it)
            onValueChange(taskDetails.copy(imageUri = imageUri.toString()))
        }
    }

    // Request launcher for external storage permissions
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val readGranted = permissions[Manifest.permission.READ_EXTERNAL_STORAGE] ?: false
        val writeGranted = permissions[Manifest.permission.WRITE_EXTERNAL_STORAGE] ?: false

        if (readGranted && writeGranted) {
            pickMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))

        } else {
            // Permissions denied; inform the user
            Toast.makeText(context, "Permissions denied to read and save image.", Toast.LENGTH_SHORT).show()
        }
    }

    selectedImageUri?.let {
        AsyncImage(
            model = selectedImageUri,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 8.dp),
            contentScale = ContentScale.Fit
        )
        Button(
            onClick = { onValueChange(taskDetails.copy(imageUri = null)) },  // Clear the selected image URI
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFD50001),        // Background color
                contentColor = Color.White         // Text color
            )
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_broken_image_24),
                contentDescription = stringResource(R.string.remove_image),
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(text = stringResource(R.string.remove_image))  // Label for the button
        }
    }

    Button(
        onClick = {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // Android 10+ (Scoped Storage) - No need to request permissions
                pickMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
            } else {
                // Android 9 and below - Check and request permissions
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                    pickMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
                } else {
                    // Request both READ and WRITE permissions
                    requestPermissionLauncher.launch(
                        arrayOf(
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        )
                    )
                }
            }
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            painter = painterResource(id = R.drawable.baseline_image_search_24),
            contentDescription = stringResource(R.string.select_image),
            modifier = Modifier.padding(end = 8.dp)
        )
        Text(stringResource(R.string.select_image))
    }
}

fun saveImageToInternalStorage(context: Context, imageUri: Uri): Uri? {
    val contentResolver = context.contentResolver
    val inputStream = contentResolver.openInputStream(imageUri) ?: return null

    // Create a unique file name
    val fileName = "image_${System.currentTimeMillis()}.jpg"

    // Create a file in the app's internal storage directory
    val file = File(context.filesDir, fileName)

    // Use FileOutputStream to save the image
    FileOutputStream(file).use { outputStream ->
        inputStream.copyTo(outputStream) // Copy the image from the original URI to the new URI
    }

    inputStream.close()

    // Return the URI to the file in internal storage
    return Uri.fromFile(file)
}

fun saveImageToScopedStorage(context: Context, imageUri: Uri): Uri? {
    val contentResolver = context.contentResolver
    val fileName = "image_${System.currentTimeMillis()}.jpg"
    val picturesDir = "Pictures/Taskly"

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        // Use Scoped Storage API for Android 10+
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.RELATIVE_PATH, picturesDir)
        }

        val inputStream = contentResolver.openInputStream(imageUri) ?: return null
        val uri: Uri? =
            contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        if (uri != null) {
            contentResolver.openOutputStream(uri).use { outputStream ->
                inputStream.copyTo(outputStream ?: return null)
            }
            inputStream.close()
        }
        return uri
    } else {
        // For Android 9 and below, use legacy external storage APIs
        val file = File(
            context.getExternalFilesDir(picturesDir),
            fileName
        )
        val outputStream = FileOutputStream(file)
        val inputStream = contentResolver.openInputStream(imageUri)

        inputStream?.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }

        return Uri.fromFile(file)
    }
}