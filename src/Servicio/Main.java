package Servicio;

import Modelo.Libro;
import Modelo.Cliente;
import Vista.VistaEmpleado;
import java.util.ArrayList;
import java.time.LocalDate;

/**
 *
 * @author Abel Rodrigo
 */
    
public class Main {
    public static void main(String[] args) {
        Biblioteca b = Biblioteca.getInstancia();
        Prestamos prestamos = Prestamos.getInstancia();
        GestorClientes gestorClientes = GestorClientes.getInstancia();

        // ==================== REGISTRAR CLIENTES DE PRUEBA ====================
        
        Cliente cliente1 = new Cliente("Juan", "Pérez García", "12345678", 
                                      "Av. Arequipa 1234, Lima", "987654321");
        gestorClientes.registrarCliente(cliente1);
        
        Cliente cliente2 = new Cliente("Ana María", "López Torres", "23456789", 
                                      "Jr. Puno 567, Lima", "912345678");
        gestorClientes.registrarCliente(cliente2);
        
        Cliente cliente3 = new Cliente("Carlos", "Ramos Silva", "34567890", 
                                      "Av. Brasil 890, Lima", "998765432");
        gestorClientes.registrarCliente(cliente3);
        
        Cliente cliente4 = new Cliente("María", "Torres Díaz", "45678901", 
                                      "Calle Luna 345, Lima", "923456789");
        gestorClientes.registrarCliente(cliente4);
        
        Cliente cliente5 = new Cliente("Luis", "García Vargas", "56789012", 
                                      "Av. Colonial 678, Lima", "945678901");
        gestorClientes.registrarCliente(cliente5);
        
        Cliente cliente6 = new Cliente("Sofía", "Vargas Ruiz", "67890123", 
                                      "Jr. Junín 234, Lima", "967890123");
        gestorClientes.registrarCliente(cliente6);

        System.out.println("✅ " + gestorClientes.getClientes().size() + " clientes registrados");

        // ==================== REGISTRAR PRÉSTAMOS ====================
        
        // Juan Pérez - 2 préstamos (1 vencido, 1 pendiente)
        prestamos.registrarPrestamo("Juan Pérez García", "Cien Años de Soledad");
        prestamos.registrarPrestamo("Juan Pérez García", "Don Quijote de la Mancha");
        
        // Ana López - 1 préstamo pendiente
        prestamos.registrarPrestamo("Ana María López Torres", "Harry Potter y la piedra filosofal");
        
        // Carlos Ramos - 3 préstamos (2 pendientes, simularemos 1 vencido)
        prestamos.registrarPrestamo("Carlos Ramos Silva", "1984");
        prestamos.registrarPrestamo("Carlos Ramos Silva", "Dune");
        prestamos.registrarPrestamo("Carlos Ramos Silva", "Breve historia del tiempo");
        
        // María Torres - 1 préstamo pendiente
        prestamos.registrarPrestamo("María Torres Díaz", "Romeo y Julieta");
        
        // Luis García - 2 préstamos
        prestamos.registrarPrestamo("Luis García Vargas", "Sapiens: De animales a dioses");
        prestamos.registrarPrestamo("Luis García Vargas", "It");
        
        // Sofía Vargas - 1 préstamo
        prestamos.registrarPrestamo("Sofía Vargas Ruiz", "El Principito");

        System.out.println("✅ " + prestamos.getListaPrestamos().size() + " préstamos registrados");

        // ==================== SIMULAR PRÉSTAMOS VENCIDOS ====================
        // Modificar manualmente algunos préstamos para que aparezcan como vencidos
        
        // Obtener préstamo de Juan Pérez y hacerlo vencido (cambiar fecha límite al pasado)
        for (Modelo.Prestamo p : prestamos.getListaPrestamos()) {
            if (p.getCliente().equals("Juan Pérez García") && 
                p.getLibro().equals("Don Quijote de la Mancha")) {
                // Usar reflexión para cambiar la fecha límite (simulación)
                try {
                    java.lang.reflect.Field campoFechaLimite = p.getClass().getDeclaredField("fechaLimite");
                    campoFechaLimite.setAccessible(true);
                    campoFechaLimite.set(p, LocalDate.now().minusDays(5)); // 5 días vencido
                    System.out.println("⚠️ Préstamo vencido simulado: " + p.getLibro());
                } catch (Exception e) {
                    System.out.println("No se pudo simular préstamo vencido");
                }
            }
            
            // Hacer vencido un préstamo de Carlos Ramos
            if (p.getCliente().equals("Carlos Ramos Silva") && 
                p.getLibro().equals("Breve historia del tiempo")) {
                try {
                    java.lang.reflect.Field campoFechaLimite = p.getClass().getDeclaredField("fechaLimite");
                    campoFechaLimite.setAccessible(true);
                    campoFechaLimite.set(p, LocalDate.now().minusDays(10)); // 10 días vencido
                    System.out.println("⚠️ Préstamo vencido simulado: " + p.getLibro());
                } catch (Exception e) {
                    System.out.println("No se pudo simular préstamo vencido");
                }
            }
        }

        // Actualizar estados de préstamos vencidos
        prestamos.actualizarVencidos();

        System.out.println("\n========== DATOS DE PRUEBA PARA DEVOLUCIONES ==========");
        System.out.println("\n📋 CLIENTES REGISTRADOS:");
        System.out.println("1. Juan Pérez García - DNI: 12345678 (2 préstamos: 1 vencido, 1 pendiente)");
        System.out.println("2. Ana María López Torres - DNI: 23456789 (1 préstamo pendiente)");
        System.out.println("3. Carlos Ramos Silva - DNI: 34567890 (3 préstamos: 1 vencido, 2 pendientes)");
        System.out.println("4. María Torres Díaz - DNI: 45678901 (1 préstamo pendiente)");
        System.out.println("5. Luis García Vargas - DNI: 56789012 (2 préstamos pendientes)");
        System.out.println("6. Sofía Vargas Ruiz - DNI: 67890123 (1 préstamo pendiente)");
        
        System.out.println("\n💡 PRUEBA LA FUNCIONALIDAD:");
        System.out.println("- Ve a la pestaña 'Devoluciones'");
        System.out.println("- Ingresa un DNI (ej: 12345678)");
        System.out.println("- Presiona 'Buscar'");
        System.out.println("- Selecciona un préstamo y prueba 'Registrar devolución' o 'Registrar multa'");
        System.out.println("======================================================\n");

        // Mostramos la interfaz
        new VistaEmpleado().setVisible(true);
    }
}