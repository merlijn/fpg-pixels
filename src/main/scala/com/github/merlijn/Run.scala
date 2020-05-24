package com.github.merlijn

import com.github.merlijn.draw.Penrose.PenroseP3
import com.github.merlijn.draw.{CircleTest, Mandelbrot, NightSky, Penrose}
import org.scalajs.dom
import org.scalajs.dom.html.{Canvas, Div}
import rx._
import scalatags.JsDom
import scalatags.JsDom.all._

object Run {

  def main(args: Array[String]): Unit = {

    implicit val ctx: Ctx.Owner = Ctx.Owner.safe()

    def drawPage(fn: (dom.CanvasRenderingContext2D, Int, Int) => ()): JsDom.TypedTag[Div] = {
      // append canvas
      val c: Canvas = canvas(`class` := "my-4").render

      c.width = 900
      c.height = 600

      c.onclick = e => println("click")

      c.onmousedown = e => {
        println("down")
      }

      c.onmouseup = { e =>
        println("up")
      }

      val ctx = c.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D]

      def renderImage(): Unit = fn(ctx, 900, 600)

      val buttonGroup = div(`class` := "button-group", role := "group")(
        button(`type` := "button", `class` := "btn btn-light", onclick := { () => renderImage() })
          (Components.featherIcon("refresh-ccw")),
        button(`type` := "button", `class` := "btn btn-light")
          (Components.featherIcon("download"))
      )

      val container = div(
        `class` := "my-4"
      )(buttonGroup, c)

      renderImage()

      Components.tabs(
        Map(
          "Image" -> container,
          "Source" -> h3("source")
        ))
    }

    val pages: Map[String, Frag] = Map(
      "Stary Sky" ->  drawPage { NightSky.drawNightSky(80) },
      "Penrose Tiling" -> drawPage { PenroseP3.draw(Penrose.example1, 6) },
      "Mandelbrot Set" -> drawPage { Mandelbrot.draw _ },
      "Experiment" -> drawPage { CircleTest.drawB _ }
    )

    val defaultPage = pages.head._1

    val activePage: Var[String] = Var(defaultPage)

    // called when url changes, updates states, triggers re-render
    def navigate(href: String): Unit = href.split('#').tail.headOption match {
      case Some(destination) =>
        activePage.update(destination.replaceAll("%20", " "))
      case _                 =>
        activePage.update(defaultPage)
    }

    // user navigation callback (back, forward buttons)
    dom.window.onpopstate = _ => { navigate(dom.window.location.href) }

    dom.document.body.appendChild(Components.topBar.render)
    dom.document.body.appendChild(Components.pageWithSidebar(pages.keySet.toSeq, activePage).render)

    activePage.trigger { value =>

      val content = dom.document.getElementById("appContent")

      // remove old page
      if (content.childNodes.length == 1)
        content.removeChild(content.childNodes.apply(0))

      // add new page
      content.appendChild(pages(value).render)
    }
  }
}
