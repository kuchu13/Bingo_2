import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class MainFrame extends JFrame {
    private Container cp;
    private JButton jbtnstart = new JButton("Start");
    private JButton jbtnauto = new JButton("Auto");
    private JButton jbtnclear = new JButton("Clear");
    private JButton jbtnexit = new JButton("Exit");
    private JButton jbtnsend = new JButton("Send");
    private JButton jbtnconnect = new JButton("Connect");

    private JTextField jtfnconnect = new JTextField("");

    private JButton jbts[] = new JButton[25];

    private JPanel jplbingo = new JPanel(new GridLayout(5, 5, 3, 3));
    private JPanel jplsouth = new JPanel();
    private JPanel jpnc = new JPanel(new GridLayout(2, 2, 3, 3));
    private JPanel jple = new JPanel(new GridLayout(2,1,3,3));


    private JTextArea jtaU = new JTextArea();
    private JTextArea jtaD = new JTextArea();

    private JScrollPane jspU = new JScrollPane(jtaU);
    private JScrollPane jspD = new JScrollPane(jtaD);

    private Random rnd = new Random();

    private boolean start = false;

    int num = 1;

    public MainFrame() {
        initComp();
    }

    private void initComp() {

        this.setBounds(100, 100, 700, 500);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        MainFrame.this.setTitle("Bingo");

        cp = this.getContentPane();
        cp.setLayout(new BorderLayout(3, 3));

        jpnc.add(jbtnstart);
        jpnc.add(jbtnauto);
        jpnc.add(jbtnclear);
        jpnc.add(jbtnexit);
        jplsouth.add(jspD, BorderLayout.CENTER);
        jplsouth.add(jbtnsend, BorderLayout.EAST);

        jple.add(jtfnconnect,BorderLayout.NORTH);
        jple.add(jbtnconnect,BorderLayout.SOUTH);




        for (int i = 0; i < jbts.length; i++) {
            jbts[i] = new JButton();
            jbts[i].setFont(new Font(null, Font.BOLD, 38));
            jbts[i].addActionListener(new NumberButtonAction(i,this));
            jplbingo.add(jbts[i]);

            int finalI = i;

            int finalI1 = i;
            jbtnstart.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {


                    jbts[finalI1].addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            String input;
                            input=jbts[finalI].getText();
                            String get=String.valueOf(input);
                            jtaU.append("選擇了"+get+"\n");
                        }
                    });}
            });
        }

        cp.add(jpnc, BorderLayout.WEST);
        cp.add(jplbingo, BorderLayout.CENTER);
        cp.add(jspU, BorderLayout.NORTH);
        cp.add(jplsouth, BorderLayout.SOUTH);
        cp.add(jple, BorderLayout.EAST);

        jspU.setPreferredSize(new Dimension(400, 50));
        jspD.setPreferredSize(new Dimension(500, 50));

        jtaU.setLineWrap(true);
        jtaU.setEditable(false);
        jtaD.setLineWrap(true);

        jbtnexit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        jbtnstart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainFrame.this.start = true;
                MainFrame.this.checkProgress();
            }
        });

        jbtnclear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainFrame.this.clearNumber();
                MainFrame.this.checkProgress();
            }
        });

        jbtnauto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainFrame.this.autoNumber();
                MainFrame.this.checkProgress();
            }
        });

        jbtnconnect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainFrame.this.connect();
                MainFrame.this.checkProgress();
            }
        });

        checkProgress();

        jbtnsend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input;
                input=jtaD.getText();
                String get=String.valueOf(input);
                jtaU.append(get+"\n");
                jtaD.setText("");
            }
        });

    }

    private void clearNumber() {
        if (start)
            return;
        for (int i = 0; i < jbts.length; i++) {
            jbts[i].setText("");
            jbts[i].setBackground(null);
        }
        num = 1;
    }

    private boolean chooseNumber(int pos) {
        if(jbts[pos].getText().equals("")){
            jbts[pos].setText(String.valueOf(num));
            jbts[pos].setBackground(Color.pink);
            num++;
            return true;
        }
        return false;
    }

    private void autoNumber(){
        while(num<=jbts.length){
            if (!chooseNumber(rnd.nextInt(jbts.length))){

            }
        }
    }
    private void connect(){
        String input;
        input=jtfnconnect.getText();



    }

    int temp_line = 0;

    private void checkProgress() {
        if(jbtnconnect ) {
            jbtnstart.setEnabled(true);
            jbtnclear.setEnabled(true);
            jbtnauto.setEnabled(true);
            jbtnsend.setEnabled(true);

        } if(jbtnconnect){
            jbtnstart.setEnabled(false);
            jbtnclear.setEnabled(false);
            jbtnauto.setEnabled(false);
            jbtnsend.setEnabled(false);
        }
            return;
        }



        for (int i = 0; i < jbts.length; i++) {

            if(jbts[i].getText().equals("")){
                jbtnstart.setEnabled(false);
                jbtnclear.setEnabled(true);
                jbtnauto.setEnabled(true);
                return;
            }
        }
        if (start) {
            jbtnstart.setEnabled(false);
            jbtnclear.setEnabled(false);
            jbtnauto.setEnabled(false);
            int line = 0;
            if(jbts.length==25){
                if(checkLine25(0,6,12,18,24))
                    line++;
                if(checkLine25(4,8,12,16,20))
                    line++;
                for (int i = 0; i < 5; i++) {
                    if(checkLine25(i*5+0,i*5+1,i*5+2,i*5+3,i*5+4))
                        line++;
                    if(checkLine25(i+0*5,i+1*5,i+2*5,i+3*5,i+4*5))
                        line++;
                }
            }
            if(line>temp_line){
                temp_line = line;
                jtaU.append("你完成了"+temp_line+"條連線\r\n");
                if(temp_line>=3){
                    jtaU.append("WINNER");
                    JOptionPane.showMessageDialog(null, "WINNER", "Congratulation!!", JOptionPane.INFORMATION_MESSAGE );
                    System.exit(0);
                }
            }
        } else {
            jbtnstart.setEnabled(true);
        }
    }

    private boolean checkLine25(int a,int b,int c,int d,int e){
        Color check = Color.RED;
        if (a >= 25 || a < 0)
            return false;
        if (b >= 25 || b < 0)
            return false;
        if (c >= 25 || c < 0)
            return false;
        if (d >= 25 || d < 0)
            return false;
        if (e >= 25 || e < 0)
            return false;
        return jbts[a].getBackground() == check && jbts[b].getBackground() == check && jbts[c].getBackground() == check && jbts[d].getBackground() == check&& jbts[e].getBackground() == check;
    }

    private static class NumberButtonAction extends AbstractAction {

        final int pos;
        final MainFrame core;

        private NumberButtonAction(int pos,MainFrame core) {
            this.pos = pos;
            this.core = core;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (core.start) {
                JButton jb = (JButton) e.getSource();
                jb.setBackground(Color.RED);
            } else {
                core.chooseNumber(pos);
            }
            core.checkProgress();
        }

    }

}