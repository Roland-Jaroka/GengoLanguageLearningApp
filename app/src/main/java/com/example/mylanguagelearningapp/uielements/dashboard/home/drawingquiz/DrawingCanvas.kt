package com.example.mylanguagelearningapp.uielements.dashboard.home.drawingquiz


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import com.example.mylanguagelearningapp.ui.theme.BgBlue
import com.example.mylanguagelearningapp.ui.theme.White
import com.example.mylanguagelearningapp.uielements.dashboard.home.drawingquiz.drawPath
import kotlin.math.abs


@Composable
fun DrawingCanvas(
    paths: List<PathData> = emptyList(),
    currentPath: PathData?,
    onAction: (DrawingActions) -> Unit,
    modifier: Modifier = Modifier
    ) {

    Card(shape = RoundedCornerShape(30.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
            .padding(10.dp),
        colors = CardDefaults.cardColors(White),
        elevation = CardDefaults.cardElevation(10.dp)
    ) {
        Box(modifier = Modifier
            .fillMaxSize()
            ) {
            Canvas(
                modifier= Modifier
                    .fillMaxSize()
                    .pointerInput(true){
                        detectDragGestures(
                            onDragStart = {
                                onAction(DrawingActions.OnNewPathStart)
                            },
                            onDragEnd= {
                                onAction(DrawingActions.OnPathEnd)
                            },
                            onDrag = { change, _ ->
                                onAction(DrawingActions.OnDraw(change.position))
                            },
                            onDragCancel= {
                                onAction(DrawingActions.OnPathEnd)
                            }

                        )


                    }
            ) {
                paths.fastForEach{ pathData ->
                    drawPath(path = pathData.path, color = pathData.color)

                }
                currentPath?.let {
                    drawPath(
                        path = it.path,
                        color = it.color
                    )
                }
            }

        }
    }
}

private fun DrawScope.drawPath(
    path: List<Offset>,
    color: Color = Color.Black,
    strokeWidth: Float= 10f
){

    val smoothedPath = androidx.compose.ui.graphics.Path().apply {
        if (path.isNotEmpty()) {
            moveTo(path.first().x, path.first().y)
            for (i in 1..path.lastIndex) {
                val from= path[i - 1]
                val to = path[i]

                    quadraticTo(
                        x1= (from.x +to.x) /2f,
                        y1= (from.y +to.y) /2f,
                        x2= to.x,
                        y2= to.y
                    )

            }
        }
    }

    drawPath(
        path = smoothedPath,
        color= color,
        style= Stroke(
            width= strokeWidth,
            cap= StrokeCap.Round,
            join = StrokeJoin.Round

        )

    )

}
