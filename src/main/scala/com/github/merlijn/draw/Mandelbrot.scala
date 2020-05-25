package com.github.merlijn.draw

import com.github.merlijn.calc.Complex
import org.scalajs.dom

import scala.annotation.tailrec

object Mandelbrot {

  val MAX = 100

  def coloring(i: Int): (Int, Int, Int) = {

    val f: Double = i / 50d

    val r: Int = (f * 255).toInt
    val g: Int = (f * 135).toInt
    val b: Int = 100

    // 255,135,5)

    (r, g, b)
  }

  def mandelbrot(c0: Complex): (Int, Int, Int) = {

    @tailrec
    def recur(i: Int, c: Complex, max: Int): (Int, Int, Int) = {

      if (i >= max)
        (232,57,59) // #ff9a00
      else if ((!c) > 2)
        coloring(i)
      else
        recur(i + 1, c * c + c0, max)
    }

    val result = recur(0, 0, MAX)
    result
  }

  def draw(ctx: dom.CanvasRenderingContext2D, w: Int, h: Int): Unit = {

    val imageData = ctx.getImageData(0, 0, w, h)
    val data = imageData.data

    for (x <- 0 to w) {
      for (y <- 0 to h) {
        // translates (x,y) to coordinates in the complex plane for the mandelbrot
        // the interesting range is [re: -2 to 1, im: -1 to 1]
        val mx: Double = -2 + x * (3 / w.toDouble)
        val my: Double = -1 + y * (2 / h.toDouble)

        // obtain the rgb coloring for the mandelbrot at x, y
        val (r, g, b) = mandelbrot(Complex(mx, my))

        // translate (x,y) coordinates to image data array index
        val idx = y * (w * 4) + x * 4

        // set the color data
        data.update(idx, r)
        data.update(idx + 1, g)
        data.update(idx + 2, b)
        data.update(idx + 3, 255)
      }
    }

    ctx.putImageData(imageData, 0, 0)
  }
}
