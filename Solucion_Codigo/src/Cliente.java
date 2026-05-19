
public class Cliente {

    private String cedula;
    private String nombre;
    private String direccion;
    private String sector;
    private double distanciaKm;

    public Cliente(String cedula, String nombre, String direccion, String sector, double distanciaKm) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.direccion = direccion;
        this.sector = sector;
        this.distanciaKm = distanciaKm;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public double getDistanciaKm() {
        return distanciaKm;
    }

    public void setDistanciaKm(double distanciaKm) {
        this.distanciaKm = distanciaKm;
    }

}
