package com.github.merlijn

import com.github.merlijn.pages.Tabs
import org.scalajs.dom
import org.scalajs.dom.html
import org.scalajs.dom.html.Canvas
import rx._
import scalatags.JsDom.all._

object Run {

  def main(args: Array[String]): Unit = {

    implicit val ctx: Ctx.Owner = Ctx.Owner.safe()

    val pages: Map[String, Frag] = Map(
      "Stary Sky" -> {

        // append canvas
        val c: Canvas = canvas(`class` := "my-4").render

        c.width = 900
        c.height = 600

        Draw.drawNightSky(c, 80)

        Tabs.tabs(
          Map(
            "Image" -> c,
            "Source" -> h3("source")
        ))
      },
      "Penrose tile" -> { h3("TODO - Penrose tile") },
      "Bricks" -> { h3("TODO - Bricks")}
    )

    val defaultPage = pages.head._1

    val page: Var[String] = Var(defaultPage)


    // called when url changes, updates states, triggers re-render
    def navigate(href: String): Unit = href.split('#').tail.headOption match {
      case Some(destination) =>
        page.update(destination.replaceAll("%20", " "))
      case _                 =>
        page.update(defaultPage)
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
