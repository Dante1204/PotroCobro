package bdd;

import modelo.Producto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConexionDB {
    private static final String URL = "jdbc:sqlite:C:/Users/dmgzd/OneDrive/Documentos/NetBeansProjects/PotroCobro/tienda.db";
    private static ConexionDB instancia;
    private Connection connection;

    private ConexionDB() {
        try {
            connection = DriverManager.getConnection(URL);
            System.out.println("Conexión a SQLite establecida.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static synchronized ConexionDB getInstance() {
        if (instancia == null) {
            instancia = new ConexionDB();
        }
        return instancia;
    }

    public Connection getConnection() {
        return connection;
    }

    // --- EL MÉTODO QUE FALTABA ---
    /**
     * Revisa la base de datos. Si las tablas no existen, las crea.
     * Si las tablas existen pero están vacías, inserta los datos iniciales.
     */
    public void inicializarBaseDeDatos() {
        // SQL para crear la tabla de usuarios
        String sqlUsuarios = "CREATE TABLE IF NOT EXISTS usuarios (\n"
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + " nombre_usuario TEXT NOT NULL UNIQUE,\n"
                + " contrasena TEXT NOT NULL\n"
                + ");";

        // SQL para crear la tabla de productos
        String sqlProductos = "CREATE TABLE IF NOT EXISTS productos (\n"
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + " nombre TEXT NOT NULL,\n"
                + " precio REAL NOT NULL,\n"
                + " categoria TEXT\n"
                + ");";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sqlUsuarios);
            stmt.execute(sqlProductos);
        } catch (SQLException e) {
            System.err.println("Error al crear las tablas: " + e.getMessage());
            return;
        }

        // Verifica si la tabla de productos está vacía para insertar datos
        String sqlCheck = "SELECT COUNT(*) AS total FROM productos";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sqlCheck)) {
            
            if (rs.getInt("total") == 0) {
                System.out.println("Base de datos vacía. Insertando datos iniciales...");
                insertarDatosIniciales();
            } else {
                System.out.println("La base de datos ya contiene datos de productos.");
            }

        } catch (SQLException e) {
            System.err.println("Error al verificar la tabla de productos: " + e.getMessage());
        }
    }

    /**
     * Inserta la lista de productos iniciales en la base de datos.
     */
    private void insertarDatosIniciales() {
        String sqlInsert = "INSERT INTO productos(nombre, precio, categoria) VALUES(?,?,?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sqlInsert)) {
            List<Producto> productos = obtenerListaDeProductosIniciales();
            
            for (Producto producto : productos) {
                pstmt.setString(1, producto.getNombre());
                pstmt.setDouble(2, producto.getPrecio());
                pstmt.setString(3, obtenerCategoria(producto.getNombre()));
                pstmt.executeUpdate();
            }
            System.out.println("Datos iniciales insertados correctamente.");

        } catch (SQLException e) {
            System.err.println("Error al insertar datos iniciales: " + e.getMessage());
        }
    }
    
    // --- MÉTODOS PARA USUARIOS (ya los tenías) ---

    public boolean validarUsuario(String usuario, String contrasena) {
        String sql = "SELECT * FROM usuarios WHERE nombre_usuario = ? AND contrasena = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, usuario);
            pstmt.setString(2, contrasena);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    public boolean registrarUsuario(String usuario, String contrasena) {
        String sql = "INSERT INTO usuarios(nombre_usuario, contrasena) VALUES(?,?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, usuario);
            pstmt.setString(2, contrasena);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            if (e.getErrorCode() == 19) { 
                System.err.println("Error: El nombre de usuario ya existe.");
            } else {
                System.err.println(e.getMessage());
            }
            return false;
        }
    }
    
    public boolean registrarProducto(String nombre, Double precio){
        String sql = "INSERT INTO productos(nombre, precio) VALUES(?,?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setString(1, nombre);
            pstmt.setDouble(2, precio);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            if (e.getErrorCode() == 19) {
                System.err.println("Error: El producto ya existe.");
            } else {
                System.err.println(e.getMessage());
            }
            return false;
        }
    }

    // --- MÉTODOS PARA PRODUCTOS (ya lo tenías) ---

    public List<Producto> obtenerTodosLosProductos() {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT nombre, precio FROM productos";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                String nombre = rs.getString("nombre");
                double precio = rs.getDouble("precio");
                productos.add(new Producto(nombre, precio));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return productos;
    }

    // --- MÉTODOS PRIVADOS AUXILIARES ---

    private List<Producto> obtenerListaDeProductosIniciales() {
        List<Producto> productos = new ArrayList<>();
        // Leche
        productos.add(new Producto("Leche entera Lala 1L", 28.50));
        productos.add(new Producto("Leche entera Santa Clara 1L (6pzas)", 230.00));
        // ... (Aquí iría la lista completa de todos tus productos del PDF) ...
        // Snacks
        productos.add(new Producto("Gansito Marinela 50g", 20.90));
        productos.add(new Producto("Pingüinos Marinela 80g", 27.90));
        // Limpieza
        productos.add(new Producto("Pinol El Original 5.1L", 179.00));
        // Bebidas
        productos.add(new Producto("Coca-Cola 600ml", 18.00));
        return productos;
    }

    private String obtenerCategoria(String nombreProducto) {
        String nombreLower = nombreProducto.toLowerCase();
        if (nombreLower.contains("leche") || nombreLower.contains("yogurt")) return "Lácteos";
        if (nombreLower.contains("galletas") || nombreLower.contains("gansito") || nombreLower.contains("sabritas")) return "Snacks";
        if (nombreLower.contains("pinol") || nombreLower.contains("ariel") || nombreLower.contains("salvo")) return "Limpieza";
        if (nombreLower.contains("coca-cola") || nombreLower.contains("agua") || nombreLower.contains("pepsi")) return "Bebidas";
        return "General";
    }
}