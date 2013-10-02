package ttkom.tt.userinfo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: g1yyk
 * Date: 03.05.13
 * Time: 12:48
 * To change this template use File | Settings | File Templates.
 */
public abstract class UserInfo {
    Object[] userInfo = new Object[22];
    UserInfo(String id,Connection connect) throws SQLException {
        String query = setQueryUserInfo(Integer.parseInt(id));

        ResultSet result = connect.createStatement().executeQuery(query);

        if (result.next()) {

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
            userInfo[11] = result.getObject("phone_w");
            userInfo[12] = result.getObject("id_commutator");
            //userInfo[13] = result.getObject("commutator");
            userInfo[13] = new Object();
            userInfo[14] = result.getObject("port");
            userInfo[15] = result.getObject("id_status");
            userInfo[16] = result.getObject("status");
            userInfo[17] = result.getObject("id_statconn");
            userInfo[18] = result.getObject("statconn");
            userInfo[19] = result.getObject("descr");
            userInfo[20] = result.getString("id_street");
            //userInfo[21] = result.getString("descrComm");
            userInfo[21] = new Object();

            if (getIdCommutator()!=null && getIdCommutator()>0) {
                query = "SELECT com.name as commutator,com.descr as descrComm FROM abonents abn,commutator com WHERE abn.idComm =com.id AND abn.id = "+getIdSubscriber();
                result = connect.createStatement().executeQuery(query);
                if (result.next()) {
                    userInfo[13] = result.getObject("commutator");
                    userInfo[21] = result.getString("descrComm");
                }
            }

        } else {
            for (int i = 0; i<userInfo.length;i++) {
                userInfo[i] = new Object();
            }
        }
    }

    public Integer getIdSubscriber()     { return (Integer) userInfo[0]; }
    public String  getDogSubscriber()    { return userInfo[1]!= null ? (String) userInfo[1]: ""; }
    public String  getNameSubscriber()   { return userInfo[2]!= null ? (String) userInfo[2]: ""; }

    public Integer getIdAddres()         { return (Integer) userInfo[3]; }
    public String  getStreet()           { return userInfo[4]!= null  ? (String) userInfo[4]: ""; }
    public String  getIdStreet()         { return userInfo[20]!= null ? (String) userInfo[20]: "";}
    public String  getHouse()            { return userInfo[5]!= null  ? (String) userInfo[5]: ""; }
    public String  getFlat()             { return userInfo[6]!= null  ? (String) userInfo[6]: ""; }

    public Integer getIdTariff()         { return (Integer) userInfo[7]; }
    public Integer getTariff()           { return (Integer) userInfo[8]; }

    public String getPhoneM()            { return userInfo[9] != null ? (String) userInfo[9] : ""; }
    public String getPhomeH()            { return userInfo[10]!= null ? (String) userInfo[10]: ""; }
    public String getPhoneW()            { return userInfo[11]!= null ? (String) userInfo[11]: ""; }

    public Integer getIdCommutator()     { return (Integer) userInfo[12];}
    public String  getCommutator()       { return userInfo[13]!= null ? (String) userInfo[13]: "";}
    public Integer getPort()             { return userInfo[14]!= null ? (Integer) userInfo[14]: 0;}

    public Integer getIdStatus()         { return (Integer) userInfo[15];}
    public String  getStatus()           { return userInfo[16]!= null ? (String) userInfo[16]: "";}

    public Integer getIdStatConn()       { return (Integer) userInfo[17];}
    public String  getStatConn()         { return userInfo[18]!= null ? (String) userInfo[18]: "";}

    public String getDescrSubscriber()   { return userInfo[19]!= null ? (String) userInfo[19]: "";}
    public String getDescrComm()         { return userInfo[21]!= null ? (String) userInfo[21]: "";}


    protected abstract String setQueryUserInfo(Integer id);











}
