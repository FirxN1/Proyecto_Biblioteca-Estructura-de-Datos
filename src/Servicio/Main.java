/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servicio;

import Modelo.Libro;
import java.util.ArrayList;

/**
 *
 * @author Abel Rodrigo
 */
    
public class Main {
    public static void main(String[] args) {
        Biblioteca b = Biblioteca.getInstancia(); // Usando Singleton

        ArrayList<String> generos = new ArrayList<>();
        generos.add("Novela");

        Libro libro1 = new Libro("Cien Años de Soledad", "Gabriel García Márquez",
                "Sudamericana", "12345", 1967, 417, generos, true);

        b.agregarLibro(libro1);

        b.listarLibros();

        System.out.println("\n🔎 Buscando por autor: Gabriel García Márquez");
        for (Libro l : b.buscarPorAutor("Gabriel García Márquez")) {
            System.out.println("Encontrado: " + l.getTitulo());
        }

        System.out.println("\n📖 Prestando libro...");
        b.prestarLibro("12345");
        b.listarLibros();
    }
}
