package com.example.gengolearning.ui.features.dashboard.home.mylist


import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gengolearning.model.appmodels.WordCategories
import com.example.gengolearning.ui.theme.White
import com.gengolearning.app.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategorySelectorBottomSheet(categories: List<WordCategories>, onClick: (category: WordCategories) -> Unit = {}, onDismiss: () -> Unit = {}) {
    ModalBottomSheet(
        onDismissRequest = {
              onDismiss()
        },
        containerColor = White
    ) {
        Text(
            text = stringResource(R.string.category_selector_bottomsheet_title),
            fontSize = 20.sp,
            modifier = Modifier.padding(start = 10.dp)
        )

        FlowRow(
            modifier = Modifier.padding(5.dp)
        ) {
            categories.forEach { categories ->
                FilterChip(
                    selected = false,
                    onClick = {
                        onClick(
                            categories
                        )
                    },
                    label = {
                        Text(text = categories.categoryName,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis)
                    },
                    colors = if (categories.color != null) FilterChipDefaults.filterChipColors(
                        containerColor = Color(categories.color)
                    ) else FilterChipDefaults.filterChipColors(
                        containerColor = Color.Unspecified
                    ),
                    modifier = Modifier.padding(5.dp),
                    shape = RoundedCornerShape(20.dp)
                )
            }
        }


    }
}


@Preview
@Composable
private fun Preview() {
    val categories = listOf(
        WordCategories(categoryName = "Category 1"),
        WordCategories(categoryName = "Category 2"),
        WordCategories(categoryName = "Category 2"),
        WordCategories(categoryName = "Category 2"),
        WordCategories(categoryName = "Category 2"),
        WordCategories(categoryName = "Category 2")

    )
    CategorySelectorBottomSheet(categories)
}