package Client;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Vector;

public class ClientAgentThread extends Thread {

    Client father;
    boolean flag = true;

    DataInputStream din;
    DataOutputStream dout;

    String tiaoZhanZhe = null;

    public ClientAgentThread(Client father) {
        this.father = father;
        try {

            din = new DataInputStream(father.sc.getInputStream());
            dout = new DataOutputStream(father.sc.getOutputStream());

            String name = father.jtfNickName.getText().trim();
            dout.writeUTF("<#NICK_NAME#>" + name);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while (flag) {
            try {

                String msg = din.readUTF().trim();

                if (msg.startsWith("<#NAME_CHONGMING#>")) {
                    this.name_chongming();
                } else if (msg.startsWith("<#NICK_LIST#>")) {
                    this.nick_list(msg);
                } else if (msg.startsWith("<#SERVER_DOWN#>")) {
                    this.server_down();
                } else if (msg.startsWith("<#TIAO_ZHAN#>")) {
                    this.tiao_zhan(msg);
                } else if (msg.startsWith("<#TONG_YI#>")) {
                    this.tong_yi();
                } else if (msg.startsWith("<#BUTONG_YI#>")) {
                    this.butong_yi();
                } else if (msg.startsWith("<#BUSY#>")) {
                    this.busy();
                } else if (msg.startsWith("<#MOVE#>")) {
                    this.move(msg);
                } else if (msg.startsWith("<#RENSHU#>")) {
                    this.renshu();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void name_chongming() {
        try {
            JOptionPane.showMessageDialog(this.father, "Tên người chơi đã được sử dụng, vui lòng điền lại!",
                    "misstake", JOptionPane.ERROR_MESSAGE);

            din.close();
            dout.close();

            this.father.jtfHost.setEnabled(!false);
            this.father.jtfPort.setEnabled(!false);
            this.father.jtfNickName.setEnabled(!false);
            this.father.jbConnect.setEnabled(!false);
            this.father.jbDisconnect.setEnabled(!true);
            this.father.jbChallenge.setEnabled(!true);
            this.father.jbYChallenge.setEnabled(false);
            this.father.jbNChallenge.setEnabled(false);
            this.father.jbFail.setEnabled(false);

            father.sc.close();
            father.sc = null;
            father.cat = null;
            flag = false;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void nick_list(String msg) {

        String s = msg.substring(13);
        String[] na = s.split("\\|");

        Vector v = new Vector();

        for (int i = 0; i < na.length; i++) {
            if (na[i].trim().length() != 0 && (!na[i].trim().equals(father.jtfNickName.getText().trim()))) {
                v.add(na[i]);
            }
        }

        father.jcbNickList.setModel(new DefaultComboBoxModel(v));
    }

    public void server_down() {

        this.father.jtfHost.setEnabled(!false);
        this.father.jtfPort.setEnabled(!false);
        this.father.jtfNickName.setEnabled(!false);
        this.father.jbConnect.setEnabled(!false);
        this.father.jbDisconnect.setEnabled(!true);
        this.father.jbChallenge.setEnabled(!true);
        this.father.jbYChallenge.setEnabled(false);
        this.father.jbNChallenge.setEnabled(false);
        this.father.jbFail.setEnabled(false);

        this.flag = false;
        father.cat = null;

        JOptionPane.showMessageDialog(this.father, "Server đã dừng! ! !", "missatake",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public void tiao_zhan(String msg) {

        try {

            String name = msg.substring(13);

            if (this.tiaoZhanZhe == null) {

                tiaoZhanZhe = msg.substring(13);

                this.father.jtfHost.setEnabled(false);
                this.father.jtfPort.setEnabled(false);
                this.father.jtfNickName.setEnabled(false);
                this.father.jbConnect.setEnabled(false);
                this.father.jbDisconnect.setEnabled(!true);
                this.father.jbChallenge.setEnabled(!true);
                this.father.jbYChallenge.setEnabled(!false);
                this.father.jbNChallenge.setEnabled(!false);
                this.father.jbFail.setEnabled(false);

                JOptionPane.showMessageDialog(this.father, tiaoZhanZhe + "Gửi lời mời chơi đến bạn !!!",
                        "misstake", JOptionPane.INFORMATION_MESSAGE);
            } else {
                this.dout.writeUTF("<#BUSY#>" + name);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void tong_yi() {

        this.father.jtfHost.setEnabled(false);
        this.father.jtfPort.setEnabled(false);
        this.father.jtfNickName.setEnabled(false);
        this.father.jbConnect.setEnabled(false);
        this.father.jbDisconnect.setEnabled(!true);
        this.father.jbChallenge.setEnabled(!true);
        this.father.jbYChallenge.setEnabled(false);
        this.father.jbNChallenge.setEnabled(false);
        this.father.jbFail.setEnabled(!false);

        JOptionPane.showMessageDialog(this.father, "Đối thủ chấp nhận lời mời của bạn! Bạn đi trước (cờ đỏ)",
                "hint", JOptionPane.INFORMATION_MESSAGE);
    }

    public void butong_yi() {

        this.father.referee = false;
        this.father.color = 0;
        this.father.jtfHost.setEnabled(false);
        this.father.jtfPort.setEnabled(false);
        this.father.jtfNickName.setEnabled(false);
        this.father.jbConnect.setEnabled(false);
        this.father.jbDisconnect.setEnabled(true);
        this.father.jbChallenge.setEnabled(true);
        this.father.jbYChallenge.setEnabled(false);
        this.father.jbNChallenge.setEnabled(false);
        this.father.jbFail.setEnabled(false);

        JOptionPane.showMessageDialog(this.father, "\n"
        		+ "Đối thủ từ chối thách thức của bạn!", "hint",
                JOptionPane.INFORMATION_MESSAGE);

        this.tiaoZhanZhe = null;
    }

    public void busy() {

        this.father.referee = false;
        this.father.color = 0;

        this.father.jtfHost.setEnabled(false);
        this.father.jtfPort.setEnabled(false);
        this.father.jtfNickName.setEnabled(false);
        this.father.jbConnect.setEnabled(false);
        this.father.jbDisconnect.setEnabled(true);
        this.father.jbChallenge.setEnabled(true);
        this.father.jbYChallenge.setEnabled(false);
        this.father.jbNChallenge.setEnabled(false);
        this.father.jbFail.setEnabled(false);

        JOptionPane.showMessageDialog(this.father, "Đối thủ đang bận! ! !", "hint",
                JOptionPane.INFORMATION_MESSAGE);

        this.tiaoZhanZhe = null;
    }

    public void move(String msg) {

        int length = msg.length();

        int startI = Integer.parseInt(msg.substring(length - 4, length - 3));
        int startJ = Integer.parseInt(msg.substring(length - 3, length - 2));
        int endI = Integer.parseInt(msg.substring(length - 2, length - 1));
        int endJ = Integer.parseInt(msg.substring(length - 1));
        this.father.jpz.move(startI, startJ, endI, endJ);

        this.father.referee = true;
    }

    public void renshu() {

        JOptionPane.showMessageDialog(this.father, "Xin chúc mừng, bạn thắng, đối thủ thừa nhận thất bại", "hint",
                JOptionPane.INFORMATION_MESSAGE);

        this.tiaoZhanZhe = null;
        this.father.color = 0;
        this.father.referee = false;
        this.father.next();

        this.father.jtfHost.setEnabled(false);
        this.father.jtfPort.setEnabled(false);
        this.father.jtfNickName.setEnabled(false);
        this.father.jbConnect.setEnabled(false);
        this.father.jbDisconnect.setEnabled(true);
        this.father.jbChallenge.setEnabled(true);
        this.father.jbYChallenge.setEnabled(false);
        this.father.jbNChallenge.setEnabled(false);
        this.father.jbFail.setEnabled(false);
    }
}