package ttkom.tt.loging;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: g1yyk
 * Date: 12.02.13
 * Time: 12:26
 * To change this template use File | Settings | File Templates.
 */
public class LogAction {

    public static void insertEvent(Connection conn, Integer idSub, Integer event, Integer idOper) throws SQLException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String query = "INSERT INTO events (idAbon,idEvent,dtEvent,idOper) VALUES (" + idSub + "," + event + ",'" + format.format(new Date()) + "'," + idOper + ")";
        //System.out.println(query);
        conn.createStatement().executeUpdate(query);
    }
}
