package Servicio;

import Modelo.Libro;
import Vista.VistaEmpleado;
import java.util.ArrayList;
import java.util.Date;
import java.time.LocalDate;

/**
 *
 * @author Abel Rodrigo
 */
    
public class Main {
    public static void main(String[] args) {
        Biblioteca b = Biblioteca.getInstancia();
        Prestamos prestamos = Prestamos.getInstancia(); // mismo objeto compartido

        ArrayList<String> generos = new ArrayList<>();
        generos.add("Novela");

        Libro libro1 = new Libro("Cien Años de Soledad", "Gabriel García Márquez",
                "Sudamericana", "12345", 1967, 417, generos, true);
        b.agregarLibro(libro1);

        // Agregamos algunos préstamos de ejemplo
        prestamos.registrarPrestamo("Juan Pérez", "Cien Años de Soledad");
        prestamos.registrarPrestamo("Ana López", "El Principito");
        prestamos.registrarPrestamo("Luis García", "Don Quijote de la Mancha");
        prestamos.registrarPrestamo("María Torres", "1984");
        prestamos.registrarPrestamo("Carlos Ramos", "El Alquimista");
        prestamos.registrarPrestamo("Sofía Vargas", "La Odisea");
        prestamos.registrarPrestamo("Pedro Castillo", "Crimen y Castigo");
        prestamos.registrarPrestamo("Lucía Fernández", "Orgullo y Prejuicio");
        prestamos.registrarPrestamo("Andrés López", "Los Miserables");
        prestamos.registrarPrestamo("Camila Díaz", "Fahrenheit 451");
        prestamos.registrarPrestamo("José Muñoz", "El Hobbit");
        prestamos.registrarPrestamo("Diana Chávez", "Matar a un ruiseñor");
        prestamos.registrarPrestamo("Jorge Paredes", "Cumbres Borrascosas");
        prestamos.registrarPrestamo("Fernanda Ruiz", "Rayuela");
        prestamos.registrarPrestamo("Alejandro Silva", "Ensayo sobre la ceguera");
        prestamos.registrarPrestamo("Gabriela Soto", "El Retrato de Dorian Gray");
        prestamos.registrarPrestamo("Ricardo Núñez", "El Principito");
        prestamos.registrarPrestamo("Daniela Salazar", "El viejo y el mar");
        prestamos.registrarPrestamo("Martín Rojas", "La Metamorfosis");
        prestamos.registrarPrestamo("Valeria Mendoza", "Las Mil y Una Noches");
        // Mostramos la interfaz
        new VistaEmpleado().setVisible(true);
    }
}
