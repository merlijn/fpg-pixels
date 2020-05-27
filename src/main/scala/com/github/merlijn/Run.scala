package com.github.merlijn

import com.github.merlijn.draw.Penrose.PenroseP3
import com.github.merlijn.draw._
import org.scalajs.dom
import org.scalajs.dom.html.{Canvas, Div}
import rx._
import scalatags.JsDom
import scalatags.JsDom.all._

object Run {

  def main(args: Array[String]): Unit = {

    implicit val ctx: Ctx.Owner = Ctx.Owner.safe()

    val pages: Seq[(String, Frag)] = Seq(
//      "Night Sky" ->  drawPage { NightSky.drawNightSky(80) },
      "Penrose Tiling" -> drawPage { PenroseP3.draw(Penrose.example1, 6) },
      "Mandelbrot Set" -> drawPage { Mandelbrot.draw _ },
//      "Experiment A" -> drawPage { Experiment.drawA _ },
      "Experiment" -> drawPage { Experiment.drawB _ }
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

    dom.document.body.appendChild(WebComponents.topBar.render)
    dom.document.body.appendChild(WebComponents.pageWithSidebar(pages.map(_._1), activePage).render)

    activePage.trigger { value =>

      val content = dom.document.getElementById("appContent")

      // remove old page
      if (content.childNodes.length == 1)
        content.removeChild(content.childNodes.apply(0))

      // add new page
      pages.collectFirst { case (`value`, p) => p }.foreach { newPage =>
        content.appendChild(newPage.render)
      }
    }

    // navigate to the active page
    navigate(dom.window.location.href)
  }
}
