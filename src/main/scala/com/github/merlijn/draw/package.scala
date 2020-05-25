package com.github.merlijn

import org.scalajs.dom
import org.scalajs.dom.html.{Canvas, Div}
import scalatags.JsDom
import scalatags.JsDom.all._

package object draw {
  def drawPage(fn: (dom.CanvasRenderingContext2D, Int, Int) => ()): JsDom.TypedTag[Div] = {
    // append canvas
    val c: Canvas = canvas(`class` := "my-4").render

    val width = 1024
    val height= 768

    c.width = width
    c.height = height

    c.onclick = e => println("click")

    c.onmousedown = e => {
      println("down")
    }

    c.onmouseup = { e =>
      println("up")
    }

    val ctx = c.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D]

    def renderImage(): Unit = fn(ctx, width, height)

    val buttonGroup = div(`class` := "button-group", role := "group")(
      button(`type` := "button", `class` := "btn btn-light", onclick := { () => renderImage() })
      (WebComponents.featherIcon("refresh-ccw")),
      button(`type` := "button", `class` := "btn btn-light")
      (WebComponents.featherIcon("download"))
    )

    val container = div(
      `class` := "my-4"
    )(buttonGroup, c)

    renderImage()

    WebComponents.tabs(
      Map(
        "Image" -> container,
        "Source" -> div(`class` := "my-4")(a(href := "https://github.com/merlijn/fpg-pixels/blob/master/src/main/scala/com/github/merlijn/draw")("Github link"))
      ))
  }
}
