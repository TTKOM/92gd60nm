package ttkom.tt.windows.modal.card.funmodal;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: g1yyk
 * Date: 07.06.13
 * Time: 11:35
 * To change this template use File | Settings | File Templates.
 */
public class TestModal extends AbFunModal {
    public TestModal(Integer id_subscriber, Integer id_tariff, String title, Window window, Connection conn) throws SQLException {
        super(id_subscriber, id_tariff, title, window, conn);

        getWin().setSize(new Dimension(500,100));
        getWin().setLocation(500,500);
        getWin().setModal(true);
        getWin().setVisible(true);
        getWin().validate();
        getWin().repaint();
    }


}
