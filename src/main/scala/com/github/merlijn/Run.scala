package com.github.merlijn

import org.scalajs.dom
import org.scalajs.dom.html
import rx._
import scalatags.JsDom.all._

object Run {

  def main(args: Array[String]): Unit = {

    implicit val ctx: Ctx.Owner = Ctx.Owner.safe()

    val defaultPage = "Stary Sky"

    val page: Var[String] = Var(defaultPage)

    val pages: Map[String, Frag] = Map(
      "Stary Sky" -> {
        // append canvas
        val canvas = dom.document.createElement("canvas").asInstanceOf[html.Canvas]

        // set size
        val w = 900
        val h = 600

        canvas.width = w
        canvas.height = h

        Draw.drawStarSky(w, h, 80, canvas.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D])

        canvas
      },
      "Penrose tile" -> { h3("TODO - Penrose tile") },
      "Bricks" -> { h3("TODO - Bricks")}
    )

    // called when url changes, updates states, triggers re-render
    def navigate(href: String): Unit = href.split('#').tail.headOption match {
      case Some(destination) => page.update(destination)
      case _                 => page.update("")
    }

    // user navigation callback (back, forward buttons)
    dom.window.onpopstate = _ => { navigate(dom.window.location.href) }
    
    dom.document.body.appendChild(Components.topBar.render)
    dom.document.body.appendChild(Components.pageWithSidebar(pages.keySet.toSeq, page.now).render)

    page.trigger { value =>

      val content = dom.document.getElementById("appContent")

      // remove old page
      if (content.childNodes.length == 1)
        content.removeChild(content.childNodes.apply(0))

      // add new page
      content.appendChild(pages(value).render)
    }
  }
}
