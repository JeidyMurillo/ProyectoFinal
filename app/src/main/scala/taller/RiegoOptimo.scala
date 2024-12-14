package  Taller

import scala.util.Random
import common._
import scala.collection.parallel.CollectionConverters._


class RiegoOptimo {

    // Un tablon es una tripleta con el tiempo de supervivencia,
    // el tiempo de riego y la prioridad del tablon
    type Tablon = (Int,Int,Int)

    // Una finca es un vector de tablones
    type Finca = Vector[Tablon]
    // Si f : Finca , f(i) = (tsi, tri, pi)

    // La distancia entre dos tablones se representa por una matriz
    type Distancia = Vector[Vector[Int]]
    
    // Una programación de riego es un vector que asocia
    // cada tablon i con su turno de riego (0 es el primer turno,
    // n-1 es el último turno)
    type ProgRiego = Vector[Int]
    // Si v : ProgRiego , y v.length == n, v es una permutación
    // de {0, ..., n-1} v(i) es el turno de riego del tablon i
    // para 0 <= i < n
    
    // El tiempo de inicio de riego es un vector que asocia
    // cada tablon i con el momento del tiempo en que se riega
    type TiempoInicioRiego = Vector[Int]
    // Si t : TiempoInicioRiego y t.length == n, t(i) es la hora a
    // la que inicia a regarse el tablon i

    //2.1. Generación de entradas aleatorias
    val random = new Random()

    def fincaAlAzar (long: Int): Finca = {

        // Crea una finca de long tablones ,
        // con valores aleatorios entre 1 y long * 2 para el tiempo
        // de supervivencia , entre 1 y long para el tiempo
        // de regado y entre 1 y 4 para la prioridad
        val v = Vector.fill(long) (
            (random.nextInt(long * 2) + 1,
            random.nextInt(long) + 1 ,
            random.nextInt(4) + 1 )
        )
        v
    }

    def distanciaAlAzar(long: Int): Distancia = {
        // Crea una matriz de distancias para una finca
        // de long tablones , con valores aleatorios entre
        // 1 y long * 3
        val v = Vector.fill(long, long)(random.nextInt(long * 3) + 1)
        Vector.tabulate(long, long)(( i , j ) =>
            if ( i < j ) v ( i ) ( j )
            else if ( i == j ) 0
            else v ( j ) ( i ))
    }

    //2.2. Exploracion de entradas
    def tsup(f:Finca, i:Int) : Int = {
        f(i)._1
    }

    def treg(f:Finca, i:Int) : Int = {
        f(i)._2
    }

    def prio(f:Finca, i:Int) : Int = {
        f(i)._3
    }

    //2.3. Calculando el tiempo de inicio de riego
    def tIR(f: Finca , pi: ProgRiego): TiempoInicioRiego = {

        // Dada una finca f y una programacion de riego pi,
        // y f.length == n, tIR(f, pi) devuelve t: TiempoInicioRiego
        // tal que t(i) es el tiempo en que inicia el riego del
        // tablon i de la finca f segun pi

        val tiempos = Array.fill(f.length)(0)
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
        // Dada una finca de n tablones, devuelve todas las
        // posibles programaciones de riego de la finca
        val indices = (0 until f.length).toVector
        indices.permutations.toVector
    }

    //2.6. Calculando una programación de riego óptima
    def ProgramacionRiegoOptimo(f:Finca, d:Distancia) : (ProgRiego, Int) = {
        // Dada una finca devuelve la programacion de riego optima
        val programaciones = generarProgramacionesRiego(f)
        val costos = programaciones.map(pi => 
            (pi, costoRiegoFinca(f, pi) + costoMovilidad(f, pi, d))
        )
        costos.minBy(_._2) 
    }

    //3.1. Paralelizando el calculo de los costos de riego y de movilidad
    def costoRiegoFincaPar(f:Finca, pi:ProgRiego) : Int = {
        // Devuelve el costo total de regar una finca f dada una
        // programacion de riego pi, calculando en paralelo
        (0 until f.length).par.map(i => costoRiegoTablon(i, f, pi)).sum
    }

    def costoMovilidadPar(f:Finca,pi:ProgRiego, d:Distancia) : Int = {
        // Calcula el costo de movilidad de manera paralela
        (0 until pi.length - 1).par.map(j => d(pi(j))(pi(j+1))).sum
    }

    //3.2. Paralelizando la generacion de programaciones de riego
    def generarProgramacionesRiegoPar(f:Finca) : Vector[ProgRiego] = {
        // Genera las programaciones posibles de manera paralela
        val indices = (0 until f.length).toVector
        indices.permutations.toVector.par.toVector
    }

    //3.3. Paralelizando la programacion de riego optima
    def ProgramacionRiegoOptimoPar(f:Finca, d:Distancia) : (ProgRiego, Int) = {
        // Dada una finca, calcula la programacion optima de riego
        val programaciones = generarProgramacionesRiegoPar(f)
        val costos = programaciones.par.map(pi => 
            (pi, costoRiegoFincaPar(f, pi) + costoMovilidadPar(f, pi, d))
        )
        costos.minBy(_._2)
    }
    
/*
3.4. Produciendo datos para hacer la evaluacion comparativa
.......

*/

/*  

3.5. Produciendo datos para hacer la evaluacion comparativa
.......
*/
    
}