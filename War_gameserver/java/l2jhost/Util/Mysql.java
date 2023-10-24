package l2jhost.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import net.sf.l2j.commons.pool.ConnectionPool;

public abstract class Mysql
{
    public static final Logger _log = Logger.getLogger(Mysql.class.getName());
    private static ConnectionPool _connectionPool; // Utilizando la clase ConnectionPool que proporcionaste

    public static void setConnectionPool(ConnectionPool connectionPool)
    {
        _connectionPool = connectionPool;
    }

    public static boolean setEx(String query, Object... vars)
    {
        Connection con = null;
        PreparedStatement pstatement = null;
        boolean successed = true;

        try
        {
            if (_connectionPool == null)
            {
                _log.warning("Connection pool not set. Call setConnectionPool before using.");
                return false;
            }

            con = ConnectionPool.getConnection();
            if (vars.length == 0)
            {
                @SuppressWarnings("resource")
				Statement statement = con.createStatement();
                statement.executeUpdate(query);
                statement.close();
            }
            else
            {
                pstatement = con.prepareStatement(query);
                setVars(pstatement, vars);
                pstatement.executeUpdate();
                pstatement.close();
            }
        }
        catch (Exception e)
        {
            _log.warning("Could not execute update '" + query + "': " + e);
            e.printStackTrace();
            successed = false;
        }
        finally
        {
            closeQuietly(con, pstatement);
        }
        return successed;
    }

   public static void setVars(PreparedStatement statement, Object... vars) throws SQLException
   {
       Number n;
       long long_val;
       double double_val;
       for(int i = 0; i < vars.length; i++)
           if(vars[i] instanceof Number)
           {
               n = (Number) vars[i];
               long_val = n.longValue();
               double_val = n.doubleValue();
               if(long_val == double_val)
                   statement.setLong(i + 1, long_val);
               else
                   statement.setDouble(i + 1, double_val);
           }
           else if(vars[i] instanceof String)
               statement.setString(i + 1, (String) vars[i]);
   }

   public static boolean set(String query, Object... vars)
   {
       return setEx(null, query, vars);
   }

   public static boolean set(String query)
   {
       return setEx(null, query);
   }
   
   public static void closeQuietly(Connection conn) 
   {
       try {
           close(conn);
       } catch (SQLException e) { // NOPMD
           // quiet
       }
   }

   public static void closeQuietly(Connection conn, Statement stmt, ResultSet rs) {

        try {
            closeQuietly(rs);
        } finally {
            try {
                closeQuietly(stmt);
            } finally {
                closeQuietly(conn);
            }
        }
    }
   
   public static void closeQuietly(Connection conn, Statement stmt) 
    {
       try {
           closeQuietly(stmt);
       } finally {
           closeQuietly(conn);
       }
    }

    public static void closeQuietly(ResultSet rs) {
        try {
            close(rs);
        } catch (SQLException e) { // NOPMD
            // quiet
        }
    }

    public static void closeQuietly(Statement stmt) {
        try {
            close(stmt);
        } catch (SQLException e) { // NOPMD
            // quiet
        }
    }

    public static void close(Connection conn) throws SQLException {
        if (conn != null) {
            conn.close();
        }
    }

    public static void close(ResultSet rs) throws SQLException {
        if (rs != null) {
            rs.close();
        }
    }

    public static void close(Statement stmt) throws SQLException {
        if (stmt != null) {
            stmt.close();
        }
    }
   
}