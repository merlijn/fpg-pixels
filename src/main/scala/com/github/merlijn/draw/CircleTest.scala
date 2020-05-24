package com.github.merlijn.draw

import org.scalajs.dom

object CircleTest {

  def next(x: Double, y: Double): (Double, Double) = {
    // intersection
    val iy = Math.sqrt(1 - Math.pow(x, 2))
    val ix = Math.sqrt(1 - Math.pow(y, 2))

    val dfy = if (y > 0) y - (iy - y) else y - (-iy - y)
    val dfx = if (x > 0) x - (ix - x) else x - (-ix - x)

    (dfx, dfy)
  }

  def iterA(n: Int, max: Int, x: Double, y: Double): Int = {

    if (Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)) > 1)
      n
    else if (n >= max)
      n
    else {
      val (dfx, dfy) = next(x, y)

      iterA(n + 1, max, dfx, dfy)
    }
  }

  def iterB(n: Int, max: Int, x: Double, y: Double): List[(Double, Double)] = {

//    if (Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)) > 1)
//      List.empty
    if (n > max)
      List.empty
    else {
      val (dfx, dfy) = next(x, y)

      (x, y) +: iterB(n + 1, max, dfx, dfy)
    }
  }

  def draw(ctx: dom.CanvasRenderingContext2D, w: Int, h: Int): Unit = {

    val r = Math.min(w, h) / 2

    val imageData = ctx.getImageData(0, 0, w, h)
    val data = imageData.data

    val offsetX = 0
    val offsetY = 0
    val zoom = 1

    for (x <- 0 to w) {
      for (y <- 0 to h) {

        val cx = (x - r).toDouble / (r * zoom) + offsetX
        val cy = (y - r).toDouble / (r * zoom) + offsetY

        def color(b: Int, max: Int): Int = {

          val s = iterA(0, max, cx, cy)
          val f = 1 - (s.toDouble / max)
          (f * b).toInt
        }

        // translate (x,y) coordinates to image data array index
        val idx = y * (w * 4) + x * 4

        // set the color data
        data.update(idx, color(255, 80))
        data.update(idx + 1, color(255, 80))
        data.update(idx + 2, color(255, 80))
        data.update(idx + 3, 255)
      }
    }

    ctx.putImageData(imageData, 0, 0)
  }


  def drawB(ctx: dom.CanvasRenderingContext2D, w: Int, h: Int): Unit = {

    val r = Math.min(w, h) / 2

    val imageData = ctx.getImageData(0, 0, w, h)
    val data = imageData.data
    val dataSize = h * (w * 4)

    for (x <- 0 to w) {
      for (y <- 0 to h) {

        val cx = (x - r).toDouble / r
        val cy = (y - r).toDouble / r

        val seq = iterB(0, 50, cx, cy)

        for (p <- seq) {

          val imgX = p._1 * r + r
          val imgY = p._2 * r + r
          val idx = imgY.toInt * (w * 4) + imgX.toInt * 4

          if (idx >= 0 && idx < dataSize) {

            data(idx)
            // set the color data
            data.update(idx, data(idx) + 12)
            data.update(idx + 1, data(idx + 1) + 10)
            data.update(idx + 2, data(idx + 2) + 6)
            data.update(idx + 3, 255)
          }
        }
      }
    }

    ctx.putImageData(imageData, 0, 0)
  }
}
