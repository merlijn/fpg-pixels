import com.github.merlijn.draw.Mandelbrot
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class MandelbrotTest extends AnyFlatSpec with Matchers {

  "The recur" should "do something" in {

    println(Mandelbrot.mandelbrot(-2, 0))
  }
}
