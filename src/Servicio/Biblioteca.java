package Servicio;

import java.util.ArrayList;
import Modelo.Libro;
import javax.swing.table.DefaultTableModel;

public class Biblioteca {

    private ArrayList<Libro> libros;

    private static Biblioteca instancia;

    public Biblioteca() {
        this.libros = new ArrayList<>();

        ArrayList<String> generos1 = new ArrayList<>();
        generos1.add("Fantasía");
        generos1.add("Aventura");
        libros.add(new Libro("Harry Potter y la piedra filosofal", "J.K. Rowling", "Salamandra",
                "9788478884452", 1997, 223, generos1, true));

        ArrayList<String> generos2 = new ArrayList<>();
        generos2.add("Ciencia Ficción");
        libros.add(new Libro("Dune", "Frank Herbert", "Chilton Books",
                "9780441013593", 1965, 412, generos2, true));

        ArrayList<String> generos3 = new ArrayList<>();
        generos3.add("Realismo Mágico");
        libros.add(new Libro("Cien años de soledad", "Gabriel García Márquez", "Sudamericana",
                "9780307474728", 1967, 471, generos3, false));

        ArrayList<String> generos4 = new ArrayList<>();
        generos4.add("Novela");
        libros.add(new Libro("Don Quijote de la Mancha", "Miguel de Cervantes", "Francisco de Robles",
                "9788491050295", 1605, 863, generos4, true));

        ArrayList<String> generos5 = new ArrayList<>();
        generos5.add("Drama");
        generos5.add("Romance");
        libros.add(new Libro("Romeo y Julieta", "William Shakespeare", "Penguin Classics",
                "9780141396477", 1597, 230, generos5, true));

        ArrayList<String> generos6 = new ArrayList<>();
        generos6.add("Terror");
        libros.add(new Libro("It", "Stephen King", "Viking",
                "9780450411434", 1986, 1138, generos6, false));

        ArrayList<String> generos7 = new ArrayList<>();
        generos7.add("Ficción");
        generos7.add("Política");
        libros.add(new Libro("1984", "George Orwell", "Secker & Warburg",
                "9780451524935", 1949, 328, generos7, true));

        ArrayList<String> generos8 = new ArrayList<>();
        generos8.add("Ciencia");
        generos8.add("Divulgación");
        libros.add(new Libro("Breve historia del tiempo", "Stephen Hawking", "Bantam Books",
                "9780553380163", 1988, 212, generos8, true));

        ArrayList<String> generos9 = new ArrayList<>();
        generos9.add("Historia");
        libros.add(new Libro("Sapiens: De animales a dioses", "Yuval Noah Harari", "Debate",
                "9788499926226", 2011, 498, generos9, true));

        ArrayList<String> generos10 = new ArrayList<>();
        generos10.add("Fantasía");
        generos10.add("Épica");
        libros.add(new Libro("El Señor de los Anillos", "J.R.R. Tolkien", "Allen & Unwin",
                "9780618640157", 1954, 1216, generos10, false));
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
        String criterio = titulo.toLowerCase();

        for (Libro l : libros) {
            if (l.getTitulo().toLowerCase().contains(criterio)) {
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

    public ArrayList<Libro> buscarPorAño(int año) {
        ArrayList<Libro> resultado = new ArrayList<>();
        for (Libro l : libros) {
            if (l.getAñoPublicacion() == año) {
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

    public void listarLibros() {
        if (libros.isEmpty()) {
            System.out.println("No hay libros en la biblioteca.");
        } else {
            for (Libro l : libros) {
                System.out.println("Título: " + l.getTitulo()
                        + ", Autor: " + l.getAutor()
                        + ", Editorial: " + l.getEditorial()
                        + ", ISBN: " + l.getIsbn()
                        + ", Año: " + l.getAñoPublicacion()
                        + ", Páginas: " + l.getNumeroPaginas()
                        + ", Géneros: " + l.getGeneros()
                        + ", Disponible: " + (l.isDisponible() ? "Sí" : "No"));
            }
        }
    }

    public DefaultTableModel listarLibrosTableModel() {
        String[] columnas = {"Título", "Autor", "Editorial", "ISBN", "Año", "Páginas", "Géneros", "Disponible"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0);

        if (!libros.isEmpty()) {
            for (Libro l : libros) {
                Object[] fila = {
                    l.getTitulo(),
                    l.getAutor(),
                    l.getEditorial(),
                    l.getIsbn(),
                    l.getAñoPublicacion(),
                    l.getNumeroPaginas(),
                    String.join(", ", l.getGeneros()),
                    l.isDisponible() ? "Sí" : "No"
                };
                modelo.addRow(fila);
            }
        }
        return modelo;
    }

}
