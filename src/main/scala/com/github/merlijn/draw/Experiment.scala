package com.github.merlijn.draw

import org.scalajs.dom

object Experiment {

//  Math.E

  // 1.87
  // 2.21
  // 2.29558714939

  val FAC = 2 // 2.5 // 1.87 // 0.6931471805 //1.92 //1.83 //  // 1.61803398875
  val MAX_R = Math.sqrt(2.0) //3

  def next(x: Double, y: Double): (Double, Double) = {

    // intersection
    val iy = Math.sqrt(1 - Math.pow(x, 2))
    val ix = Math.sqrt(1 - Math.pow(y, 2))

    val dfy = if (y > 0) FAC * y - iy else FAC * y + iy
    val dfx = if (x > 0) FAC * x - ix else FAC * x + ix

    (dfx, dfy)
  }

  def recurLength(n: Int, max: Int, x: Double, y: Double): Int = {

    if (x >= 1 || y >= 1)
      n
    else if (Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)) >= MAX_R)
      n
    else if (n >= max)
      n
    else {
      val (dfx, dfy) = next(x, y)

      recurLength(n + 1, max, dfx, dfy)
    }
  }

  def recurPoints(n: Int, max: Int, x: Double, y: Double): List[(Double, Double)] = {

    if (x >= 1 || y >= 1)
      List.empty
    else if (Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)) >= MAX_R)
      List.empty
    else if (n > max)
      List.empty
    else {
      val (dfx, dfy) = next(x, y)

      (x, y) +: recurPoints(n + 1, max, dfx, dfy)
    }
  }

  def drawA(ctx: dom.CanvasRenderingContext2D, w: Int, h: Int): Unit = {

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

          val s = recurLength(0, max, cx, cy)
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

    val s = Math.min(w, h)
    val r = s / 2

    val imageData = ctx.getImageData(0, 0, w, h)
    val data = imageData.data
    val dataSize = h * (w * 4)

    def toImage(c: Double): Int = (c * r + r).round.toInt

    def incPixel(x: Int, y: Int, f: Double = 1): Unit = {
      val idx = x.toInt * (w * 4) + y.toInt * 4

      if (x < s && y < s && idx >= 0 && idx < dataSize) {

        data(idx)
        // set the color data
        data.update(idx, data(idx) + (5 * f).toInt)
        data.update(idx + 1, data(idx + 1) + (4 * f).toInt)
        data.update(idx + 2, data(idx + 2) + (2 * f).toInt)
        data.update(idx + 3, 255)
      }
    }

    for (x <- 0 to Math.min(w, h)) {
      for (y <- 0 to Math.min(w, h)) {

        val cx = (x - r).toDouble / r
        val cy = (y - r).toDouble / r

        val seq = recurPoints(0, 32, cx, cy)

        for (p <- seq) {

          val imgX = p._1 * r + r
          val imgY = p._2 * r + r

          incPixel(imgX.round.toInt, imgY.round.toInt, FAC * 1.5) //2 - (imgX - imgX.floor) - (imgY - imgY.floor))
        }
      }
    }


    ctx.putImageData(imageData, 0, 0)


    def plot(x: Double, y: Double) = {
      val points = recurPoints(0, 32, x, y)

      println(points.size)

      ctx.beginPath()

      val (sx, sy) = points.head
      ctx.moveTo(toImage(sx), toImage(sy))

      points.foreach{ case (x, y) => ctx.lineTo(toImage(x), toImage(y)) }
      ctx.closePath()
      ctx.stroke()
    }

//    ctx.strokeStyle = "black"
//    plot(0.25, 0.45)
//    ctx.strokeStyle = "red"
//    plot(0.23, 0.47)

//    ctx.strokeStyle = "black"
//    ctx.beginPath()
//    ctx.arc(r, r, r, 0, 2 * Math.PI)
//    ctx.closePath()
//    ctx.stroke()
  }
}
