package Servicio;

import Modelo.Multa;
import java.util.ArrayList;
import java.util.HashMap;

public class GestorMultas {
    private static GestorMultas instancia;
    private ArrayList<Multa> listaMultas;
    private HashMap<Integer, Multa> multasPorId;
    private HashMap<String, ArrayList<Multa>> multasPorDni;
    private int contadorId;

    private GestorMultas() {
        listaMultas = new ArrayList<>();
        multasPorId = new HashMap<>();
        multasPorDni = new HashMap<>();
        contadorId = 1;
    }

    public static GestorMultas getInstancia() {
        if (instancia == null) {
            instancia = new GestorMultas();
        }
        return instancia;
    }

    // Registrar nueva multa
    public Multa registrarMulta(String dniCliente, String nombreCliente, int idPrestamo,
                                String libroTitulo, String motivo, double monto) {
        Multa nueva = new Multa(contadorId++, dniCliente, nombreCliente, 
                               idPrestamo, libroTitulo, motivo, monto);
        
        listaMultas.add(nueva);
        multasPorId.put(nueva.getId(), nueva);
        
        // Índice por DNI
        multasPorDni.computeIfAbsent(dniCliente.toLowerCase(), k -> new ArrayList<>()).add(nueva);
        
        System.out.println("✅ Multa registrada con ID: " + nueva.getId());
        return nueva;
    }

    // Marcar multa como pagada
    public boolean pagarMulta(int id) {
        Multa multa = multasPorId.get(id);
        if (multa != null && !multa.isPagada()) {
            multa.setPagada(true);
            System.out.println("✅ Multa " + id + " marcada como pagada");
            return true;
        }
        return false;
    }

    // Buscar multa por ID
    public Multa buscarPorId(int id) {
        return multasPorId.get(id);
    }

    // Buscar multas por DNI de cliente
    public ArrayList<Multa> buscarPorDni(String dni) {
        return multasPorDni.getOrDefault(dni.toLowerCase(), new ArrayList<>());
    }

    // Obtener multas pendientes de un cliente
    public ArrayList<Multa> getMultasPendientes(String dni) {
        ArrayList<Multa> pendientes = new ArrayList<>();
        ArrayList<Multa> multasCliente = buscarPorDni(dni);
        
        for (Multa m : multasCliente) {
            if (!m.isPagada()) {
                pendientes.add(m);
            }
        }
        
        return pendientes;
    }

    // Obtener total de multas pendientes de un cliente
    public double getTotalMultasPendientes(String dni) {
        double total = 0;
        for (Multa m : getMultasPendientes(dni)) {
            total += m.getMonto();
        }
        return total;
    }

    // Obtener todas las multas
    public ArrayList<Multa> getListaMultas() {
        return listaMultas;
    }

    // Verificar si un cliente tiene multas pendientes
    public boolean tieneMultasPendientes(String dni) {
        return !getMultasPendientes(dni).isEmpty();
    }
}