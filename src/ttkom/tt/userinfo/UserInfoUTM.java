package ttkom.tt.userinfo;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: g1yyk
 * Date: 03.05.13
 * Time: 16:33
 * To change this template use File | Settings | File Templates.
 */
public class UserInfoUTM extends  UserInfo {

    public UserInfoUTM(String id, Connection connect) throws SQLException {
        super(id, connect);
    }

    @Override
    protected String setQueryUserInfo(Integer id) {
        String query = "";
        /*
            userInfo[0] = result.getObject("id");
            userInfo[1] = result.getObject("dognum");
            userInfo[2] = result.getObject("name");
            userInfo[3] = result.getObject("id_addres");
            userInfo[4] = result.getObject("street");
            userInfo[5] = result.getObject("house");
            userInfo[6] = result.getObject("flat");
            userInfo[7] = result.getObject("id_tariff");
            userInfo[8] = result.getObject("tariff");
            userInfo[9] = result.getObject("phone_m");
            userInfo[10] = result.getObject("phone_h");
            userInfo[11] = result.getObject("phokne_w");
            userInfo[12] = result.getObject("id_commutator");
            userInfo[13] = result.getObject("commutator");
            userInfo[14] = result.getObject("port");
            userInfo[15] = result.getObject("id_status");
            userInfo[16] = result.getObject("status");
            userInfo[17] = result.getObject("id_statconn");
            userInfo[18] = result.getObject("statconn");
            userInfo[19] = result.getObject("descr");
         */


       /* query = "SELECT abn.id,abn.idDog as dognum,abn.name " +
                "FROM abonents abn,houses hs,streets str,tariff tf,commutators com,conn_value cv,status st" +
                "WHERE abn.id = "+id+" AND abn.idAdres = hs.id AND abn.idComm =com.id AND abn.status = st.id AND abn.statconn = cv.id AND abn.idTariff = tf.id " +
                "AND hs.id_street = str.id";
       */

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
                "abn.port," +
                "abn.status as id_status," +
                "st.name as status," +
                "abn.statconn as id_statconn," +
                "cv.name as statconn," +
                "str.id as id_street," +
                "abn.descr " +
                "FROM abonents abn,houses hs,streets str,tariff tf,conn_value cv,status st " +
                "WHERE abn.id = "+id+" AND abn.idAdres = hs.id AND abn.status = st.id AND abn.statconn = cv.id AND abn.idTariff = tf.id " +
                "AND hs.id_street = str.id";
        System.out.println(query);
        return query;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
