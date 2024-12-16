package taller

import org.scalatest.funsuite.AnyFunSuite
import org.junit.runner.RunWith
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class GenerarProgramacionesRiegoParTest extends AnyFunSuite {
  val objRiego = new RiegoOptimo()

  // Test 1: Generar programaciones para una finca con 3 tablones
  test("Generar programaciones para finca con 3 tablones") {
    val finca = objRiego.fincaAlAzar(3)  // Crear una finca con 3 tablones aleatorios
    val programaciones = objRiego.generarProgramacionesRiegoPar(finca)
    assert(programaciones.size == 6)  // Para 3 tablones, el número de permutaciones es 3! = 6
  }

  // Test 2: Generar programaciones para una finca con 2 tablones
  test("Generar programaciones para finca con 2 tablones") {
    val finca = objRiego.fincaAlAzar(2)  // Crear una finca con 2 tablones aleatorios
    val programaciones = objRiego.generarProgramacionesRiegoPar(finca)
    assert(programaciones.size == 2)  // Para 2 tablones, el número de permutaciones es 2! = 2
  }

  // Test 3: Verificar que las programaciones generadas sean únicas
  test("Generar programaciones únicas para finca con 3 tablones") {
    val finca = objRiego.fincaAlAzar(3)
    val programaciones = objRiego.generarProgramacionesRiegoPar(finca)
    assert(programaciones.distinct.size == programaciones.size)  // Asegurarse de que no haya duplicados
  }

  // Test 4: Generar programaciones para una finca vacía (sin tablones)
  test("Generar programaciones para finca con 1 tablon") {
    val finca = objRiego.fincaAlAzar(1)  // Crear una finca con un solo tablón
    val programaciones = objRiego.generarProgramacionesRiegoPar(finca)
    assert(programaciones.size == 1)  // Para 1 tablon, solo debe existir una programación posible (0)
    assert(programaciones.head == Vector(0))  // La única permutación debe ser el índice 0
  }


  // Test 5: Verificar el orden de los tablones en las programaciones generadas
  test("Generar programaciones y verificar el orden de los tablones") {
    val finca = objRiego.fincaAlAzar(3)
    val programaciones = objRiego.generarProgramacionesRiegoPar(finca)
    programaciones.foreach { p =>
      assert(p.sorted == (0 until finca.length).toVector)  // Las permutaciones deben ser una reordenación de 0, 1, 2...
    }
  }
}
