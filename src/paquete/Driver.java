package paquete;

import java.util.Arrays;
import java.util.Random;

public class Driver {
    // Tamaño del array 
    private static Random random = new Random();
    private static final int size = 9999999;
    private static final Integer list[] = new Integer[size];
    // Llenar el array inicial con elementos aleatorios dentro del rango
    static {
        for (int i = 0; i < size; i++) {
            // agregar un desplazamiento positivo al número aleatorio generado y restar el mismo desplazamiento
            // del total para que el número se desplace hacia el lado negativo por el desplazamiento.
            // ej: si random_num = 10, entonces (10+100)-100 => -10
            list[i] = random.nextInt(size + (size - 1)) - (size - 1);
        }
    }
    
   
    // Probar el rendimiento de los métodos de ordenamiento
    public static void main(String[] args) {
       

    	/*System.out.print("Entrada = [");
        for (Integer each : list)
            System.out.print(each + ", ");
        System.out.print("] \n" + "Input.length = " + list.length + '\n');
  */
/* Esta parte del codigo se encuentra comentada ya que el autor del mismo prueba el tiempo que tarda la funcion original de los arrays para ordenar
   el vector de forma ascendente, en este proyecto estos datos no son relevantes para las comparaciones 
    
        // Probar el método Arrays.sort() estándar del sistema
        Integer[] arr1 = Arrays.copyOf(list, list.length);
        long t = System.currentTimeMillis();
        Arrays.sort(arr1, (a, b) -> a > b ? 1 : a == b ? 0 : -1);
        t = System.currentTimeMillis() - t;
        System.out.println("Tiempo empleado para Arrays.sort() basado en el sistema: " + t + "ms");
*/
    
        // Probar la implementación personalizada de ordenamiento por mezcla de un solo hilo (merge recursivo)
        Integer[] arr2 = Arrays.copyOf(list, list.length);
        long t = System.currentTimeMillis();
        MergeSort.mergeSort(arr2, 0, arr2.length - 1);
        t = System.currentTimeMillis() - t;
        System.out.println("Tiempo empleado para merge_sort() recursivo de un solo hilo personalizado: " + t + "ms");

        // Probar la implementación personalizada de ordenamiento por mezcla (merge recursivo) multihilo
        Integer[] arr = Arrays.copyOf(list, list.length);
        MergeSort.threadedSort(arr);
        /*System.out.print("Salida = [");
        for (Integer each : arr)
            System.out.print(each + ", ");
        System.out.print("]\n");
         */
        }
}
