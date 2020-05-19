package com.github.merlijn

import scalatags.JsDom.all.{a, _}

object Components {

  val topBar =
    tag("nav")(
      `class` := "navbar navbar-dark sticky-top bg-dark flex-md-nowrap p-0 shadow")(
        a(`class` := "navbar-brand col-md-3 col-lg-2 mr-0 px-3", href := "#")("Pixel XP"),
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
            a(`class` := "nav-link", href := "#", "About")
           )
        )
    )

  val sideBar =
    tag("nav")(id := "sidebarMenu", `class` := "col-md-3 col-lg-2 d-md-block bg-light sidebar collapse")(
      div(`class` := "sidebar-sticky pt-3")(
        ul(`class` := "nav flex-column")(
          li(`class` := "nav-item")(a( `class` := "nav-link active", href :="#")("Stary Sky")),
          li(`class` := "nav-item")(a( `class` := "nav-link", href := "#")("Penrose tile")),
          li(`class` := "nav-item")(a( `class` := "nav-link", href := "#")("Bricks"))
        )
      )
    )

  val main =
    tag("main")(`class` := "col-md-9 ml-sm-auto col-lg-10 px-md-4", role := "main")(
      div(`class` := "d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom")(
        div(id := "appContent", `class` := "my-4 w-100")
      )
    )

  val container =
    div(`class` := "container-fluid")(
      div(`class` := "row")(
        sideBar,
        main
      )
    )
}
