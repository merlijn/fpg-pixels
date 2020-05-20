package com.github.merlijn.pages

import scalatags.JsDom.all.{li, _}

object Tabs {

  def tabs(content: Map[String, Frag]) = {

    val (firstName, firstContent) = content.head

    val activeTab = li(`class` := "nav-item")(
      a(`class` := "nav-link active", href := s"#$firstName", attr("data-toggle") := "tab")(firstName)
    )

    val tail = for (i <- content.keys.toSeq.tail) yield {
      li(`class` := "nav-item")(
        a(`class` := "nav-link", href := s"#$i", attr("data-toggle") := "tab")(i)
      )
    }

    val activeTabContent = div(id := firstName, `class` := "tab-pane active show")(
      firstContent
    )

    val tabContent = content.tail.map { case (name, c) =>
      div(id := name, `class` := "tab-pane")(c)
    }.toSeq

    div(
      ul(`class` := "nav nav-tabs", role := "tablist")(activeTab +: tail),
      div(`class` := "tab-content")(activeTabContent +: tabContent)
    )
  }
}
