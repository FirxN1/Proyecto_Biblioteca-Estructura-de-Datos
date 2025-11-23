
package Vista;
import Modelo.Libro;
import Servicio.Biblioteca;
import java.util.ArrayList;
import javax.swing.JOptionPane;
/**
 *
 * @author chris
 */
public class AgregarLibro extends javax.swing.JDialog {

    private Biblioteca biblioteca;
    private boolean libroAgregado = false;

    public AgregarLibro(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        biblioteca = Biblioteca.getInstancia();
        
        // Configurar eventos de los botones
        jButtonAgregar.addActionListener(e -> agregarLibro());
        jButtonCancelar.addActionListener(e -> cancelar());
        
        // Centrar el diálogo
        setLocationRelativeTo(parent);
    }

    private void agregarLibro() {
        try {
            // Validar campos obligatorios
            String titulo = jTextFieldTituloAgregarLibro.getText().trim();
            String autor = jTextFieldAutorAgregarLibro.getText().trim();
            String editorial = jTextFieldEditorialAgregarLibro.getText().trim();
            String isbn = jTextFieldISBNAgregarLibro.getText().trim();
            String anioStr = jTextFieldAñoPublicacionAgregarLibro.getText().trim();
            String paginasStr = jTextFieldNumeroPaginasAgregarLibro.getText().trim();
            String generoStr = jTextFieldGeneroAgregarLibro.getText().trim();
            String disponibleStr = jTextFieldDisponibleAgregarLibro.getText().trim();

            // Validaciones
            if (titulo.isEmpty() || autor.isEmpty() || editorial.isEmpty() || isbn.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Por favor complete todos los campos obligatorios:\nTítulo, Autor, Editorial e ISBN", 
                    "Campos incompletos", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Verificar que el ISBN no exista
            for (Libro l : biblioteca.getLibros()) {
                if (l.getIsbn().equalsIgnoreCase(isbn)) {
                    JOptionPane.showMessageDialog(this, 
                        "Ya existe un libro con ese ISBN", 
                        "ISBN duplicado", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            // Parsear año de publicación
            int anio = 0;
            if (!anioStr.isEmpty()) {
                try {
                    anio = Integer.parseInt(anioStr);
                    if (anio < 1000 || anio > 2025) {
                        JOptionPane.showMessageDialog(this, 
                            "El año debe estar entre 1000 y 2025", 
                            "Año inválido", 
                            JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, 
                        "El año debe ser un número válido", 
                        "Error en año", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            // Parsear número de páginas
            int paginas = 0;
            if (!paginasStr.isEmpty()) {
                try {
                    paginas = Integer.parseInt(paginasStr);
                    if (paginas <= 0) {
                        JOptionPane.showMessageDialog(this, 
                            "El número de páginas debe ser mayor a 0", 
                            "Páginas inválidas", 
                            JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, 
                        "El número de páginas debe ser un número válido", 
                        "Error en páginas", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            // Procesar géneros
            ArrayList<String> generos = new ArrayList<>();
            if (!generoStr.isEmpty()) {
                String[] generosArray = generoStr.split(",");
                for (String g : generosArray) {
                    String generoLimpio = g.trim();
                    if (!generoLimpio.isEmpty()) {
                        generos.add(generoLimpio);
                    }
                }
            }
            if (generos.isEmpty()) {
                generos.add("General");
            }

            // Procesar disponibilidad
            boolean disponible = true;
            if (!disponibleStr.isEmpty()) {
                disponibleStr = disponibleStr.toLowerCase();
                if (disponibleStr.equals("no") || disponibleStr.equals("false") || 
                    disponibleStr.equals("0") || disponibleStr.equals("n")) {
                    disponible = false;
                } else if (!disponibleStr.equals("si") && !disponibleStr.equals("sí") && 
                           !disponibleStr.equals("yes") && !disponibleStr.equals("true") && 
                           !disponibleStr.equals("1") && !disponibleStr.equals("s")) {
                    JOptionPane.showMessageDialog(this, 
                        "Disponibilidad debe ser: Sí/Si/S/1 o No/N/0", 
                        "Disponibilidad inválida", 
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }

            // Crear y agregar el libro
            Libro nuevoLibro = new Libro(titulo, autor, editorial, isbn, anio, paginas, generos, disponible);
            biblioteca.agregarLibro(nuevoLibro);
            
            libroAgregado = true;
            JOptionPane.showMessageDialog(this, 
                "Libro agregado exitosamente", 
                "Éxito", 
                JOptionPane.INFORMATION_MESSAGE);
            
            dispose();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error al agregar el libro: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void cancelar() {
        int respuesta = JOptionPane.showConfirmDialog(this, 
            "¿Está seguro que desea cancelar?\nLos datos no se guardarán.", 
            "Confirmar cancelación", 
            JOptionPane.YES_NO_OPTION);
        
        if (respuesta == JOptionPane.YES_OPTION) {
            dispose();
        }
    }

    public boolean isLibroAgregado() {
        return libroAgregado;
    }



    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jTextFieldDisponibleAgregarLibro = new javax.swing.JTextField();
        jTextFieldTituloAgregarLibro = new javax.swing.JTextField();
        jTextFieldAutorAgregarLibro = new javax.swing.JTextField();
        jTextFieldEditorialAgregarLibro = new javax.swing.JTextField();
        jTextFieldISBNAgregarLibro = new javax.swing.JTextField();
        jTextFieldAñoPublicacionAgregarLibro = new javax.swing.JTextField();
        jTextFieldNumeroPaginasAgregarLibro = new javax.swing.JTextField();
        jTextFieldGeneroAgregarLibro = new javax.swing.JTextField();
        jButtonAgregar = new javax.swing.JButton();
        jButtonCancelar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setText("Título:");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, -1, -1));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setText("Autor:");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, -1, -1));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel3.setText("AGREGAR LIBRO");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 20, -1, -1));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setText("Editorial:");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 190, -1, -1));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setText("ISBN:");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 240, -1, -1));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setText("Año de publicación:");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 90, -1, -1));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel7.setText("Número de páginas:");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 140, -1, -1));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel8.setText("Género:");
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 190, -1, -1));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel9.setText("Disponible:");
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 240, -1, -1));
        getContentPane().add(jTextFieldDisponibleAgregarLibro, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 240, 150, -1));
        getContentPane().add(jTextFieldTituloAgregarLibro, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 90, 150, -1));
        getContentPane().add(jTextFieldAutorAgregarLibro, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 140, 150, -1));

        jTextFieldEditorialAgregarLibro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldEditorialAgregarLibroActionPerformed(evt);
            }
        });
        getContentPane().add(jTextFieldEditorialAgregarLibro, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 190, 150, -1));
        getContentPane().add(jTextFieldISBNAgregarLibro, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 240, 150, -1));
        getContentPane().add(jTextFieldAñoPublicacionAgregarLibro, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 90, 150, -1));
        getContentPane().add(jTextFieldNumeroPaginasAgregarLibro, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 140, 150, -1));
        getContentPane().add(jTextFieldGeneroAgregarLibro, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 190, 150, -1));

        jButtonAgregar.setText("Agregar");
        getContentPane().add(jButtonAgregar, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 290, -1, -1));

        jButtonCancelar.setText("Cancelar");
        getContentPane().add(jButtonCancelar, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 290, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextFieldEditorialAgregarLibroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldEditorialAgregarLibroActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldEditorialAgregarLibroActionPerformed

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAgregar;
    private javax.swing.JButton jButtonCancelar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JTextField jTextFieldAutorAgregarLibro;
    private javax.swing.JTextField jTextFieldAñoPublicacionAgregarLibro;
    private javax.swing.JTextField jTextFieldDisponibleAgregarLibro;
    private javax.swing.JTextField jTextFieldEditorialAgregarLibro;
    private javax.swing.JTextField jTextFieldGeneroAgregarLibro;
    private javax.swing.JTextField jTextFieldISBNAgregarLibro;
    private javax.swing.JTextField jTextFieldNumeroPaginasAgregarLibro;
    private javax.swing.JTextField jTextFieldTituloAgregarLibro;
    // End of variables declaration//GEN-END:variables
}
