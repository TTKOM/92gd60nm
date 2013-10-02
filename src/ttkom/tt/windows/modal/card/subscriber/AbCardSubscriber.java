package ttkom.tt.windows.modal.card.subscriber;


import net.miginfocom.swing.MigLayout;
import ttkom.tt.userinfo.UserInfo;
import ttkom.tt.userinfo.UserInfoKabTV;
import ttkom.tt.userinfo.UserInfoUTM;
import ttkom.tt.windows.MainWindow;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: g1yyk
 * Date: 03.05.13
 * Time: 12:32
 * Abstract class for create modal window the with info about subscriber
 */
public abstract class AbCardSubscriber {


    protected MainWindow mainWindow;
    private JDialog window;
    private JPanel mainPanel,leftPanel,rightPanel;
    private Dimension screenSize;
    private UserInfo userInfo;
    /* Левая панель */
    private JTextField numDog,fio,flat,phone_h,phone_m,phone_w,port;
    private JComboBox tariff;
    private JComboBox street;
    public JComboBox house;
    private JComboBox commutator;
    private JScrollPane frameDescr;
    private JTextArea descr;

    private JButton addAdres,addComm,checkPort,editComm;
    private JButton save,close;
    /* Правая панель */
    private JTabbedPane mainTabs;
    private JButton addPayment,updateList,updateEvent,connectSub,disconnSub,freezySub,unfreezySub;
    private JTable paymentTable,eventTable;
    private JScrollPane payScroll,eventScroll;
    private TableModel payModel,eventModel;
    private HashMap listStreet,listHouse,listTariff,listCommutator;


    AbCardSubscriber(JFrame frame, MainWindow mainMenu,String idSubscriber) throws SQLException {
        setMainWindow(mainMenu);
        window = new JDialog(frame);
        userInfo = setUserInfo(idSubscriber,mainMenu.getConnectDB().getConnect());

        MigLayout mainLayout    =   new MigLayout("wrap 2","[450]5[800]","[grow, fill][grow,fill]");
        MigLayout leftLayout    =   new MigLayout("wrap 3", "[]5[300]5[20]");
        MigLayout rightLayout   =   new MigLayout("wrap 1","grow,fill");

        mainPanel = new JPanel(mainLayout);
        leftPanel = new JPanel(leftLayout);   leftPanel.setMinimumSize(new Dimension(410, 450));
        rightPanel = new JPanel(rightLayout);

        mainPanel.add(addCompToLeftPanel());
        mainPanel.add(addCompToRightPanel(),"grow");
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        window.add(mainPanel);
        window.setSize(950, 550);
        window.setLocation(screenSize.width / 2 - window.getSize().width / 2, screenSize.height / 2 - window.getSize().height / 2);

    }

    abstract UserInfo setUserInfo(String idSubscriber,Connection conn) throws SQLException;
    abstract HashMap getStreetArray(Connection conn) throws SQLException;
    abstract HashMap getHouseArray(Connection conn,String id_street) throws SQLException;
    abstract HashMap getTariffArray(Connection conn) throws SQLException;
    abstract HashMap getCommutatorArray(Connection conn) throws SQLException;
    abstract ArrayList<String[]> getListPayment(Connection conn,Integer id_sub) throws SQLException;
    abstract ArrayList<String[]> getListEvent(Connection conn,Integer id_sub) throws SQLException;
    abstract void logString(String old,String id_sub,String id_oper,Connection conn,Integer type) throws SQLException;
    abstract String setQueryUpdate() throws SQLException;
    abstract String setQueryInsert() throws SQLException;




    private JPanel addCompToLeftPanel() throws SQLException {

        setNumDog(getTextField(mainWindow.getRules(), "DOGNUM", userInfo.getDogSubscriber().toString()));
        setFio(getTextField(mainWindow.getRules(),"ABON",String.valueOf(userInfo.getNameSubscriber())));

        setTariff(getComboBox(mainWindow.getRules(),"TARIFF",setListTariff(),userInfo.getIdTariff()));


        setStreet(getComboBox(mainWindow.getRules(),"ADDRES",setListStreet(),Integer.parseInt(userInfo.getIdStreet())));
        setAddAdres(getButton(mainWindow.getRules(),"ADDADRESS","plus.png",""));
        setHouse(getComboBox(mainWindow.getRules(),"ADDRES",setListHouse(),userInfo.getIdAddres()));
        setFlat(getTextField(mainWindow.getRules(),"ADDRES",String.valueOf(userInfo.getFlat())));

        setPhone_h(getTextField(mainWindow.getRules(),"ABON",String.valueOf(userInfo.getPhomeH())));
        setPhone_m(getTextField(mainWindow.getRules(), "ABON", String.valueOf(userInfo.getPhoneM())));
        setPhone_w(getTextField(mainWindow.getRules(), "ABON", String.valueOf(userInfo.getPhoneW())));

        setCommutator(getComboBox(mainWindow.getRules(),"CONNECT",setListCommutator(),userInfo.getIdCommutator()));
        setAddComm(getButton(mainWindow.getRules(),"ADDCOMM","plus.png",""));
        setPort(getTextField(mainWindow.getRules(),"CONNECT",String.valueOf(userInfo.getPort())));
        setCheckPort(getButton(mainWindow.getRules(),"CHECKPORT","eye.png",""));
        setEditComm(getButton(mainWindow.getRules(),"EDITPORT","edit.png",""));
        setDescr(getTextArea(mainWindow.getRules(), "DESCR", String.valueOf(userInfo.getDescrSubscriber())));

        setSave(getButton(mainWindow.getRules(), "SAVECARD", "", "Сохранить"));
        setClose(getButton(mainWindow.getRules(), "CLOSECARD", "", "Закрыть"));

        leftPanel.add(new JLabel("Номер договора:"), "grow");
        leftPanel.add(getNumDog(),"grow,span 2,wrap");

        leftPanel.add(new JLabel("ФИО:"),"grow");
        leftPanel.add(getFio(),"grow,span 2,wrap");

        leftPanel.add(new JLabel("Тариф:"),"grow");
        leftPanel.add(getTariff(),"grow,span 2,wrap");

        leftPanel.add(new JSeparator(SwingConstants.HORIZONTAL),"grow,span 3,wrap");

        leftPanel.add(new JLabel("Улица:"),"grow, ");
        leftPanel.add(getStreet(),"grow,h 20!");

        leftPanel.add(getAddAdres(),"growy,span 1 3");

        leftPanel.add(new JLabel("Дом:"),"grow");
        leftPanel.add(getHouse(),"grow");

        leftPanel.add(new JLabel("Квартира:"),"grow");
        leftPanel.add(getFlat(),"grow");

        leftPanel.add(new JSeparator(SwingConstants.HORIZONTAL),"grow,span 3,wrap");

        leftPanel.add(new JLabel("Телфон домашний:"),"grow");
        leftPanel.add(getPhone_h(),"grow,span 2,wrap");

        leftPanel.add(new JLabel("Телефон мобильный:"),"grow");
        leftPanel.add(getPhone_m(),"grow,span 2,wrap");

        leftPanel.add(new JLabel("Телефон рабочий:"),"grow");
        leftPanel.add(getPhone_w(),"grow,span 2,wrap");

        leftPanel.add(new JSeparator(SwingConstants.HORIZONTAL),"grow,span 3,wrap");

        leftPanel.add(new JLabel("Имя коммутатора:"),"grow");
        leftPanel.add(getCommutator(),"grow,split 2");
        leftPanel.add(getAddComm(),"grow,split 2");
        leftPanel.add(getEditComm(),"grow,split 2,wrap");


        leftPanel.add(new JLabel("Порт:"),"grow");
        leftPanel.add(getPort(),"grow");
        leftPanel.add(getCheckPort(),"grow");

        leftPanel.add(new JLabel("Статус подключения:"));
        leftPanel.add(new JLabel("<html><b style='color:red';>"+userInfo.getStatus()+" "+userInfo.getStatConn()+"</b></html>"),"grow,span 2");

        leftPanel.add(new JLabel("Примечание:"),"grow,wrap");
        leftPanel.add(getDescr(),"grow,span 3,wrap");

        /*
        Панель с кнопками для сохранения/закрытия формы
         */
        MigLayout buttonLayout   =   new MigLayout("wrap 2","grow,fill");
        JPanel buttonPanel = new JPanel(buttonLayout);
        buttonPanel.add(getSave()); buttonPanel.add(getClose());

        leftPanel.add(buttonPanel,"grow,span 3");

        return leftPanel;
    }
    private JPanel addCompToRightPanel() throws SQLException {
        mainTabs = new JTabbedPane();
        addTab(mainWindow.getRules(),"PAYMENT","Платежи","connect.png",setPanelTab("payment",0),"Список платежей абонента",mainTabs);
        addTab(mainWindow.getRules(),"EVENT","События","connect.png",setPanelTab("event",1),"Список всех изменений по абоненту",mainTabs);
        addTab(mainWindow.getRules(),"ACTION","Действия","connect.png",setPanelTab("action",2),"Подключение/Отключение/Заморозка",mainTabs);
        rightPanel.add(mainTabs);

        return rightPanel;
    }
    private void setMainWindow(MainWindow mainWindow) { this.mainWindow = mainWindow; }

    private JTextField getTextField(HashMap rules,String type,String variable) {
        JTextField frame = new JTextField();
        if (rules.get(type+"_EDIT")!=null) {
            frame.setText(variable);
            frame.setName(variable);
            return frame;
        } else if (rules.get(type+"_SEE")!=null) {
            frame.setText(variable);
            frame.setEditable(false);
            frame.setName(variable);
            return frame;
        } else {
            frame.setText("У вас нет прав");
            frame.setName("У вас нет прав");
            frame.setEditable(false);
            return frame;
        }
    }
    private JScrollPane getTextArea(HashMap rules,String type,String variable) {

        JTextArea frame = new JTextArea();
        frame.setRows(7);
        JScrollPane scrollPane = new JScrollPane(frame);

        if (rules.get(type+"_EDIT")!=null) {
            frame.setText(variable);
            frame.setName(variable);
            descr = frame;
            return scrollPane;
        } else if (rules.get(type+"_SEE")!=null) {
            frame.setText(variable);
            frame.setEditable(false);
            frame.setName(variable);
            descr = frame;
            return scrollPane;
        } else {
            frame.setText(variable);
            frame.setName(variable);
            frame.setEnabled(false);
            frame.setEditable(false);
            descr = frame;
            return scrollPane;
        }
    }
    JComboBox getComboBox(HashMap rules, String type, HashMap variable, Integer select) {
        JComboBox frame = new JComboBox();
        for (Object i:variable.keySet())
            frame.addItem(variable.get(i));

        for(int i =0;i<frame.getItemCount();i++)
            if (frame.getItemAt(i)==variable.get(select))
                frame.setSelectedIndex(i);

        if (frame.getSelectedItem().toString().length()>0)
            frame.setName(frame.getSelectedItem().toString());
        else
            frame.setName(frame.getItemAt(0).toString());

        if (rules.get(type + "_EDIT")!=null) {
            return frame;
        } else if (rules.get(type+"_SEE")!=null) {
            frame.setEnabled(false);
            return frame;
        } else {
            frame = new JComboBox();
            return frame;
        }
    }

    private JButton getButton(HashMap rules,String type,String img,String text) {
        JButton frame = new JButton();
        frame.setEnabled(false);
        if (img.length()>0)
            frame.setIcon(new ImageIcon(getClass().getClassLoader().getResource("ttkom/tt/img/"+img)));
        else
            frame.setText(text);
        if (rules.get(type + "_EDIT")!=null) {
            frame.setEnabled(true);
            return frame;
        } else if (rules.get(type+"_SEE")!=null) {
            return frame;
        } else {
            return frame;
        }
    }
    private JButton getButtonText(HashMap rules,String type,String img,String text) {
        JButton frame = new JButton();
        frame.setEnabled(false);
        frame.setIcon(new ImageIcon(getClass().getClassLoader().getResource("ttkom/tt/img/"+img)));
        frame.setText(text);

        if (rules.get(type + "_EDIT")!=null) {
            frame.setEnabled(true);
            return frame;
        } else if (rules.get(type+"_SEE")!=null) {
            frame.setEnabled(false);
            return frame;
        } else {
            return frame;
        }
    }

    JDialog getWindow() { return window; }

    HashMap setListHouse() throws SQLException {
        return listHouse = getHouseArray(mainWindow.getConnectDB().getConnect(),userInfo.getIdStreet());
    }
    HashMap getListHouse() { return listHouse; }

    HashMap setListStreet() throws SQLException {
        return listStreet = getStreetArray(mainWindow.getConnectDB().getConnect());
    }
    HashMap getListStreet() { return listStreet; }

    HashMap setListTariff() throws SQLException {
        return listTariff = getTariffArray(mainWindow.getConnectDB().getConnect());
    }
    HashMap getListTariff() { return listTariff; }

    HashMap setListCommutator() throws SQLException {
        return listCommutator = getCommutatorArray(mainWindow.getConnectDB().getConnect());
    }
    HashMap getListCommutator() { return listCommutator; }


    JTextField getNumDog() { return numDog; }
    void setNumDog(JTextField arg) {numDog = arg;}

    JTextField getFio() { return fio; }
    void setFio(JTextField arg) { fio = arg; }

    JComboBox getTariff() { return tariff; }
    void setTariff(JComboBox arg) { tariff = arg;}

    JComboBox getStreet() { return street; }
    void setStreet(JComboBox arg) { street = arg;}

    JComboBox getHouse() { return house; }
    void setHouse(JComboBox arg) { house = arg; }
    void removeHouse() {
        if (house!=null) {
            house.removeItem(0);
        }
    }

    String getHouseName() { return house.getName();}

    JTextField getFlat() { return flat;}
    void setFlat(JTextField arg) { flat = arg; }

    JTextField getPhone_h() { return phone_h;}
    void setPhone_h(JTextField arg) { phone_h = arg; }

    JTextField getPhone_m() { return phone_m;}
    void setPhone_m(JTextField arg) { phone_m=arg;}

    JTextField getPhone_w() { return phone_w;}
    void setPhone_w(JTextField arg) {phone_w = arg;}

    JComboBox getCommutator() { return commutator; }
    void setCommutator(JComboBox arg) { commutator = arg;}

    JTextField getPort() { return (port == null) ? new JTextField() : port; }
    void setPort(JTextField arg) { port = arg;}

    JScrollPane getDescr() {  return (frameDescr == null)? new JScrollPane() : frameDescr; }
    void setDescr(JScrollPane arg) { frameDescr = arg; }
    JTextArea getDescrArea() { return descr; }

    JButton getAddAdres() { return addAdres; }
    void setAddAdres(JButton arg) { addAdres = arg; }

    JButton getAddComm() { return addComm;}
    void setAddComm(JButton arg) { addComm = arg; }

    JButton getCheckPort() { return  checkPort; }
    void  setCheckPort(JButton arg) { checkPort = arg; }

    JButton getEditComm() { return editComm; }
    void setEditComm(JButton arg) { editComm = arg; }

    JButton getSave() { return  save; }
    void setSave(JButton arg) { save = arg; }

    JButton getClose() { return close; }
    void setClose(JButton arg) { close = arg; }

    /* Првая панель
    JButton addPayment,updateList,connectSub,disconnSub,freezySub,unfreezySub;*/
    JButton getAddPayment() { return addPayment; }
    void setAddPayment(JButton arg) { addPayment = arg; }

    JButton getUpdateEvent() { return updateList; }
    void setUpdateEvent() {
        updateList = new JButton("Обновить");
        updateList . setIcon(new ImageIcon(getClass().getClassLoader().getResource("ttkom/tt/img/update.png")));
    }

    JButton getUpdateList() { return updateList; }
    void setUpdateList() {
        updateList = new JButton("Обновить");
        updateList . setIcon(new ImageIcon(getClass().getClassLoader().getResource("ttkom/tt/img/update.png")));
    }

    JButton getConnectSub() { return connectSub; }
    void  setConnectSub(JButton arg) {
        connectSub = arg;
        /*
        Тут необходимо ввести условия для проверки статуса подключения
        Что бы не делать двонойго подключения.
         */
    }

    JButton getDisconnectSub() { return disconnSub; }
    void  setDisconnectSub(JButton arg) {
        disconnSub = arg;
        /*
        Тут необходимо ввести условия для проверки статуса подключения
        Отключать абонента только в том случаее если он подключен
         */
    }

    JButton getFreezySub() { return freezySub; }
    void setFreezySub(JButton arg) {
        freezySub = arg;
        /*
        Устанавливаем правила обображение кнопки
         */
    }

    JButton getUnfreezySub() { return unfreezySub; }
    void setUnfreezySub(JButton arg) {
        unfreezySub = arg;
        /*
        Устанавливаем правила обображение кнопки
         */
    }
    /* JTable paymentTable,eventTable; */

    JScrollPane getPayScroll() { return payScroll; }
    void setPayScroll(JTable table) { payScroll = new JScrollPane(table); }

    JScrollPane getEventScroll() { return payScroll; }
    void setEventScroll(JTable table) { payScroll = new JScrollPane(table); }

    class TableModel extends DefaultTableModel {
        public TableModel(Object rowData[][], Object columnNames[]) {
            super(rowData, columnNames);
        }

        public boolean isCellEditable(int row, int col) {
            return false;
        }
    }

    class ColorTableCellRenderer extends JLabel implements TableCellRenderer {
        public ColorTableCellRenderer() {
            setOpaque(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JPanel pan = new JPanel(new BorderLayout());
            JLabel lbl = new JLabel((String) value);
            if (table.getSelectedRow() == row) {
                pan.setBackground(new Color(253, 255, 225));
            } else {
                if ((row % 2) == 0) {
                    pan.setBackground(new Color(200, 200, 255));
                } else {
                    pan.setBackground(new Color(253, 253, 253));
                }
            }
            pan.add(lbl, BorderLayout.CENTER);
            return pan;
        }
    }

    JTable getPaymentTable() { return paymentTable; }
    void setPaymentTable() throws SQLException {
        payModel = new TableModel(new Object[][]{}, new Object[]{"Сумма", "Дата приема", "Оператор"});
        paymentTable = new JTable(payModel);
        paymentTable.setDefaultRenderer(Object.class, new ColorTableCellRenderer());
        ArrayList<String[]> array = getListPayment(mainWindow.getConnectDB().getConnect(), userInfo.getIdSubscriber());

        for (int i = 0; i<array.size();i++)
            payModel.addRow(new Object[]{
                    array.get(i)[1],
                    array.get(i)[0],
                    array.get(i)[2]
            });

        setPayScroll(paymentTable);
    }
    TableModel getPayModel() { return payModel; }
    void updatePayTable() throws SQLException {
        ArrayList<String[]> array = getListPayment(mainWindow.getConnectDB().getConnect(),userInfo.getIdSubscriber());

        while (getPayModel().getRowCount()>0)
            getPayModel().removeRow(0);

        for (int i = 0; i<array.size();i++)
            payModel.addRow(new Object[]{
                    array.get(i)[1],
                    array.get(i)[0],
                    array.get(i)[2]
            });
    }


    JTable getEventTable() { return eventTable; }
    void setEventTable() throws SQLException {
        eventModel = new TableModel(new Object[][]{}, new Object[]{"Дата","Статус","Старое значение","Оператор"});
        eventTable = new JTable(eventModel);
        eventTable.setDefaultRenderer(Object.class, new ColorTableCellRenderer());
        ArrayList<String[]> array = getListEvent(mainWindow.getConnectDB().getConnect(),userInfo.getIdSubscriber());

        for (int i = 0; i<array.size();i++)
            eventModel.addRow(new Object[]{
                    array.get(i)[0],
                    array.get(i)[1],
                    array.get(i)[2],
                    array.get(i)[3]
            });

        setEventScroll(eventTable);
    }
    TableModel getEventModel() { return eventModel;}

    void updateEventTable() throws SQLException {

        ArrayList<String[]> array = getListEvent(mainWindow.getConnectDB().getConnect(),userInfo.getIdSubscriber());

        while (getEventTable().getModel().getRowCount()>0)
            getEventModel().removeRow(0);

        for (int i = 0; i<array.size();i++)
            eventModel.addRow(new Object[]{
                    array.get(i)[0],
                    array.get(i)[1],
                    array.get(i)[2],
                    array.get(i)[3]
            });
    }
    void addTab(HashMap rules,String type,String name,String img,JPanel panel,String hint,JTabbedPane tabs) {
        if (rules.get(type+"_EDIT")!=null)
            tabs.addTab(name,new ImageIcon(getClass().getClassLoader().getResource("ttkom/tt/img/"+img+"")),panel,hint);
        else if (rules.get(type+"_SEE")!=null)
            tabs.addTab(name,new ImageIcon(getClass().getClassLoader().getResource("ttkom/tt/img/"+img+"")),panel,hint);
    }

    JPanel setPanelTab(String nameTab,Integer typePanel) throws SQLException {
        MigLayout layout = new MigLayout("wrap 4","grow,fill");
        JPanel panel = new JPanel(layout);
        panel.setName(String.valueOf(typePanel));
        switch (typePanel) {
            case 0:
                setPaymentTable();
                setAddPayment(getButtonText(mainWindow.getRules(), "ADDPAYMENT", "plus.png", "Добавить платеж"));
                setUpdateList();

                panel . add(getAddPayment(),"w 200!");
                panel . add(getUpdateList(),"w 200!,wrap");
                panel . add(getPayScroll(),"grow,span 4");
                break;
            case 1:
                setEventTable();
                setUpdateEvent();

                panel . add(getUpdateEvent(),"w 200!,wrap");
                panel . add(getEventScroll(),"grow,span 4");
                break;
            case 2:
                setConnectSub   (getButtonText( mainWindow.getRules(),   "SETCONNECT",      "plus.png", "Подключить"));
                setDisconnectSub(getButtonText( mainWindow.getRules(),   "SETDISCONNECT",   "plus.png", "Отключить"));
                setFreezySub    (getButtonText( mainWindow.getRules(),   "SETFREEZY",       "plus.png", "Замарозить"));
                setUnfreezySub  (getButtonText( mainWindow.getRules(),   "SETUNFREEZY",     "plus.png", "Разамарозить"));

                panel . add(getConnectSub(),    "w 200!,wrap");
                panel . add(getDisconnectSub(), "w 200!,wrap");
                panel . add(getFreezySub(),     "w 200!,wrap");
                panel . add(getUnfreezySub(),   "w 200!,wrap");
                break;
        }
        panel.setName(nameTab);
        return panel;
    }


    Boolean is_change(String old,String next) {
        if(old != null) {

        if (old.equals(next))
            return false;
        else
            return true;
        } else {
            return true;
        }
    }

}

