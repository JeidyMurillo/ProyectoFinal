package taller

import org.scalatest.funsuite.AnyFunSuite
import org.junit.runner.RunWith
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class CostoRiegoFincaTest extends AnyFunSuite{
  
  val riegoOptimo = new RiegoOptimo()

  test("Costo para una finca con tres tablones y programación de regado inversa") {
    val finca = Vector(
      (10, 3, 2), // Tablón 1
      (5, 3, 3),  // Tablón 2
      (8, 4, 1)   // Tablón 3
    )
    val programacion = Vector(2, 1, 0) // Orden: Tablón 3 -> 2 -> 1

    assert(riegoOptimo.costoRiegoFinca(finca, programacion) == 10) // Suma de costos
  }

  test("Costo para una finca con cinco tablones y programación de regado en orden") {    
    val finca = Vector(
      (15, 5, 2), // Tablón 1
      (8, 2, 4),  // Tablón 2
      (10, 3, 3), // Tablón 3
      (6, 1, 1),  // Tablón 4
      (12, 4, 2)  // Tablón 5
    )
    val programacion = Vector(0, 1, 2, 3, 4) // Orden: Tablón 1 -> 2 -> 3 -> 4 -> 5

    assert(riegoOptimo.costoRiegoFinca(finca, programacion) == 22) // Suma de costos
  }


  test("Costo para una finca con ocho tablones y programación inversa") {
    val finca = Vector(
      (20, 5, 2), // Tablón 1
      (10, 3, 4), // Tablón 2
      (15, 2, 3), // Tablón 3
      (12, 4, 1), // Tablón 4
      (18, 6, 2), // Tablón 5
      (8, 2, 4),  // Tablón 6
      (14, 3, 3), // Tablón 7
      (16, 5, 1)  // Tablón 8
    )
    val programacion = Vector(7, 6, 5, 4, 3, 2, 1, 0) // Orden inverso

    assert(riegoOptimo.costoRiegoFinca(finca, programacion) == 136) // Suma de costos
  }

  test("Costo para una finca con diez tablones y programación mixta") {
    val finca = Vector(
      (25, 7, 2),  // Tablón 1
      (18, 5, 3),  // Tablón 2
      (22, 6, 4),  // Tablón 3
      (20, 3, 1),  // Tablón 4
      (24, 8, 2),  // Tablón 5
      (19, 4, 3),  // Tablón 6
      (21, 5, 2),  // Tablón 7
      (23, 7, 1),  // Tablón 8
      (17, 3, 4),  // Tablón 9
      (26, 9, 2)   // Tablón 10
    )
    val programacion = Vector(9, 2, 8, 3, 1, 4, 7, 5, 6, 0) // Orden mixto

    assert(riegoOptimo.costoRiegoFinca(finca, programacion) == 291) // Suma de costos
  }

  test("Costo para una finca con doce tablones y valores grandes") {
    val finca = Vector(
      (200, 15, 4),  // Tablón 1
      (150, 10, 3),  // Tablón 2
      (180, 12, 5),  // Tablón 3
      (170, 8, 2),   // Tablón 4
      (190, 14, 3),  // Tablón 5
      (160, 11, 4),  // Tablón 6
      (175, 9, 2),   // Tablón 7
      (185, 13, 1),  // Tablón 8
      (165, 10, 3),  // Tablón 9
      (155, 7, 5),   // Tablón 10
      (195, 16, 2),  // Tablón 11
      (180, 12, 4)   // Tablón 12
    )
    val programacion = Vector(11, 2, 10, 3, 8, 6, 5, 1, 4, 9, 7, 0) // Orden mixto

    assert(riegoOptimo.costoRiegoFinca(finca, programacion) == 1220) // Suma de costos
  }

}
