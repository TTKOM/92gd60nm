package ttkom.tt.dbase.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: g1yyk
 * Date: 28.10.12
 * Time: 1:16
 * To change this template use File | Settings | File Templates.
 */
public class ConnectDB  {
    public  Connection con = null;
    private  String begin = "jdbc:mysql://";
    private  String url = "";
    private  String user = "";
    private  String pass = "";
    private  String dbase = "";


    //Default login is "franchise" and password is 43202129472 for connecting to DB megaline
    public ConnectDB(String dbase, String addres, String login, String pass) {
        super();
        setUrlConnect(dbase,addres);
        setUserPassword(login,pass);
        setConnection();
    }


    private void setConnection() {
        try {
            con = DriverManager.getConnection(url, user, pass);
        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
            e.printStackTrace();
        }
    }

    private void setUrlConnect(String dbase,String adress) {
        this.dbase = dbase;
        url = begin + adress + "/" + dbase+"?autoReconnect=true";
    }

    public Connection getConnect() {
        return con;
    }
    public String getUrl() {
        return "Подключенны к БД "+dbase;
    }

    public void closeConnect() throws SQLException {
        con.close();
        System.out.println("Закрываю соединение с БД");
    }

    private void setUserPassword(String userName,String passWord) {
        user =  userName; //"callcenter";
        pass =  passWord;//"vjqgfhjkmcallcenter";
    }

}
