package com.imcys.bilibilias.home.ui.viewmodel.player

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.view.LayoutInflater
import android.view.View
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSavedStateRegistryOwner
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.imcys.bilibilias.R
import com.imcys.bilibilias.base.utils.DialogUtils
import com.imcys.bilibilias.databinding.DialogCollectionBinding

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ddddd() {
    Column {
        ModalBottomSheet(onDismissRequest = { }) {
            LazyColumn(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxSize()
                    .clip(RoundedCornerShape(8.dp))
            ) {
                items(60) {
                    Row(modifier = Modifier.fillMaxSize()) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                        ) {
                            Text(text = "haaa")
                            Text(text = "ddddddddd\t公开")
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        RadioButton(
                            selected = true,
                            onClick = null,
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                    }
                    HorizontalDivider()
                }
            }
            Button(onClick = { /*TODO*/ }, modifier = Modifier.fillMaxWidth()) {
                Text(text = "完成")
            }
        }
    }
}

fun getUserCreateCollectionDialog(activity: ComponentActivity): BottomSheetDialog {
    val binding = DialogCollectionBinding.inflate(LayoutInflater.from(activity))

    val bottomSheetDialog = BottomSheetDialog(activity, R.style.BottomSheetDialog)
    binding.dialogCollectionRv.setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
    setViewTreeOwners(activity)
    bottomSheetDialog.setContentView(binding.root)

    DialogUtils.initDialogBehaviorBinding(
        binding.dialogCollectionBar,
        activity,
        binding.root.parent,
    )

    binding.dialogCollectionRv.setContent {


    }
    return bottomSheetDialog
}

private fun setViewTreeOwners(context: Context) {
    val activity =
        extractActivity(context) // if you have activity present nearby, then just use your activity here.
    val decorView = activity?.window?.decorView
    if (activity != null && decorView != null) {
        decorView.setViewTreeLifecycleOwner(activity as? LifecycleOwner)
        decorView.setViewTreeViewModelStoreOwner(activity as? ViewModelStoreOwner)
        decorView.setViewTreeSavedStateRegistryOwner(activity as? SavedStateRegistryOwner)
    }
}

private fun extractActivity(context: Context): Activity? {
    var currentContext = context
    while (true) {
        if (currentContext is Activity) return currentContext
        if (currentContext !is ContextWrapper) break
        currentContext = currentContext.baseContext
    }
    return null
}

@Composable
fun showBottomSheetDialog() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val savedStateRegistryOwner = LocalSavedStateRegistryOwner.current

    val bottomSheetDialog = object : BottomSheetDialog(context) {
        override fun setContentView(view: View) {
            super.setContentView(view)

            // Workaround :/
            val fieldContainer =
                BottomSheetDialog::class.java.getDeclaredField("container").apply {
                    isAccessible = true
                }
            val container = fieldContainer.get(this) as View
            container.setViewTreeLifecycleOwner(lifecycleOwner)
            container.setViewTreeSavedStateRegistryOwner(savedStateRegistryOwner)
        }
    }

    @Composable
    fun BottomSheetContent() {
        // ...
    }

    bottomSheetDialog.setContentView(
        ComposeView(bottomSheetDialog.context).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                BottomSheetContent()
            }
        }
    )

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_PAUSE) {
                bottomSheetDialog.dismiss()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    bottomSheetDialog.show()
}
