package ttkom.tt.userinfo;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: g1yyk
 * Date: 06.05.13
 * Time: 10:01
 * To change this template use File | Settings | File Templates.
 */
public class UserInfoKabTV extends UserInfo {
    public UserInfoKabTV(String id, Connection connect) throws SQLException {
        super(id, connect);
    }

    @Override
    protected String setQueryUserInfo(Integer id) {
        String query = "";
        query = "SELECT " +
                "abn.id,abn.idDog as dognum,abn.name," +
                "abn.idAdres as id_addres," +
                "str.name as street," +
                "hs.house, abn.flat," +
                "abn.idTariff as id_tariff," +
                "tf.name as tariff," +
                "abn.phone_m," +
                "abn.phone_h," +
                "abn.phone_w," +
                "abn.idComm as id_commutator," +
                "com.name as commutator," +
                "abn.port," +
                "abn.status as id_status," +
                "st.name as status," +
                "abn.statconn as id_statconn," +
                "cv.name as statconn," +
                "abn.descr " +
                "FROM abonents abn,houses hs,streets str,tariff tf,commutator com,conn_value cv,status st " +
                "WHERE abn.id = "+id+" AND abn.idAdres = hs.id AND abn.idComm =com.id AND abn.status = st.id AND abn.statconn = cv.id AND abn.idTariff = tf.id " +
                "AND hs.id_street = str.id";

        return query;
    }
}
