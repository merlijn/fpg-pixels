package com.github.merlijn.calc

import scala.math._

/**
 * Complex number class
 *
 * @param re Real part
 * @param im Imaginary part
 */
case class Complex(re: Double, im: Double) extends Ordered[Complex] {
  private val modulus = sqrt(pow(re, 2) + pow(im, 2))

  // Constructors
  def this(re: Double) = this(re, 0)

  // Unary operators
  def unary_+ : Complex = this
  def unary_- = new Complex(-re, -im)
  def unary_~ = new Complex(re, -im) // conjugate
  def unary_! : Double = modulus

  def conjugate: Complex = unary_~

  // Comparison
  def compare(that: Complex): Int = !this compare !that

  // Arithmetic operations
  def +(c: Complex) = Complex(re + c.re, im + c.im)
  def -(c: Complex) = Complex(re - c.re, im - c.im)
  def *(c: Complex) = Complex(re * c.re - im * c.im, im * c.re + re * c.im)
  def /(c: Complex): Complex = {
    require(c.re != 0 || c.im != 0)
    val d = pow(c.re, 2) + pow(c.im, 2)
    new Complex((re * c.re + im * c.im) / d, (im * c.re - re * c.im) / d)
  }

  // String representation
  override def toString() =
    this match {
      case Complex.i => "i"
      case Complex(re, 0) => re.toString
      case Complex(0, im) => im.toString + "*i"
      case _ => asString
    }

  private def asString = {

    val ims =
      (if (im < 0)
        s"-${-im}"
      else
        s"+$im")

    s"$re$ims*i"
  }
}

object Complex {
  // Constants
  val i = new Complex(0, 1)

  // Factory methods
  def apply(re: Double) = new Complex(re)

  // Implicit conversions
  implicit def fromDouble(d: Double) = new Complex(d)
  implicit def fromFloat(f: Float) = new Complex(f.toDouble)
  implicit def fromLong(l: Long) = new Complex(l.toDouble)
  implicit def fromInt(i: Int) = new Complex(i.toDouble)
  implicit def fromShort(s: Short) = new Complex(s.toDouble)
}
