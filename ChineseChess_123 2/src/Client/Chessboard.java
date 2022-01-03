package Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Chessboard extends JPanel implements MouseListener {

    private int width;
    boolean focus = false;

    int jiang1_i = 4;
    int jiang1_j = 0;
    int jiang2_i = 4;
    int jiang2_j = 9;
    int startI = -1;
    int startJ = -1;
    int endI = -1;
    int endJ = -1;

    public Model chess[][];
    Client xq = null;
    Rules rules;

    public Chessboard(Model chess[][], int width, Client xq) {

        this.xq = xq;
        this.chess  = chess;
        this.width = width;

        rules = new Rules(chess);

        this.addMouseListener(this);
        this.setBounds(0, 0, 700, 700);
        this.setLayout(null);
    }

    public void paint(Graphics g1) {

        Graphics2D g = (Graphics2D) g1;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        Color c = g.getColor();
        g.setColor(Client.bgColor);
        g.fill3DRect(60, 30, 580, 630, false);
        g.setColor(Color.black);

        for (int i = 80; i <= 620; i = i + 60) {
            g.drawLine(110, i, 590, i);
        }

        g.drawLine(110, 80, 110, 620);
        g.drawLine(590, 80, 590, 620);

        for (int i = 170; i <= 530; i = i + 60) {
            g.drawLine(i, 80, i, 320);
            g.drawLine(i, 380, i, 620);
        }

        g.drawLine(290, 80, 410, 200);
        g.drawLine(290, 200, 410, 80);
        g.drawLine(290, 500, 410, 620);
        g.drawLine(290, 620, 410, 500);

        this.smallLine(g, 1, 2);
        this.smallLine(g, 7, 2);
        this.smallLine(g, 0, 3);
        this.smallLine(g, 2, 3);
        this.smallLine(g, 4, 3);
        this.smallLine(g, 6, 3);
        this.smallLine(g, 8, 3);
        this.smallLine(g, 0, 6);
        this.smallLine(g, 2, 6);
        this.smallLine(g, 4, 6);
        this.smallLine(g, 6, 6);
        this.smallLine(g, 8, 6);
        this.smallLine(g, 1, 7);
        this.smallLine(g, 7, 7);

        g.setColor(Color.black);

        Font font = new Font("宋体", Font.BOLD, 30); // font chữ thời tống 
       g.setFont(font);

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 10; j++) {
                if (chess [i][j] != null) {

                    if (this.chess [i][j].getFocus() != false) {
                        g.setColor(Client.focusbg);
                        g.fillOval(110 + i * 60 - 25, 80 + j * 60 - 25, 50, 50);
                        g.setColor(Client.focuschar);

                    } else {

                        g.fillOval(110 + i * 60 - 25, 80 + j * 60 - 25, 50, 50);
                        g.setColor(chess [i][j].getColor());
                    }

                    g.drawString(chess [i][j].getName(), 110 + i * 60 - 15, 80 + j * 60 + 10);
                    g.setColor(Color.black);
                }
            }
        }
        g.setColor(c);
    }

    public void mouseClicked(MouseEvent e) {

        if (this.xq.referee == true) {

            int i = -1, j = -1;
            int[] pos = getPos(e);

            i = pos[0];
            j = pos[1];

            if (i >= 0 && i <= 8 && j >= 0 && j <= 9) {

                if (focus == false) {
                    this.noFocus(i, j);
                } else {

                    if (chess [i][j] != null) {
                        if (chess [i][j].getColor() == chess [startI][startJ].getColor()) {
                            chess [startI][startJ].setFocus(false);
                            chess [i][j].setFocus(true);
                            startI = i;
                            startJ = j;
                        } else {
                            endI = i;
                            endJ = j;
                            String name = chess [startI][startJ].getName();
                         // System.out.println(name);
                            boolean canMove = rules.canMove(startI, startJ, endI, endJ, name);
                            if (canMove)
                            {
                                try {
                                    this.xq.cat.dout.writeUTF("<#MOVE#>" +
                                            this.xq.cat.tiaoZhanZhe + startI + startJ + endI + endJ);
                                    this.xq.referee = false;
                                    if (chess [endI][endJ].getName().equals("帥") ||
                                            chess [endI][endJ].getName().equals("將")) {
                                        this.success();
                                    } else {
                                        this.noJiang();
                                    }
                                } catch (Exception ee) {
                                    ee.printStackTrace();
                                }
                            }
                        }
                    } else {
                        endI = i;
                        endJ = j;
                        String name = chess [startI][startJ].getName();
                        boolean canMove = rules.canMove(startI, startJ, endI, endJ, name);
                        if (canMove) {
                            this.noChess();
                        }
                    }
                }
            
            this.xq.repaint();
        }
    }

    }

    public int[] getPos(MouseEvent e) {

        int[] pos = new int[2];

        pos[0] = -1;
        pos[1] = -1;

        Point p = e.getPoint();

        double x = p.getX();
        double y = p.getY();

        if (Math.abs((x - 110) / 1 % 60) <= 25) {
            pos[0] = Math.round((float) (x - 110)) / 60;
        } else if (Math.abs((x - 110) / 1 % 60) >= 35) {
            pos[0] = Math.round((float) (x - 110)) / 60 + 1;
        }
        if (Math.abs((y - 80) / 1 % 60) <= 25) {
            pos[1] = Math.round((float) (y - 80)) / 60;
        } else if (Math.abs((y - 80) / 1 % 60) >= 35) {
            pos[1] = Math.round((float) (y - 80)) / 60 + 1;
        }
        return pos;
    }

    public void noFocus(int i, int j) {

        if (this.chess [i][j] != null)
        {
            if (this.xq.color == 0)
            {
                if (this.chess [i][j].getColor().equals(Client.color1))
                {
                    this.chess [i][j].setFocus(true);
                    focus = true;
                    startI = i;
                    startJ = j;
                }
            } else
            {
                if (this.chess [i][j].getColor().equals(Client.color2))
                {
                    this.chess [i][j].setFocus(true);
                    focus = true;
                    startI = i;
                    startJ = j;
                }
            }
        }
    }

    public void success() {

        chess [endI][endJ] = chess [startI][startJ];
        chess [startI][startJ] = null;

        this.xq.repaint();

        JOptionPane.showMessageDialog(this.xq, "Chúc mừng bạn đã chiến thắng", "Hint",
                JOptionPane.INFORMATION_MESSAGE);

        this.xq.cat.tiaoZhanZhe = null;
        this.xq.color = 0;
        this.xq.referee = false;
        this.xq.next();

        this.xq.jtfHost.setEnabled(false);
        this.xq.jtfPort.setEnabled(false);
        this.xq.jtfNickName.setEnabled(false);
        this.xq.jbConnect.setEnabled(false);
        this.xq.jbDisconnect.setEnabled(true);
        this.xq.jbChallenge.setEnabled(true);
        this.xq.jbYChallenge.setEnabled(false);
        this.xq.jbNChallenge.setEnabled(false);
        this.xq.jbFail.setEnabled(false);

        startI = -1;
        startJ = -1;
        endI = -1;
        endJ = -1;
        jiang1_i = 4;
        jiang1_j = 0;
        jiang2_i = 4;
        jiang2_j = 9;
        focus = false;
    }

    public void noJiang() {

        chess [endI][endJ] = chess [startI][startJ];
        chess [startI][startJ] = null;
        chess [endI][endJ].setFocus(false);

        this.xq.repaint();

        if (chess [endI][endJ].getName().equals("帥")) {
            jiang1_i = endI;
            jiang1_j = endJ;
        } else if (chess [endI][endJ].getName().equals("將")) {
            jiang2_i = endI;
            jiang2_j = endJ;
        }
        if (jiang1_i == jiang2_i) {
            int count = 0;
            for (int jiang_j = jiang1_j + 1; jiang_j < jiang2_j; jiang_j++) {
                if (chess [jiang1_i][jiang_j] != null) {
                    count++;
                    break;
                }
            }

            if (count == 0) {
                JOptionPane.showMessageDialog(this.xq, "Bạn đã thất bại !!!", "Hint",
                        JOptionPane.INFORMATION_MESSAGE);

                this.xq.cat.tiaoZhanZhe = null;
                this.xq.color = 0;
                this.xq.referee = false;
                this.xq.next();
                this.xq.jtfHost.setEnabled(false);
                this.xq.jtfPort.setEnabled(false);
                this.xq.jtfNickName.setEnabled(false);
                this.xq.jbConnect.setEnabled(false);
                this.xq.jbDisconnect.setEnabled(true);
                this.xq.jbChallenge.setEnabled(true);
                this.xq.jbYChallenge.setEnabled(false);
                this.xq.jbNChallenge.setEnabled(false);
                this.xq.jbFail.setEnabled(false);

                jiang1_i = 4;
                jiang1_j = 0;
                jiang2_i = 4;
                jiang2_j = 9;

            }
        }
        startI = -1;
        startJ = -1;
        endI = -1;
        endJ = -1;
        focus = false;
    }

    public void noChess() {

        try {

            this.xq.cat.dout.writeUTF("<#MOVE#>" + this.xq.cat.tiaoZhanZhe + startI + startJ + endI + endJ);
            this.xq.referee = false;
            chess [endI][endJ] = chess [startI][startJ];
            chess [startI][startJ] = null;
            chess [endI][endJ].setFocus(false);

            this.xq.repaint();
            if (chess [endI][endJ].getName().equals("帥")) {
                jiang1_i = endI;
                jiang1_j = endJ;
            } else if (chess [endI][endJ].getName().equals("將")) {
                jiang2_i = endI;
                jiang2_j = endJ;
            }
            if (jiang1_i == jiang2_i)
            {
                int count = 0;
                for (int jiang_j = jiang1_j + 1; jiang_j < jiang2_j; jiang_j++) {
                    if (chess [jiang1_i][jiang_j] != null) {
                        count++;
                        break;
                    }
                }
                if (count == 0) {
                    JOptionPane.showMessageDialog(this.xq, "Bạn đã thất bại!!!", "Hint",
                            JOptionPane.INFORMATION_MESSAGE);

                    this.xq.cat.tiaoZhanZhe = null;
                    this.xq.color = 0;
                    this.xq.referee = false;
                    this.xq.next();

                    this.xq.jtfHost.setEnabled(false);
                    this.xq.jtfPort.setEnabled(false);
                    this.xq.jtfNickName.setEnabled(false);
                    this.xq.jbConnect.setEnabled(false);
                    this.xq.jbDisconnect.setEnabled(true);
                    this.xq.jbChallenge.setEnabled(true);
                    this.xq.jbYChallenge.setEnabled(false);
                    this.xq.jbNChallenge.setEnabled(false);
                    this.xq.jbFail.setEnabled(false);

                    jiang1_i = 4;
                    jiang1_j = 0;
                    jiang2_i = 4;
                    jiang2_j = 9;

                }
            }
            startI = -1;
            startJ = -1;
            endI = -1;
            endJ = -1;
            focus = false;

        } catch (Exception ee) {
            ee.printStackTrace();
        }
    }

    public void move(int startI, int startJ, int endI, int endJ) {

        if (chess [endI][endJ] != null && (chess [endI][endJ].getName().equals("帥") ||
                chess [endI][endJ].getName().equals("將"))) {
            chess [endI][endJ] = chess [startI][startJ];
            chess [startI][startJ] = null;
            this.xq.repaint();

            JOptionPane.showMessageDialog(this.xq, "Bạn đã thất bại !!! ", "hint",
                    JOptionPane.INFORMATION_MESSAGE);

            this.xq.cat.tiaoZhanZhe = null;
            this.xq.color = 0;
            this.xq.referee = false;
            this.xq.next();

            this.xq.jtfHost.setEnabled(false);
            this.xq.jtfPort.setEnabled(false);
            this.xq.jtfNickName.setEnabled(false);
            this.xq.jbConnect.setEnabled(false);
            this.xq.jbDisconnect.setEnabled(true);
            this.xq.jbChallenge.setEnabled(true);
            this.xq.jbYChallenge.setEnabled(false);
            this.xq.jbNChallenge.setEnabled(false);
            this.xq.jbFail.setEnabled(false);

            jiang1_i = 4;
            jiang1_j = 0;
            jiang2_i = 4;
            jiang2_j = 9;

        } else {

            chess [endI][endJ] = chess [startI][startJ];
            chess [startI][startJ] = null;
            this.xq.repaint();

            if (chess [endI][endJ].getName().equals("帥")) {
                jiang1_i = endI;
                jiang1_j = endJ;
            } else if (chess [endI][endJ].getName().equals("將")) {
                jiang2_i = endI;
                jiang2_j = endJ;
            }
            if (jiang1_i == jiang2_i) {
                int count = 0;
                for (int jiang_j = jiang1_j + 1; jiang_j < jiang2_j; jiang_j++) {
                    if (chess [jiang1_i][jiang_j] != null) {
                        count++;
                        break;
                    }
                }
                if (count == 0) {
                    JOptionPane.showMessageDialog(this.xq, "Bạn chiến thắng !!!",
                            "Hint", JOptionPane.INFORMATION_MESSAGE);
                    this.xq.cat.tiaoZhanZhe = null;

                    this.xq.color = 0;
                    this.xq.referee = false;
                    this.xq.next();

                    this.xq.jtfHost.setEnabled(false);
                    this.xq.jtfPort.setEnabled(false);
                    this.xq.jtfNickName.setEnabled(false);
                    this.xq.jbConnect.setEnabled(false);
                    this.xq.jbDisconnect.setEnabled(true);
                    this.xq.jbChallenge.setEnabled(true);
                    this.xq.jbYChallenge.setEnabled(false);
                    this.xq.jbNChallenge.setEnabled(false);
                    this.xq.jbFail.setEnabled(false);

                    jiang1_i = 4;
                    jiang1_j = 0;
                    jiang2_i = 4;
                    jiang2_j = 9;
                }
            }
        }
        this.xq.repaint();
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void smallLine(Graphics2D g, int i, int j) {

        int x = 110 + 60 * i;
        int y = 80 + 60 * j;

        if (i > 0) {
            g.drawLine(x - 3, y - 3, x - 20, y - 3);
            g.drawLine(x - 3, y - 3, x - 3, y - 20);
        }
        if (i < 8) {
            g.drawLine(x + 3, y - 3, x + 20, y - 3);
            g.drawLine(x + 3, y - 3, x + 3, y - 20);
        }
        if (i > 0) {
            g.drawLine(x - 3, y + 3, x - 20, y + 3);
            g.drawLine(x - 3, y + 3, x - 3, y + 20);
        }
        if (i < 8) {
            g.drawLine(x + 3, y + 3, x + 20, y + 3);
            g.drawLine(x + 3, y + 3, x + 3, y + 20);
        }
    }
}
