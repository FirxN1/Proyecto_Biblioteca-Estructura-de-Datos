/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author Abel Rodrigo
 */
public class Libro {
    private String titulo;
    private String autor;
    private String editorial;
    private String isbn;
    private int añoPublicacion;
    private int numeroPaginas;
    private String genero;
    private boolean disponible;
    private double precio;

    public Libro(String titulo, String autor, String editorial, String isbn, 
                 int anioPublicacion, int numeroPaginas, String genero, 
                 boolean disponible, double precio) {
        this.titulo = titulo;
        this.autor = autor;
        this.editorial = editorial;
        this.isbn = isbn;
        this.añoPublicacion = anioPublicacion;
        this.numeroPaginas = numeroPaginas;
        this.genero = genero;
        this.disponible = disponible;
        this.precio = precio;
    }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getAñoPublicacion() {
        return añoPublicacion;
    }

    public void setAnioPublicacion(int añoPublicacion) {
        this.añoPublicacion = añoPublicacion;
    }

    public int getNumeroPaginas() {
        return numeroPaginas;
    }

    public void setNumeroPaginas(int numeroPaginas) {
        this.numeroPaginas = numeroPaginas;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }
    
    
}

