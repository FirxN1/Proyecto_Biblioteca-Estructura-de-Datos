package Servicio;

import Modelo.Cliente;
import java.util.ArrayList;
import java.util.HashMap;

public class GestorClientes {
    private static GestorClientes instancia;
    private HashMap<String, Cliente> clientesPorDni; // DNI -> Cliente
    private ArrayList<Cliente> listaClientes;

    private GestorClientes() {
        clientesPorDni = new HashMap<>();
        listaClientes = new ArrayList<>();
    }

    public static GestorClientes getInstancia() {
        if (instancia == null) {
            instancia = new GestorClientes();
        }
        return instancia;
    }

    // Registrar nuevo cliente
    public boolean registrarCliente(Cliente cliente) {
        if (clientesPorDni.containsKey(cliente.getDni())) {
            return false; // Ya existe
        }
        clientesPorDni.put(cliente.getDni(), cliente);
        listaClientes.add(cliente);
        return true;
    }

    // Buscar cliente por DNI
    public Cliente buscarPorDni(String dni) {
        return clientesPorDni.get(dni);
    }

    // Verificar si existe cliente
    public boolean existeCliente(String dni) {
        return clientesPorDni.containsKey(dni);
    }

    // Obtener todos los clientes
    public ArrayList<Cliente> getClientes() {
        return listaClientes;
    }

    // Actualizar cliente
    public boolean actualizarCliente(String dni, Cliente clienteActualizado) {
        if (!clientesPorDni.containsKey(dni)) {
            return false;
        }
        clientesPorDni.put(dni, clienteActualizado);
        
        // Actualizar en la lista
        for (int i = 0; i < listaClientes.size(); i++) {
            if (listaClientes.get(i).getDni().equals(dni)) {
                listaClientes.set(i, clienteActualizado);
                break;
            }
        }
        return true;
    }

    // Eliminar cliente
    public boolean eliminarCliente(String dni) {
        Cliente cliente = clientesPorDni.remove(dni);
        if (cliente != null) {
            listaClientes.remove(cliente);
            return true;
        }
        return false;
    }

    // Validar formato DNI peruano (8 dígitos)
    public static boolean validarDNI(String dni) {
        if (dni == null || dni.trim().isEmpty()) {
            return false;
        }
        dni = dni.trim();
        
        // DNI peruano debe tener exactamente 8 dígitos
        if (dni.length() != 8) {
            return false;
        }
        
        // Verificar que todos sean dígitos
        for (char c : dni.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        
        return true;
    }

    // Validar teléfono (9 dígitos en Perú)
    public static boolean validarTelefono(String telefono) {
        if (telefono == null || telefono.trim().isEmpty()) {
            return false;
        }
        telefono = telefono.trim();
        
        // Teléfono peruano debe tener 9 dígitos
        if (telefono.length() != 9) {
            return false;
        }
        
        // Verificar que todos sean dígitos
        for (char c : telefono.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        
        return true;
    }
}