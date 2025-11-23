
package Vista;
import Modelo.Libro;
import Servicio.Biblioteca;
import java.util.ArrayList;
import javax.swing.JOptionPane;
/**
 *
 * @author chris
 */
public class ModificarLibro extends javax.swing.JDialog {

    private Biblioteca biblioteca;
    private String isbnOriginal;
    private boolean libroModificado = false;

    public ModificarLibro(java.awt.Frame parent, boolean modal, String isbn) {
        super(parent, modal);
        initComponents();
        biblioteca = Biblioteca.getInstancia();
        this.isbnOriginal = isbn;
        
        // Configurar eventos
        jButtonModificar.addActionListener(e -> modificarLibro());
        jButtonCancelar.addActionListener(e -> cancelar());
        
        // Centrar el diálogo
        setLocationRelativeTo(parent);
        
        // Cargar datos del libro
        cargarDatosLibro();
    }

    private void cargarDatosLibro() {
        for (Libro l : biblioteca.getLibros()) {
            if (l.getIsbn().equalsIgnoreCase(isbnOriginal)) {
                jTextFieldTituloModificarLibro.setText(l.getTitulo());
                jTextFieldAutorModificarLibro.setText(l.getAutor());
                jTextFieldEditorialModificarLibro.setText(l.getEditorial());
                jTextFieldISBNModificarLibro.setText(l.getIsbn());
                jTextFieldAñoPublicacionModificarLibro.setText(String.valueOf(l.getAñoPublicacion()));
                jTextFieldNumeroPaginasModificarLibro.setText(String.valueOf(l.getNumeroPaginas()));
                jTextFieldGeneroModificarLibro.setText(String.join(", ", l.getGeneros()));
                jTextFieldDisponibleMdificarLibro.setText(l.isDisponible() ? "Sí" : "No");
                break;
            }
        }
    }

    private void modificarLibro() {
        try {
            // Obtener valores de los campos
            String titulo = jTextFieldTituloModificarLibro.getText().trim();
            String autor = jTextFieldAutorModificarLibro.getText().trim();
            String editorial = jTextFieldEditorialModificarLibro.getText().trim();
            String isbn = jTextFieldISBNModificarLibro.getText().trim();
            String anioStr = jTextFieldAñoPublicacionModificarLibro.getText().trim();
            String paginasStr = jTextFieldNumeroPaginasModificarLibro.getText().trim();
            String generoStr = jTextFieldGeneroModificarLibro.getText().trim();
            String disponibleStr = jTextFieldDisponibleMdificarLibro.getText().trim();

            // Validaciones
            if (titulo.isEmpty() || autor.isEmpty() || editorial.isEmpty() || isbn.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Por favor complete todos los campos obligatorios:\nTítulo, Autor, Editorial e ISBN", 
                    "Campos incompletos", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Si cambió el ISBN, verificar que no exista
            if (!isbn.equalsIgnoreCase(isbnOriginal)) {
                for (Libro l : biblioteca.getLibros()) {
                    if (l.getIsbn().equalsIgnoreCase(isbn)) {
                        JOptionPane.showMessageDialog(this, 
                            "Ya existe un libro con ese ISBN", 
                            "ISBN duplicado", 
                            JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
            }

            // Parsear año
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

            // Parsear páginas
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

            // Modificar el libro
            boolean exito = biblioteca.editarLibro(isbnOriginal, titulo, autor, editorial, 
                                                   anio, paginas, generos, disponible);
            
            if (exito) {
                // Si cambió el ISBN, actualizar el original
                if (!isbn.equalsIgnoreCase(isbnOriginal)) {
                    for (Libro l : biblioteca.getLibros()) {
                        if (l.getTitulo().equals(titulo) && l.getAutor().equals(autor)) {
                            l.setIsbn(isbn);
                            break;
                        }
                    }
                }
                
                libroModificado = true;
                JOptionPane.showMessageDialog(this, 
                    "Libro modificado exitosamente", 
                    "Éxito", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "No se pudo modificar el libro", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error al modificar el libro: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void cancelar() {
        int respuesta = JOptionPane.showConfirmDialog(this, 
            "¿Está seguro que desea cancelar?\nLos cambios no se guardarán.", 
            "Confirmar cancelación", 
            JOptionPane.YES_NO_OPTION);
        
        if (respuesta == JOptionPane.YES_OPTION) {
            dispose();
        }
    }

    public boolean isLibroModificado() {
        return libroModificado;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
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
        jTextFieldDisponibleMdificarLibro = new javax.swing.JTextField();
        jTextFieldTituloModificarLibro = new javax.swing.JTextField();
        jTextFieldAutorModificarLibro = new javax.swing.JTextField();
        jTextFieldEditorialModificarLibro = new javax.swing.JTextField();
        jTextFieldISBNModificarLibro = new javax.swing.JTextField();
        jTextFieldAñoPublicacionModificarLibro = new javax.swing.JTextField();
        jTextFieldNumeroPaginasModificarLibro = new javax.swing.JTextField();
        jTextFieldGeneroModificarLibro = new javax.swing.JTextField();
        jButtonModificar = new javax.swing.JButton();
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
        jLabel3.setText("MODIFICAR LIBRO");
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
        getContentPane().add(jTextFieldDisponibleMdificarLibro, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 240, 150, -1));
        getContentPane().add(jTextFieldTituloModificarLibro, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 90, 150, -1));
        getContentPane().add(jTextFieldAutorModificarLibro, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 140, 150, -1));

        jTextFieldEditorialModificarLibro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldEditorialModificarLibroActionPerformed(evt);
            }
        });
        getContentPane().add(jTextFieldEditorialModificarLibro, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 190, 150, -1));
        getContentPane().add(jTextFieldISBNModificarLibro, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 240, 150, -1));
        getContentPane().add(jTextFieldAñoPublicacionModificarLibro, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 90, 150, -1));
        getContentPane().add(jTextFieldNumeroPaginasModificarLibro, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 140, 150, -1));
        getContentPane().add(jTextFieldGeneroModificarLibro, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 190, 150, -1));

        jButtonModificar.setText("Modificar");
        getContentPane().add(jButtonModificar, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 290, -1, -1));

        jButtonCancelar.setText("Cancelar");
        getContentPane().add(jButtonCancelar, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 290, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextFieldEditorialModificarLibroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldEditorialModificarLibroActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldEditorialModificarLibroActionPerformed

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCancelar;
    private javax.swing.JButton jButtonModificar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JTextField jTextFieldAutorModificarLibro;
    private javax.swing.JTextField jTextFieldAñoPublicacionModificarLibro;
    private javax.swing.JTextField jTextFieldDisponibleMdificarLibro;
    private javax.swing.JTextField jTextFieldEditorialModificarLibro;
    private javax.swing.JTextField jTextFieldGeneroModificarLibro;
    private javax.swing.JTextField jTextFieldISBNModificarLibro;
    private javax.swing.JTextField jTextFieldNumeroPaginasModificarLibro;
    private javax.swing.JTextField jTextFieldTituloModificarLibro;
    // End of variables declaration//GEN-END:variables
}
