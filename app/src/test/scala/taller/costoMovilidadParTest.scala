package taller

import org.scalatest.funsuite.AnyFunSuite
import org.junit.runner.RunWith
import org.scalatestplus.junit.JUnitRunner
import scala.util.Random

@RunWith(classOf[JUnitRunner])
class costoMovilidadParTest extends AnyFunSuite {

  val riegoOptimo = new RiegoOptimo()

  test("Caso con distancias extremas") {
    val distancias: riegoOptimo.Distancia = Vector(
      Vector(0, 1000, 2000, 3000),
      Vector(1000, 0, 1500, 2500),
      Vector(2000, 1500, 0, 3500),
      Vector(3000, 2500, 3500, 0)
    )
    val finca: riegoOptimo.Finca = Vector(
      (30, 5, 2),
      (50, 3, 1),
      (60, 4, 4),
      (40, 6, 3)
    )
    val progRiego: riegoOptimo.ProgRiego = Vector(3, 0, 2, 1)
    val costo = riegoOptimo.costoMovilidadPar(finca, progRiego, distancias)
    assert(costo == 6500, s"El costo esperado es 6500, y se obtuvo $costo")
  }

  test("Caso con distancias asimétricas") {
    val distancias: riegoOptimo.Distancia = Vector(
      Vector(0, 10, 20, 30),
      Vector(15, 0, 25, 35),
      Vector(40, 50, 0, 60),
      Vector(70, 80, 90, 0)
    )
    val finca: riegoOptimo.Finca = Vector(
      (20, 2, 3),
      (25, 4, 2),
      (30, 3, 1),
      (35, 5, 4)
    )
    val progRiego: riegoOptimo.ProgRiego = Vector(1, 2, 0, 3)
    val costo = riegoOptimo.costoMovilidadPar(finca, progRiego, distancias)
    assert(costo == 95, s"El costo esperado es 95, y se obtuvo $costo")
  }

  test("Caso con recorrido completo en ciclo") {
    val distancias: riegoOptimo.Distancia = Vector(
      Vector(0, 7, 8, 9),
      Vector(6, 0, 5, 4),
      Vector(3, 2, 0, 1),
      Vector(10, 11, 12, 0)
    )
    val finca: riegoOptimo.Finca = Vector(
      (15, 1, 2),
      (20, 2, 3),
      (25, 3, 1),
      (30, 4, 4)
    )
    val progRiego: riegoOptimo.ProgRiego = Vector(0, 1, 2, 3, 0)
    val costo = riegoOptimo.costoMovilidadPar(finca, progRiego, distancias)
    assert(costo == 23, s"El costo esperado es 23, y se obtuvo $costo")
  }

  test("Caso con distancias dispersas y programa de riego irregular") {
    val distancias: riegoOptimo.Distancia = Vector(
      Vector(0, 3, 8, 6),
      Vector(5, 0, 4, 9),
      Vector(7, 2, 0, 1),
      Vector(6, 5, 3, 0)
    )
    val finca: riegoOptimo.Finca = Vector(
      (18, 3, 2),
      (21, 2, 3),
      (19, 4, 1),
      (22, 5, 4)
    )
    val progRiego: riegoOptimo.ProgRiego = Vector(2, 1, 3, 0, 2)
    val costo = riegoOptimo.costoMovilidadPar(finca, progRiego, distancias)
    assert(costo == 25, s"El costo esperado es 25, y se obtuvo $costo")
  }

  test("Caso con distancias moderadas y secuencia de riego equilibrada") {
    val distancias: riegoOptimo.Distancia = Vector(
      Vector(0, 5, 10, 15),
      Vector(5, 0, 20, 25),
      Vector(10, 20, 0, 30),
      Vector(15, 25, 30, 0)
    )
    val finca: riegoOptimo.Finca = Vector(
      (10, 2, 3),
      (15, 3, 2),
      (20, 1, 1),
      (25, 4, 4)
    )
    val progRiego: riegoOptimo.ProgRiego = Vector(0, 2, 3, 1)
    val costo = riegoOptimo.costoMovilidadPar(finca, progRiego, distancias)
    assert(costo == 65)
  }
}