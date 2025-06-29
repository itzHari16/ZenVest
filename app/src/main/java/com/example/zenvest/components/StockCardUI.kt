package com.example.zenvest.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.zenvest.R


@Composable
fun StockCard(
    name: String,
    price: String,
    change : String,
    changeIsPositive: Boolean,
    onClick: ()-> Unit
) {

    val changeColor = if (changeIsPositive) Color(0xFF00FF66) else Color.Red
    Box(
        modifier = Modifier
            .padding(horizontal = 12.dp, vertical = 12.dp)
            .width(180.dp)
            .height(140.dp)
            .border(
                width = 2.dp,
                color = Color.White,
                shape = RoundedCornerShape(18.dp)
            )
            .clip(RoundedCornerShape(18.dp))
            //.background(Color.Transparent) // Card background
            .background(Color.Black)
            .padding(12.dp)
            .clickable{onClick()}
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = name,
                        style = TextStyle(
                            color = Color.White,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.W300
                        )
                    )
                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    fontSize = 17.sp, // smaller font for dollar symbol
                                    fontWeight = FontWeight.Normal
                                )
                            ) {
                                append("$")
                            }
                            withStyle(
                                style = SpanStyle(
                                    fontSize = 30.sp, // larger font for price
                                    fontWeight = FontWeight.W500
                                )
                            ) {
                                val formattedPrice = price.toDoubleOrNull()?.let { String.format("%.3f", it) } ?: price
                                append(formattedPrice) // assuming price is a variable of type String or can be converted to one
                            }
                        },
                        style = TextStyle(
                            color = Color.White
                        ))

                }
                Icon(
                    painter = painterResource(id = R.drawable.apple_icon), // Add this drawable
                    contentDescription = "Apple Logo",
                    modifier = Modifier.size(70.dp),
                    tint = Color.Gray
                )

            }
            Text(
                text = change,
                style = TextStyle(
                    color = changeColor, // Bright green
                    fontSize = 15.sp,
                )
            )

        }


    }
}


@Preview(showBackground = true)
@Composable
fun stockui(){
    StockCard(
        name = "hari",
        price = "0.132",
        change = "5.77",
        changeIsPositive = true,
        onClick = TODO(),
    )
}
