package com.assigment.assignmentgallery.presentation.photo_list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.assigment.assignmentgallery.R
import com.assigment.assignmentgallery.domain.model.Photo

@Composable
fun PhotoListItem(
    photo: Photo,
    onClick: () -> Unit
) {

    Card(

        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },

        shape = RoundedCornerShape(20.dp),

        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF242438)
        ),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )

    ) {

        Row(

            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),

            verticalAlignment = Alignment.CenterVertically

        ) {

            AsyncImage(

                model = photo.thumbnailUrl,

                contentDescription = stringResource(R.string.photo),

                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(16.dp)),

                placeholder = painterResource(R.drawable.placeholder_image),

                error = painterResource(R.drawable.placeholder_image),
                contentScale = ContentScale.Crop

            )

            Spacer(
                modifier = Modifier.size(16.dp)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(

                    text = photo.title,

                    color = Color.White,

                    style = MaterialTheme.typography.titleMedium,

                    fontWeight = FontWeight.SemiBold,

                    maxLines = 2,

                    overflow = TextOverflow.Ellipsis

                )

                Spacer(
                    modifier = Modifier.height(6.dp)
                )

                Box(

                    modifier = Modifier
                        .background(
                            color = Color(0xFF303048),
                            shape = RoundedCornerShape(50)
                        )
                        .padding(
                            horizontal = 10.dp,
                            vertical = 4.dp
                        )

                ) {

                    Text(

                        text = "#${photo.id}",

                        color = Color(0xFFB8B8C5),

                        style = MaterialTheme.typography.bodySmall

                    )

                }

            }

        }

    }

}