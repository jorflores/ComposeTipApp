package com.example.tipapp


import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tipapp.components.InputField
import com.example.tipapp.ui.theme.TipAppTheme
import com.example.tipapp.widgets.RoundedIconButton

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp() {
                //  Text(text = "Hello Again")
                MainContent()
            }
        }
    }
}

@Preview
@Composable
fun TopHeader(totalPerPerson: Double = 0.0) {

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .clip(shape = RoundedCornerShape(corner = CornerSize(12.dp))), color = Color(0xFFE9D7F7)
    ) {

        val total = "%.2f".format(totalPerPerson)

        Column(
            modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "Total Per Person",
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = "$$total",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold
            )

        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainContent() {

    Column(modifier = Modifier.padding(all = 12.dp))
    {
        BillForm()
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BillForm(
    modifier: Modifier = Modifier

) {

    var splitNumber by remember {
        mutableIntStateOf(2)
    }

    var sliderPositionState by remember {
        mutableFloatStateOf(0f)
    }

    val tipPercentage = sliderPositionState.toInt()

    val totalBillState = remember {
        mutableStateOf("")
    }
    val validState = remember(totalBillState.value) {
        totalBillState.value.trim().isNotEmpty()
    }
    val keyboardController = LocalSoftwareKeyboardController.current

    var tipAmountState by remember {
        mutableDoubleStateOf(0.0)
    }

    val totalPerPersonState = remember {
        mutableDoubleStateOf(0.0)
    }

    TopHeader(totalPerPerson = totalPerPersonState.value)

    Surface(
        modifier = Modifier
            .padding(2.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(corner = CornerSize(8.dp)),
        border = BorderStroke(width = 1.dp, color = Color.LightGray)
    ) {
        Column(
            modifier = Modifier.padding(6.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {

            InputField(valueState = totalBillState,
                labelId = "Enter Bill",
                enabled = true,
                isSingleLine = true,
                onAction = KeyboardActions {

                    if (!validState) return@KeyboardActions

                    //  onValChange(totalBillState.value.trim())

                    keyboardController?.hide()
                }
            )
            //    if (validState) {
            Row(modifier = Modifier.padding(3.dp), horizontalArrangement = Arrangement.Start) {

                Text(
                    text = "Split", modifier = Modifier.align(
                        alignment = Alignment.CenterVertically
                    )
                )

                Spacer(modifier = Modifier.width(120.dp))

                Row(
                    modifier = Modifier.padding(horizontal = 3.dp),
                    horizontalArrangement = Arrangement.End
                ) {

                    RoundedIconButton(
                        imageVector = Icons.Default.Remove,
                        onClick = {
                            if (splitNumber > 1) {
                                splitNumber--
                                totalPerPersonState.value =
                                    calculateTotalPerPerson(
                                        totalBill = totalBillState.value.toDouble(),
                                        splitBy = splitNumber, tipPercentage = tipPercentage
                                    )
                            }
                        })

                    Text(
                        text = splitNumber.toString(),
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(start = 9.dp, end = 9.dp)
                    )

                    RoundedIconButton(
                        imageVector = Icons.Default.Add,
                        onClick = {
                            splitNumber++
                            totalPerPersonState.value =
                                calculateTotalPerPerson(
                                    totalBill = totalBillState.value.toDouble(),
                                    splitBy = splitNumber, tipPercentage = tipPercentage
                                )
                        })
                }
            }

            //Tip Row
            Row(modifier = Modifier.padding(horizontal = 3.dp, vertical = 12.dp)) {

                Text(text = "Tip")
                Spacer(modifier = Modifier.width(200.dp))
                Text(text = "$tipAmountState")
            }

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(text = "$tipPercentage %")
                Spacer(modifier = Modifier.height(14.dp))

                //Slider
                Slider(
                    value = sliderPositionState,
                    onValueChange = { newval ->
                        sliderPositionState = newval

                        tipAmountState =
                            calculateTotalTip(totalBillState.value.toDouble(), tipPercentage)

                        totalPerPersonState.value =
                            calculateTotalPerPerson(
                                totalBill = totalBillState.value.toDouble(),
                                splitBy = splitNumber, tipPercentage = tipPercentage
                            )

                    },
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                    valueRange = (0f..100f)
                )
            }

            /* } else {
                 Box {

                 }
             }*/
        }
    }
}

fun calculateTotalTip(totalBill: Double, tipPercentage: Int): Double {

    return if (totalBill > 1 && totalBill.toString().isNotEmpty())
        (totalBill * tipPercentage) / 100 else 0.0
}

fun calculateTotalPerPerson(totalBill: Double, splitBy: Int, tipPercentage: Int): Double {

    val bill = calculateTotalTip(totalBill = totalBill, tipPercentage = tipPercentage) + totalBill

    return (bill / splitBy)
}


@Composable
fun MyApp(content: @Composable () -> Unit) {

    TipAppTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            content()
        }
    }
}

@Composable
@Preview(showBackground = true)
fun DefaultPreview() {

    TipAppTheme {
        MyApp {

            MainContent()
        }
    }

}

