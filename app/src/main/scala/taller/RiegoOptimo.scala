package  taller

import scala.util.Random
import common._
import scala.collection.parallel.CollectionConverters._
import org.scalameter._

class RiegoOptimo {

    //Definición de los tipos

    /*
    Un tablon es una tripleta con el tiempo de supervivencia,
    el tiempo de riego y la prioridad del tablon
    */
    type Tablon = (Int, Int, Int) // (tiempoSupervivencia, tiempoRegado, prioridad)

    // Una finca es un vector de tablones
    type Finca = Vector[Tablon]  
    // Si f : Finca , f(i) = (tsi, tri, pi)

    // La distancia entre dos tablones se representa por una matriz
    type Distancia = Vector[Vector[Int]]
    
    /*
    Una programación de riego es un vector que asocia
    cada tablon i con su turno de riego (0 es el primer turno,
    n-1 es el último turno)
    */
    type ProgRiego = Vector[Int]

    /*
    Si v : ProgRiego , y v.length == n, v es una permutación
    de {0, ..., n-1} v(i) es el turno de riego del tablon i
    para 0 <= i < n
    
    El tiempo de inicio de riego es un vector que asocia
    cada tablon i con el momento del tiempo en que se riega 
    */

    type TiempoInicioRiego = Vector[Int]
    
    /* 
    Si t : TiempoInicioRiego y t.length == n, t(i) es la hora a
    la que inicia a regarse el tablon 
    */

    //2.1. Generación de entradas aleatorias
    val random = new Random()

    def fincaAlAzar (long: Int): Finca = {
        /*
        Crea una finca de long tablones ,
        con valores aleatorios entre 1 y long * 2 para el tiempo
        de supervivencia , entre 1 y long para el tiempo
        de regado y entre 1 y 4 para la prioridad
        */
        val v = Vector.fill(long) (
            (random.nextInt(long * 2) + 1,
            random.nextInt(long) + 1 ,
            random.nextInt(4) + 1 )
        )
        v
    }

    def distanciaAlAzar(long: Int): Distancia = {
        /* 
        Crea una matriz de distancias para una finca
        de long tablones , con valores aleatorios entre
        1 y long * 3
        */

        val v = Vector.fill(long, long)(random.nextInt(long * 3) + 1)
        Vector.tabulate(long, long)(( i , j ) =>
            if ( i < j ) v ( i ) ( j )
            else if ( i == j ) 0
            else v ( j ) ( i ))
    }

    //2.2. Exploración de entradas
    def tsup(f:Finca, i:Int) : Int = { f(i)._1 }

    def treg(f:Finca, i:Int) : Int = { f(i)._2 }

    def prio(f:Finca, i:Int) : Int = { f(i)._3 }

    //2.3. Calculando el tiempo de inicio de riego
    def tIR(f: Finca , pi: ProgRiego): TiempoInicioRiego = {
        /*
        Dada una finca f y una programacion de riego pi,
        y f.length == n, tIR(f, pi) devuelve t: TiempoInicioRiego
        tal que t(i) es el tiempo en que inicia el riego del
        tablon i de la finca f segun pi
        */

        // Inicializamos tiempos en 0 para todos los tablones
        val tiempos = Array.fill(f.length)(0)
        // Recorremos la programación en orden de riego  pi
        for (j <- 1 until pi.length) {
            val prevTablon = pi(j-1)
            val currTablon = pi(j)
            tiempos(currTablon) = tiempos(prevTablon) + treg(f, prevTablon) 
        }
        tiempos.toVector
    }

    //2.4. Calculando costos
    def costoRiegoTablon(i:Int, f:Finca, pi:ProgRiego) : Int = {
        val tiempoInicio = tIR(f, pi)(i)
        val tiempoFinal = tiempoInicio + treg(f, i)
        if (tsup(f,i) - treg(f, i) >= tiempoInicio) {
            tsup(f,i) - tiempoFinal
        } else  {
            prio(f,i) * (tiempoFinal - tsup(f,i))
        }
    }

    def costoRiegoFinca(f:Finca, pi:ProgRiego) : Int = {
        (0 until f.length).map(i => costoRiegoTablon(i, f, pi)).sum
    }

    def costoMovilidad(f:Finca, pi:ProgRiego, d:Distancia) : Int = {
        (0 until pi.length - 1).map(j => d(pi(j))(pi(j+1))).sum
    }

    //2.5. Generando programaciones de riego
    def generarProgramacionesRiego(f:Finca) : Vector[ProgRiego] = {
        // Dada una finca de n tablones, devuelve todas las posibles programaciones de riego de la finca
        val indices = (0 until f.length).toVector
        indices.permutations.toVector
    }

    //2.6. Calculando una programación de riego óptima
    def ProgramacionRiegoOptimo(f:Finca, d:Distancia) : (ProgRiego, Int) = {
        // Dada una finca devuelve la programación de riego óptima
        val programaciones = generarProgramacionesRiego(f)
        val costos = programaciones.map(pi => 
            (pi, costoRiegoFinca(f, pi) + costoMovilidad(f, pi, d))
        )
        costos.minBy(_._2) 
    }

    //3.1. Paralelizando el calculo de los costos de riego y de movilidad
    def costoRiegoFincaPar(f:Finca, pi:ProgRiego) : Int = {
        // Devuelve el costo total de regar una finca f dada una programación de riego pi, calculando en paralelo
        (0 until f.length).par.map(i => costoRiegoTablon(i, f, pi)).sum
    }

    def costoMovilidadPar(f:Finca,pi:ProgRiego, d:Distancia) : Int = {
        // Calcula el costo de movilidad de manera paralela
        (0 until pi.length - 1).par.map(j => d(pi(j))(pi(j+1))).sum
    }

    //3.2. Paralelizando la generación de programaciones de riego
    def generarProgramacionesRiegoPar(f:Finca) : Vector[ProgRiego] = {
        // Genera las programaciones posibles de manera paralela
        val indices = (0 until f.length).toVector
        indices.permutations.toVector.par.toVector
    }

    //3.3. Paralelizando la programación de riego óptima
    def ProgramacionRiegoOptimoPar(f:Finca, d:Distancia) : (ProgRiego, Int) = {
        // Dada una finca, calcula la programación optima de riego
        val programaciones = generarProgramacionesRiegoPar(f)
        val costos = programaciones.par.map(pi => 
            (pi, costoRiegoFincaPar(f, pi) + costoMovilidadPar(f, pi, d))
        )
        costos.minBy(_._2)
    }
    
    //3.4. Produciendo datos para hacer la evaluación comparativa


    // Comparación de costos de riego (secuencial vs paralelo)
    def compararCostos(
        funcionSecuencial: (Finca, ProgRiego) => Int, // Función secuencial a evaluar (costo de riego) recibe finca y programación que son tipos de datos vector de tablones y vector de turnos de riego y devuelve un entero
        funcionParalela: (Finca, ProgRiego) => Int,  // Función paralela a evaluar (costo de riego) recibe finca y programación que son tipos de datos vector de tablones y vector de turnos de riego y devuelve un entero
        nombreSecuencial: String,                   // Nombre descriptivo de la función secuencial
        nombreParalela: String                     // Nombre descriptivo de la función paralela
    )(finca: Finca, programacion: ProgRiego): Unit = {
        // Mide el tiempo de ejecución de la función secuencial
        val tiempoSecuencial = withWarmer(new Warmer.Default) measure { funcionSecuencial(finca, programacion) }
        
        // Mide el tiempo de ejecución de la función paralela
        val tiempoParalelo = withWarmer(new Warmer.Default) measure { funcionParalela(finca, programacion) }
        
        // Calcula la aceleración de la función paralela con respecto a la secuencial
        val aceleracion = tiempoSecuencial.value / tiempoParalelo.value
        
        // Impresión de resultados
        println(f"\nTiempo $nombreSecuencial: ${tiempoSecuencial.value}%.4f ms") // %.4f imprime el número con 4 decimales
        println(f"Tiempo $nombreParalela: ${tiempoParalelo.value}%.4f ms")
        println(f"Aceleración: $aceleracion%.4f")
        
    }

    def compararCostosMovilidad(
        funcionSecuencial: (Finca, ProgRiego, Distancia) => Int, // Función secuencial a evaluar (costo de movilidad) recibe finca, programación y distancia que son tipos de datos Finca, ProgRiego y Distancia y devuelve un entero
        funcionParalela: (Finca, ProgRiego, Distancia) => Int, // Función paralela a evaluar (costo de movilidad) recibe finca, programación y distancia que son tipos de datos vector de tablones, vector de turnos de riego y matriz de distancias y devuelve un entero
        nombreSecuencial: String,
        nombreParalela: String
    )(finca: Finca, programacion: ProgRiego, distancia: Distancia): Unit = {
        val tiempoSecuencial = withWarmer(new Warmer.Default) measure { funcionSecuencial(finca, programacion, distancia) }
        val tiempoParalelo = withWarmer(new Warmer.Default) measure { funcionParalela(finca, programacion, distancia) }
        val aceleracion = tiempoSecuencial.value / tiempoParalelo.value
        
        // Impresión de resultados
        println(f"\nTiempo $nombreSecuencial: ${tiempoSecuencial.value}%.4f ms")
        println(f"Tiempo $nombreParalela: ${tiempoParalelo.value}%.4f ms")
        println(f"Aceleración: $aceleracion%.4f")
    }

  // Comparación de generación de programaciones
  def compararGeneracion(
                          funcionSecuencial: Finca => Vector[ProgRiego], // Función secuencial a evaluar (generar programaciones) recibe una finca y devuelve un vector de programaciones
                          funcionParalela: Finca => Vector[ProgRiego],  // Función paralela a evaluar (generar programaciones) recibe una finca y devuelve un vector de programaciones
                          nombreSecuencial: String,
                          nombreParalela: String
                        )(finca: Finca): Unit = {
    val tiempoSecuencial = withWarmer(new Warmer.Default) measure { funcionSecuencial(finca) }
    val tiempoParalelo = withWarmer(new Warmer.Default) measure { funcionParalela(finca) }
    val aceleracion = tiempoSecuencial.value / tiempoParalelo.value

    // Impresión de resultados
    println(f"\nTiempo $nombreSecuencial: ${tiempoSecuencial.value}%.4f ms")
    println(f"Tiempo $nombreParalela: ${tiempoParalelo.value}%.4f ms")
    println(f"Aceleración: $aceleracion%.4f")
  }

}