package taller


import org.scalatest.funsuite.AnyFunSuite
import org.junit.runner.RunWith
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class CostoRiegoTablonTest extends AnyFunSuite{
  val riegoOptimo = new RiegoOptimo()

  test("Costo para un finca con dos tablones y programación en orden") {
    val finca = Vector(
      (20, 8, 2), // Tablón 1
      (6, 2, 3),  // Tablón 2
    )
    val programacion = Vector(0, 1) // Orden: Tablón 3 -> 2 -> 1


    assert(riegoOptimo.costoRiegoTablon(0, finca, programacion) == 12)
    assert(riegoOptimo.costoRiegoTablon(1, finca, programacion) == 12)
  }

  test("Costo para un finca con tres tablones y programación de regado inversa") {
    val finca = Vector(
      (10, 3, 2), // Tablón 1
      (5, 3, 3),  // Tablón 2
      (8, 4, 1)   // Tablón 3

    )
    val programacion = Vector(2, 1, 0) // Orden: Tablón 3 -> 2 -> 1


    assert(riegoOptimo.costoRiegoTablon(2, finca, programacion) == 4) // (10 - (4 + 3))
    assert(riegoOptimo.costoRiegoTablon(1, finca, programacion) == 6) // ((5-3)(4+3))
    assert(riegoOptimo.costoRiegoTablon(0, finca, programacion) == 0) // (10-3)(7+3) -> 7-7
  }


  test("Costo para una finca con cinco tablones y programación de regado en orden") {    
    val finca = Vector(
      (15, 5, 2), // Tablón 1
      (8, 2, 4),  // Tablón 2
      (10, 3, 3), // Tablón 3
      (6, 1, 1),  // Tablón 4
      (12, 4, 2)  // Tablón 5
    )
    val programacion = Vector(0, 1, 2, 3, 4) // Orden: Tablón 5 -> 4 -> 3 -> 2 -> 1

    assert(riegoOptimo.costoRiegoTablon(0, finca, programacion) == 10) // (12 - (0 + 4))
    assert(riegoOptimo.costoRiegoTablon(1, finca, programacion) == 1) // 6 - (4 + 1) = 0
    assert(riegoOptimo.costoRiegoTablon(2, finca, programacion) == 0) // (10 - (5 + 3))
    assert(riegoOptimo.costoRiegoTablon(3, finca, programacion) == 5) // Penalización
    assert(riegoOptimo.costoRiegoTablon(4, finca, programacion) == 6) // 15 - (12 + 5) = 0
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

    assert(riegoOptimo.costoRiegoTablon(7, finca, programacion) == 11) // 16 - (0 + 5)
    assert(riegoOptimo.costoRiegoTablon(6, finca, programacion) == 6)  // 14 - (5 + 3)
    assert(riegoOptimo.costoRiegoTablon(5, finca, programacion) == 8)  // 8 - (8 + 2)
    assert(riegoOptimo.costoRiegoTablon(4, finca, programacion) == 2)  // 18 - (10 + 6)
    assert(riegoOptimo.costoRiegoTablon(3, finca, programacion) == 8)  // 12 - (16 + 4)
    assert(riegoOptimo.costoRiegoTablon(2, finca, programacion) == 21) // Penalización
    assert(riegoOptimo.costoRiegoTablon(1, finca, programacion) == 60) // Penalización
    assert(riegoOptimo.costoRiegoTablon(0, finca, programacion) == 20)  // 20 - (26 + 5) = 0
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

    assert(riegoOptimo.costoRiegoTablon(9, finca, programacion) == 17) // 26 - (0 + 9)
    assert(riegoOptimo.costoRiegoTablon(2, finca, programacion) == 7) // 22 - (9 + 6)
    assert(riegoOptimo.costoRiegoTablon(8, finca, programacion) == 4) // Penalización
    assert(riegoOptimo.costoRiegoTablon(3, finca, programacion) == 1) // 20 - (15 + 3)
    assert(riegoOptimo.costoRiegoTablon(1, finca, programacion) == 24) // Penalización
    assert(riegoOptimo.costoRiegoTablon(4, finca, programacion) == 20) // 24 - (22 + 8)
    assert(riegoOptimo.costoRiegoTablon(7, finca, programacion) == 18) // 23 - (30 + 7)
    assert(riegoOptimo.costoRiegoTablon(5, finca, programacion) == 78) // 19 - (37 + 4)
    assert(riegoOptimo.costoRiegoTablon(6, finca, programacion) == 58) // 21 - (41 + 5)
    assert(riegoOptimo.costoRiegoTablon(0, finca, programacion) == 64) // 25 - (46 + 7)
  }
  
}
