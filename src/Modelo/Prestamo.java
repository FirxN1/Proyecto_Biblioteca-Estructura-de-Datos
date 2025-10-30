package Modelo;

import java.time.LocalDate;

public class Prestamo {
    private int id;
    private String cliente;
    private String libro;
    private LocalDate fechaPrestamo;
    private LocalDate fechaLimite;
    private LocalDate fechaDevolucion;
    private String estado;

    // Constructor
    public Prestamo(int id, String cliente, String libro, LocalDate fechaPrestamo, LocalDate fechaLimite) {
        this.id = id;
        this.cliente = cliente;
        this.libro = libro;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaLimite = fechaLimite;
        this.estado = "Pendiente";
    }

    // Getters
    public int getId() { return id; }
    public String getCliente() { return cliente; }
    public String getLibro() { return libro; }
    public LocalDate getFechaPrestamo() { return fechaPrestamo; }
    public LocalDate getFechaLimite() { return fechaLimite; }
    public LocalDate getFechaDevolucion() { return fechaDevolucion; }
    
    public String getEstado() {
        if (estado.equals("Pendiente") && LocalDate.now().isAfter(fechaLimite)) {
            estado = "Vencido";
        }
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }

    // Método para devolver libro
    public void devolverLibro() {
        this.fechaDevolucion = LocalDate.now();
        this.estado = "Devuelto";
    }
    
    
}
