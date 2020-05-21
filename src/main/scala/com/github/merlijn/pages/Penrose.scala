package com.github.merlijn.pages

import org.scalajs.dom

object Penrose {

  val psi = (Math.sqrt(5) - 1) / 2
  val psi2 = 1 - psi
  val tolerance = 0.00001

  trait Tile {

    val a: Complex
    val b: Complex
    val c: Complex

    def centre: Complex = (a + c) / 2
    def conjugate(): Tile
    def inflate(): Seq[Tile]

    def path(ctx: dom.CanvasRenderingContext2D): Unit = {

      val ab = b - a
      val bc = c - b

      ctx.beginPath()
      ctx.moveTo(a.re, a.im)
      ctx.lineTo(ab.re, ab.im)
      ctx.lineTo(bc.re, bc.im)
      ctx.lineTo((-ab).re, (-ab).im)
      ctx.closePath()
    }

    def flipY: Tile

    override def toString = f"a: ${a.re}%1.5f,${a.im}%1.5f | b: ${b.re}%1.5f,${b.im}%1.5f | c: ${c.re}%1.5f,${c.im}%1.5f"
  }

  case class BTileL(a: Complex, b: Complex, c: Complex)  extends Tile {
    override def inflate(): Seq[Tile] = {

      val d = a * psi2 + c * psi
      val e = a * psi2 + b * psi

      Seq(BTileL(d, e, a), BTileS(e, d, b), BTileL(c, d, b))
    }

    def flipY: BTileL = BTileL(Complex(-a.re, a.im),Complex(-b.re, b.im), Complex(-c.re, c.im))

    def conjugate(): BTileL = BTileL(a.conjugate, b.conjugate, c.conjugate)
  }

   case class BTileS(a: Complex, b: Complex, c: Complex) extends Tile {

     override def inflate(): Seq[Tile] = {
       val d = a * psi + b * psi2
       Seq(BTileS(d, c, a), BTileL(c, d, b))
     }

     def flipY: BTileS = BTileS(Complex(-a.re, a.im),Complex(-b.re, b.im), Complex(-c.re, c.im))

     def conjugate(): BTileS = BTileS(a.conjugate, b.conjugate, c.conjugate)
  }

  val example1: Seq[Tile] = {

    val scale = 100
    val theta = 2 * Math.PI / 5
    val rot = Complex(math.cos(theta), Math.sin(theta))

    val a = Complex(-scale/2, 0)
    val b = Complex(scale/2) * rot
    val c = Complex(scale/2) / Complex(psi)

    Seq(BTileL(a, b, c))
  }

  case class PenroseP3(initialTiles: Seq[Tile], ngen: Int = 4) {

    def draw(x: Int, y: Int, w: Int, h: Int, ctx: dom.CanvasRenderingContext2D): Unit = {

      var elements = initialTiles

      for (i <- 0 to ngen)
        elements = elements.flatMap(_.inflate())

      // reflect along the x axis
      val conjugates = elements.map(_.conjugate())
      val combined = (elements ++ conjugates).sortBy(_.centre)

      val removedDuplicates = combined.zip(combined.tail).flatMap {
        case (a, b) =>
          if (!(a.centre-b.centre) > tolerance)
            Some(a)
          else
            None
      }

      val minX = removedDuplicates.map(_.centre.re).min
      val minY = removedDuplicates.map(_.centre.im).min
      val maxX = removedDuplicates.map(_.centre.re).max
      val maxY = removedDuplicates.map(_.centre.im).max

      val scale = Math.min(w / (maxX - minX), h / (maxY - minY))

      ctx.scale(scale, scale)
      ctx.translate(x - minX, y - minY)

      removedDuplicates.foreach { e =>

        if (e.isInstanceOf[BTileL])
          ctx.fillStyle = "#ff9a00"
        else
          ctx.fillStyle = "#e8393b"

        ctx.beginPath()
        ctx.moveTo(e.a.re, e.a.im)
        ctx.lineTo(e.b.re, e.b.im)
        ctx.lineTo(e.c.re, e.c.im)

        val d = e.centre - (e.b - e.centre)

        ctx.lineTo(d.re, d.im)
        ctx.closePath()
        ctx.fill()
      }
    }
  }
}
