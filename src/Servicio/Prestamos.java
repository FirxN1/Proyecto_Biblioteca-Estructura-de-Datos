package Servicio;

import Modelo.Prestamo;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class Prestamos {
    private static Prestamos instancia;
    private ArrayList<Prestamo> listaPrestamos;
    private HashMap<Integer, Prestamo> mapaPorId;
    private HashMap<String, ArrayList<Prestamo>> mapaPorCliente;
    private HashMap<String, ArrayList<Prestamo>> mapaPorEstado;
    private int contadorId;

    private Prestamos() {
        listaPrestamos = new ArrayList<>();
        mapaPorId = new HashMap<>();
        mapaPorCliente = new HashMap<>();
        mapaPorEstado = new HashMap<>();
        contadorId = 1;
    }

    public static Prestamos getInstancia() {
        if (instancia == null) {
            instancia = new Prestamos();
        }
        return instancia;
    }

    // -------------------- REGISTRAR --------------------
    public void registrarPrestamo(String cliente, String libro) {
        LocalDate hoy = LocalDate.now();
        LocalDate limite = hoy.plusDays(15);
        Prestamo nuevo = new Prestamo(contadorId++, cliente, libro, hoy, limite);

        listaPrestamos.add(nuevo);
        mapaPorId.put(nuevo.getId(), nuevo);

        // Índice por cliente
        mapaPorCliente.computeIfAbsent(cliente.toLowerCase(), k -> new ArrayList<>()).add(nuevo);

        // Índice por estado (Pendiente)
        mapaPorEstado.computeIfAbsent("pendiente", k -> new ArrayList<>()).add(nuevo);

        System.out.println("✅ Préstamo registrado con ID: " + nuevo.getId());
    }

    // -------------------- DEVOLVER --------------------
    public void devolverPrestamo(int id) {
        Prestamo p = mapaPorId.get(id);
        if (p != null && p.getEstado().equals("Pendiente")) {
            // Cambiar estado del préstamo
            p.devolverLibro();

            // Actualizar índices de estado
            mapaPorEstado.get("pendiente").remove(p);
            mapaPorEstado.computeIfAbsent("devuelto", k -> new ArrayList<>()).add(p);

            System.out.println("📗 Libro devuelto correctamente (ID " + id + ")");
        } else {
            System.out.println("⚠️ No se encontró un préstamo activo con ese ID.");
        }
    }

    // -------------------- VERIFICAR VENCIDOS --------------------
    public void actualizarVencidos() {
        LocalDate hoy = LocalDate.now();
        ArrayList<Prestamo> pendientes = mapaPorEstado.getOrDefault("pendiente", new ArrayList<>());

        for (Prestamo p : new ArrayList<>(pendientes)) {
            if (p.getFechaLimite().isBefore(hoy) && p.getEstado().equals("Pendiente")) {
                p.setEstado("Vencido");
                mapaPorEstado.get("pendiente").remove(p);
                mapaPorEstado.computeIfAbsent("vencido", k -> new ArrayList<>()).add(p);
            }
        }
    }

    // -------------------- VERIFICAR PRÉSTAMOS ACTIVOS --------------------
    public boolean tienePrestamosPendientes(String nombreCliente) {
        ArrayList<Prestamo> prestamosCliente = mapaPorCliente.get(nombreCliente.toLowerCase());
        
        if (prestamosCliente == null || prestamosCliente.isEmpty()) {
            return false;
        }
        
        // Verificar si tiene préstamos en estado Pendiente o Vencido
        for (Prestamo p : prestamosCliente) {
            String estado = p.getEstado();
            if (estado.equals("Pendiente") || estado.equals("Vencido")) {
                return true;
            }
        }
        
        return false;
    }

    // Obtener préstamos pendientes de un cliente
    public ArrayList<Prestamo> getPrestamosPendientes(String nombreCliente) {
        ArrayList<Prestamo> pendientes = new ArrayList<>();
        ArrayList<Prestamo> prestamosCliente = mapaPorCliente.get(nombreCliente.toLowerCase());
        
        if (prestamosCliente != null) {
            for (Prestamo p : prestamosCliente) {
                String estado = p.getEstado();
                if (estado.equals("Pendiente") || estado.equals("Vencido")) {
                    pendientes.add(p);
                }
            }
        }
        
        return pendientes;
    }

    // -------------------- LISTAR --------------------
    public ArrayList<Prestamo> getListaPrestamos() {
        return listaPrestamos;
    }

    // -------------------- BÚSQUEDAS HASHMAP --------------------
    public Prestamo buscarPorId(int id) {
        return mapaPorId.get(id);
    }

    public ArrayList<Prestamo> buscarPorCliente(String nombre) {
        return mapaPorCliente.getOrDefault(nombre.toLowerCase(), new ArrayList<>());
    }

    public ArrayList<Prestamo> buscarPorEstado(String estado) {
        return mapaPorEstado.getOrDefault(estado.toLowerCase(), new ArrayList<>());
    }
    
    public ArrayList<Prestamo> getPrestamos() {
        return listaPrestamos;
    }
}