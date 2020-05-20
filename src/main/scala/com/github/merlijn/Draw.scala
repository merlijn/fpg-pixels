package com.github.merlijn

import org.scalajs.dom
import org.scalajs.dom.html.Canvas

object Draw {

  def drawNightSky(c: Canvas, nrOfStars: Int): Unit = {
    val ctx = c.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D]
    drawNightSky(c.width, c.height, nrOfStars, ctx)
  }

  def drawNightSky(w: Int, h: Int, nrOfStars: Int, ctx: dom.CanvasRenderingContext2D): Unit = {

    val (minSides,  maxSides)  = (4, 8)
    val (minRadius, maxRadius) = (5, w / 20)

    ctx.lineWidth = 2
    ctx.fillStyle = "darkblue"
    ctx.fillRect(0, 0, w, h)

    ctx.fillStyle = "yellow"

    // draw the stars
    for (i <- 1 to nrOfStars) {

      val x = (Math.random() * w).toInt
      val y = (Math.random() * h).toInt
      val r = (Math.random() * (maxRadius - minRadius)).toInt + minRadius
      val sides = (Math.random() * (maxSides - minSides)).toInt + minSides
      val a = (Math.random() * Math.PI * 2)

      fillStar(x, y, r, sides, a, ctx)
    }
  }

  def fillStar(x: Int, y: Int, r: Int, sides: Int, start: Double = 0, ctx: dom.CanvasRenderingContext2D): Unit = {

    val d = (Math.PI * 2) / sides

    // move to the first star point
    ctx.beginPath()
    ctx.moveTo(x + Math.cos(start) * r, y + Math.sin(start) * r)

    for (i <- 1 to sides) {

      val angle = start + (i - 0.5) * d

      val xi = x + Math.cos(angle) * (r/2)
      val yi = y + Math.sin(angle) * (r/2)

      val angle2 = start + i * d

      val xi2 = x + Math.cos(angle2) * r
      val yi2 = y + Math.sin(angle2) * r

      ctx.lineTo(xi, yi)
      ctx.lineTo(xi2, yi2)
    }

    ctx.closePath()
    ctx.fill()
  }
}
