/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.tiptime

import android.inputmethodservice.Keyboard
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
/*Importamos la libreria */
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tiptime.ui.theme.TipTimeTheme
import java.text.NumberFormat
/*Importamos la librería*/
import androidx.annotation.StringRes
/*Importamos las librerias*/
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Switch
/*Importamos las librerias*/
import androidx.annotation.DrawableRes
import androidx.compose.material3.Icon
import androidx.compose.ui.res.painterResource


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            TipTimeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    TipTimeLayout()
                }
            }
        }
    }
}

@Composable
fun TipTimeLayout() {
    var amountInput by remember { mutableStateOf("") }

    val amount = amountInput.toDoubleOrNull() ?: 0.0

    /*Agregamos un var llamado tipInput para la variable de estado del campo*/
    var tipInput by remember { mutableStateOf("") }
    /*Definimos un elemento de tipo val que comvierte la variable tipInput en Double*/
    val tipPercent = tipInput.toDoubleOrNull() ?: 0.0
    /*Se agrega una variable var para el estado del elemento Swich*/
    var roundUp by remember { mutableStateOf(false) }
    /*Se actuaiza la funcion calculaTip() para que pase la variable tipPercent */
    /*Se vuelve a actualizar la variable tip para que la funcion calculateTip acepte roundUp*/
    val tip = calculateTip(amount, tipPercent, roundUp)


    Column(
        modifier = Modifier
            .statusBarsPadding()
            .padding(horizontal = 40.dp)
            .verticalScroll(rememberScrollState())
            .safeDrawingPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.calculate_tip),
            modifier = Modifier
                .padding(bottom = 16.dp, top = 40.dp)
                .align(alignment = Alignment.Start)
        )

        EditNumberField(
            /*Establecemos label en el recurso de Cadens R.R.string.bill_amount*/
            label = R.string.bill_amount,
            leadingIcon = R.drawable.money,
            /*Pasamos los parrametros que teniamos en la otra funcion*/
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            value = amountInput,
            onValueChanged = { amountInput = it },
            modifier = Modifier.padding(bottom = 32.dp).fillMaxWidth()
        )
        /*Agregamos otro campo de texto para el porcentaje de propina*/
        EditNumberField(
            label = R.string.how_was_the_service,
            leadingIcon = R.drawable.percent,
            /*Aqui pegamos lo mismo que la otra llamada pero en el imeAction ponemos Done envez de next*/
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            /*Configuramos el parametro en el value por el tipInput
            y actualizamos la variable en la exprecion lambda onValueChanged*/
            value = tipInput,
            onValueChanged = { tipInput= it },
            modifier = Modifier.padding(bottom = 32.dp).fillMaxWidth()
        )
        /*Llamamos a la funcion RoundTheTipRow con los argumentos roundUp,onRoundUpChanged, modifier */
        RoundTheTipRow(
            roundUp = roundUp,
            onRoundUpChanged = { roundUp = it },
            modifier = Modifier.padding(bottom = 32.dp)
        )
        Text(
            text = stringResource(R.string.tip_amount, tip),
            style = MaterialTheme.typography.displaySmall
        )
        Spacer(modifier = Modifier.height(150.dp))
    }
}

@Composable
fun EditNumberField(
    /*Agregamos un recurso de cadeda de tipo Int*/
    /*Indicamos que el parametro Label sea un recurso de cadena*/
    @StringRes label: Int,
    /*Agregamos el parametro leadingIcon  de tipo Int con anotacion @DrawableRes */
    @DrawableRes leadingIcon: Int,
    /*Agregamos un parametro KeyboardOptions*/
    keyboardOptions: KeyboardOptions,
    value: String,
    onValueChanged: (String) -> Unit,
    modifier: Modifier
) {
    TextField(
        value = value,
        /*Agregamos el icono al campo de texto */
        leadingIcon = { Icon(painter = painterResource(id = leadingIcon), null) },
        singleLine = true,
        modifier = modifier,
        onValueChange = onValueChanged,
        /*Reemplazamos el ID de recurso de cadenas con el parametro Label*/
        label = { Text(stringResource(label)) },
        /*En keyboardOpcions aseguramos usar otras opciones predeterminadas con KeyboardOptions.Default.copy */
        /*Eliminamos lo anterior y asignamos el parametro con nombre KeyboardOptions*/
        keyboardOptions = keyboardOptions
    )
}
/*Creamos una nueva funcion y agregamos un Modifier como argumentos similares a la funcion EfitNumberField()*/
/*Agregamos los parametros roundUp de tipo Boolean y una funcion lambda que tome un Boolean */
@Composable
fun RoundTheTipRow(roundUp: Boolean,
                   onRoundUpChanged: (Boolean) -> Unit,
                   modifier: Modifier = Modifier){
    /*Agregamos un elemento Row con modifier para establecer el ancho de los elementos, centrar la alineacion y garantizar un tamaño de 48dp*/
    Row(modifier = modifier
            .fillMaxWidth()
            .size(48.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        /*Agregamos la funcion de compatibilidad Text y usa el recurso de cadenas R.string.round_up_tip */
        Text(text = stringResource(R.string.round_up_tip))
        /*Agregamos una funcion Swich con un checked y un paramatro con nombre onCheckedChange  */
        Switch(
            /*Se agrega este modifier para alinear la funcion Switch al final de la pantalla*/
            modifier = modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.End),
            checked = roundUp,
            onCheckedChange = onRoundUpChanged,
        )
    }
}

/**
 * Calculates the tip based on the user input and format the tip amount
 * according to the local currency.
 * Example would be "$10.00".
 */
/*Agregamos el parrametro roundUp de tipo Boolean*/
private fun calculateTip(amount: Double, tipPercent: Double = 15.0, roundUp: Boolean): String {
    var tip = tipPercent / 100 * amount
    /*Agregamos un if que verifique el valor roundUp, si es true define una variable tip, establécela en kotlin.math.ceil() y pasa la función tip como argumento*/
    if (roundUp) {
        tip = kotlin.math.ceil(tip)
    }
    return NumberFormat.getCurrencyInstance().format(tip)
}

@Preview(showBackground = true)
@Composable
fun TipTimeLayoutPreview() {
    TipTimeTheme {
        TipTimeLayout()
    }
}

