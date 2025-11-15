package uni.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import uni.database.AccesoDB;
import uni.entity.DetalleTo;
import uni.entity.VentaTo;
import uni.service.IProceso;

public class VentaDao implements IProceso<VentaTo> {

    Connection cn = null;
    CallableStatement csv, csd, csp, csNumero = null;

    @Override
    public void Procesar(VentaTo o) throws Exception {
        try {
            cn = AccesoDB.getConnection();
            cn.setAutoCommit(false);

            // Obtener el número de venta desde procedimiento almacenado
            int nro = numeroFactura();
            o.setIdventa(nro);

            // Registrar venta
            csv = cn.prepareCall("{call sp_Registra_Venta(?,?,?,?,?,?)}");
            csv.setInt(1, o.getIdventa());
            csv.setString(2, o.getIdcliente());
            csv.setString(3, o.getIdempleaado());
            csv.setString(4, o.getTipodoc());
            csv.setString(5, o.getNrodoca());
            csv.setDouble(6, o.getTotal());
            csv.executeUpdate();

            // Registrar detalle y actualizar stock
            csd = cn.prepareCall("{call sp_Registra_Detalle(?,?,?,?,?)}");
            csp = cn.prepareCall("{call sp_Actualiza_Stock(?,?)}");

            for (DetalleTo d : o.getLista()) {
                // Detalle
                csd.setInt(1, o.getIdventa());
                csd.setString(2, d.getIdproducto());
                csd.setDouble(3, d.getPrecio());
                csd.setInt(4, d.getCantidad());
                csd.setDouble(5, d.getTotal());
                csd.executeUpdate();

                // Actualizar stock
                csp.setString(1, d.getIdproducto());
                csp.setInt(2, d.getCantidad());
                csp.executeUpdate();
            }

            // Cerrar recursos
            csv.close();
            csd.close();
            csp.close();

            cn.commit();

        } catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            if (cn != null) {
                try {
                    cn.rollback();
                } catch (SQLException ex) {
                    // Manejar error rollback
                }
            }
            throw e;
        } finally {
            if (cn != null) {
                try {
                    cn.close();
                } catch (SQLException ex) {
                    // Manejar error cierre
                }
            }
        }
    }

    private int numeroFactura() throws SQLException {
        int numero = 0;
        CallableStatement cs = cn.prepareCall("{call sp_Obtener_Numero_Venta(?)}");
        cs.registerOutParameter(1, java.sql.Types.INTEGER);
        cs.execute();
        numero = cs.getInt(1);
        cs.close();
        return numero;
    }

}
