package taller

import org.scalatest.funsuite.AnyFunSuite
import org.junit.runner.RunWith
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class generarProgramacionesRiegoTest extends AnyFunSuite { // Extender AnyFunSuite correctamente

  val riegoOptimo = new RiegoOptimo()

  test("Generar programaciones para finca con un solo tablón") {
    val finca = Vector((3, 1, 2))
    val programaciones = riegoOptimo.generarProgramacionesRiego(finca)
    assert(programaciones == Vector(Vector(0)))
  }

  test("Generar programaciones para finca con dos tablones") {
    val finca = Vector((3, 2, 1), (4, 1, 3))
    val programaciones = riegoOptimo.generarProgramacionesRiego(finca)
    assert(programaciones == Vector(Vector(0, 1), Vector(1, 0)))
  }

  test("Generar programaciones para finca con tres tablones") {
    val finca = Vector((10, 3, 1), (5, 2, 2), (8, 1, 3))
    val programaciones = riegoOptimo.generarProgramacionesRiego(finca)
    val esperadas = Vector(
      Vector(0, 1, 2), Vector(0, 2, 1),
      Vector(1, 0, 2), Vector(1, 2, 0),
      Vector(2, 0, 1), Vector(2, 1, 0)
    )
    assert(programaciones == esperadas)
  }

  test("Generar programaciones para finca con cuatro tablones") {
    val finca = Vector((1, 1, 1), (2, 2, 2), (3, 3, 3), (4, 4, 4))
    val programaciones = riegoOptimo.generarProgramacionesRiego(finca)
    assert(programaciones.size == 24) // 4! = 24
  }

  test("Generar programaciones para finca vacía") {
    val finca = Vector()
    val programaciones = riegoOptimo.generarProgramacionesRiego(finca)
    assert(programaciones == Vector(Vector()))
  }
}
