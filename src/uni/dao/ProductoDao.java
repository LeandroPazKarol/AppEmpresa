package uni.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.CallableStatement; // Importante: se añade CallableStatement
import java.util.ArrayList;
import java.util.List;
import uni.database.AccesoDB;
import uni.entity.ProductoTo;
import uni.service.ICrudDao;

public class ProductoDao implements ICrudDao<ProductoTo> {

    // variables
    Connection cn = null;
    CallableStatement cs = null; // Se usa para Stored Procedures
    PreparedStatement ps = null; // Se usa para Consultas (SELECT)
    ResultSet rs = null;
    String sql = "";
    String sp = ""; // Para Stored Procedures

    @Override
    public void create(ProductoTo o) throws Exception {
        try {
            cn = AccesoDB.getConnection();
            cn.setAutoCommit(false); // Iniciar transacción
            
            String cod = generaCodigo(); // Generar nuevo código
            o.setIdproducto(cod);
            
            sp = "{call sp_Producto_Adicionar(?,?,?,?,?,?)}";
            cs = cn.prepareCall(sp);
            
            cs.setString(1, o.getIdproducto());
            cs.setString(2, o.getDescripcion());
            cs.setInt(3, o.getIdlinea());
            cs.setDouble(4, o.getPreciocompra());
            cs.setDouble(5, o.getPrecioventa());
            cs.setInt(6, o.getStock());
            
            cs.executeUpdate();
            cs.close();
            cn.commit(); // Confirmar transacción
            
        } catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            try {
                cn.rollback(); // Revertir transacción en caso de error
            } catch (Exception e1) {
            }
            throw e;
        } finally {
            cn.close();
        }
    }

    @Override
    public void update(ProductoTo o) throws Exception {
        try {
            cn = AccesoDB.getConnection();
            cn.setAutoCommit(false); // Iniciar transacción
            
            sp = "{call sp_Producto_Actualizar(?,?,?,?,?,?)}";
            cs = cn.prepareCall(sp);
            
            cs.setString(1, o.getIdproducto());
            cs.setString(2, o.getDescripcion());
            cs.setInt(3, o.getIdlinea());
            cs.setDouble(4, o.getPreciocompra());
            cs.setDouble(5, o.getPrecioventa());
            cs.setInt(6, o.getStock());
            
            cs.executeUpdate();
            cs.close();
            cn.commit(); // Confirmar transacción
            
        } catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            try {
                cn.rollback(); // Revertir transacción en caso de error
            } catch (Exception e1) {
            }
            throw e;
        } finally {
            cn.close();
        }
    }

    @Override
    public void delete(ProductoTo o) throws Exception {
        try {
            cn = AccesoDB.getConnection();
            cn.setAutoCommit(false); // Iniciar transacción
            
            sp = "{call sp_Producto_Eliminar(?)}";
            cs = cn.prepareCall(sp);
            
            cs.setString(1, o.getIdproducto());
            
            cs.executeUpdate();
            cs.close();
            cn.commit(); // Confirmar transacción
            
        } catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            try {
                cn.rollback(); // Revertir transacción en caso de error
            } catch (Exception e1) {
            }
            throw e;
        } finally {
            cn.close();
        }
    }

    @Override
    public ProductoTo find(Object o) throws Exception {
        ProductoTo pr = null;
        try {
            cn = AccesoDB.getConnection();
            // Corrección de seguridad (evita Inyección SQL)
            sql = "select * from productos where idproducto = ?"; 
            ps = cn.prepareStatement(sql);
            ps.setString(1, (String) o); // Se asigna el parámetro de forma segura
            
            rs = ps.executeQuery();
            if (rs.next()) {
                pr = new ProductoTo();
                pr.setIdproducto(rs.getString("idproducto"));
                pr.setDescripcion(rs.getString("descripcion"));
                pr.setIdlinea(rs.getInt("idlinea"));
                pr.setPreciocompra(rs.getDouble("preciocompra"));
                pr.setPrecioventa(rs.getDouble("precioventa"));
                pr.setStock(rs.getInt("stock"));
            }
            rs.close();
            ps.close();
        } catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw e;
        } finally {
            cn.close();
        }
        return pr;
    }

    @Override
    public List<ProductoTo> readAll() throws Exception {
        List<ProductoTo> lista = new ArrayList<>();
        try {
            cn = AccesoDB.getConnection();
            sql = "select * from productos";
            ps = cn.prepareStatement(sql);
            rs = ps.executeQuery();
            lista = cargaLista(rs); // cargaLista sigue siendo el mismo
            rs.close();
            ps.close();
        } catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw e;
        } finally {
            cn.close();
        }
        return lista;
    }

    // --- MÉTODOS ADICIONALES (SE MANTIENEN IGUAL O MEJORAN) ---
    
    public List<ProductoTo> readAll(String nombre) throws Exception {
        List<ProductoTo> lista = new ArrayList<>();
        try {
            cn = AccesoDB.getConnection();
            sql = "select * from productos where descripcion like ?";
            ps = cn.prepareStatement(sql);
            ps.setString(1, nombre + "%");
            rs = ps.executeQuery();
            lista = cargaLista(rs);
            rs.close();
            ps.close();
        } catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw e;
        } finally {
            cn.close();
        }
        return lista;
    }

    public List<ProductoTo> readAll(int id) throws Exception {
        List<ProductoTo> lista = new ArrayList<>();
        try {
            cn = AccesoDB.getConnection();
            sql = "select * from productos where idlinea=?";
            ps = cn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            lista = cargaLista(rs);
            rs.close();
            ps.close();
        } catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw e;
        } finally {
            cn.close();
        }
        return lista;
    }

    public ProductoTo find1(String o) throws Exception {
        ProductoTo pr = null;
        try {
            cn = AccesoDB.getConnection();
            // Corrección de seguridad (evita Inyección SQL)
            sql = "select * from productos where descripcion = ?";
            ps = cn.prepareStatement(sql);
            ps.setString(1, o); // Se asigna el parámetro de forma segura
            
            rs = ps.executeQuery();
            if (rs.next()) {
                pr = new ProductoTo();
                pr.setIdproducto(rs.getString("idproducto"));
                pr.setDescripcion(rs.getString("descripcion"));
                pr.setIdlinea(rs.getInt("idlinea"));
                pr.setPreciocompra(rs.getDouble("preciocompra"));
                pr.setPrecioventa(rs.getDouble("precioventa"));
                pr.setStock(rs.getInt("stock"));
            }
            rs.close();
            ps.close();
        } catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw e;
        } finally {
            cn.close();
        }
        return pr;
    }

    private List<ProductoTo> cargaLista(ResultSet rs) throws SQLException {
        List<ProductoTo> aux = new ArrayList<>();
        while (rs.next()) {
            ProductoTo pr = new ProductoTo();
            pr.setIdproducto(rs.getString(1));
            pr.setDescripcion(rs.getString(2));
            pr.setIdlinea(rs.getInt(3));
            pr.setPreciocompra(rs.getDouble(4));
            pr.setPrecioventa(rs.getDouble(5));
            pr.setStock(rs.getInt(6));
            aux.add(pr);
        }
        rs.close();
        return aux;
    }

    private String generaCodigo() throws SQLException {
        String sql = "SELECT MAX(idproducto) FROM productos";
        ps = cn.prepareStatement(sql);
        rs = ps.executeQuery();
        String cod = "A0001"; // Código por defecto si la tabla está vacía
        
        if (rs.next() && rs.getString(1) != null) {
            String ultimo = rs.getString(1); // "A0009"
            int num = Integer.parseInt(ultimo.substring(1)) + 1; // 9 + 1 = 10
            
            if (num < 10) {
                cod = "A000" + num;
            } else if (num < 100) {
                cod = "A00" + num; // "A0010"
            } else if (num < 1000) {
                cod = "A0" + num;
            } else {
                cod = "A" + num;
            }
        }
        rs.close();
        ps.close();
        return cod;
    }
}
