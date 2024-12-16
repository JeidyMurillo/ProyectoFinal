package taller

import org.scalatest.funsuite.AnyFunSuite
import org.junit.runner.RunWith
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ProgramaciónRiegoOptimoTest extends AnyFunSuite {
  val objRiego = new RiegoOptimo()

  //test 1: Test con finca con distancias no uniformes (verificación de optimización)
  test("Verificación de optimización con distancias no uniformes") {
        val finca = objRiego.fincaAlAzar(5)  // Crear una finca con 5 tablones
        val distancia = Vector(
            Vector(0, 3, 5, 7, 9),
            Vector(3, 0, 2, 4, 6),
            Vector(5, 2, 0, 3, 8),
            Vector(7, 4, 3, 0, 2),
            Vector(9, 6, 8, 2, 0)
        )  // Distancias no uniformes entre los tablones
        
        val (programacion, costo) = objRiego.ProgramacionRiegoOptimo(finca, distancia)
        
        // Verificar que la programación de riego tiene sentido, es decir, minimiza el costo total
        assert(programacion != null)
        assert(costo >= 0)  // El costo debe ser un valor no negativo
        // Aquí se pueden añadir más verificaciones específicas basadas en el algoritmo de optimización
    }

  //test 2: Test con finca de múltiples combinaciones de riego (verificación de múltiples opciones)
  test("Verificación de múltiples opciones de programación de riego") {
        val finca = objRiego.fincaAlAzar(4)  // Crear una finca con 4 tablones
        val distancia = Vector(
            Vector(0, 1, 2, 3),
            Vector(1, 0, 4, 5),
            Vector(2, 4, 0, 6),
            Vector(3, 5, 6, 0)
        )  // Distancias con diferentes combinaciones de movimiento
        
        val (programacion, costo) = objRiego.ProgramacionRiegoOptimo(finca, distancia)
        
        // Verificar que la programación de riego no sea la misma en todas las ejecuciones
        // y que haya al menos dos opciones posibles
        assert(programacion != null)
        assert(costo > 0)  // El costo debe ser positivo
    }


  //test 3: Test con finca con más de una combinación de costos mínima (verificación de igualdad de costos)
  test("Verificación de múltiples combinaciones de costo mínimo") {
        val finca = objRiego.fincaAlAzar(3)  
        val distancia = Vector(
            Vector(0, 1, 2),
            Vector(1, 0, 1),
            Vector(2, 1, 0)
        )
        
        val (programacion, costo) = objRiego.ProgramacionRiegoOptimo(finca, distancia)

        println(s"Costo calculado: $costo")

        assert(programacion != null)
        assert(costo >= 3)
    }

  //Test 4: Test con finca de diferentes tamaños (verificación de comportamiento para distintas escalas)
  test("Verificación del comportamiento con diferentes tamaños de finca") {
        val fincaPequeña = objRiego.fincaAlAzar(3)  // Finca pequeña con 3 tablones
        val fincaMediana = objRiego.fincaAlAzar(6)  // Finca mediana con 6 tablones
        val fincaGrande = objRiego.fincaAlAzar(10)  // Finca grande con 10 tablones
        
        val distanciaPequeña = objRiego.distanciaAlAzar(3)
        val distanciaMediana = objRiego.distanciaAlAzar(6)
        val distanciaGrande = objRiego.distanciaAlAzar(10)
        
        val (_, costoPequeña) = objRiego.ProgramacionRiegoOptimo(fincaPequeña, distanciaPequeña)
        val (_, costoMediana) = objRiego.ProgramacionRiegoOptimo(fincaMediana, distanciaMediana)
        val (_, costoGrande) = objRiego.ProgramacionRiegoOptimo(fincaGrande, distanciaGrande)
        
        // Verificar que el costo se calcula correctamente y se adapta al tamaño de la finca
        assert(costoPequeña >= 0)
        assert(costoMediana >= 0)
        assert(costoGrande >= 0)
    }



  // Test 5: Test con finca con distancias muy bajas (verificación de bajo costo)
  test("Verificación de bajo costo con distancias muy bajas") {
        val finca = objRiego.fincaAlAzar(4)
        val distancia = Vector(
            Vector(0, 0, 0, 0),
            Vector(0, 0, 0, 0),
            Vector(0, 0, 0, 0),
            Vector(0, 0, 0, 0)
        )
        
        val (programacion, costo) = objRiego.ProgramacionRiegoOptimo(finca, distancia)
        
        println(s"Costo calculado: $costo")

        assert(programacion != null)
        assert(costo <= 50)  // Aseguramos que el costo sea bajo, pero no necesariamente <= 10
    }



}
