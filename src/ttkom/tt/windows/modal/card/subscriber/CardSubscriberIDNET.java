package ttkom.tt.windows.modal.card.subscriber;

import ttkom.tt.userinfo.UserInfo;
import ttkom.tt.userinfo.UserInfoKabTV;
import ttkom.tt.userinfo.UserInfoUTM;
import ttkom.tt.windows.MainWindow;

import javax.swing.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: g1yyk
 * Date: 06.05.13
 * Time: 10:52
 * To change this template use File | Settings | File Templates.
 */
public class CardSubscriberIDNET extends AbCardSubscriber {

    CardSubscriberIDNET(JFrame frame, MainWindow mainMenu,String idSubscriber) throws SQLException {
        super(frame, mainMenu,idSubscriber);
        setUserInfo(idSubscriber,mainMenu.getConnectDB().getConnect());

        //new UserInfoKabTV("842",mainMenu.getConnectDB().getConnect());
    }

    @Override
    UserInfo setUserInfo(String idSubscriber, Connection conn) throws SQLException {
        return new UserInfoKabTV(idSubscriber,conn);
    }

    @Override
    HashMap getStreetArray(Connection conn) throws SQLException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    HashMap getHouseArray(Connection conn, String id_street) throws SQLException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    HashMap getTariffArray(Connection conn) throws SQLException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    HashMap getCommutatorArray(Connection conn) throws SQLException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    ArrayList<String[]> getListPayment(Connection conn, Integer id_sub) throws SQLException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    ArrayList<String[]> getListEvent(Connection conn, Integer id_sub) throws SQLException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    void logString(String old, String id_sub, String id_oper, Connection conn, Integer type) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    String setQueryUpdate() throws SQLException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    String setQueryInsert() throws SQLException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }


}
