import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

public class ClientFrame extends JFrame {

    private Map<String, List<JButton>> buttons = new HashMap<String, List<JButton>>();

    private JTextField jtfRoom = new JTextField();
    private JTextField jtfName = new JTextField();
    private JTextField jtfIP = new JTextField();
    private JTextArea jtaR = new JTextArea();
    private JTextArea jtaD = new JTextArea();
    private JScrollPane jspR = new JScrollPane(jtaR);
    private JScrollPane jspD = new JScrollPane(jtaD);
    private JButton[] bingo;

    private Random rnd = new Random();

    private boolean room = false;
    private String roomID = "";
    private int width = -1;
    private int height = -1;
    private int num = 0;
    private int[] nums;
    private boolean ready = false;
    private boolean start = false;
    private boolean yourturn = false;

    public ClientFrame() {
        regElement();
        initComp();
    }

    private void addButton(String key, JButton button) {
        this.addButton(key, button, true);
    }

    private void addButton(String key, JButton button, boolean flag) {
        if (!this.buttons.containsKey(key))
            this.buttons.put(key, new ArrayList<JButton>());
        List<JButton> buttons = this.buttons.get(key);
        if (!buttons.contains(button)) {
            buttons.add(button);
            if (flag)
                this.getContentPane().add(button);
        }
    }

    private void removeButton(String key) {
        if (!this.buttons.containsKey(key))
            this.buttons.put(key, new ArrayList<JButton>());
        List<JButton> buttons = this.buttons.get(key);
        List<JButton> removes = new ArrayList<JButton>();
        for (JButton button : buttons) {
            this.remove(button);
            removes.add(button);
        }
        for (JButton button : removes) {
            buttons.remove(button);
        }
    }

    private List<JButton> getButton(String key) {
        if (!this.buttons.containsKey(key))
            this.buttons.put(key, new ArrayList<JButton>());
        return this.buttons.get(key);
    }

    // 註冊元素
    private void regElement() {
        // top:27px else:8px
        this.setBounds(100, 100, 716, 535);// 內部 700x500
        this.setResizable(false);// 禁止視窗大小調整
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setTitle("Bingo");
        this.getContentPane().setLayout(null);

        this.jtfName.setBounds(10, 10, 90, 30);
        this.jtfName.setEditable(false);
        this.add(jtfName);

        this.jtfRoom.setBounds(110, 10, 190, 30);
        this.jtfRoom.setEditable(false);
        this.jtfRoom.setText("未加入房間");
        this.add(jtfRoom);

        this.jtfIP.setBounds(310, 10, 280, 30);
        this.jtfIP.setText("127.0.0.1");// 預設
        this.add(jtfIP);
        JButton connectIP = new JButton("連接");
        connectIP.setBounds(600, 10, 90, 30);
        this.addButton("connect", connectIP);

        this.jspR.setBounds(550, 60, 140, 330);
        this.jtaR.setEditable(false);
        this.add(jspR);

        this.jspD.setBounds(550, 410, 140, 80);
        this.add(jspD);

        int y = 60 + 5 * (50 + 10);
        int height = 60;
        JButton start = new JButton("開始");
        start.setBounds(30, 0 + y, 240, height);
        this.addButton("start", start);
        this.addButton("menu", start, false);
        JButton auto = new JButton("自動選號");
        auto.setBounds(30 + 250, 0 + y, 240, height);
        this.addButton("auto", auto);
        this.addButton("menu", auto, false);
        JButton clear = new JButton("重新選號");
        clear.setBounds(30, 0 + height + 10 + y, 240, height);
        this.addButton("clear", clear);
        this.addButton("menu", clear, false);
        JButton exit = new JButton("離開");
        exit.setBounds(30 + 250, 0 + height + 10 + y, 240, height);
        this.addButton("exit", exit);
        for (JButton menu : this.getButton("menu")) {
            menu.setEnabled(false);
        }

    }

    // 遊戲運行初始化
    private void initComp() {
        for (JButton connect : this.getButton("connect")) {
            connect.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Client.core.connect(ClientFrame.this.jtfIP.getText());
                    ClientFrame.this.checkProgress();
                }
            });
        }

        for (JButton start : this.getButton("start")) {
            start.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ClientFrame.this.start();
                    ClientFrame.this.checkProgress();
                }
            });
        }

        for (JButton auto : this.getButton("auto")) {
            auto.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ClientFrame.this.auto();
                    ClientFrame.this.checkProgress();
                }
            });
        }

        for (JButton clear : this.getButton("clear")) {
            clear.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ClientFrame.this.clear();
                    ClientFrame.this.checkProgress();
                }
            });
        }

        for (JButton exit : this.getButton("exit")) {
            exit.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ClientFrame.this.exit();
                    ClientFrame.this.checkProgress();
                }
            });
        }

        this.jtaD.addKeyListener(new KeyListener() {

            boolean flagS = false;
            boolean flagE = false;

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    this.flagE = true;
                }
                if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
                    this.flagS = true;
                }
            }

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (this.flagE) { // 按下enter
                    if (this.flagS) { // shift 強迫換行
                        ClientFrame.this.jtaD.setText(ClientFrame.this.jtaD.getText() + "\r\n");
                    } else { // 送出
                        sendChat(ClientFrame.this.jtaD.getText());
                        ClientFrame.this.jtaD.setText("");
                    }
                }
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    this.flagE = false;
                }
                if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
                    this.flagS = false;
                }
            }

        });
    }

    // 檢查遊戲進度
    private void checkProgress() {
        if (room) {
            if (start) {
                for (JButton menu : this.getButton("menu")) {
                    menu.setEnabled(false);
                }
            } else {
                for (JButton menu : this.getButton("menu")) {
                    menu.setEnabled(true);
                }
                for (JButton bingo : this.getButton("bingo")) {
                    bingo.setEnabled(true);
                }
                boolean flag = true;
                for (int i = 0; i < this.nums.length; i++) {
                    if (this.nums[i] == -1)
                        flag = false;
                }
                for (JButton start : this.getButton("start")) {
                    start.setEnabled(flag);
                }

                if (this.ready) {
                    for (JButton start : this.getButton("start")) {
                        start.setText("取消");
                    }
                    for (JButton auto : this.getButton("auto")) {
                        auto.setText("開始");
                        auto.setEnabled(false);
                    }
                    for (JButton clear : this.getButton("clear")) {
                        clear.setEnabled(false);
                    }
                } else {
                    this.readyC(false);
                    for (JButton start : this.getButton("start")) {
                        start.setText("準備");
                    }
                    for (JButton auto : this.getButton("auto")) {
                        auto.setText("自動選號");
                        auto.setEnabled(true);
                    }
                    for (JButton clear : this.getButton("clear")) {
                        clear.setEnabled(true);
                    }
                }
            }
        } else {
            for (JButton menu : this.getButton("menu")) {
                menu.setEnabled(false);
            }
            for (JButton bingo : this.getButton("bingo")) {
                bingo.setEnabled(false);
            }
            for (JButton start : this.getButton("start")) {
                start.setEnabled(true);
            }
        }
    }

    // 連線狀態
    public void connect(boolean flag) {
        if (flag) {
            this.jtfIP.setEnabled(false);
            for (JButton button : this.getButton("connect")) {
                button.setEnabled(false);
            }
            this.checkProgress();
        } else {
            this.leaveRoom();
            this.setName("");
            this.jtfIP.setEnabled(true);
            for (JButton button : this.getButton("connect")) {
                button.setEnabled(true);
            }
            for (JButton button : this.getButton("menu")) {
                button.setEnabled(false);
            }
            this.addChat("[訊息] 從伺服斷開連線.");
        }
    }

    public void enterRoom(String roomID, int width, int height) {
        if (this.room) {
            if (roomID.equals(this.roomID))
                return;
            this.leaveRoom();
        }
        for (JButton start : this.getButton("start")) {
            start.setText("準備");
        }
        this.addChat("[訊息] 房間資料 - "+width+"*"+height);
        this.width = width;
        this.height = height;
        if (width > 0 && height > 0) {
            int w = 450 / width;
            int h = 250 / height;
            this.bingo = new JButton[width*height];
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    JButton bingo = new JButton("");
                    bingo.setBounds(30 + x * (w + 10), 60 + y * (h + 10), w, h);
                    bingo.setEnabled(false);
                    bingo.addActionListener(new NumberButtonAction(this, x, y));
                    this.addButton("bingo", bingo);
                    this.bingo[y*width+x] = bingo;
                }
            }
            this.repaint();
        }

        this.jtfRoom.setText(roomID);
        this.num = 1;
        this.nums = new int[width * height];
        for (int i = 0; i < this.nums.length; i++)
            this.nums[i] = -1;
        this.room = true;
        this.checkProgress();
    }

    public void leaveRoom() {
        this.gameStart(false);
        for (JButton start : this.getButton("start")) {
            start.setText("開始");
        }
        this.removeButton("bingo");
        this.bingo = null;
        this.repaint();
        this.jtfRoom.setText("未加入房間");
        this.width = -1;
        this.height = -1;
        this.nums = null;
        this.room = false;
        this.checkProgress();
    }

    public void setName(String name) {
        this.jtfName.setText(name);
    }

    public void trun(boolean flag) {
        this.yourturn = flag;
    }

    public void readyC(boolean flag) {
        if (this.ready && flag) {
            for (JButton auto : this.getButton("auto")) {
                auto.setEnabled(true);
            }
        } else {
            for (JButton auto : this.getButton("auto")) {
                auto.setEnabled(false);
            }
        }
    }

    public void ready(boolean flag) {
        this.ready = flag;
        this.checkProgress();
    }

    public void gameStart(boolean flag) {
        this.start = flag;
        if (this.start) {
            this.checkProgress();
        } else {
            this.clear();
            this.ready(false);
        }
    }

    public void addChat(String data) {
        this.jtaR.append(data + "\r\n");
        JScrollBar vertical = this.jspR.getVerticalScrollBar();
        vertical.setValue(vertical.getMaximum());
    }

    public void addPlayerChat(String data) {
        String[] datas = data.split("@");
        if (datas.length > 1) {
            String msg = "";
            for (int i = 1; i < datas.length; i++) {
                if (i > 1)
                    msg += "@";
                msg += datas[i];
            }
            this.addChat("<" + datas[0] + "> " + msg);
        }
    }

    // button event
    private void sendChat(String msg) {
        try {
            Client.core
                    .sendData(new PacketDataHandle(PacketDataHandle.DataType.Msg, this.jtfName.getText() + "@" + msg));
        } catch (Exception e) {
            this.addChat("[訊息] 傳送訊息失敗!");
        }
    }

    private void start() {
        if (this.room) {
            if (this.ready) {
                try {
                    Client.core.sendData(new PacketDataHandle(PacketDataHandle.DataType.Cancel, ""));
                } catch (Exception e) {
                }
            } else {
                try {
                    String data = "";
                    for (int i = 0; i < this.nums.length; i++) {
                        if (i > 0)
                            data += ",";
                        data += String.valueOf(this.nums[i]);
                    }
                    Client.core.sendData(new PacketDataHandle(PacketDataHandle.DataType.Choose, data));
                } catch (Exception e) {
                    this.addChat("[訊息] 無法開始,請嘗試重新填寫號碼.");
                }
            }
        } else {
            try {
                Client.core.sendData(new PacketDataHandle(PacketDataHandle.DataType.Start, ""));
            } catch (Exception e) {
                this.addChat("[訊息] 無法加入房間!");
            }
        }
    }

    private void auto() {
        if (this.ready) {
            try {
                Client.core.sendData(new PacketDataHandle(PacketDataHandle.DataType.Start, "room"));
            } catch (Exception e) {
            }
            return;
        }
        int n = this.rnd.nextInt(this.width*this.height);
        this.bingoClick(n);
        for (int i = 0; i < this.nums.length; i++) {
            if(this.nums[i] == -1) {
                this.auto();
                break;
            }
        }
    }

    private void clear() {
        this.num = 1;
        this.nums = new int[width * height];
        for (int i = 0; i < this.nums.length; i++)
            this.nums[i] = -1;
        for (JButton bingo : this.getButton("bingo")) {
            bingo.setText("");
            bingo.setBackground(null);
        }
    }

    private void exit() {
        if (room) {
            try {
                Client.core.sendData(new PacketDataHandle(PacketDataHandle.DataType.Start, "leave"));
            } catch (Exception e) {
            }
        } else if (Client.core.disconnect()) {
            System.exit(0);
        }
    }

    public void bingo(int num) {
        for (int i = 0; i<this.nums.length;i++){
            if(this.nums[i]==num)
                this.bingo[i].setBackground(Color.red);
        }
    }

    private void bingoClick(int pos) {
        if (room) {
            if (start) {
                if (this.yourturn) {
                    try {
                        Client.core.sendData(new PacketDataHandle(PacketDataHandle.DataType.Bingo, String.valueOf(this.nums[pos])));
                    } catch (Exception e) {
                        this.addChat("[訊息] 號碼選擇有誤");
                    }
                    this.checkProgress();
                }
            } else {
                if (this.bingo[pos].getBackground() != Color.pink) {
                    this.nums[pos] = this.num;
                    this.bingo[pos].setText(String.valueOf(this.num));
                    this.bingo[pos].setBackground(Color.pink);
                    this.num++;
                    this.checkProgress();
                }
            }
        }
    }

    private static class NumberButtonAction extends AbstractAction {

        final int pos;
        final ClientFrame core;

        private NumberButtonAction(ClientFrame core, int x, int y) {
            this.core = core;
            this.pos = y * core.width + x;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            this.core.bingoClick(this.pos);
            this.core.checkProgress();
        }

    }

}
