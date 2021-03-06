package com.github.merlijn.draw

import com.github.merlijn.calc.Complex
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

    override def toString =
      // returns a string representation of (a, b, c) shortened to 5 decimal precision
      f"a: ${a.re}%1.5f,${a.im}%1.5f | b: ${b.re}%1.5f,${b.im}%1.5f | c: ${c.re}%1.5f,${c.im}%1.5f"
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

  object PenroseP3 {

    def draw(initialTiles: Seq[Tile] = example1, ngen: Int = 5)(ctx: dom.CanvasRenderingContext2D, w: Int, h: Int): Unit = {

      val backGroundColor = "#eeeeee"
      val tileLColor = "#ff9a00"
      val tileSColor = "#e8393b"
      val autoFit: Boolean = true

      var elements = initialTiles

      for (i <- 0 to ngen)
        elements = elements.flatMap(_.inflate())

      // reflect along the x axis
      val conjugates = elements.map(_.conjugate())
      val combined = (elements ++ conjugates).sortBy(_.centre)

      // remove duplicates
      val removedDuplicates = combined.zip(combined.tail).flatMap {
        case (a, b) =>
          if (!(a.centre-b.centre) > tolerance)
            Some(a)
          else
            None
      }

      // find the bounds
      val minX = removedDuplicates.map(_.centre.re).min
      val minY = removedDuplicates.map(_.centre.im).min
      val maxX = removedDuplicates.map(_.centre.re).max
      val maxY = removedDuplicates.map(_.centre.im).max


      // draw background
      ctx.fillStyle = backGroundColor
      ctx.fillRect(0, 0, w, h)

      ctx.save()

      if (autoFit) {
        // auto scale to fit
        val scale = Math.min(w / (maxX - minX), h / (maxY - minY))

        ctx.scale(scale, scale)
        ctx.translate(-minX, - minY)
      }

      removedDuplicates.foreach { e =>

        // draw the tile path
        ctx.beginPath()
        ctx.moveTo(e.a.re, e.a.im)
        ctx.lineTo(e.b.re, e.b.im)
        ctx.lineTo(e.c.re, e.c.im)

        val d = e.centre - (e.b - e.centre)

        ctx.lineTo(d.re, d.im)
        ctx.closePath()

        // fill the path
        if (e.isInstanceOf[BTileL])
          ctx.fillStyle = tileLColor
        else
          ctx.fillStyle = tileSColor

        ctx.fill()
      }

      ctx.restore()
    }
  }
}
