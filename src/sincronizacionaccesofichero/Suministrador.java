package sincronizacionaccesofichero;

// Librerías
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;

/**
 * Escribe en un fichero unidades que luego serán leídas por otro proceso
 * 
 * @author davibern
 * @version 1.0
 */
public class Suministrador {

    /**
     * El primer argumento pasado será la ruta dle fichero
     * @param args argumentos en línea de comandos
     */
    public static void main(String[] args) {
        
        // Variables
        int orden = 0;
        int i = 0;
        String nombreFichero = "";
        File archivo = null;
        RandomAccessFile raf = null;
        FileLock bloqueo = null;
        
        // Redirigimos salida y error estándar en un fichero
        try {
            PrintStream ps = new PrintStream(
                             new BufferedOutputStream(new FileOutputStream(
                             new File("javalog_suministrador.txt"), true)), true);
            System.setOut(ps);
            System.setErr(ps);
        } catch (FileNotFoundException ex) {
            System.err.println(ex.getMessage());
        } catch (Exception ex) {
            System.err.println("Suministrador ERROR: no se puede redirigir salidas.");
        }
        
        // Se obtiene nombre del fichero de la línea de argumentos
        if (args.length > 0) {
            nombreFichero = args[0];
        } else {
            nombreFichero = "buffer.txt";
        }
        
        // Preparamos el acceso al fichero
        archivo = new File(nombreFichero);
        while (i < 10) {
            try {
                // Abrimos el fichero
                raf = new RandomAccessFile(archivo, "rwd");
                // INICIO SECCIÓN CRÍTICA
                /*  - Bloqueamos el canal de acceso al fichero.
                    - Obtenemos el objeto que representa el bloqueo.
                    - Se libera el bloqueo
                */
                bloqueo = raf.getChannel().lock();
                System.out.println("Suministrador: ENTRA sección.");
                // Lectura del fichero (SÓLO ESCRIBE SI SE VACÍA)
                if (raf.length() == 0) {
                    // Escribimos el valor actual de i
                    raf.writeInt(i);
                    System.out.println("Suministrador: valor escrito ->" + i);
                    // Aumentamos i
                    i++;
                } else {
                    System.out.println("Suministrador: no PUEDE escribir.");
                }
                System.out.println("Suministrador: SALE sección.");
                // Liberamos el bloqueo del canal del fichero
                bloqueo.release();
                bloqueo = null;
                // FIN SECCIÓN CRÍTICA
                // Se simula un tiempo de creación del objeto
                Thread.sleep(500);
            } catch (FileNotFoundException ex) {
                System.err.println(ex.getMessage());
            } catch (Exception ex) {
                System.err.println(ex.getMessage());
            } finally {
                try {
                    if (bloqueo != null) bloqueo.release();
                    if (raf != null) raf.close();
                } catch (Exception ex) {
                    System.out.println("Suministrador: Error al cerrar el fichero.");
                    // Si hay error finalizamos
                    System.exit(1);
                }
            }
        }
        
    }
    
}
