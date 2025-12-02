
package Vista;

import Modelo.Libro;
import Servicio.Biblioteca;
import Servicio.Prestamos;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;
import Servicio.GestorClientes;
import java.time.LocalDate;
import javax.swing.DefaultListModel;
import Modelo.Cliente;
import Modelo.Prestamo;
import Modelo.Multa;
import Servicio.GestorMultas;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author JUAN
 */
public class VistaEmpleado extends javax.swing.JFrame {
    
    private GestorClientes gestorClientes = GestorClientes.getInstancia();
    private DefaultListModel<String> modeloListaLibros = new DefaultListModel<>();
    private ArrayList<Libro> librosSeleccionados = new ArrayList<>();

    Biblioteca biblioteca = Biblioteca.getInstancia();
    DefaultTableModel modeloTabla = new DefaultTableModel(
            new Object[]{"Título", "Autor", "Editorial", "ISBN", "Año", "Páginas", "Géneros", "Disponible"}, 0
    );

    Prestamos gestorPrestamos = Prestamos.getInstancia();
    DefaultTableModel modeloPrestamos = new DefaultTableModel(
            new Object[]{"ID", "Cliente", "Libro", "Fecha Préstamo", "Fecha Límite", "Fecha Devolución", "Estado"}, 0
    );

    private void listar(DefaultTableModel modelo) {
        ArrayList<Libro> libros = biblioteca.getLibros();
        for (Libro l : libros) {
            modelo.addRow(new Object[]{
                l.getTitulo(),
                l.getAutor(),
                l.getEditorial(),
                l.getIsbn(),
                l.getAñoPublicacion(),
                l.getNumeroPaginas(),
                String.join(", ", l.getGeneros()),
                l.isDisponible() ? "Sí" : "No"
            });
        }
    }

    private void listarPrestamos(DefaultTableModel modelo) {
        modelo.setRowCount(0); // limpiar tabla
        Prestamos gestor = Prestamos.getInstancia();

        for (Prestamo p : gestor.getListaPrestamos()) {
            modelo.addRow(new Object[]{
                p.getId(),
                p.getCliente(),
                p.getLibro(),
                p.getFechaPrestamo(),
                p.getFechaLimite(),
                p.getFechaDevolucion() != null ? p.getFechaDevolucion() : "-",
                p.getEstado()
            });
        }
    }
    
    // Método auxiliar para actualizar la tabla de libros
    private void actualizarTablaLibros() {
        DefaultTableModel modelo = (DefaultTableModel) tabla_libros.getModel();
        modelo.setRowCount(0); // Limpiar tabla
        listar(modelo); // Volver a cargar datos
    }

    public VistaEmpleado() {
        initComponents();
        
        // Configurar tabla de libros
        listar((DefaultTableModel) tabla_libros.getModel());
        tabla_libros.getColumnModel().getColumn(0).setPreferredWidth(200);
        tabla_libros.getColumnModel().getColumn(1).setPreferredWidth(150);
        tabla_libros.getColumnModel().getColumn(2).setPreferredWidth(120);
        tabla_libros.getColumnModel().getColumn(3).setPreferredWidth(100);
        tabla_libros.getColumnModel().getColumn(4).setPreferredWidth(50);
        tabla_libros.getColumnModel().getColumn(5).setPreferredWidth(70);
        tabla_libros.getColumnModel().getColumn(6).setPreferredWidth(150);
        tabla_libros.getColumnModel().getColumn(7).setPreferredWidth(80);
        tabla_libros.getColumnModel().getColumn(4).setResizable(false);

        // Configurar tabla de préstamos
        DefaultTableModel modelo = new DefaultTableModel(
                new Object[]{"ID", "Cliente", "Libro", "Fecha Préstamo", "Fecha Límite", "Fecha Devolución", "Estado"}, 0
        );
        jTablePrestamos.setModel(modelo);
        listarPrestamos(modelo);

        // ========== CONFIGURAR BOTONES DE LIBROS ==========
        
        // Botón Agregar
        btn_agregar_libro.addActionListener(e -> {
            AgregarLibro dialogo = new AgregarLibro(this, true);
            dialogo.setVisible(true);
            
            // Si se agregó un libro, actualizar la tabla
            if (dialogo.isLibroAgregado()) {
                actualizarTablaLibros();
            }
        });
        
        // Botón Modificar
        btn_modificar_libro.addActionListener(e -> {
            int filaSeleccionada = tabla_libros.getSelectedRow();
            
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(this,
                    "Por favor seleccione un libro de la tabla",
                    "Ningún libro seleccionado",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Obtener el ISBN del libro seleccionado
            String isbn = tabla_libros.getValueAt(filaSeleccionada, 3).toString();
            
            ModificarLibro dialogo = new ModificarLibro(this, true, isbn);
            dialogo.setVisible(true);
            
            // Si se modificó el libro, actualizar la tabla
            if (dialogo.isLibroModificado()) {
                actualizarTablaLibros();
            }
        });
        
        // Botón Eliminar
        btn_eliminar_libro.addActionListener(e -> {
            int filaSeleccionada = tabla_libros.getSelectedRow();
            
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(this,
                    "Por favor seleccione un libro de la tabla",
                    "Ningún libro seleccionado",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Obtener datos del libro seleccionado
            String titulo = tabla_libros.getValueAt(filaSeleccionada, 0).toString();
            String isbn = tabla_libros.getValueAt(filaSeleccionada, 3).toString();
            String disponible = tabla_libros.getValueAt(filaSeleccionada, 7).toString();
            
            // Verificar si el libro está disponible
            if (disponible.equals("No")) {
                int respuesta = JOptionPane.showConfirmDialog(this,
                    "Este libro NO está disponible (puede estar prestado).\n" +
                    "¿Está seguro que desea eliminarlo de todas formas?\n\n" +
                    "Libro: " + titulo,
                    "Libro no disponible",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
                
                if (respuesta != JOptionPane.YES_OPTION) {
                    return;
                }
            } else {
                // Confirmación normal
                int respuesta = JOptionPane.showConfirmDialog(this,
                    "¿Está seguro que desea eliminar este libro?\n\n" +
                    "Libro: " + titulo + "\n" +
                    "ISBN: " + isbn + "\n\n" +
                    "Esta acción no se puede deshacer.",
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
                
                if (respuesta != JOptionPane.YES_OPTION) {
                    return;
                }
            }
            
            // Eliminar el libro
            boolean eliminado = biblioteca.eliminarLibro(isbn);
            
            if (eliminado) {
                JOptionPane.showMessageDialog(this,
                    "Libro eliminado exitosamente",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
                
                actualizarTablaLibros();
            } else {
                JOptionPane.showMessageDialog(this,
                    "No se pudo eliminar el libro",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
        
        );
        
        
        // Configurar modelo de la lista de libros seleccionados
lista_librosseleccionados.setModel(modeloListaLibros);

// Configurar modelo de la tabla de búsqueda de libros
DefaultTableModel modeloTablaLibro = new DefaultTableModel(
    new Object[]{"Título", "Autor", "Editorial", "ISBN", "Año", "Páginas", "Géneros", "Disponible"}, 0
);
tabla_libro.setModel(modeloTablaLibro);

// Botón Buscar Libro
jButtonBuscar.addActionListener(e -> {
    String criterio = jTextFieldBuscarLibro.getText().trim();
    
    if (criterio.isEmpty()) {
        JOptionPane.showMessageDialog(this,
            "Por favor ingrese un criterio de búsqueda",
            "Campo vacío",
            JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    // Buscar por título, autor o ISBN
    ArrayList<Libro> resultados = new ArrayList<>();
    resultados.addAll(biblioteca.buscarPorTitulo(criterio));
    resultados.addAll(biblioteca.buscarPorAutor(criterio));
    resultados.addAll(biblioteca.buscarPorISBN(criterio));
    
    // Eliminar duplicados
    ArrayList<Libro> resultadosUnicos = new ArrayList<>();
    for (Libro l : resultados) {
        boolean existe = false;
        for (Libro r : resultadosUnicos) {
            if (r.getIsbn().equals(l.getIsbn())) {
                existe = true;
                break;
            }
        }
        if (!existe) {
            resultadosUnicos.add(l);
        }
    }
    
    // Mostrar en la tabla
    DefaultTableModel modeloBusqueda = (DefaultTableModel) tabla_libro.getModel();
    modeloBusqueda.setRowCount(0);
    
    for (Libro l : resultadosUnicos) {
        modeloBusqueda.addRow(new Object[]{
            l.getTitulo(),
            l.getAutor(),
            l.getEditorial(),
            l.getIsbn(),
            l.getAñoPublicacion(),
            l.getNumeroPaginas(),
            String.join(", ", l.getGeneros()),
            l.isDisponible() ? "Sí" : "No"
        });
    }
    
    if (resultadosUnicos.isEmpty()) {
        JOptionPane.showMessageDialog(this,
            "No se encontraron libros con ese criterio",
            "Sin resultados",
            JOptionPane.INFORMATION_MESSAGE);
    }
});

// Botón Seleccionar (agregar a la lista)
jButtonSeleccionarlibro.addActionListener(e -> {
    int filaSeleccionada = tabla_libro.getSelectedRow();
    
    if (filaSeleccionada == -1) {
        JOptionPane.showMessageDialog(this,
            "Por favor seleccione un libro de la tabla",
            "Ningún libro seleccionado",
            JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    String titulo = tabla_libro.getValueAt(filaSeleccionada, 0).toString();
    String isbn = tabla_libro.getValueAt(filaSeleccionada, 3).toString();
    String disponible = tabla_libro.getValueAt(filaSeleccionada, 7).toString();
    
    // Verificar que el libro esté disponible
    if (disponible.equals("No")) {
        JOptionPane.showMessageDialog(this,
            "Este libro NO está disponible para préstamo",
            "Libro no disponible",
            JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    // Verificar que no esté ya en la lista
    for (Libro l : librosSeleccionados) {
        if (l.getIsbn().equals(isbn)) {
            JOptionPane.showMessageDialog(this,
                "Este libro ya está en la lista de préstamo",
                "Libro duplicado",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
    }
    
    // Buscar el libro completo
    Libro libroCompleto = null;
    for (Libro l : biblioteca.getLibros()) {
        if (l.getIsbn().equals(isbn)) {
            libroCompleto = l;
            break;
        }
    }
    
    if (libroCompleto != null) {
        librosSeleccionados.add(libroCompleto);
        modeloListaLibros.addElement(titulo + " (ISBN: " + isbn + ")");
        
        JOptionPane.showMessageDialog(this,
            "Libro agregado a la lista de préstamo",
            "Éxito",
            JOptionPane.INFORMATION_MESSAGE);
    }
});

// Botón Realizar Préstamo
jButtonRealizarPrestamo.addActionListener(e -> {
    // 1. Validar campos del cliente
    String nombres = jTextFieldNombresPrestamo.getText().trim();
    String apellidos = jTextFieldApellidosPrestamo.getText().trim();
    String dni = jTextFieldDNIPrestamo.getText().trim();
    String direccion = jTextFieldDireccionPrestamo.getText().trim();
    String telefono = jTextFieldTelefonoPrestamo.getText().trim();
    
    if (nombres.isEmpty() || apellidos.isEmpty() || dni.isEmpty() || 
        direccion.isEmpty() || telefono.isEmpty()) {
        JOptionPane.showMessageDialog(this,
            "Por favor complete todos los datos del cliente",
            "Datos incompletos",
            JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    // 2. Validar formato DNI
    if (!GestorClientes.validarDNI(dni)) {
        JOptionPane.showMessageDialog(this,
            "El DNI debe tener exactamente 8 dígitos numéricos",
            "DNI inválido",
            JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    // 3. Validar formato teléfono
    if (!GestorClientes.validarTelefono(telefono)) {
        JOptionPane.showMessageDialog(this,
            "El teléfono debe tener exactamente 9 dígitos numéricos",
            "Teléfono inválido",
            JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    // 4. Validar que haya al menos un libro seleccionado
    if (librosSeleccionados.isEmpty()) {
        JOptionPane.showMessageDialog(this,
            "Por favor seleccione al menos un libro para prestar",
            "Sin libros seleccionados",
            JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    String nombreCompleto = nombres + " " + apellidos;
    
    // 5. Verificar si el cliente tiene préstamos pendientes
    if (gestorPrestamos.tienePrestamosPendientes(nombreCompleto)) {
        ArrayList<Prestamo> pendientes = gestorPrestamos.getPrestamosPendientes(nombreCompleto);
        
        StringBuilder mensaje = new StringBuilder();
        mensaje.append("El cliente ").append(nombreCompleto)
               .append(" tiene ").append(pendientes.size())
               .append(" préstamo(s) activo(s):\n\n");
        
        for (Prestamo p : pendientes) {
            mensaje.append("- ").append(p.getLibro())
                   .append(" (Estado: ").append(p.getEstado()).append(")\n");
        }
        
        mensaje.append("\n¿Desea continuar con el nuevo préstamo?");
        
        int respuesta = JOptionPane.showConfirmDialog(this,
            mensaje.toString(),
            "Cliente con préstamos activos",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (respuesta != JOptionPane.YES_OPTION) {
            return;
        }
    }
    
    // 6. Registrar o actualizar cliente
    Cliente cliente = gestorClientes.buscarPorDni(dni);
    if (cliente == null) {
        // Cliente nuevo
        cliente = new Cliente(nombres, apellidos, dni, direccion, telefono);
        gestorClientes.registrarCliente(cliente);
    } else {
        // Cliente existente - actualizar datos
        cliente.setNombres(nombres);
        cliente.setApellidos(apellidos);
        cliente.setDireccion(direccion);
        cliente.setTelefono(telefono);
        gestorClientes.actualizarCliente(dni, cliente);
    }
    
    // 7. Registrar préstamos y cambiar disponibilidad de libros
    int prestamosRealizados = 0;
    StringBuilder resumen = new StringBuilder();
    resumen.append("Préstamos realizados exitosamente:\n\n");
    
    for (Libro libro : librosSeleccionados) {
        if (biblioteca.prestarLibro(libro.getIsbn())) {
            gestorPrestamos.registrarPrestamo(nombreCompleto, libro.getTitulo());
            resumen.append("✓ ").append(libro.getTitulo()).append("\n");
            prestamosRealizados++;
        }
    }
    
    if (prestamosRealizados > 0) {
        resumen.append("\nTotal: ").append(prestamosRealizados).append(" libro(s)");
        resumen.append("\nFecha de devolución: ").append(LocalDate.now().plusDays(15));
        
        JOptionPane.showMessageDialog(this,
            resumen.toString(),
            "Préstamos registrados",
            JOptionPane.INFORMATION_MESSAGE);
        
        // 8. Limpiar campos y listas
        jTextFieldNombresPrestamo.setText("");
        jTextFieldApellidosPrestamo.setText("");
        jTextFieldDNIPrestamo.setText("");
        jTextFieldDireccionPrestamo.setText("");
        jTextFieldTelefonoPrestamo.setText("");
        jTextFieldBuscarLibro.setText("");
        
        modeloListaLibros.clear();
        librosSeleccionados.clear();
        
        DefaultTableModel modeloTablaBusqueda = (DefaultTableModel) tabla_libro.getModel();
        modeloTablaBusqueda.setRowCount(0);
        
        // Actualizar tabla principal de libros
        actualizarTablaLibros();
        
        // Actualizar tabla de préstamos
        listarPrestamos((DefaultTableModel) jTablePrestamos.getModel());
    } else {
        JOptionPane.showMessageDialog(this,
            "No se pudo realizar ningún préstamo",
            "Error",
            JOptionPane.ERROR_MESSAGE);
    }
});
        
        






// Botón Buscar Cliente (en devoluciones)
btnBuscarCliente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent    e) {
                String dni = txtDNI.getText().trim();
                // Validar que el campo no esté vacío
                if (dni.isEmpty()) {
                    JOptionPane.showMessageDialog(VistaEmpleado.this, "Por favor ingrese un DNI", "Campo vacío", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                // Validar formato DNI
                if (!GestorClientes.validarDNI(dni)) {
                    JOptionPane.showMessageDialog(VistaEmpleado.this, "El DNI debe tener exactamente 8 dígitos numéricos", "DNI inválido", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                // Buscar cliente
                Cliente cliente = gestorClientes.buscarPorDni(dni);
                if (cliente == null) {
                    JOptionPane.showMessageDialog(VistaEmpleado.this, "No se encontró ningún cliente con el DNI: " + dni, "Cliente no encontrado", JOptionPane.WARNING_MESSAGE);
                    // Limpiar campos
                    txtNombres.setText("");
                    txtApellidos.setText("");
                    // Limpiar tabla
                    DefaultTableModel modelo = (DefaultTableModel) tablePrestamos.getModel();
                    modelo.setRowCount(0);
                    return;
                }
                // Cargar datos del cliente
                txtNombres.setText(cliente.getNombres());
                txtApellidos.setText(cliente.getApellidos());
                // Buscar préstamos del cliente
                String nombreCompleto = cliente.getNombreCompleto();
                ArrayList<Prestamo> prestamosCliente = gestorPrestamos.buscarPorCliente(nombreCompleto);
                // Filtrar solo préstamos activos (Pendiente o Vencido)
                ArrayList<Prestamo> prestamosActivos = new ArrayList<>();
                for (Prestamo p : prestamosCliente) {
                    String estado = p.getEstado();
                    if (estado.equals("Pendiente") || estado.equals("Vencido")) {
                        prestamosActivos.add(p);
                    }
                }           // Cargar préstamos en la tabla
                DefaultTableModel modelo = (DefaultTableModel) tablePrestamos.getModel();
                modelo.setRowCount(0);
                for (Prestamo p : prestamosActivos) {
                    modelo.addRow(new Object[]{
                        p.getId(),
                        p.getLibro(),
                        p.getFechaPrestamo(),
                        p.getFechaLimite(),
                        p.getEstado()
                    });
                }           if (prestamosActivos.isEmpty()) {
                    JOptionPane.showMessageDialog(VistaEmpleado.this, "El cliente " + nombreCompleto + " no tiene préstamos activos", "Sin préstamos activos", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(VistaEmpleado.this, "Se encontraron " + prestamosActivos.size() + " préstamo(s) activo(s)", "Préstamos encontrados", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

// Botón Registrar Devolución
btnRegistrarDev.addActionListener(e -> {
    // Validar que haya un cliente cargado
    String dni = txtDNI.getText().trim();
    String nombres = txtNombres.getText().trim();
    String apellidos = txtApellidos.getText().trim();
    
    if (dni.isEmpty() || nombres.isEmpty() || apellidos.isEmpty()) {
        JOptionPane.showMessageDialog(this,
            "Por favor busque un cliente primero",
            "Sin cliente seleccionado",
            JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    // Validar que haya una fila seleccionada en la tabla
    int filaSeleccionada = tablePrestamos.getSelectedRow();
    
    if (filaSeleccionada == -1) {
        JOptionPane.showMessageDialog(this,
            "Por favor seleccione un préstamo de la tabla",
            "Sin préstamo seleccionado",
            JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    // Obtener datos del préstamo seleccionado
    int idPrestamo = (int) tablePrestamos.getValueAt(filaSeleccionada, 0);
    String tituloLibro = tablePrestamos.getValueAt(filaSeleccionada, 1).toString();
    String estado = tablePrestamos.getValueAt(filaSeleccionada, 4).toString();
    
    // Buscar el préstamo completo
    Prestamo prestamo = gestorPrestamos.buscarPorId(idPrestamo);
    
    if (prestamo == null) {
        JOptionPane.showMessageDialog(this,
            "Error: No se encontró el préstamo",
            "Error",
            JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    // Verificar que el préstamo no esté ya devuelto
    if (prestamo.getEstado().equals("Devuelto")) {
        JOptionPane.showMessageDialog(this,
            "Este préstamo ya fue devuelto anteriormente",
            "Préstamo ya devuelto",
            JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    // Confirmar la devolución
    int respuesta = JOptionPane.showConfirmDialog(this,
        "¿Confirmar devolución del libro?\n\n" +
        "Libro: " + tituloLibro + "\n" +
        "Cliente: " + nombres + " " + apellidos + "\n" +
        "Estado actual: " + estado,
        "Confirmar devolución",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.QUESTION_MESSAGE);
    
    if (respuesta == JOptionPane.YES_OPTION) {
        // Registrar la devolución
        gestorPrestamos.devolverPrestamo(idPrestamo);
        
        // Devolver el libro a la biblioteca
        biblioteca.devolverLibro(biblioteca.getLibros().stream()
            .filter(l -> l.getTitulo().equals(tituloLibro))
            .findFirst()
            .map(Modelo.Libro::getIsbn)
            .orElse(""));
        
        JOptionPane.showMessageDialog(this,
            "Devolución registrada exitosamente\n" +
            "El libro ya está disponible en el sistema",
            "Devolución exitosa",
            JOptionPane.INFORMATION_MESSAGE);
        
        // Actualizar la tabla de préstamos del cliente
        btnBuscarCliente.doClick();
        
        // Actualizar tabla de libros
        actualizarTablaLibros();
        
        // Actualizar tabla de administrar préstamos
        listarPrestamos((DefaultTableModel) jTablePrestamos.getModel());
    }
});

// Botón Registrar Multa
btnRegistrarMulta.addActionListener(e -> {
    // Validar que haya un cliente cargado
    String dni = txtDNI.getText().trim();
    String nombres = txtNombres.getText().trim();
    String apellidos = txtApellidos.getText().trim();
    
    if (dni.isEmpty() || nombres.isEmpty() || apellidos.isEmpty()) {
        JOptionPane.showMessageDialog(this,
            "Por favor busque un cliente primero",
            "Sin cliente seleccionado",
            JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    // Validar que haya una fila seleccionada en la tabla
    int filaSeleccionada = tablePrestamos.getSelectedRow();
    
    if (filaSeleccionada == -1) {
        JOptionPane.showMessageDialog(this,
            "Por favor seleccione un préstamo de la tabla",
            "Sin préstamo seleccionado",
            JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    // Obtener datos del préstamo seleccionado
    int idPrestamo = (int) tablePrestamos.getValueAt(filaSeleccionada, 0);
    
    // Buscar el préstamo completo
    Prestamo prestamo = gestorPrestamos.buscarPorId(idPrestamo);
    
    if (prestamo == null) {
        JOptionPane.showMessageDialog(this,
            "Error: No se encontró el préstamo",
            "Error",
            JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    // Verificar que el préstamo no esté ya devuelto
    if (prestamo.getEstado().equals("Devuelto")) {
        JOptionPane.showMessageDialog(this,
            "No se puede multar un préstamo ya devuelto",
            "Préstamo devuelto",
            JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    // Abrir diálogo de registro de multa
    String nombreCompleto = nombres + " " + apellidos;
    RegistrarMulta dialogo = new RegistrarMulta(this, prestamo, dni, nombreCompleto);
    dialogo.setVisible(true);
    
    // Si se registró la multa, mostrar confirmación
    if (dialogo.isMultaRegistrada()) {
        Modelo.Multa multa = dialogo.getMultaCreada();
        
        JOptionPane.showMessageDialog(this,
            "Multa registrada exitosamente\n\n" +
            "ID Multa: " + multa.getId() + "\n" +
            "Monto: S/. " + String.format("%.2f", multa.getMonto()) + "\n" +
            "Motivo: " + multa.getMotivo(),
            "Multa registrada",
            JOptionPane.INFORMATION_MESSAGE);
    }
});
        

        // Establecer layout vertical para que las notificaciones se apilen
        jPanelNotificaciones.setLayout(new javax.swing.BoxLayout(jPanelNotificaciones, javax.swing.BoxLayout.Y_AXIS));

        // Ejemplo de datos (pueden venir de la BD luego)
        String[] solicitudes = {
            "Juan Pérez solicita el libro: 'El Quijote'",
            "María Torres solicita el libro: 'La Odisea'",
            "Carlos Ruiz solicita el libro: 'Cien años de soledad'",
            "Ana Díaz solicita el libro: 'El Principito'"
        };

        // Crear y agregar paneles de notificación dinámicamente
        for (String solicitud : solicitudes) {
            JPanel card = new JPanel();
            card.setBorder(javax.swing.BorderFactory.createTitledBorder(
                    null,
                    "Solicitud de préstamo",
                    javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                    javax.swing.border.TitledBorder.DEFAULT_POSITION,
                    new java.awt.Font("Segoe UI", 1, 12)
            ));
            card.setLayout(new java.awt.BorderLayout());

            JLabel texto = new JLabel(solicitud);
            texto.setFont(new java.awt.Font("Segoe UI", 0, 14));
            texto.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 10, 5, 10));

            card.add(texto, java.awt.BorderLayout.CENTER);

            jPanelNotificaciones.add(card);
        }

        // Refrescar la interfaz
        jPanelNotificaciones.revalidate();
        jPanelNotificaciones.repaint();
    }
    
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        menuBar1 = new java.awt.MenuBar();
        menu1 = new java.awt.Menu();
        menu2 = new java.awt.Menu();
        Menu = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel4 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanelLibros = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabla_libros = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        txt_buscar_libro = new javax.swing.JTextField();
        btn_buscar_libro = new javax.swing.JButton();
        btn_agregar_libro = new javax.swing.JButton();
        btn_eliminar_libro = new javax.swing.JButton();
        btn_modificar_libro = new javax.swing.JButton();
        cbxOpcion = new javax.swing.JComboBox<>();
        jPanelPrestamos = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTextFieldNombresPrestamo = new javax.swing.JTextField();
        jTextFieldApellidosPrestamo = new javax.swing.JTextField();
        jTextFieldDNIPrestamo = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jTextFieldDireccionPrestamo = new javax.swing.JTextField();
        jTextFieldTelefonoPrestamo = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        lista_librosseleccionados = new javax.swing.JList<>();
        jButtonRealizarPrestamo = new javax.swing.JButton();
        jScrollPane7 = new javax.swing.JScrollPane();
        tabla_libro = new javax.swing.JTable();
        jButtonSeleccionarlibro = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jTextFieldBuscarLibro = new javax.swing.JTextField();
        jButtonBuscar = new javax.swing.JButton();
        jPanelDevoluciones = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txtDNI = new javax.swing.JTextField();
        txtNombres = new javax.swing.JTextField();
        txtApellidos = new javax.swing.JTextField();
        btnBuscarCliente = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tablePrestamos = new javax.swing.JTable();
        btnRegistrarDev = new javax.swing.JButton();
        btnRegistrarMulta = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        jPanelAdministrarPrestamos = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTablePrestamos = new javax.swing.JTable();
        txtBuscar = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        btnBuscarPrestamo = new javax.swing.JButton();
        cmbEstado = new javax.swing.JComboBox<>();
        jLabel16 = new javax.swing.JLabel();
        btnGenerarReporte = new javax.swing.JButton();
        btnEditarPrestamo = new javax.swing.JButton();
        btnEliminarPrestamo = new javax.swing.JButton();
        jPanelPeticiones = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jPanelNotificaciones = new javax.swing.JPanel();

        menu1.setLabel("File");
        menuBar1.add(menu1);

        menu2.setLabel("Edit");
        menuBar1.add(menu2);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        Menu.setBackground(new java.awt.Color(255, 102, 102));
        Menu.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        Menu.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jSeparator1.setPreferredSize(new java.awt.Dimension(50, 5));
        Menu.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 150, 120, 20));

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/libro.png"))); // NOI18N
        Menu.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 128, 128));

        getContentPane().add(Menu, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 160, 640));

        jPanelLibros.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tabla_libros.setModel(modeloTabla);
        jScrollPane1.setViewportView(tabla_libros);

        jPanelLibros.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 120, 810, 410));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jLabel1.setText("Libros");
        jPanelLibros.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 20, -1, -1));

        txt_buscar_libro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_buscar_libroActionPerformed(evt);
            }
        });
        jPanelLibros.add(txt_buscar_libro, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, 540, 30));

        btn_buscar_libro.setText("Buscar");
        btn_buscar_libro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_buscar_libroActionPerformed(evt);
            }
        });
        jPanelLibros.add(btn_buscar_libro, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 70, 130, 30));

        btn_agregar_libro.setText("Agregar...");
        jPanelLibros.add(btn_agregar_libro, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 550, 120, 30));

        btn_eliminar_libro.setText("Eliminar");
        jPanelLibros.add(btn_eliminar_libro, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 550, 120, 30));

        btn_modificar_libro.setText("Modificar...");
        jPanelLibros.add(btn_modificar_libro, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 550, 120, 30));

        cbxOpcion.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Título", "Autor", "Editorial", "Año", "ISBN", " " }));
        cbxOpcion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxOpcionActionPerformed(evt);
            }
        });
        jPanelLibros.add(cbxOpcion, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 70, 120, 30));

        jTabbedPane1.addTab("Libros", jPanelLibros);

        jPanelPrestamos.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jLabel2.setText("Préstamos");
        jPanelPrestamos.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, -1, -1));

        jPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel6.setText("DNI:");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 120, -1, -1));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setText("Apellidos:");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 70, -1, -1));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setText("Nombres:");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, -1, -1));

        jTextFieldNombresPrestamo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldNombresPrestamoActionPerformed(evt);
            }
        });
        jPanel1.add(jTextFieldNombresPrestamo, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 20, 220, -1));
        jPanel1.add(jTextFieldApellidosPrestamo, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 70, 220, -1));
        jPanel1.add(jTextFieldDNIPrestamo, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 120, 220, -1));

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel8.setText("Teléfono:");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 70, -1, -1));

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel7.setText("Dirección:");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 20, -1, -1));
        jPanel1.add(jTextFieldDireccionPrestamo, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 20, 220, -1));
        jPanel1.add(jTextFieldTelefonoPrestamo, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 70, 220, -1));

        jPanelPrestamos.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, 820, 160));

        jPanel6.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jScrollPane3.setViewportView(lista_librosseleccionados);

        jPanel6.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 30, 320, 180));

        jButtonRealizarPrestamo.setText("Realizar préstamo");
        jPanel6.add(jButtonRealizarPrestamo, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 230, 170, 30));

        tabla_libro.setModel(modeloTabla);
        jScrollPane7.setViewportView(tabla_libro);

        jPanel6.add(jScrollPane7, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 400, 200));

        jButtonSeleccionarlibro.setText("Seleccionar");
        jPanel6.add(jButtonSeleccionarlibro, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 240, -1, 30));

        jPanelPrestamos.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 310, 820, 280));

        jLabel9.setText("Buscar libro:");
        jPanelPrestamos.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 290, -1, -1));
        jPanelPrestamos.add(jTextFieldBuscarLibro, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 280, 190, 30));

        jButtonBuscar.setText("Buscar");
        jButtonBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBuscarActionPerformed(evt);
            }
        });
        jPanelPrestamos.add(jButtonBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 280, 90, 30));

        jTabbedPane1.addTab("Préstamos", jPanelPrestamos);

        jPanelDevoluciones.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jLabel10.setText("Devoluciones");
        jPanelDevoluciones.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));

        jPanel8.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel11.setText("DNI:");
        jPanel8.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, -1, -1));

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel12.setText("Apellidos:");
        jPanel8.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 90, -1, -1));

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel13.setText("Nombres:");
        jPanel8.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 90, -1, -1));

        txtDNI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDNIActionPerformed(evt);
            }
        });
        jPanel8.add(txtDNI, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 40, 220, -1));

        txtNombres.setEditable(false);
        jPanel8.add(txtNombres, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 90, 220, -1));

        txtApellidos.setEditable(false);
        jPanel8.add(txtApellidos, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 90, 220, -1));

        btnBuscarCliente.setText("Buscar");
        jPanel8.add(btnBuscarCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 40, -1, -1));

        jPanelDevoluciones.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, 820, 140));

        jPanel7.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel7.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tablePrestamos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "id_prestamo", "Título de libro", "Fecha préstamo", "Fecha límite de devolución", "Estado"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane5.setViewportView(tablePrestamos);

        jPanel7.add(jScrollPane5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 580, 270));

        btnRegistrarDev.setBackground(new java.awt.Color(153, 153, 255));
        btnRegistrarDev.setText("Registrar devolución");
        jPanel7.add(btnRegistrarDev, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 200, 140, 30));

        btnRegistrarMulta.setBackground(new java.awt.Color(255, 102, 102));
        btnRegistrarMulta.setText("Registrar multa");
        btnRegistrarMulta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrarMultaActionPerformed(evt);
            }
        });
        jPanel7.add(btnRegistrarMulta, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 270, 140, 30));

        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/devolucion_libro.png"))); // NOI18N
        jLabel14.setText("jLabel14");
        jPanel7.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 20, 160, 150));

        jPanelDevoluciones.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 260, 820, 320));

        jTabbedPane1.addTab("Devoluciones", jPanelDevoluciones);

        jPanelAdministrarPrestamos.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTablePrestamos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID Préstamo", "Cliente", "Libro", "Fecha de préstamo", "Fecha límite", "Fecha de devolución", "Estado"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane4.setViewportView(jTablePrestamos);

        jPanelAdministrarPrestamos.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 90, 640, 480));
        jPanelAdministrarPrestamos.add(txtBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 30, 280, 30));

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel15.setText("Estado:");
        jPanelAdministrarPrestamos.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 40, -1, -1));

        btnBuscarPrestamo.setText("Buscar");
        btnBuscarPrestamo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarPrestamoActionPerformed(evt);
            }
        });
        jPanelAdministrarPrestamos.add(btnBuscarPrestamo, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 30, 70, 30));

        cmbEstado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todos", "Pendiente", "Devueltos", "Vencidos" }));
        cmbEstado.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanelAdministrarPrestamos.add(cmbEstado, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 30, 80, 30));

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel16.setText("Buscar:");
        jPanelAdministrarPrestamos.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, -1, -1));

        btnGenerarReporte.setText("Generar reporte");
        jPanelAdministrarPrestamos.add(btnGenerarReporte, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 530, 130, 40));

        btnEditarPrestamo.setText("Editar préstamo");
        jPanelAdministrarPrestamos.add(btnEditarPrestamo, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 90, 130, 40));

        btnEliminarPrestamo.setText("Eliminar préstamo");
        jPanelAdministrarPrestamos.add(btnEliminarPrestamo, new org.netbeans.lib.awtextra.AbsoluteConstraints(704, 160, -1, 40));

        jTabbedPane1.addTab("Administrar préstamos", jPanelAdministrarPrestamos);

        jPanelPeticiones.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel18.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jLabel18.setText("Peticiones");
        jLabel18.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jPanelPeticiones.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));

        jPanel10.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jScrollPane6.setToolTipText("");
        jScrollPane6.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        jPanelNotificaciones.setPreferredSize(new java.awt.Dimension(400, 860));
        jScrollPane6.setViewportView(jPanelNotificaciones);

        jPanel10.add(jScrollPane6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 860, 540));

        jPanelPeticiones.add(jPanel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 60, 860, 540));

        jTabbedPane1.addTab("Peticiones", jPanelPeticiones);

        getContentPane().add(jTabbedPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 0, 860, 640));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextFieldNombresPrestamoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldNombresPrestamoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldNombresPrestamoActionPerformed

    private void cbxOpcionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxOpcionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxOpcionActionPerformed

    private void btn_buscar_libroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_buscar_libroActionPerformed
        String criterio = txt_buscar_libro.getText().trim();
        String opcion = cbxOpcion.getSelectedItem().toString();
        if (criterio != null) {

            ArrayList<Libro> encontrados = new ArrayList<>();

            switch (opcion) {
                case "Título":
                    encontrados = biblioteca.buscarPorTitulo(criterio);
                    break;
                case "Autor":
                    encontrados = biblioteca.buscarPorAutor(criterio);
                    break;
                case "Editorial":
                    encontrados = biblioteca.buscarPorEditorial(criterio);
                    break;
                case "Año":
                    try {
                        int anio = Integer.parseInt(criterio);
                        encontrados = biblioteca.buscarPorAño(anio);
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(this, "Ingrese un número válido para el año.");
                        return;
                    }
                    break;
                case "ISBN":
                    encontrados = biblioteca.buscarPorISBN(criterio);
                    break;
                default:
                    JOptionPane.showMessageDialog(this, "Seleccione un criterio de búsqueda.");
                    return;
            }

            DefaultTableModel modelo = (DefaultTableModel) tabla_libros.getModel();
            modelo.setRowCount(0);

            for (Libro l : encontrados) {
                modelo.addRow(new Object[]{
                    l.getTitulo(),
                    l.getAutor(),
                    l.getEditorial(),
                    l.getIsbn(),
                    l.getAñoPublicacion(),
                    l.getNumeroPaginas(),
                    String.join(", ", l.getGeneros()),
                    l.isDisponible() ? "Sí" : "No"
                });
            }

            if (encontrados.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No se encontraron resultados para: " + criterio);
            }
        } else if (criterio == null) {
            listar((DefaultTableModel) tabla_libros.getModel());
        }

    }//GEN-LAST:event_btn_buscar_libroActionPerformed

    private void txt_buscar_libroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_buscar_libroActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_buscar_libroActionPerformed

    private void txtDNIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDNIActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDNIActionPerformed

    private void btnRegistrarMultaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarMultaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnRegistrarMultaActionPerformed

    private void btnBuscarPrestamoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarPrestamoActionPerformed

        Prestamos gestor = Prestamos.getInstancia();
        gestor.actualizarVencidos(); // actualizar automáticamente
        DefaultTableModel modelo = (DefaultTableModel) jTablePrestamos.getModel();
        modelo.setRowCount(0);

        String texto = txtBuscar.getText().trim();
        String estadoSeleccionado = cmbEstado.getSelectedItem().toString();

        if (texto.isEmpty() && estadoSeleccionado.equals("Todos")) {
            for (Prestamo p : gestor.getPrestamos()) {
                modelo.addRow(new Object[]{
                    p.getId(),
                    p.getCliente(),
                    p.getLibro(),
                    p.getFechaPrestamo(),
                    p.getFechaLimite(),
                    p.getFechaDevolucion() != null ? p.getFechaDevolucion() : "-",
                    p.getEstado()
                });
            }
            return; // Salir del método
        }

        // Buscar por ID (si es número)
        try {
            int id = Integer.parseInt(texto);
            Prestamo p = gestor.buscarPorId(id);
            if (p != null && (estadoSeleccionado.equals("Todos") || p.getEstado().equalsIgnoreCase(estadoSeleccionado))) {
                modelo.addRow(new Object[]{
                    p.getId(),
                    p.getCliente(),
                    p.getLibro(),
                    p.getFechaPrestamo(),
                    p.getFechaLimite(),
                    p.getFechaDevolucion() != null ? p.getFechaDevolucion() : "-",
                    p.getEstado()
                });
                return;
            }
        } catch (NumberFormatException e) {
            // No es un número → buscar por cliente o estado
        }

        ArrayList<Prestamo> resultados = new ArrayList<>();

        // Buscar por cliente si hay texto
        if (!texto.isEmpty()) {
            resultados.addAll(gestor.buscarPorCliente(texto));
        }

        // Si no hay texto, pero hay filtro de estado
        if (texto.isEmpty() && !estadoSeleccionado.equals("Todos")) {
            resultados.addAll(gestor.buscarPorEstado(estadoSeleccionado));
        }

        // Si hay texto y también se filtró por estado
        if (!texto.isEmpty() && !estadoSeleccionado.equals("Todos")) {
            resultados.removeIf(p -> !p.getEstado().equalsIgnoreCase(estadoSeleccionado));
        }

        // Mostrar resultados
        for (Prestamo p : resultados) {
            modelo.addRow(new Object[]{
                p.getId(),
                p.getCliente(),
                p.getLibro(),
                p.getFechaPrestamo(),
                p.getFechaLimite(),
                p.getFechaDevolucion() != null ? p.getFechaDevolucion() : "-",
                p.getEstado()
            });
        }

        if (modelo.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No se encontraron resultados.");
        }


    }//GEN-LAST:event_btnBuscarPrestamoActionPerformed

    private void jButtonBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBuscarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonBuscarActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(VistaEmpleado.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VistaEmpleado.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VistaEmpleado.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VistaEmpleado.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VistaEmpleado().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Menu;
    private javax.swing.JButton btnBuscarCliente;
    private javax.swing.JButton btnBuscarPrestamo;
    private javax.swing.JButton btnEditarPrestamo;
    private javax.swing.JButton btnEliminarPrestamo;
    private javax.swing.JButton btnGenerarReporte;
    private javax.swing.JButton btnRegistrarDev;
    private javax.swing.JButton btnRegistrarMulta;
    private javax.swing.JButton btn_agregar_libro;
    private javax.swing.JButton btn_buscar_libro;
    private javax.swing.JButton btn_eliminar_libro;
    private javax.swing.JButton btn_modificar_libro;
    private javax.swing.JComboBox<String> cbxOpcion;
    private javax.swing.JComboBox<String> cmbEstado;
    private javax.swing.JButton jButtonBuscar;
    private javax.swing.JButton jButtonRealizarPrestamo;
    private javax.swing.JButton jButtonSeleccionarlibro;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanelAdministrarPrestamos;
    private javax.swing.JPanel jPanelDevoluciones;
    private javax.swing.JPanel jPanelLibros;
    private javax.swing.JPanel jPanelNotificaciones;
    private javax.swing.JPanel jPanelPeticiones;
    private javax.swing.JPanel jPanelPrestamos;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTablePrestamos;
    private javax.swing.JTextField jTextFieldApellidosPrestamo;
    private javax.swing.JTextField jTextFieldBuscarLibro;
    private javax.swing.JTextField jTextFieldDNIPrestamo;
    private javax.swing.JTextField jTextFieldDireccionPrestamo;
    private javax.swing.JTextField jTextFieldNombresPrestamo;
    private javax.swing.JTextField jTextFieldTelefonoPrestamo;
    private javax.swing.JList<String> lista_librosseleccionados;
    private java.awt.Menu menu1;
    private java.awt.Menu menu2;
    private java.awt.MenuBar menuBar1;
    private javax.swing.JTable tabla_libro;
    private javax.swing.JTable tabla_libros;
    private javax.swing.JTable tablePrestamos;
    private javax.swing.JTextField txtApellidos;
    private javax.swing.JTextField txtBuscar;
    private javax.swing.JTextField txtDNI;
    private javax.swing.JTextField txtNombres;
    private javax.swing.JTextField txt_buscar_libro;
    // End of variables declaration//GEN-END:variables
}
