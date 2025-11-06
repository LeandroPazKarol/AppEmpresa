package uni.database;

import java.sql.Connection;

import java.sql.DriverManager;

import java.sql.SQLException;

public class AccesoDB {

    //constantes para la conexion
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/empresa_db";
    private static final String USER = "root";
    private static final String password = "root";
    public static Connection getConnection() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {

        Connection cn = null;
        try {
            //cargar el driver
            Class.forName(DRIVER);
            //conexion con la base de datos
            cn = DriverManager.getConnection(URL, USER, password);
        } catch (ClassNotFoundException e) {
            System.out.println("Error: no se encontro el driver de Mysql");
            throw new ClassNotFoundException(e.getMessage());
        } catch (SQLException e) {
            System.out.println("Error al conectar con la base de datos: " + e.getMessage());
            throw new SQLException(e.getMessage());
        }
        return cn;

    }

}
