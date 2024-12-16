/*
 * This Scala source file was generated by the Gradle 'init' task.
 */
package taller

import common._
import org.scalameter._
import taller.RiegoOptimo

object App {

  // Método principal del programa
  def main(args: Array[String]): Unit = {
    println(greeting())
    println("\n....... Iniciando benchmarking de Riego Optimo .......")
    benchmarkingCostos()
    //benchmarkingCostosMovilidad()
    //benchmarkingProgramaciones()
    //benchmarkingOptimo()

  }

  def greeting(): String = "Hello, world!"

    /* 
    %6s: Se utiliza para formatear cadenas (String), alineándolas a la derecha en un campo de al menos 6 caracteres de ancho.
    %6d: Se utiliza para formatear enteros (Int), alineándolos a la derecha en un campo de al menos 6 caracteres de ancho.
   */

  // Función auxiliar para imprimir una matriz
  def imprimirMatriz(matriz: Vector[Vector[Int]]): Unit = { // Esta función toma una matriz (un Vector de Vector[Int]) y la imprime en formato de tabla.
      /* Iteramos sobre cada fila de la matriz 
      / matriz: Vector[Vector[Int]] es una matriz de enteros.
      / foreach: Itera sobre cada fila de la matriz.
      / fila: Es un Vector[Int] que representa una fila de la matriz.
       */
      matriz.foreach { fila =>
        /* Imprimimos cada elemento de la fila en formato de tabla.
        / fila.map aplica una funcion a cada elemento de la fila
        / elem es el número entero en la fila
        / elem => f"$elem%4d" formatea cada número para que ocupe al menos 4 espacios, alineando los números en columnas.
        / mkString(" "): Convierte la fila en una cadena de texto, separando los elementos con un espacio. 
         */
          println(fila.map(elem => f"$elem%4d").mkString("  "))
      }
  }

  // Función auxiliar para imprimir la información de los tablones en formato de tabla
  def imprimirInfo(info: Vector[(Int, Int, Int)]): Unit = { //Esta función toma la colección info (un Vector de tuplas) y la imprime en formato de tabla.
      
      println("-" * 37) // Línea divisoria de la tabla
      // Encabezado de la tabla, alinieado con las columnas 
      println(f"| ${"Tablón"}%6s | ${"Tsup"}%6s | ${"Treg"}%6s | ${"Prio"}%6s |")
      println("-" * 37) // Línea divisoria de la tabla
      
      /*Fila de la tabla: Iteramos sobre la colección info, desempaquetando las tuplas y los índices*
      / info: Vector[(Int, Int, Int)] es una colección de tuplas (tsup, treg, prio) que representan la información de los tablones.
      / zipWithIndex: Combina la colección info con los índices de cada tablón.
      / foreach: Itera sobre cada tupla (tsup, treg, prio) y su índice i en la colección info.
      / case ((tsup, treg, prio), i): Desempaqueta la tupla (tsup, treg, prio) y el índice i.
      */
      info.zipWithIndex.foreach { case ((tsup, treg, prio), i) =>
        /*Cuerpo de la tabla: Imprime cada tupla (tsup, treg, prio) y su índice i en formato de tabla.
        / Alineación de los datos en columnas
        */
          println(f"| $i%6d | $tsup%6d | $treg%6d | $prio%6d |")
      }
      println("-" * 37) // Línea divisoria de la tabla
  }

  

  // Benchmarking para los costos de riego y movilidad
  def benchmarkingCostos(): Unit = {
    val riego = new RiegoOptimo() // Instancia de la clase RiegoOptimo
    println("\n------------- Benchmarking: Costos de Riego y Movilidad ----------------")
    
    val tamanos = List(4,5,6,7,8) // Tamaños de las fincas a evaluar
    
    // Iteramos sobre los tamaños de las fincas
    tamanos.foreach { tamano =>
      println(f"\n============= Tamaño finca: $tamano =============\n")
      (1 to 10).foreach { iteracion =>
        // Imprimir la información de los tablones y la matriz de distancias
            val finca = riego.fincaAlAzar(tamano)
            val distancia = riego.distanciaAlAzar(tamano) 

            // Encontrar la programación de riego óptima
            val (programacionOptima, costoOptimo) = riego.ProgramacionRiegoOptimo(finca, distancia)     
                    
            // Crear una colección de información sobre los tablones
              val info = (0 until tamano).map { i =>
                  (riego.tsup(finca, i), riego.treg(finca, i), riego.prio(finca, i))
              }.toVector
            
            // Imprimir la información de los tablones y la matriz de distancias
            println(f"\n------------- Tamaño finca: $tamano  (Iteración $iteracion)---------------\n")
            imprimirInfo(info)
            //println(info)

            println(f"\n-------------- Matriz de Distancias $tamano x $tamano ---------------\n")
            imprimirMatriz(distancia)
            //println(distancia)


            // Comparación de los costos y movilidades entre los métodos secuenciales y paralelos
            println(f"\n ------------Comparación de las funciones secuenciales y paralelas para el tamaño de la finca: $tamano (Iteración $iteracion)---------------\n")
            println(f"\nComparación de las funciones secuenciales y paralelas para el tamaño de la finca: $tamano (Iteración $iteracion)\n")
            riego.compararCostos(riego.costoRiegoFinca, riego.costoRiegoFincaPar, "Secuencial", "Paralelo")(finca, programacionOptima)
          }
    }
  }

   // Benchmarking para los costos de riego y movilidad
  def benchmarkingCostosMovilidad(): Unit = {
    val riego = new RiegoOptimo() // Instancia de la clase RiegoOptimo
    println("\n------------- Benchmarking: Costos de Riego y Movilidad ----------------")
    
    val tamanos = List(4,5,7,8,9) // Tamaños de las fincas a evaluar
    
    // Iteramos sobre los tamaños de las fincas
    tamanos.foreach { tamano =>
      println(f"\n============= Tamaño finca: $tamano =============\n")
      (1 to 10).foreach { iteracion =>

        val finca = riego.fincaAlAzar(tamano)
        val distancia = riego.distanciaAlAzar(tamano)
        
        // Encontrar la programación de riego óptima
        val (programacionOptima, costoOptimo) = riego.ProgramacionRiegoOptimo(finca, distancia)     
                
        // Crear una colección de información sobre los tablones
          val info = (0 until tamano).map { i =>
              (riego.tsup(finca, i), riego.treg(finca, i), riego.prio(finca, i))
          }.toVector

        println(f"\n------------- Tamaño finca: $tamano (Iteración $iteracion) ---------------\n")
        imprimirInfo(info)
        //println(info)

        println(f"\n-------------- Matriz de Distancias $tamano x $tamano ---------------\n")
        imprimirMatriz(distancia)
        //println(distancia)


        // Comparación de los costos y movilidades entre los métodos secuenciales y paralelos
        println(f"\n ------------Comparación de las funciones secuenciales y paralelas para el tamaño de la finca: $tamano (Iteración $iteracion)---------------\n")
        println(f"\nComparación de las funciones Costo movilidad secuenciales y paralelas para el tamaño de la finca: $tamano (Iteración $iteracion)\n")
        riego.compararCostosMovilidad(riego.costoMovilidad, riego.costoMovilidadPar, "Secuencial", "Paralelo")(finca, programacionOptima, distancia)
      }
    }
  }



  // Benchmarking para la generación de programaciones de riego
  def benchmarkingProgramaciones(): Unit = {
    val riego = new RiegoOptimo()
    println("\n------------- Benchmarking: Generación de Programaciones de Riego ----------------")
    val tamanos = List(4,5,6,8,9) // Tamaños más pequeños debido al crecimiento factorial

    tamanos.foreach { tamano =>
      println(f"\n============= Tamaño finca: $tamano =============\n")
      (1 to 10).foreach { iteracion =>
          // Encontrar la programación de riego óptima
          val finca = riego.fincaAlAzar(tamano)
          val distancia = riego.distanciaAlAzar(tamano)

          // Crear una colección de información sobre los tablones
          val info = (0 until tamano).map { i =>
            (riego.tsup(finca, i), riego.treg(finca, i), riego.prio(finca, i))
          }.toVector

          println(f"\n------------- Tamaño finca: $tamano  (Iteración $iteracion)---------------\n")
          imprimirInfo(info)
          println(f"\n-------------- Matriz de Distancias $tamano x $tamano ---------------\n")
          imprimirMatriz(distancia)

          // Comparación de las programaciones entre los métodos secuenciales y paralelos
          println(f"\n ------------Comparación de las funciones secuenciales y paralelas para el tamaño de la finca: $tamano  (Iteración $iteracion)---------------\n")
          println(f"\nComparación de Programaciones  (Iteración $iteracion):")
          riego.compararGeneracion(riego.generarProgramacionesRiego, riego.generarProgramacionesRiegoPar, "Secuencial", "Paralelo")(finca)
        } 
    }
  }

  // Benchmarking para la programación de riego óptima
  def benchmarkingOptimo(): Unit = {
    val riego = new RiegoOptimo()
    println("\n------------- Benchmarking: Programación de Riego Óptima ----------------")
    val tamanos = List(4,5,6,7,9) // Tamaños factibles para evaluación

    tamanos.foreach { tamano =>
      println(f"\n============= Tamaño finca: $tamano =============\n")
      (1 to 10).foreach { iteracion =>
        val finca = riego.fincaAlAzar(tamano)
        val distancia = riego.distanciaAlAzar(tamano)
        
        // Crear una colección de información sobre los tablones
        val info = (0 until tamano).map { i =>
          (riego.tsup(finca, i), riego.treg(finca, i), riego.prio(finca, i))
        }.toVector

        println(f"\n------------- Tamaño finca: $tamano (Iteración $iteracion)---------------\n")
        imprimirInfo(info)
        println(f"\n-------------- Matriz de Distancias $tamano x $tamano ---------------\n")
        imprimirMatriz(distancia)

        // Comparación de las optimizaciones entre los métodos secuenciales y paralelos
        println(f"\n ------------Comparación de las funciones secuenciales y paralelas para el tamaño de la finca: $tamano (Iteración $iteracion)---------------\n")
        println(f"\nComparación de Optimizaciones de riego (Iteración $iteracion):")
        riego.compararOptimo(riego.ProgramacionRiegoOptimo, riego.ProgramacionRiegoOptimoPar, "Secuencial", "Paralelo")(finca, distancia)
      }
    }
  }
}
