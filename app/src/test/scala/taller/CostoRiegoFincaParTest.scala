package taller

import org.scalatest.funsuite.AnyFunSuite
import org.junit.runner.RunWith
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class CostoRiegoFincaParTest extends AnyFunSuite{
    val riegoOptimo = new RiegoOptimo()

    test("Costo para una finca con tres tablones y programación en orden") {
        val finca = Vector(
        (12, 4, 3), // Tablón 1
        (7, 3, 2),  // Tablón 2
        (9, 5, 1)   // Tablón 3
        )
        val programacion = Vector(0,1, 2) // Orden: Tablón 3 -> 2 -> 1

        assert(riegoOptimo.costoRiegoFincaPar(finca, programacion) == 11) // Suma de costos
    }

    test("Costo para una finca con cinco tablones y programación de regado en orden") {    
        val finca = Vector(
        (18, 6, 3), // Tablón 1
        (10, 3, 5),  // Tablón 2
        (13, 4, 4), // Tablón 3
        (8, 2, 2),  // Tablón 4
        (15, 5, 3)  // Tablón 5
        )
        val programacion = Vector(0, 1, 2, 3, 4) // Orden: Tablón 1 -> 2 -> 3 -> 4 -> 5

        assert(riegoOptimo.costoRiegoFincaPar(finca, programacion) == 42) // Suma de costos
    }

    test("Costo para una finca con ocho tablones y programación inversa") {
        val finca = Vector(
        (25, 7, 3), // Tablón 1
        (12, 5, 5), // Tablón 2
        (17, 4, 4), // Tablón 3
        (14, 6, 2), // Tablón 4
        (20, 8, 3), // Tablón 5
        (10, 3, 5),  // Tablón 6
        (16, 5, 4), // Tablón 7
        (18, 7, 2)  // Tablón 8
        )
        val programacion = Vector(7, 6, 5, 4, 3, 2, 1, 0) // Orden inverso

        assert(riegoOptimo.costoRiegoFincaPar(finca, programacion) == 333) // Suma de costos
    }

    test("Costo para una finca con diez tablones y programación mixta") {
        val finca = Vector(
        (30, 10, 3),  // Tablón 1
        (22, 7, 4),  // Tablón 2
        (27, 8, 5),  // Tablón 3
        (25, 6, 2),  // Tablón 4
        (29, 9, 3),  // Tablón 5
        (24, 7, 4),  // Tablón 6
        (26, 8, 2),  // Tablón 7
        (28, 10, 1), // Tablón 8
        (21, 5, 4),  // Tablón 9
        (32, 11, 3)  // Tablón 10
        )
        val programacion = Vector(9, 2, 8, 3, 1, 4, 7, 5, 6, 0) // Orden mixto

        assert(riegoOptimo.costoRiegoFincaPar(finca, programacion) == 589) // Suma de costos
    }

    test("Costo para una finca con doce tablones y valores grandes") {
        val finca = Vector(
        (220, 18, 5),  // Tablón 1
        (160, 12, 4),  // Tablón 2
        (200, 14, 6),  // Tablón 3
        (180, 10, 3),  // Tablón 4
        (210, 16, 4),  // Tablón 5
        (170, 13, 5),  // Tablón 6
        (190, 11, 3),  // Tablón 7
        (200, 15, 2),  // Tablón 8
        (175, 12, 4),  // Tablón 9
        (165, 9, 6),   // Tablón 10
        (215, 17, 3),  // Tablón 11
        (195, 14, 5)   // Tablón 12
        )
        val programacion = Vector(11, 2, 10, 3, 8, 6, 5, 1, 4, 9, 7, 0) // Orden mixto

        assert(riegoOptimo.costoRiegoFincaPar(finca, programacion) == 1248) // Suma de costos
    }
}
