package Servicio;

import Modelo.Prestamo;
import java.time.LocalDate;
import java.util.ArrayList;

public class Prestamos {
    private ArrayList<Prestamo> listaPrestamos;
    private int contadorId;

    public Prestamos() {
        listaPrestamos = new ArrayList<>();
        contadorId = 1;
    }

    public void registrarPrestamo(String cliente, String libro) {
        LocalDate hoy = LocalDate.now();
        LocalDate limite = hoy.plusDays(15); // 15 días de préstamo
        Prestamo nuevo = new Prestamo(contadorId++, cliente, libro, hoy, limite);
        listaPrestamos.add(nuevo);
        System.out.println("✅ Préstamo registrado con ID: " + nuevo.getId());
    }

    public void devolverPrestamo(int id) {
        for (Prestamo p : listaPrestamos) {
            if (p.getId() == id && p.getEstado().equals("Activo")) {
                p.devolverLibro();
                System.out.println("📗 Libro devuelto correctamente (ID " + id + ")");
                return;
            }
        }
        System.out.println("⚠️ No se encontró un préstamo activo con ese ID.");
    }

    public void listarPrestamos() {
        System.out.println("\n📋 LISTA DE PRÉSTAMOS:");
        for (Prestamo p : listaPrestamos) {
            System.out.println("ID: " + p.getId()
                    + " | Cliente: " + p.getCliente()
                    + " | Libro: " + p.getLibro()
                    + " | Fecha préstamo: " + p.getFechaPrestamo()
                    + " | Fecha límite: " + p.getFechaLimite()
                    + " | Fecha devolución: " + (p.getFechaDevolucion() != null ? p.getFechaDevolucion() : "-")
                    + " | Estado: " + p.getEstado());
        }
    }
}