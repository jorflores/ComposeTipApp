package com.example.tipapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.rounded.AttachMoney
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tipapp.ui.theme.TipAppTheme

class MainActivity2 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            MyApp2 {
                MainContent2()
            }
        }
    }
}


// 1
@Composable
fun MyApp2(content: @Composable () -> Unit) {

    TipAppTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            content()
        }
    }

}

// 2
@Composable
fun TopHeader2(totalPerPerson: Double = 0.0) {

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .clip(shape = RoundedCornerShape(corner = CornerSize(12.dp))),
        color = Color(0xFFE9D7F7)
    ) {

        val total = "%.2f".format(totalPerPerson)

        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
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


//3
@Composable
@Preview(showBackground = true)
fun MainContent2() {

    Column(modifier = Modifier.padding(all = 12.dp)) {
        BillForm2()
    }

}

//4 Bill Form

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BillForm2(modifier: Modifier = Modifier) {


    var sliderPositionState = remember {
        mutableStateOf(0f)
    }

    val tipPercentage = sliderPositionState.value.toInt()

    var tipAmountState = remember {
        mutableStateOf(0.0)
    }

    var splitNumber = remember {
        mutableStateOf(2)
    }


    val totalBillState = remember {
        mutableStateOf("")
    }

    val keyboardController = LocalSoftwareKeyboardController.current

    val validState = remember(totalBillState.value) {
        totalBillState.value.trim().isNotEmpty()
    }

    val totalPerPersonState = remember {
        mutableStateOf(0.0)
    }

    TopHeader(totalPerPerson = totalPerPersonState.value)

    Surface(
        modifier = Modifier
            .padding(2.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(corner = CornerSize(8.dp)),
        border = BorderStroke(width = 1.dp, color = Color.LightGray)
    ) {
        Column {

            OutlinedTextField(
                value = totalBillState.value,
                onValueChange = { totalBillState.value = it },
                label = { Text(text = "Enter Bill") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.AttachMoney,
                        contentDescription = "Money Icon"
                    )
                },
                singleLine = true,
                textStyle = TextStyle(
                    fontSize = 18.sp,
                    color = /*MaterialTheme.colorScheme.outline*/LocalContentColor.current
                ),
                modifier = modifier
                    .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
                    .fillMaxWidth(),
                enabled = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions {
                    if (!validState) return@KeyboardActions

                    keyboardController?.hide()
                }

            )



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


                    Card(modifier = modifier
                        .padding(4.dp)
                        .clickable {

                            if (splitNumber.value > 1) {
                                splitNumber.value--
                                totalPerPersonState.value =
                                    calculateTotalPerPerson(
                                        totalBill = totalBillState.value.toDouble(),
                                        splitBy = splitNumber.value,
                                        tipPercentage = tipPercentage
                                    )
                            }


                        }
                        .then(Modifier.size(40.dp)),
                        shape = CircleShape,
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
                        elevation = CardDefaults.cardElevation(4.dp)) {

                        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize())

                        {
                            Icon(
                                imageVector = Icons.Default.Remove,
                                contentDescription = "Plus or Minus Icon",
                                tint = Color.Black.copy(alpha = 0.8f)
                            )
                        }

                    }

                    Text(
                        text = splitNumber.value.toString(),
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(start = 9.dp, end = 9.dp)
                    )

                    Card(modifier = modifier
                        .padding(4.dp)
                        .clickable {

                            splitNumber.value++
                            totalPerPersonState.value =
                                calculateTotalPerPerson(
                                    totalBill = totalBillState.value.toDouble(),
                                    splitBy = splitNumber.value, tipPercentage = tipPercentage
                                )


                        }
                        .then(Modifier.size(40.dp)),
                        shape = CircleShape,
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
                        elevation = CardDefaults.cardElevation(4.dp)) {

                        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize())

                        {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Plus or Minus Icon",
                                tint = Color.Black.copy(alpha = 0.8f)
                            )
                        }
                    }
                }
            }


            Row(modifier = Modifier.padding(horizontal = 3.dp, vertical = 12.dp)) {

                Text(text = "Tip")
                Spacer(modifier = Modifier.width(200.dp))
                Text(text = "${tipAmountState.value}")
            }

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(text = "$tipPercentage %")
                Spacer(modifier = Modifier.height(14.dp))

                //Slider
                Slider(
                    value = sliderPositionState.value,
                    onValueChange = { newval ->
                        sliderPositionState.value = newval


                        tipAmountState.value =
                            calculateTotalTip2(
                                totalBill = totalBillState.value.toDouble(),
                                tipPercentage
                            )

                        totalPerPersonState.value =
                            calculateTotalPerPerson2(
                                totalBill = totalBillState.value.toDouble(),
                                splitBy = splitNumber.value, tipPercentage = tipPercentage
                            )

                    },
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                    valueRange = (0f..100f)
                )
            }


        }
    }

}

fun calculateTotalTip2(totalBill: Double, tipPercentage: Int): Double {

    return if (totalBill > 1 && totalBill.toString().isNotEmpty())
        (totalBill * tipPercentage) / 100 else 0.0
}

fun calculateTotalPerPerson2(totalBill: Double, splitBy: Int, tipPercentage: Int): Double {

    val bill = calculateTotalTip(totalBill = totalBill, tipPercentage = tipPercentage) + totalBill

    return (bill / splitBy)
}