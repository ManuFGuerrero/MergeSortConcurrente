package paquete;

// Programa en Java para implementar el ordenamiento por mezcla utilizando
// multi-threading (multihilo)
import java.lang.System;
import java.util.ArrayList;


class MergeSort {
    
    // Se toman los procesadores logicos del sistema para realizarlo de la forma mas eficiente posible
    private static final int MAX_THREADS = Runtime.getRuntime().availableProcessors();
    
    // Clase personalizada de hilo con constructores
    private static class SortThreads extends Thread {  //Se utiliza esta clase para llamar a la funcion de ordenamiento e iniciar hilos
        SortThreads(Integer[] array, int begin, int end) {
            super(() -> {
                MergeSort.mergeSort(array, begin, end);
            });
            this.start();
        }
    }
    
    // Ordenamiento MergeSort aplicado de forma concurrente con Hilos 
    public static void threadedSort(Integer[] array) {
        
    	// Para el rendimiento - obtener el tiempo actual en milisegundos antes de comenzar
    	long time = System.currentTimeMillis();
        
    	final int length = array.length;
        // Carga de trabajo por hilo (chunk_of_data) = total_elementos/número_de_procesadores
        // si el número de elementos exactamente se distribuye entre el número de hilos disponibles,
        // entonces divide el trabajo de manera equitativa,
        // de lo contrario, si queda algún resto, entonces asume que tenemos (hilos_reales-1) trabajadores disponibles
        // y asigna los elementos restantes para ser trabajados por el ultimo hilo real restante.
        
    	boolean exact = length % MAX_THREADS == 0;
        int maxlim = exact ? length / MAX_THREADS : length / (MAX_THREADS - 1);
        // si la carga de trabajo es menor y no se necesita más de 1 hilo para el trabajo, entonces asigna todo a 1 hilo
        
        maxlim = maxlim < MAX_THREADS ? MAX_THREADS : maxlim;
        // Para hacer seguimiento de los hilos
        
        final ArrayList<SortThreads> threads = new ArrayList<>();
        // Dado que cada hilo es independiente para trabajar en su fragmento asignado,
        // Se crean hilos y asignar sus rangos de índice de trabajo
        // ej: para una lista de 16 elementos, h1 = 0-3, h2 = 4-7, h3 = 8-11, h4 = 12-15
        for (int i = 0; i < length; i += maxlim) {
            int beg = i;										//Elemento de inicio del hilo
            int remain = (length) - i;							//Elementos restantes
            int end = remain < maxlim ? i + (remain - 1) : i + (maxlim - 1);  //Elemento final del hilo
            final SortThreads t = new SortThreads(array, beg, end);
            
            // Agregar las referencias de los hilos para unirlos más tarde
            threads.add(t);
        }
        for (Thread t : threads) {
            try {
                // Esta implementación de merge requiere que todos los fragmentos trabajados por los hilos sean ordenados primero.
                // por lo que esperamos hasta que todos los hilos completen
                t.join();
            } catch (InterruptedException ignored) {}
        }
        // System.out.println("Uniendo el array de k-partes, donde m número de partes están ordenadas distintamente por cada hilo de los MAX_THREADS disponibles=" + MAX_THREADS);
        /*
        La mezcla toma 2 partes a la vez y las une en 1,
        luego nuevamente mezcla el resultado en la siguiente parte y así sucesivamente... hasta el final.
        Para MAXLIMIT = 2 (2 elementos por hilo donde el total de hilos = 4, en un total de 4*2 = 8 elementos)
        lista1 = (beg, mid); lista2 = (mid+1, end);
        1ra mezcla = 0,0,1 (beg, mid, end)
        2da mezcla = 0,1,3 (beg, mid, end)
        3ra mezcla = 0,3,5 (beg, mid, end)
        4ta mezcla = 0,5,7 (beg, mid, end)
        */
        for (int i = 0; i < length; i += maxlim) {
            int mid = i == 0 ? 0 : i - 1;
            int remain = (length) - i;
            int end = remain < maxlim ? i + (remain - 1) : i + (maxlim - 1); 
            // System.out.println("Comienzo: " + 0 + " Medio: " + mid + " Fin: " + end + " MAXLIM = " + maxlim);
            merge(array, 0, mid, end);
        }
        time = System.currentTimeMillis() - time;
        System.out.println("Tiempo empleado para merge_sort() recursivo personalizado multihilo: " + time + "ms");
    }

    // Se define el ordenamiento secuencial y recursivo de MergeSort
    public static void mergeSort(Integer[] array, int begin, int end) {
        if (begin < end) {
            int mid = (begin + end) / 2;
            mergeSort(array, begin, mid);
            mergeSort(array, mid + 1, end);
            merge(array, begin, mid, end);
        }
    }
    
    // Algoritmo que une o mezcla 2 arrays 
    public static void merge(Integer[] array, int begin, int mid, int end) {
        Integer[] temp = new Integer[(end - begin) + 1];
        
        int i = begin, j = mid + 1; 
        int k = 0;

        // Agregar elementos de la primera mitad o la segunda mitad según cual sea menor,
        // hacerlo hasta que una de las listas se agote y no se pueda hacer más comparación directa uno a uno
        while (i <= mid && j <= end) {
            if (array[i] <= array[j]) {
                temp[k] = array[i];
                i += 1;
            } else {
                temp[k] = array[j];
                j += 1;
            }
            k += 1;
        }

        // Agregar los elementos restantes al array temporal desde la primera mitad que quedan
        while (i <= mid) {
            temp[k] = array[i];
            i += 1; k += 1;
        } 
        
        // Agregar los elementos restantes al array temporal desde la segunda mitad que quedan
        while (j <= end) {
            temp[k] = array[j];
            j += 1; k += 1;
        }

        for (i = begin, k = 0; i <= end; i++, k++) {
            array[i] = temp[k];
        }
    }
}

