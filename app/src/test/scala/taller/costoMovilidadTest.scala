package taller

import org.scalatest.funsuite.AnyFunSuite
import org.junit.runner.RunWith
import org.scalatestplus.junit.JUnitRunner
import scala.util.Random

@RunWith(classOf[JUnitRunner])
class costoMovilidadTest extends AnyFunSuite {
  val riegoOptimo = new RiegoOptimo()
  import riegoOptimo._

  test("CostoMovilidad con finca grande y distancia uniforme") {
    val finca: Finca = Vector((10, 4, 2), (20, 6, 3), (15, 5, 2), (30, 8, 4))
    val distancia: Distancia = Vector(
      Vector(0, 10, 15, 20),
      Vector(10, 0, 25, 30),
      Vector(15, 25, 0, 35),
      Vector(20, 30, 35, 0)
    )
    val pi: ProgRiego = Vector(1, 2, 1, 3)

    val costo = riegoOptimo.costoMovilidad(finca, pi, distancia)
    val costoEsperado = distancia(1)(2) + distancia(2)(1) + distancia(1)(3)
    assert(costo == costoEsperado, s"El costo esperado es 80, y se obtuvo $costo")
  }

  test("CostoMovilidad con distancias muy grandes y programación aleatoria") {
    val finca: Finca = Vector((50, 10, 5), (40, 9, 4), (60, 11, 6), (70, 12, 7))
    val distancia: Distancia = Vector(
      Vector(0, 500, 1000, 1500),
      Vector(500, 0, 700, 1200),
      Vector(1000, 700, 0, 800),
      Vector(1500, 1200, 800, 0)
    )
    val pi: ProgRiego = Vector(1, 0, 3, 2)

    val costo = riegoOptimo.costoMovilidad(finca, pi, distancia)
    val costoEsperado = distancia(1)(0) + distancia(0)(3) + distancia(3)(2)
    assert(costo == costoEsperado, s"El costo esperado es $costoEsperado y se obtuvo $costo")
  }

  test("CostoMovilidad con finca y distancia generadas aleatoriamente (caso 1)") {
    val long = 8
    val finca = fincaAlAzar(long)
    val distancia = distanciaAlAzar(long)
    val pi = Random.shuffle((0 until long).toVector)

    val costo = costoMovilidad(finca, pi, distancia)
    assert(costo >= 0, s"El costo esperado debe ser no negativo, y se obtuvo $costo")
  }

  test("CostoMovilidad con finca y distancia generadas aleatoriamente (caso 2)") {
    val long = 10
    val finca = fincaAlAzar(long)
    val distancia = distanciaAlAzar(long)
    val pi = Random.shuffle((0 until long).toVector)

    val costo = costoMovilidad(finca, pi, distancia)
    assert(costo >= 0, s"El costo esperado debe ser no negativo, y se obtuvo $costo")
  }

  test("CostoMovilidad con finca y distancia generadas aleatoriamente (caso 3)") {
    val long = 12
    val finca = fincaAlAzar(long)
    val distancia = distanciaAlAzar(long)
    val pi = Random.shuffle((0 until long).toVector)

    val costo = costoMovilidad(finca, pi, distancia)
    assert(costo >= 0, s"El costo esperado debe ser no negativo, y se obtuvo $costo")
  }
}