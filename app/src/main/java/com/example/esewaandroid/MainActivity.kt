package com.example.esewaandroid

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.esewaandroid.ui.theme.ESewaAndroidTheme
import com.f1soft.esewapaymentsdk.ESewaConfiguration
import com.f1soft.esewapaymentsdk.ESewaPayment
import com.f1soft.esewapaymentsdk.ui.ESewaPaymentActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ESewaAndroidTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PaymentScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun PaymentScreen(modifier: Modifier = Modifier) {

    val clientId = "JB0BBQ4aD0UqIThFJwAKBgAXEUkEGQUBBAwdOgABHD4DChwUAB0R"
    val secretKey = "BhwIWQQADhIYSxILExMcAgFXFhcOBwAKBgAXEQ=="

    var amount by remember { mutableStateOf(TextFieldValue("")) }
    val context = LocalContext.current

    val eSewaConfiguration: ESewaConfiguration = ESewaConfiguration()
        .clientId(clientId)
        .secretKey(secretKey)
        .environment(ESewaConfiguration.ENVIRONMENT_TEST)

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            when (it.resultCode) {
                Activity.RESULT_OK -> {
                    Toast.makeText(context, R.string.success, Toast.LENGTH_LONG).show()
                }
                Activity.RESULT_CANCELED -> {
                    Toast.makeText(context, R.string.cancelled, Toast.LENGTH_LONG).show()
                }
                ESewaPayment.RESULT_EXTRAS_INVALID -> {
                    Toast.makeText(context, R.string.invalid, Toast.LENGTH_LONG).show()
                }
            }
        }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = amount,
            label = { Text(text = stringResource(id = R.string.enter_amount)) },
            onValueChange = {
                amount = it
            }
        )
        
        Spacer(modifier = Modifier.height(40.dp))

        Button(onClick = {
            val eSewaPayment = ESewaPayment(
                amount = amount.text,
                productName = "productName",
                productUniqueId = "1",
                callbackUrl = ""
            )

            val intent = Intent(context, ESewaPaymentActivity::class.java)
            intent.putExtra(
                ESewaConfiguration.ESEWA_CONFIGURATION,
                eSewaConfiguration
            )

            intent.putExtra(ESewaPayment.ESEWA_PAYMENT, eSewaPayment)
            launcher.launch(intent)
        }) {
            Text(text = stringResource(id = R.string.make_payment))
        }
    }
}
