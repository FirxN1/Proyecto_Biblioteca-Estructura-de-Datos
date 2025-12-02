package Modelo;

import java.time.LocalDate;

public class Multa {
    private int id;
    private String dniCliente;
    private String nombreCliente;
    private int idPrestamo;
    private String libroTitulo;
    private String motivo;
    private double monto;
    private LocalDate fechaRegistro;
    private boolean pagada;

    public Multa(int id, String dniCliente, String nombreCliente, int idPrestamo, 
                 String libroTitulo, String motivo, double monto) {
        this.id = id;
        this.dniCliente = dniCliente;
        this.nombreCliente = nombreCliente;
        this.idPrestamo = idPrestamo;
        this.libroTitulo = libroTitulo;
        this.motivo = motivo;
        this.monto = monto;
        this.fechaRegistro = LocalDate.now();
        this.pagada = false;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDniCliente() {
        return dniCliente;
    }

    public void setDniCliente(String dniCliente) {
        this.dniCliente = dniCliente;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public int getIdPrestamo() {
        return idPrestamo;
    }

    public void setIdPrestamo(int idPrestamo) {
        this.idPrestamo = idPrestamo;
    }

    public String getLibroTitulo() {
        return libroTitulo;
    }

    public void setLibroTitulo(String libroTitulo) {
        this.libroTitulo = libroTitulo;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDate fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public boolean isPagada() {
        return pagada;
    }

    public void setPagada(boolean pagada) {
        this.pagada = pagada;
    }

    @Override
    public String toString() {
        return "Multa{" +
                "id=" + id +
                ", nombreCliente='" + nombreCliente + '\'' +
                ", libroTitulo='" + libroTitulo + '\'' +
                ", monto=" + monto +
                ", pagada=" + pagada +
                '}';
    }
}