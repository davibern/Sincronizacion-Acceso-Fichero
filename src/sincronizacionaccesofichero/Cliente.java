package sincronizacionaccesofichero;

// Librerías
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;

/**
 * Clase que lee de un fichero el valor y lo vacía
 * 
 * @author davibern
 * @version 1.0
 */
public class Cliente {
    
    /**
     * El primer argumento pasado será la ruta del fichero
     * @param args argumentos de la línea de comandos
     */
    public static void main(String[] args) {
        
        // Variables
        int orden = 0;
        int valor = 0;
        String nombreFichero = "";
        File archivo = null;
        RandomAccessFile raf = null;
        FileLock bloqueo = null;
        
        // Redirigimos salida y error estándas a un fichero
        try {
            PrintStream ps = new PrintStream(
                             new BufferedOutputStream(new FileOutputStream(
                             new File("javalog_cliente.txt"), true)), true);
        } catch (Exception ex) {
            System.err.println("Cliente ERROR: no se puede redirigir salidas.");
        }
        
        // Se obtiene nombre del fichero de la línea de argumentos
        if (args.length > 0) {
            nombreFichero = args[0];
        } else {
            nombreFichero = "buffer.txt";
        }
        
        // Preparamos el acceso al fichero
        archivo = new File(nombreFichero);
        // Se leen 10 datos
        while (valor < 9) {
            try {
                // Se abre el fichero
                raf = new RandomAccessFile(archivo, "rwd");
                // INICIO SECCIÓN CRÍTICA
                /*
                    - Bloqueamos el canal de acceso al fichero.
                    - Obtenemos el valor del objeto
                    - Se libera el canal del objeto
                */
                bloqueo = raf.getChannel().lock();
                System.out.println("Cliente ENTRA sección.");
                // Lectura del fichero (SÓLO LEE SI TIENE 1 VALOR)
                if (raf.length() > 0) {
                    // Leemos el valor actual
                    valor = raf.readInt();
                    // Vaciamos el fichero
                    raf.setLength(0);
                    System.out.println("Cliente valor leído -> " + valor);
                } else {
                    System.out.println("Cliente ERROR: no PUEDE leer.");
                }
                System.out.println("Cliente: SALE sección.");
                bloqueo.release();
                bloqueo = null;
                // FIN SECCIÓN CRÍTICA
                try {
                    // Simulamos un tiempo de ejecución
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    System.err.println(ex.getMessage());
                }
            } catch (FileNotFoundException ex) {
                System.err.println(ex.getMessage());
            } catch (IOException ex) {
                
            } finally {
                try {
                    if (raf != null) raf.close();
                    if (bloqueo != null) bloqueo.close();
                } catch (Exception ex) {
                    System.out.println("Cliente: Error al cerrar el fichero.");
                    // Si hay error finalizamos
                    System.exit(1);
                }
            }
        }
        
    }
    
}
