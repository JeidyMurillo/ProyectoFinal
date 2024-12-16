package taller

import org.scalatest.funsuite.AnyFunSuite
import org.junit.runner.RunWith
import org.scalatestplus.junit.JUnitRunner
import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global



@RunWith(classOf[JUnitRunner])
class ProgramacionRiegoOptimoParTest extends AnyFunSuite {
  
  val objRiego = new RiegoOptimo()

  // Test 1: Verificación de la programación óptima con distancias mínimas
  test("Verificación de programación óptima con distancias mínimas") {
    val finca = objRiego.fincaAlAzar(3)
    val distancia = Vector(
        Vector(0, 1, 2),
        Vector(1, 0, 1),
        Vector(2, 1, 0)
    )
    
    val (programacion, costo) = objRiego.ProgramacionRiegoOptimoPar(finca, distancia)
    
    assert(programacion != null)
    assert(costo >= 0)
  }

  // Test 2: Verificación de programación óptima con alto costo de movilidad
  test("Verificación de programación óptima con alto costo de movilidad") {
    val finca = objRiego.fincaAlAzar(5)
    val distancia = Vector(
        Vector(0, 5, 10, 15, 20),
        Vector(5, 0, 5, 10, 15),
        Vector(10, 5, 0, 5, 10),
        Vector(15, 10, 5, 0, 5),
        Vector(20, 15, 10, 5, 0)
    )
    
    val (programacion, costo) = objRiego.ProgramacionRiegoOptimoPar(finca, distancia)
    
    assert(programacion != null)
    assert(costo >= 20)  // El costo de movilidad es alto
  }

  // Test 3: Verificación con varias combinaciones de costo mínimo, usando paralelización
 test("Verificación de costo óptimo en finca con 2 tablones") {
  val finca = Vector(
    (5, 3, 1),  // Tablón 1: tiempoSupervivencia=5, tiempoRegado=3, prioridad=1
    (6, 4, 2)   // Tablón 2: tiempoSupervivencia=6, tiempoRegado=4, prioridad=2
  )
  
  val distancia = Vector(
    Vector(0, 1),  // Distancia entre Tablón 1 y Tablón 2
    Vector(1, 0)   // Distancia entre Tablón 2 y Tablón 1
  )

  // Llamamos a la función que genera la programación óptima
  val (programacion, costo) = objRiego.ProgramacionRiegoOptimoPar(finca, distancia)

  // Validamos que la programación no sea nula
  assert(programacion != null, "La programación de riego no debe ser nula.")

  // Desglosamos los cálculos para depurar:
  val costoRiego = programacion.map(tab => finca(tab)._2).sum
  val costoMovilidad = programacion.zip(programacion.tail).map {
    case (prev, next) => distancia(prev)(next)
  }.sum

  println(s"Programación generada: $programacion")
  println(s"Costo de riego calculado: $costoRiego")
  println(s"Costo de movilidad calculado: $costoMovilidad")
  println(s"Costo total calculado: $costo")

  // Validamos que el costo esperado coincida
  val costoEsperado = 5
  assert(costo == costoEsperado, s"El costo esperado era $costoEsperado, pero se calculó $costo.")
}






  test("Verificación con distancias iguales (sin movimiento)") {
  val finca = objRiego.fincaAlAzar(4)
  
  val distancia = Vector(
    Vector(0, 0, 0, 0),
    Vector(0, 0, 0, 0),
    Vector(0, 0, 0, 0),
    Vector(0, 0, 0, 0)
  )
  
  val (programacion, costo) = objRiego.ProgramacionRiegoOptimoPar(finca, distancia)
  
  assert(programacion != null, "La programación de riego no debe ser nula.")

  println(s"Programación generada: $programacion")
  println(s"Costo calculado: $costo")

  assert(costo >= 0, "El costo no puede ser negativo")
  assert(programacion.length == finca.length, "La programación debe tener el mismo número de elementos que la finca")
  assert(programacion.toSet == (0 until finca.length).toSet, "La programación debe ser una permutación de los índices")
}


  // Test 5: Verificación de la programación óptima en finca con pocos tablones
  test("Verificación de programación óptima en finca con pocos tablones") {
        val finca = objRiego.fincaAlAzar(2)
        val distancia = Vector(
            Vector(0, 1),
            Vector(1, 0)
        )
        
        val (programacion, costo) = objRiego.ProgramacionRiegoOptimoPar(finca, distancia)
        
        assert(programacion != null)
        
        val costoRiego = objRiego.costoRiegoFincaPar(finca, programacion)
        val costoMovilidad = objRiego.costoMovilidadPar(finca, programacion, distancia)
        
        println(s"Programación: $programacion")
        println(s"Costo total: $costo")
        println(s"Costo de riego: $costoRiego")
        println(s"Costo de movilidad: $costoMovilidad")
        
        assert(costo == costoRiego + costoMovilidad, "El costo total debe ser la suma del costo de riego y movilidad")
        assert(programacion.length == finca.length, "La programación debe tener el mismo número de elementos que la finca")
        assert(programacion.toSet == (0 until finca.length).toSet, "La programación debe ser una permutación de los índices")
    }
}
