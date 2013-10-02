package ttkom.tt.windows.modal.card.subscriber;

import net.miginfocom.swing.MigLayout;
import ttkom.tt.AddCommutator;
import ttkom.tt.userinfo.UserInfo;
import ttkom.tt.userinfo.UserInfoUTM;
import ttkom.tt.windows.MainWindow;
import ttkom.tt.windows.modal.card.funmodal.DisconnectModal;
import ttkom.tt.windows.modal.card.funmodal.PyamentModal;
import ttkom.tt.windows.modal.card.funmodal.TestModal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: g1yyk
 * Date: 06.05.13
 * Time: 10:53
 * To change this template use File | Settings | File Templates.
 */
public class CardSubscriberTL extends AbCardSubscriber {

    UserInfo userInfo;

    public CardSubscriberTL(JFrame frame, MainWindow mainMenu,String idSubscriber) throws SQLException {

        super(frame, mainMenu,idSubscriber);
        userInfo = setUserInfo(idSubscriber,mainMenu.getConnectDB().getConnect());

        getSave().addActionListener(new SaveForm());
        getUpdateList().addActionListener(new UpdatePaymentList());
        getUpdateEvent().addActionListener(new UpdateEventList());
        getStreet().addActionListener(new UpdateListHouses());
        getEditComm().addActionListener(new EditCommutator());
        getCheckPort().addActionListener(new CheckPortUse(Integer.parseInt(idSubscriber)));
        getDisconnectSub().addActionListener(new DisconnectSub(Integer.parseInt(idSubscriber)));

        getAddPayment().addActionListener(new TestClass(Integer.parseInt(idSubscriber),mainMenu.getConnectDB().getConnect()));


        getWindow().setModal(true);
        getWindow().setVisible(true);
        getWindow().validate();
        getWindow().repaint();
    }

    @Override
    UserInfo setUserInfo(String idSubscriber, Connection conn) throws SQLException {
        UserInfo userInfo = new UserInfoUTM(idSubscriber,conn);
        return userInfo;
    }

    @Override
    HashMap getStreetArray(Connection conn) throws SQLException {
        HashMap<Integer,String> map = new HashMap<Integer, String>();
        String query = "SELECT * FROM streets ORDER BY name ASC";
        ResultSet result = conn.createStatement().executeQuery(query);
        map.put(0,"");
        while (result.next())
            map.put(result.getInt("id"),result.getString("name"));

        return map;
    }

    @Override
    HashMap getHouseArray(Connection conn,String id_street) throws SQLException {
        HashMap<Integer,String> map = new HashMap<Integer, String>();
        String query = "SELECT * FROM houses WHERE id_street="+id_street+" ORDER BY house ASC";
        ResultSet result = conn.createStatement().executeQuery(query);
        map.put(0,"");
        while (result.next())
            map.put(result.getInt("id"),result.getString("house"));

        return map;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    HashMap getTariffArray(Connection conn) throws SQLException {
        HashMap<Integer,String> map = new HashMap<Integer, String>();
        String query = "SELECT * FROM tariff ORDER BY name ASC";
        ResultSet result = conn.createStatement().executeQuery(query);
        map.put(0,"");
        while (result.next())
            map.put(result.getInt("id"),result.getString("name"));

        return map;
    }

    @Override
    HashMap getCommutatorArray(Connection conn) throws SQLException {
        HashMap<Integer,String> map = new HashMap<Integer, String>();
        String query = "SELECT * FROM commutator ORDER BY name ASC";
        ResultSet result = conn.createStatement().executeQuery(query);
        map.put(0,"");
        while (result.next())
            map.put(result.getInt("id"),result.getString("name"));

        return map;
    }

    @Override
    ArrayList<String[]> getListPayment(Connection conn,Integer id_sub) throws SQLException {
        ArrayList<String[]> array = new ArrayList<String[]>();
        String query = "" +
                "SELECT pm.date as date_pay,pm.cost as summ,op.name as operator " +
                "FROM payment pm,operator op " +
                "WHERE op.id = pm.idOperator AND idAbonent = "+id_sub;
        ResultSet result = conn.createStatement().executeQuery(query);

        while (result.next())
            array.add(new String[]{
                    result.getString("date_pay"),
                    result.getString("summ"),
                    result.getString("operator")
            });

        return array;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    ArrayList<String[]> getListEvent(Connection conn,Integer id_sub) throws SQLException {
        ArrayList<String[]> array = new ArrayList<String[]>();
        String query = "" +
                "SELECT evs.dtEvent as date_event,op.name as operator,evs_s.name as status,evs.value " +
                "FROM events evs,events_status evs_s,operator op " +
                "WHERE evs.idEvent = evs_s.code AND op.id = evs.idOper AND idAbon = "+id_sub;
        ResultSet result = conn.createStatement().executeQuery(query);

        while (result.next())
            array.add(new String[]{
                    result.getString("date_event"),
                    result.getString("status"),
                    result.getString("value"),
                    result.getString("operator")
            });
        return array;
    }

    @Override
    void logString(String old,String id_sub,String id_oper,Connection conn,Integer type) throws SQLException {
        SimpleDateFormat simDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String query = "INSERT INTO events (idAbon,idEvent,value,dtEvent,idOper) VALUES ("+id_sub+","+type+",'"+old+"','"+simDate.format(new Date())+"',"+id_oper+")";
        conn.createStatement().executeUpdate(query);
    }

    @Override
    String setQueryUpdate() throws SQLException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    String setQueryInsert() throws SQLException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    class UpdateEventList implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                updateEventTable();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }

    class UpdatePaymentList implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                updatePayTable();
            } catch (SQLException e1) {
                e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }

    class SaveForm implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            try {
                String query = checkForUpdate();
                if (query.trim().length()>0)
                    getUpdateSubscriber(query,userInfo.getIdSubscriber().toString());
                getWindow().dispose();

            } catch (SQLException e1) {
                e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }

    class UpdateListHouses implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String idAddres = "";
            for (Object i:getListStreet().keySet())
                if (getListStreet().get(i)==getStreet().getSelectedItem())
                    idAddres = i.toString();

                if (getHouse() != null) {
                    try {
                        getHouse().removeAllItems();
                        HashMap map = getHouseArray(mainWindow.getConnectDB().getConnect(),idAddres);
                        for (Object i:map.keySet()) getHouse().addItem(map.get(i));
                    } catch (SQLException e1) {
                        e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                }
        }
    }

    class EditCommutator implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            for (Object key : getListCommutator().keySet()) {
                if (getCommutator().getSelectedItem().equals(getListCommutator().get(key))) {
                    try {
                        new AddCommutator(mainWindow, getWindow(), (Integer) key);
                    } catch (ParseException e1) {
                        e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    } catch (SQLException e1) {
                        e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                }
            }

        }
    }

    class CheckPortUse implements ActionListener {
        Integer id_subscriber;
        CheckPortUse(Integer subscriber) {
            id_subscriber = subscriber;
        }

        @Override
        public void actionPerformed(ActionEvent e) {

            String idComm = null;

            for (Object key : getListCommutator().keySet())
                if (getCommutator().getSelectedItem().equals(getListCommutator().get(key)))
                    idComm = key.toString();


            String query = "SELECT ab.id,ab.port FROM commutator com, abonents ab WHERE ab.idComm = com.id AND com.id ="+idComm;

            try {
                String listport = "";
                Integer id_sub = 0;
                ResultSet resultSet = mainWindow.getConnectDB().getConnect().createStatement().executeQuery(query);
                System.out.println(query);
                while (resultSet.next()) {
                    System.out.println(resultSet.getInt("id")+" "+resultSet.getString("port"));
                    listport += resultSet.getString("port")+",";
                    if (resultSet.getInt("id") == id_subscriber)
                        id_sub = resultSet.getInt("id");

                }

                getPort().setToolTipText(listport.length()>0 ? listport:"Все свободны");
                String[] listPorts = listport.split(",");

                for (int i=0;i<listPorts.length;i++) {
                    if (listPorts[i].equals(getPort().getText().trim())) {
                        if (getPort().getText().trim().equals(userInfo.getPort().toString())) {
                            getPort().setBackground(Color.GREEN);
                        } else {
                            getPort().setBackground(Color.RED);
                        }
                    } else {
                        getPort().setBackground(Color.GREEN);
                    }
                }
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }

    String checkForUpdate() throws SQLException {
        String query = "";
        if (is_change(getNumDog().getName(),getNumDog().getText())) {
            try {
                if (query.length()>0) query += ",idDog="+getNumDog().getText().trim();
                else query += "idDog="+getNumDog().getText().trim();
                logString(getNumDog().getName(),userInfo.getDogSubscriber().toString(),mainWindow.getOperator()[4],mainWindow.getConnectDB().getConnect(),51);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
        if (is_change(getFio().getName(), getFio().getText())) {
            try {
                if (query.length()>0) query += ",name='"+getFio().getText().trim()+"'";
                else query += "name='"+getFio().getText().trim()+"'";
                logString(getFio().getName(),Integer.toString(userInfo.getIdSubscriber()),mainWindow.getOperator()[4],mainWindow.getConnectDB().getConnect(),52);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }        if (is_change(getStreet().getName(),(String) getStreet().getSelectedItem())) {
            try {
                logString((String) getStreet().getSelectedItem(),Integer.toString(userInfo.getIdSubscriber()),mainWindow.getOperator()[4],mainWindow.getConnectDB().getConnect(),57);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }

        if (is_change(getHouse().getName(),(String) getHouse().getSelectedItem())) {
            try {
                logString((String) getHouse().getSelectedItem(),Integer.toString(userInfo.getIdSubscriber()),mainWindow.getOperator()[4],mainWindow.getConnectDB().getConnect(),58);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }

        if (is_change(getStreet().getName(),(String) getStreet().getSelectedItem()) || is_change(getHouse().getName(),(String) getHouse().getSelectedItem())) {
            Integer id_street = null,id_house = null,id=null;

            for (Object i:getListStreet().keySet())
                if (getListStreet().get(i).equals(getStreet().getSelectedItem()))
                    id_street = (Integer) i;
            for (Object i:getListHouse().keySet())
                if (getListHouse().get(i).equals(getHouse().getSelectedItem()))
                    id_house = (Integer) i;
            if ((id = getIdNewAddres(Integer.toString(id_street),(String) getHouse().getSelectedItem()))!=null)
                if (query.length()>0) query += ",idAdres="+id;
                else query += "idAdres="+id;
        }

        if (is_change(getFlat().getName(), getFlat().getText())) {
            try {
                if (query.length()>0) query += ",flat='"+getFlat().getText().trim()+"'";
                else query += "flat='"+getFlat().getText().trim()+"'";

                logString(getFlat().getName(),Integer.toString(userInfo.getIdSubscriber()),mainWindow.getOperator()[4],mainWindow.getConnectDB().getConnect(),59);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }

        if (is_change(getPhone_h().getName(), getPhone_h().getText())) {
            try {
                if (query.length()>0) query += ",phone_h='"+getPhone_h().getText().trim().replaceAll("-", "")+"'";
                else query += "phone_h='"+getPhone_h().getText().trim().replaceAll("-", "")+"'";

                logString(getPhone_h().getName(),Integer.toString(userInfo.getIdSubscriber()),mainWindow.getOperator()[4],mainWindow.getConnectDB().getConnect(),53);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }

        if (is_change(getPhone_m().getName(), getPhone_m().getText())) {
            try {
                if (query.length()>0) query += ",phone_m='"+getPhone_m().getText().trim().replaceAll("-", "")+"'";
                else query += "phone_m='"+getPhone_m().getText().trim().replaceAll("-", "")+"'";
                logString(getPhone_m().getName(),Integer.toString(userInfo.getIdSubscriber()),mainWindow.getOperator()[4],mainWindow.getConnectDB().getConnect(),54);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }

        if (is_change(getPhone_w().getName(), getPhone_w().getText())) {
            try {
                if (query.length()>0) query += ",phone_w='"+getPhone_w().getText().trim().replaceAll("-","")+"'";
                else query += "phone_w='"+getPhone_w().getText().trim().replaceAll("-","")+"'";
                logString(getPhone_w().getName(),Integer.toString(userInfo.getIdSubscriber()),mainWindow.getOperator()[4],mainWindow.getConnectDB().getConnect(),55);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }

        if (is_change(getTariff().getName(),(String) getTariff().getSelectedItem())) {
            try {
                for (Object i:getListTariff().keySet())
                    if (getListTariff().get(i).equals(getTariff().getSelectedItem()))
                        if (query.length()>0) query += ",idTariff="+i;
                        else query += "idTariff="+i;
                logString((String) getTariff().getSelectedItem(),Integer.toString(userInfo.getIdSubscriber()),mainWindow.getOperator()[4],mainWindow.getConnectDB().getConnect(),61);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }

        if (is_change(getCommutator().getName(),(String) getCommutator().getSelectedItem())) {
            try {
                for (Object i:getListCommutator().keySet())
                    if (getListCommutator().get(i).equals(getCommutator().getSelectedItem()))
                        if (query.length()>0) query += ",idComm="+i;
                        else query += "idComm="+i;
                logString((String) getCommutator().getSelectedItem(),Integer.toString(userInfo.getIdSubscriber()),mainWindow.getOperator()[4],mainWindow.getConnectDB().getConnect(),81);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }

        if (is_change(getPort().getName(), getPort().getText())) {
            try {
                if (query.length()>0) query += ",port='"+getPort().getText().trim()+"'";
                else query += "port='"+getPort().getText().trim()+"'";
                logString(getPort().getName(),Integer.toString(userInfo.getIdSubscriber()),mainWindow.getOperator()[4],mainWindow.getConnectDB().getConnect(),82);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }

        if (is_change(getDescrArea().getName(), getDescrArea().getText())) {
            try {
                if (query.length()>0) query += ",descr='"+getDescrArea().getText().trim()+"'";
                else query += "descr='"+getDescrArea().getText().trim()+"'";
                logString(getDescrArea().getText(),Integer.toString(userInfo.getIdSubscriber()),mainWindow.getOperator()[4],mainWindow.getConnectDB().getConnect(),56);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
        return query;
    }


    void getUpdateSubscriber(String set_argument,String id_subscriber) throws SQLException {
        String query = "Update abonents SET "+set_argument+" WHERE id="+id_subscriber;
        mainWindow.getConnectDB().getConnect().createStatement().executeUpdate(query);
    }

    Integer getIdNewAddres(String idStreet,String house) throws SQLException {
        if (house!= null && house.trim().length()>0) {
            String query = "SELECT id FROM houses WHERE house="+house+" AND id_street="+idStreet;
            ResultSet result = mainWindow.getConnectDB().getConnect().createStatement().executeQuery(query);
            if (result.next())
                return result.getInt("id");
        }

        JOptionPane.showMessageDialog(getWindow(), "Не выбранн дом", "Ошибка", JOptionPane.WARNING_MESSAGE);
        return null;
    }
    /*
    Исправить класс. Нормальное название
     */
    class TestClass implements ActionListener {
        Integer idSubscriber;
        Connection connect;

        TestClass(Integer id_sub,Connection connect) {
            idSubscriber = id_sub;
            this.connect = connect;
        }


        @Override
        public void actionPerformed(ActionEvent e) {
            /*new TestModal(idSubscriber,userInfo.getIdTariff(),"Добавление платежа",(Window) getWindow(),connect);*/
            System.out.println("Тест");
            try {
                new PyamentModal((Window) getWindow(),"Add Payment",new MigLayout(),mainWindow,idSubscriber,userInfo.getIdTariff());
                updatePayTable();
                updateEventTable();
            } catch (SQLException e1) {
                e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

        }
    }
    /*
    Исправить класс
     */

    class DisconnectSub implements ActionListener {
        Integer id_sub;
        DisconnectSub(Integer id_sub) { this.id_sub = id_sub; }
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                new DisconnectModal((Window) getWindow(),"Отключение абонента",new MigLayout(),mainWindow,this.id_sub);
            } catch (SQLException e1) {
                e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }

}
