package com.github.merlijn

import org.scalajs.dom
import org.scalajs.dom.html.Div
import rx._
import scalatags.JsDom
import scalatags.JsDom.all._
import scalatags.generic.Namespace

object Components {

  // converts an Rx to a scalatags Frag, using an implicit renderFn
  implicit def rxFrag[T](r: Rx[T])(implicit renderFn: T => Frag, owner: rx.Ctx.Owner): Frag = {

    val first = renderFn(r.now).render
    var prev: dom.Node = first

    r.triggerLater { value =>

      val updated = renderFn(value).render

      prev.parentNode.replaceChild(updated, prev)
      prev = updated
    }

    first
  }

  val active = Var("Pixel FPG")

  def featherIcon(name: String) = {

    val test = s"https://github.com/feathericons/feather/raw/master/icons/$name.svg"
    img(`class` := "feather", src := test )

//    tag("svg")(`class` := "feather", attr("viewBox") := "0 0 24 24")(
//      tag("use")(attr("href", Namespace.svgXlinkNamespaceConfig) := s"feather-sprite.svg#$name")
//    )
  }

  val topBar =
    tag("nav")(
      `class` := "navbar navbar-dark sticky-top bg-dark flex-md-nowrap p-0 shadow")(
        a(`class` := "navbar-brand col-md-3 col-lg-2 mr-0 px-3", href := "#")(active),
        button(
          `class`:= "navbar-toggler position-absolute d-md-none collapsed",
          `type` := "button",
          attr("data-toggle") := "collapse"
        ),
        input(
          `class` := "form-control form-control-dark w-100",
          `type` := "text",
          attr("placeholder") := "Search",
          attr("aria-label") := "Search"
        ),
        ul(
          `class` := "navbar-nav px-3",
          li(`class` := "nav-item text-nowrap",
            a(`class` := "nav-link", href := "#", "About", onclick := { () => active.update("Changed!") })
           )
        )
    )

  def sideBar(pages: Seq[String], activePage: Rx[String])(implicit owner: rx.Ctx.Owner) = {

    val pageLinks = activePage.map(page => {

      ul(`class` := "nav flex-column")(
        for (i <- pages) yield {
          val clazz =
            if (i == page)
              "nav-link active"
            else
              "nav-link"

          li(`class` := "nav-item")(a( `class` := clazz, href := s"#$i")(i))
        }
      )
    })


    tag("nav")(id := "sidebarMenu", `class` := "col-md-3 col-lg-2 d-md-block bg-light sidebar collapse")(
      div(`class` := "sidebar-sticky pt-3")(
        pageLinks
      )
    )
  }

  val main =
    tag("main")(`class` := "col-md-9 ml-sm-auto col-lg-10 px-md-4", role := "main")(
      div(`class` := "d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom")(
        div(id := "appContent", `class` := "my-4 w-100")
      )
    )

  def pageWithSidebar(pages: Seq[String], activePage: Rx[String])(implicit owner: rx.Ctx.Owner) =
    div(`class` := "container-fluid")(
      div(`class` := "row")(
        sideBar(pages, activePage),
        main
      )
    )

  def tabs(content: Map[String, Frag]): JsDom.TypedTag[Div] = {

    val (firstName, firstContent) = content.head

    val activeTab = li(`class` := "nav-item")(
      a(`class` := "nav-link active", href := s"#$firstName", attr("data-toggle") := "tab")(firstName)
    )

    val tail = for (i <- content.keys.toSeq.tail) yield {
      li(`class` := "nav-item")(
        a(`class` := "nav-link", href := s"#$i", attr("data-toggle") := "tab")(i)
      )
    }

    val activeTabContent = div(id := firstName, `class` := "tab-pane active show")(firstContent)

    val tabContent = content.tail.map { case (name, c) =>
      div(id := name, `class` := "tab-pane")(c)
    }.toSeq

    div(
      ul(`class` := "nav nav-tabs", role := "tablist")(activeTab +: tail),
      div(`class` := "tab-content")(activeTabContent +: tabContent)
    )
  }
}
