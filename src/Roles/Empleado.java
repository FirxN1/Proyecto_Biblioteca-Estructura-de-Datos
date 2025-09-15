
package Roles;


public class Empleado {
    private String usuario;
    private String contraseña;

    public Empleado(String usuario, String contrasena) {
        this.usuario = usuario;
        this.contraseña = contrasena;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getContrasena() {
        return contraseña;
    }

    public boolean validarLogin(String usuario, String contrasena) {
        return this.usuario.equals(usuario) && this.contraseña.equals(contrasena);
    }
}