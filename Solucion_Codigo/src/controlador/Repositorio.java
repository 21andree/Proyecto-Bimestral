package controlador;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import modelo.PatioComidas;

public class Repositorio {

    private String nombreArchivo = "patio.dat";

    public void guardar(PatioComidas patio) {
        try (ObjectOutputStream write = new ObjectOutputStream(
                new FileOutputStream(nombreArchivo))) {
            write.writeObject(patio);
        } catch (IOException e) {
            System.out.println("Error al guardar los datos: " + e.getMessage());
        }
    }

    public PatioComidas cargar() {
        try (ObjectInputStream read = new ObjectInputStream(
                new FileInputStream(nombreArchivo))) {
            return (PatioComidas) read.readObject();
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error al cargar los datos: " + e.getMessage());
            return null;
        }
    }
}
