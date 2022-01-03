package Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.Socket;

public class Client extends JFrame implements ActionListener {

    public static final Color bgColor = Color.white;
    public static final Color focusbg = new Color(242, 242, 242);
    public static final Color focuschar = new Color(96, 95, 91);
    public static final Color color1 = Color.RED;
    public static final Color color2 = Color.white;

    JLabel jlHost = new JLabel("IP");
    JLabel jlPort = new JLabel("Cổng");
    JLabel jlNickName = new JLabel("Tên ");

    JTextField jtfHost = new JTextField("127.0.0.1");
    JTextField jtfPort = new JTextField("8000");
    JTextField jtfNickName = new JTextField("Player 1");

    JButton jbConnect = new JButton("Kết nối");
    JButton jbDisconnect = new JButton("Đóng");
    JButton jbFail = new JButton(" Nhận thua ");
    JButton jbChallenge = new JButton(" Thách đấu ");

    JComboBox jcbNickList = new JComboBox();
    JButton jbYChallenge = new JButton("Chấp nhận");
    JButton jbNChallenge = new JButton("Từ chối");

    int width = 60;

    Model[][] chess = new Model[9][10];
    Chessboard jpz = new Chessboard(chess, width, this);

    JPanel jpy = new JPanel();
    JSplitPane jsp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, jpz, jpy);

    boolean referee = false;
    int color = 0;

    Socket sc;

    ClientAgentThread cat;

    public Client() {

        this.initialComponent();

        this.addListener();

        this.initialState();

        this.initialChess();

        this.initialFrame();

    }

    public void initialComponent() {
    	setResizable(false);
        jpy.setLayout(null);

        this.jlHost.setBounds(10, 10, 50, 20);
        jpy.add(this.jlHost);

        this.jtfHost.setBounds(70, 10, 80, 20);
        jpy.add(this.jtfHost);

        this.jlPort.setBounds(10, 40, 50, 20);
        jpy.add(this.jlPort);

        this.jtfPort.setBounds(70, 40, 80, 20);
        jpy.add(this.jtfPort);

        this.jlNickName.setBounds(10, 70, 50, 20);
        jpy.add(this.jlNickName);

        this.jtfNickName.setBounds(70, 70, 80, 20);
        jpy.add(this.jtfNickName);

        this.jbConnect.setBounds(10, 100, 80, 20);
        jpy.add(this.jbConnect);

        this.jbDisconnect.setBounds(100, 100, 80, 20);
        jpy.add(this.jbDisconnect);

        this.jcbNickList.setBounds(20, 130, 130, 20);
        jpy.add(this.jcbNickList);

        this.jbChallenge.setBounds(10, 160, 80, 20);
        jpy.add(this.jbChallenge);

        this.jbFail.setBounds(100, 160, 80, 20);
        jpy.add(this.jbFail);

        this.jbYChallenge.setBounds(5, 190, 86, 20);
        jpy.add(this.jbYChallenge);

        this.jbNChallenge.setBounds(100, 190, 86, 20);
        jpy.add(this.jbNChallenge);

        jpz.setLayout(null);
        jpz.setBounds(0, 0, 700, 700);
    }

    public void addListener() {

        this.jbConnect.addActionListener(this);
        this.jbDisconnect.addActionListener(this);
        this.jbChallenge.addActionListener(this);
        this.jbFail.addActionListener(this);
        this.jbYChallenge.addActionListener(this);
        this.jbNChallenge.addActionListener(this);
    }

    public void initialState() {

        this.jbDisconnect.setEnabled(false);
        this.jbChallenge.setEnabled(false);
        this.jbYChallenge.setEnabled(false);
        this.jbNChallenge.setEnabled(false);
        this.jbFail.setEnabled(false);
    }

    public void initialChess() {

        chess[0][0] = new Model(color1, "車", 0, 0);
        chess[1][0] = new Model(color1, "馬", 1, 0);
        chess[2][0] = new Model(color1, "相", 2, 0);
        chess[3][0] = new Model(color1, "仕", 3, 0);
        chess[4][0] = new Model(color1, "帥", 4, 0);
        chess[5][0] = new Model(color1, "仕", 5, 0);
        chess[6][0] = new Model(color1, "相", 6, 0);
        chess[7][0] = new Model(color1, "馬", 7, 0);
        chess[8][0] = new Model(color1, "車", 8, 0);
        chess[1][2] = new Model(color1, "砲", 1, 2);
        chess[7][2] = new Model(color1, "砲", 7, 2);
        chess[0][3] = new Model(color1, "兵", 0, 3);
        chess[2][3] = new Model(color1, "兵", 2, 3);
        chess[4][3] = new Model(color1, "兵", 4, 3);
        chess[6][3] = new Model(color1, "兵", 6, 3);
        chess[8][3] = new Model(color1, "兵", 8, 3);

        chess[0][9] = new Model(color2, "車", 0, 9);
        chess[1][9] = new Model(color2, "馬", 1, 9);
        chess[2][9] = new Model(color2, "象", 2, 9);
        chess[3][9] = new Model(color2, "士", 3, 9);
        chess[4][9] = new Model(color2, "將", 4, 9);
        chess[5][9] = new Model(color2, "士", 5, 9);
        chess[6][9] = new Model(color2, "象", 6, 9);
        chess[7][9] = new Model(color2, "馬", 7, 9);
        chess[8][9] = new Model(color2, "車", 8, 9);
        chess[1][7] = new Model(color2, "炮", 1, 7);
        chess[7][7] = new Model(color2, "炮", 7, 7);
        chess[0][6] = new Model(color2, "卒", 0, 6);
        chess[2][6] = new Model(color2, "卒", 2, 6);
        chess[4][6] = new Model(color2, "卒", 4, 6);
        chess[6][6] = new Model(color2, "卒", 6, 6);
        chess[8][6] = new Model(color2, "卒", 8, 6);

    }

    public void initialFrame() {

        this.setTitle("Chinese Chess--Client");
        this.add(this.jsp);

        jsp.setDividerLocation(730);
        jsp.setDividerSize(4);

        this.setBounds(30, 30, 930, 730);
        this.setVisible(true);
        this.addWindowListener(
                new WindowAdapter() {
                    public void windowClosing(WindowEvent e) {
                        if (cat == null)
                        {
                            System.exit(0);
                            return;
                        }
                        try {
                            if (cat.tiaoZhanZhe != null)
                            {
                                try {

                                    cat.dout.writeUTF("<#RENSHU#>" + cat.tiaoZhanZhe);
                                } catch (Exception ee) {
                                    ee.printStackTrace();
                                }
                            }
                            cat.dout.writeUTF("<#CLIENT_LEAVE#>");
                            cat.flag = false;
                            cat = null;

                        } catch (Exception ee) {
                            ee.printStackTrace();
                        }
                        System.exit(0);
                    }

                }
        );
    }

    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == this.jbConnect) {
            this.jbConnect_event();
        } else if (e.getSource() == this.jbDisconnect) {
            this.jbDisconnect_event();
        } else if (e.getSource() == this.jbChallenge) {
            this.jbChallenge_event();
        } else if (e.getSource() == this.jbYChallenge) {
            this.jbYChallenge_event();
        } else if (e.getSource() == this.jbNChallenge) {
            this.jbNChallenge_event();
        } else if (e.getSource() == this.jbFail) {
            this.jbFail_event();
        }
    }

    public void jbConnect_event() {
        int port = 0;

        try {
            port = Integer.parseInt(this.jtfPort.getText().trim());
        } catch (Exception ee) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn Cổng là kiểu số nguyên", "mistake",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (port > 65535 || port < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn Cổng từ 0-65535", "mistake",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String name = this.jtfNickName.getText().trim();

        if (name.length() == 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập  tên người chơi", "mistake",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {

            sc = new Socket(this.jtfHost.getText().trim(), port);
            cat = new ClientAgentThread(this);
            cat.start();

            this.jtfHost.setEnabled(false);
            this.jtfPort.setEnabled(false);
            this.jtfNickName.setEnabled(false);
            this.jbConnect.setEnabled(false);
            this.jbDisconnect.setEnabled(true);
            this.jbChallenge.setEnabled(true);
            this.jbYChallenge.setEnabled(false);
            this.jbNChallenge.setEnabled(false);
            this.jbFail.setEnabled(false);

            JOptionPane.showMessageDialog(this, "Kết nối server thành công", "mistake",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ee) {
            JOptionPane.showMessageDialog(this, "Kết nối thất bại", "mistake",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
    }

    public void jbDisconnect_event() {
        try {
            this.cat.dout.writeUTF("<#CLIENT_LEAVE#>");
            this.cat.flag = false;
            this.cat = null;
            this.jtfHost.setEnabled(!false);
            this.jtfPort.setEnabled(!false);
            this.jtfNickName.setEnabled(!false);
            this.jbConnect.setEnabled(!false);
            this.jbDisconnect.setEnabled(!true);
            this.jbChallenge.setEnabled(!true);
            this.jbYChallenge.setEnabled(false);
            this.jbNChallenge.setEnabled(false);
            this.jbFail.setEnabled(false);

        } catch (Exception ee) {
            ee.printStackTrace();
        }
    }

    public void jbChallenge_event() {

        Object o = this.jcbNickList.getSelectedItem();

        if (o == null || ((String) o).equals("")) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn đối thủ", "mistake",
                    JOptionPane.ERROR_MESSAGE);//
        } else {

            String name2 = (String) this.jcbNickList.getSelectedItem();

            try {
                this.jtfHost.setEnabled(false);
                this.jtfPort.setEnabled(false);
                this.jtfNickName.setEnabled(false);
                this.jbConnect.setEnabled(false);
                this.jbDisconnect.setEnabled(!true);
                this.jbChallenge.setEnabled(!true);
                this.jbYChallenge.setEnabled(false);
                this.jbNChallenge.setEnabled(false);
                this.jbFail.setEnabled(false);

                this.cat.tiaoZhanZhe = name2;
                this.referee = true;
                this.color = 0;

                this.cat.dout.writeUTF("<#TIAO_ZHAN#>" + name2);
                JOptionPane.showMessageDialog(this, "Đã chuyển lời thách đấu, Vui lòng chờ phản hồi", "misstake",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ee) {
                ee.printStackTrace();
            }
        }
    }

    public void jbYChallenge_event() {

        try {

            this.cat.dout.writeUTF("<#TONG_YI#>" + this.cat.tiaoZhanZhe);
            this.referee = false;
            this.color = 1;

            this.jtfHost.setEnabled(false);
            this.jtfPort.setEnabled(false);
            this.jtfNickName.setEnabled(false);
            this.jbConnect.setEnabled(false);
            this.jbDisconnect.setEnabled(!true);
            this.jbChallenge.setEnabled(!true);
            this.jbYChallenge.setEnabled(false);
            this.jbNChallenge.setEnabled(false);
            this.jbFail.setEnabled(!false);

        } catch (Exception ee) {
            ee.printStackTrace();
        }
    }

    public void jbNChallenge_event() {

        try {

            this.cat.dout.writeUTF("<#BUTONG_YI#>" + this.cat.tiaoZhanZhe);
            this.cat.tiaoZhanZhe = null;
            this.jtfHost.setEnabled(false);
            this.jtfPort.setEnabled(false);
            this.jtfNickName.setEnabled(false);
            this.jbConnect.setEnabled(false);
            this.jbDisconnect.setEnabled(true);
            this.jbChallenge.setEnabled(true);
            this.jbYChallenge.setEnabled(false);
            this.jbNChallenge.setEnabled(false);
            this.jbFail.setEnabled(false);

        } catch (Exception ee) {
            ee.printStackTrace();
        }
    }

    public void jbFail_event() {

        try {

            this.cat.dout.writeUTF("<#RENSHU#>" + this.cat.tiaoZhanZhe);
            this.cat.tiaoZhanZhe = null;
            this.color = 0;
            this.referee = false;

            this.next();

            this.jtfHost.setEnabled(false);
            this.jtfPort.setEnabled(false);
            this.jtfNickName.setEnabled(false);
            this.jbConnect.setEnabled(false);
            this.jbDisconnect.setEnabled(true);
            this.jbChallenge.setEnabled(true);
            this.jbYChallenge.setEnabled(false);
            this.jbNChallenge.setEnabled(false);
            this.jbFail.setEnabled(false);

        } catch (Exception ee) {
            ee.printStackTrace();
        }
    }

    public void next() {

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 10; j++) {
                this.chess[i][j] = null;
            }
        }

        this.referee = false;
        this.initialChess();
        this.repaint();
    }

    public static void main(String args[]) {
        new Client();
    }
}