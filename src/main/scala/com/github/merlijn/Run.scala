package com.github.merlijn

import org.scalajs.dom
import org.scalajs.dom.html
import scalatags.JsDom.all.{tr, _}

object Run {

  def main(args: Array[String]): Unit = {

    println("Hello!")

    dom.document.body.appendChild(Components.topBar.render)
    dom.document.body.appendChild(Components.container.render)

    // append canvas
    val canvas = dom.document.createElement("canvas").asInstanceOf[html.Canvas]
    val content = dom.document.getElementById("appContent")

    content.appendChild(canvas)

    // set size
    val w = 900
    val h = 600

    canvas.width = w
    canvas.height = h

    Draw.drawStarSky(w, h, 80, canvas.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D])
  }
}
