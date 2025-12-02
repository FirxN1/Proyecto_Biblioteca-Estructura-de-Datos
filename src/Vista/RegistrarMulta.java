package Vista;

import Modelo.Multa;
import Modelo.Prestamo;
import Servicio.GestorMultas;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class RegistrarMulta extends JDialog {
    private JTextField txtMotivo;
    private JTextField txtMonto;
    private JButton btnRegistrar;
    private JButton btnCancelar;
    private JLabel lblClienteInfo;
    private JLabel lblPrestamoInfo;
    
    private Prestamo prestamo;
    private String dniCliente;
    private String nombreCliente;
    private boolean multaRegistrada = false;
    private Multa multaCreada;

    public RegistrarMulta(Frame parent, Prestamo prestamo, String dniCliente, String nombreCliente) {
        super(parent, "Registrar Multa", true);
        this.prestamo = prestamo;
        this.dniCliente = dniCliente;
        this.nombreCliente = nombreCliente;
        
        initComponents();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setLayout(null);
        setSize(500, 400);
        setResizable(false);

        // Título
        JLabel lblTitulo = new JLabel("REGISTRAR MULTA");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setBounds(130, 20, 250, 30);
        add(lblTitulo);

        // Información del cliente
        lblClienteInfo = new JLabel("Cliente: " + nombreCliente + " (DNI: " + dniCliente + ")");
        lblClienteInfo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblClienteInfo.setBounds(30, 70, 440, 20);
        add(lblClienteInfo);

        // Información del préstamo
        lblPrestamoInfo = new JLabel("Libro: " + prestamo.getLibro() + " | ID Préstamo: " + prestamo.getId());
        lblPrestamoInfo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblPrestamoInfo.setBounds(30, 95, 440, 20);
        add(lblPrestamoInfo);

        // Separador
        JSeparator separator = new JSeparator();
        separator.setBounds(30, 125, 440, 2);
        add(separator);

        // Motivo
        JLabel lblMotivo = new JLabel("Motivo de la multa:");
        lblMotivo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblMotivo.setBounds(30, 145, 150, 25);
        add(lblMotivo);

        txtMotivo = new JTextField();
        txtMotivo.setBounds(30, 175, 440, 30);
        txtMotivo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        add(txtMotivo);

        // Monto
        JLabel lblMonto = new JLabel("Monto (S/.):");
        lblMonto.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblMonto.setBounds(30, 215, 150, 25);
        add(lblMonto);

        txtMonto = new JTextField();
        txtMonto.setBounds(30, 245, 150, 30);
        txtMonto.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        add(txtMonto);

        // Botones
        btnRegistrar = new JButton("Registrar");
        btnRegistrar.setBounds(160, 290, 120, 35);
        btnRegistrar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnRegistrar.setBackground(new Color(255, 102, 102));
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.addActionListener(e -> registrarMulta());
        add(btnRegistrar);

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setBounds(300, 290, 120, 35);
        btnCancelar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnCancelar.addActionListener(e -> cancelar());
        add(btnCancelar);
    }

    private void registrarMulta() {
        String motivo = txtMotivo.getText().trim();
        String montoStr = txtMonto.getText().trim();

        // Validaciones
        if (motivo.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Por favor ingrese el motivo de la multa",
                "Campo vacío",
                JOptionPane.WARNING_MESSAGE);
            txtMotivo.requestFocus();
            return;
        }

        if (montoStr.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Por favor ingrese el monto de la multa",
                "Campo vacío",
                JOptionPane.WARNING_MESSAGE);
            txtMonto.requestFocus();
            return;
        }

        double monto;
        try {
            monto = Double.parseDouble(montoStr);
            if (monto <= 0) {
                JOptionPane.showMessageDialog(this,
                    "El monto debe ser mayor a 0",
                    "Monto inválido",
                    JOptionPane.WARNING_MESSAGE);
                txtMonto.requestFocus();
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Por favor ingrese un monto válido (use punto para decimales)",
                "Monto inválido",
                JOptionPane.ERROR_MESSAGE);
            txtMonto.requestFocus();
            return;
        }

        // Confirmar registro
        int respuesta = JOptionPane.showConfirmDialog(this,
            "¿Confirmar registro de multa?\n\n" +
            "Cliente: " + nombreCliente + "\n" +
            "Motivo: " + motivo + "\n" +
            "Monto: S/. " + String.format("%.2f", monto),
            "Confirmar multa",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);

        if (respuesta == JOptionPane.YES_OPTION) {
            // Registrar la multa
            GestorMultas gestorMultas = GestorMultas.getInstancia();
            multaCreada = gestorMultas.registrarMulta(
                dniCliente, 
                nombreCliente, 
                prestamo.getId(),
                prestamo.getLibro(),
                motivo,
                monto
            );

            multaRegistrada = true;

            JOptionPane.showMessageDialog(this,
                "Multa registrada exitosamente\n" +
                "ID de multa: " + multaCreada.getId() + "\n" +
                "Monto: S/. " + String.format("%.2f", monto),
                "Multa registrada",
                JOptionPane.INFORMATION_MESSAGE);

            dispose();
        }
    }

    private void cancelar() {
        int respuesta = JOptionPane.showConfirmDialog(this,
            "¿Está seguro que desea cancelar?\nNo se registrará la multa.",
            "Confirmar cancelación",
            JOptionPane.YES_NO_OPTION);

        if (respuesta == JOptionPane.YES_OPTION) {
            dispose();
        }
    }

    public boolean isMultaRegistrada() {
        return multaRegistrada;
    }

    public Multa getMultaCreada() {
        return multaCreada;
    }
}