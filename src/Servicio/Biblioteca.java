package Servicio;

import java.util.ArrayList;
import Modelo.Libro;

public class Biblioteca {
    private ArrayList<Libro> libros;

    private static Biblioteca instancia;

    private Biblioteca() {
        this.libros = new ArrayList<>();
    }

    public static Biblioteca getInstancia() {
        if (instancia == null) {
            instancia = new Biblioteca();
        }
        return instancia;
    }

    public void agregarLibro(Libro libro) {
        libros.add(libro);
    }

    public boolean eliminarLibro(String isbn) {
        return libros.removeIf(l -> l.getIsbn().equalsIgnoreCase(isbn));
    }

    public ArrayList<Libro> getLibros() {
        return libros;
    }

    public Libro buscarPorTitulo(String titulo) {
        for (Libro l : libros) {
            if (l.getTitulo().equalsIgnoreCase(titulo)) {
                return l;
            }
        }
        return null;
    }

    public ArrayList<Libro> buscarPorAutor(String autor) {
        ArrayList<Libro> resultado = new ArrayList<>();
        for (Libro l : libros) {
            if (l.getAutor().equalsIgnoreCase(autor)) {
                resultado.add(l);
            }
        }
        return resultado;
    }

    public ArrayList<Libro> buscarPorEditorial(String editorial) {
        ArrayList<Libro> resultado = new ArrayList<>();
        for (Libro l : libros) {
            if (l.getEditorial().equalsIgnoreCase(editorial)) {
                resultado.add(l);
            }
        }
        return resultado;
    }

    public Libro buscarPorISBN(String isbn) {
        for (Libro l : libros) {
            if (l.getIsbn().equalsIgnoreCase(isbn)) {
                return l;
            }
        }
        return null;
    }

    public ArrayList<Libro> buscarPorAnio(int anio) {
        ArrayList<Libro> resultado = new ArrayList<>();
        for (Libro l : libros) {
            if (l.getAñoPublicacion() == anio) {
                resultado.add(l);
            }
        }
        return resultado;
    }

    public ArrayList<Libro> buscarPorNumeroPaginas(int paginas) {
        ArrayList<Libro> resultado = new ArrayList<>();
        for (Libro l : libros) {
            if (l.getNumeroPaginas() == paginas) {
                resultado.add(l);
            }
        }
        return resultado;
    }

    public ArrayList<Libro> buscarPorGenero(String genero) {
        ArrayList<Libro> resultado = new ArrayList<>();
        for (Libro l : libros) {
            if (l.getGeneros().contains(genero)) {
                resultado.add(l);
            }
        }
        return resultado;
    }

    public ArrayList<Libro> buscarPorDisponibilidad(boolean disponible) {
        ArrayList<Libro> resultado = new ArrayList<>();
        for (Libro l : libros) {
            if (l.isDisponible() == disponible) {
                resultado.add(l);
            }
        }
        return resultado;
    }

    // --- Métodos prestar y devolver ---
    public boolean prestarLibro(String isbn) {
        Libro l = buscarPorISBN(isbn);
        if (l != null && l.isDisponible()) {
            l.setDisponible(false);
            return true;
        }
        return false;
    }

    public boolean devolverLibro(String isbn) {
        Libro l = buscarPorISBN(isbn);
        if (l != null && !l.isDisponible()) {
            l.setDisponible(true);
            return true;
        }
        return false;
    }

    // --- Editar libro ---
    public boolean editarLibro(String isbn, String nuevoTitulo, String nuevoAutor,
                               String nuevaEditorial, int nuevoAnio, int nuevasPaginas,
                               ArrayList<String> nuevosGeneros, boolean disponible) {
        Libro l = buscarPorISBN(isbn);
        if (l != null) {
            l.setTitulo(nuevoTitulo);
            l.setAutor(nuevoAutor);
            l.setEditorial(nuevaEditorial);
            l.setAnioPublicacion(nuevoAnio);
            l.setNumeroPaginas(nuevasPaginas);
            l.getGeneros().clear();
            l.getGeneros().addAll(nuevosGeneros);
            l.setDisponible(disponible);
            return true;
        }
        return false;
    }

    // --- Listar libros ---
    public void listarLibros() {
        if (libros.isEmpty()) {
            System.out.println("No hay libros en la biblioteca.");
        } else {
            for (Libro l : libros) {
                System.out.println("Título: " + l.getTitulo() +
                        ", Autor: " + l.getAutor() +
                        ", Editorial: " + l.getEditorial() +
                        ", ISBN: " + l.getIsbn() +
                        ", Año: " + l.getAñoPublicacion() +
                        ", Páginas: " + l.getNumeroPaginas() +
                        ", Géneros: " + l.getGeneros() +
                        ", Disponible: " + (l.isDisponible() ? "Sí" : "No"));
            }
        }
    }
}
