import com.github.merlijn.draw.{CircleTest, Mandelbrot}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class MandelbrotTest extends AnyFlatSpec with Matchers {

  "The recur" should "do something" in {

    println(CircleTest.iter(0, 0.5, 0.3))
  }
}
