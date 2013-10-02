package ttkom.tt.windows.modal.card.funmodal;

import net.miginfocom.swing.MigLayout;
import ttkom.tt.windows.MainWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: g1yyk
 * Date: 10.06.13
 * Time: 10:00
 * To change this template use File | Settings | File Templates.
 */
public class PyamentModal extends AbModalWindows {

    JButton addPayment,clear,closeWin;
    ArrayList<String[]> tarifs = new ArrayList<String[]>();
    JComboBox tariff,count;
    JTextField summa;
    MainWindow mainClass;
    Integer id_sub;

    public PyamentModal(Window frame, String title,MigLayout layout, MainWindow mainClass,Integer id_sub,Integer id_tariff) throws SQLException {
        super(frame, title, layout);
        this.mainClass = mainClass;
        this.id_sub = id_sub;
        //Тариф,кол-во,сумма
        //add,clear,summ
        tariff = setComboBox(getListTariff(mainClass.getConnectDB().getConnect(),id_sub),id_tariff.toString());
        count  = setComboBox(getListCount(),"1");
        summa = setTextField();
        getCostPeriod(tariff,count,summa);
        tariff.addActionListener(new setCostPeriod());
        count.addActionListener(new setCostPeriod());

        getWindow().setSize(500, 110);
        getWindow().setLocation((getSizeWindow().width-getWindow().getSize().width)/2,(getSizeWindow().height-getWindow().getSize().height)/2);

        getMainPanel().setLayout(new MigLayout("wrap 3","grow,fill"));
        setHeader();
        getMainPanel().add(tariff);
        getMainPanel().add(count);
        getMainPanel().add(summa);

        addPayment  = setButton(3, "Добавить"   ,"plus.png");   addPayment  .setName("add");    addPayment.addActionListener(new setActionToButton(0));
        clear       = setButton(3, "Очистить"   ,"clear.png");  clear       .setName("clear");
        closeWin    = setButton(3, "Закрыть"    ,"cancel.png"); closeWin    .setName("close");

        getMainPanel().add(addPayment);
        getMainPanel().add(clear);
        getMainPanel().add(closeWin);

        getWindow().setModal(true);
        getWindow().setVisible(true);
        getWindow().validate();
        getWindow().repaint();

    }

    public void setHeader() {
        getMainPanel().add(new JLabel("Тариф:"));
        getMainPanel().add(new JLabel("Кол-во:"));
        getMainPanel().add(new JLabel("К оплате:"));
    }

    private ArrayList<String[]> getListTariff(Connection connect,Integer id_sub) throws SQLException {


        String query = "SELECT tf.id,tf.name,etf.sum FROM tariff tf,abonents abn,ext_tariff etf WHERE tf.id_service = abn.id_service AND etf.idtarif=tf.id AND abn.id="+id_sub+" ORDER BY etf.datebegin ASC";
        ResultSet result = connect.createStatement().executeQuery(query);

        while (result.next())
            tarifs.add(new String[]{ result.getString("id"),result.getString("name"),result.getString("sum")});

        return tarifs;
    }

    private ArrayList<String[]> getListCount() {
        ArrayList<String[]> frame = new ArrayList<String[]>();
        for (int i = 1;i<13;i++)
            frame.add(new String[]{i+"",i+""});
        return frame;
    }

    private void getCostPeriod(JComboBox tariff,JComboBox count,JTextField summa) {
        if (tariff.getSelectedIndex()>0 && count.getSelectedIndex()>0) {
            Integer cost = Integer.valueOf(tarifs.get(tariff.getSelectedIndex()-1)[2])*count.getSelectedIndex();
            summa.setText(cost.toString());
        }
    }

    class setCostPeriod implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            getCostPeriod(tariff,count,summa);
        }
    }

    class setActionToButton implements ActionListener {
        Integer type;
        setActionToButton(Integer type) { this.type = type; }

        @Override
        public void actionPerformed(ActionEvent e) {
            switch (type) {
                case 0:
                    try {
                        addPayment(mainClass.getConnectDB().getConnect(),getCost(),getId_sub());
                    } catch (SQLException e1) {
                        e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                    break;
                case 1:
                    break;
                case 2:
                    break;
            }
        }
    }
    private String getCost() { return summa.getText();}
    private Integer getId_sub() { return id_sub; }
    private void addPayment(Connection connect,String cost,Integer id_sub) throws SQLException {
        System.out.println(cost+"   "+id_sub);
        /*
         SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = new Date();
                    String strDate = formatDate.format(date);


                    String query = "INSERT INTO payment (`date`,`cost`,`idAbonent`,`idOperator`) " +
                            "VALUES ('" + strDate + "'," + cost.getText() + "," + idAbonent + "," + mainWindow.getOperator()[4] + ")";
         */

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = dateFormat.format(new Date());

        String query =  "INSERT INTO payment (date,cost,idAbonent,idOperator) " +
                        "VALUES ('"+strDate+"',"+cost+","+id_sub+","+mainClass.getOperator()[4]+")";
        mainClass.getConnectDB().getConnect().createStatement().executeUpdate(query);
        System.out.println("Добавлю лог");
        /*
        String old,String id_sub,String id_oper,Connection conn,Integer type
         */
        //logString(id_sub.toString(),cost,mainClass.getOperator()[4],mainClass.getConnectDB().getConnect(),71);
        logString(cost,id_sub.toString(),mainClass.getOperator()[4],mainClass.getConnectDB().getConnect(),71);
        /*
        logString(getNumDog().getName(),userInfo.getDogSubscriber().toString(),mainWindow.getOperator()[4],mainWindow.getConnectDB().getConnect(),51);
         */

        getWindow().dispose();
    }
}
