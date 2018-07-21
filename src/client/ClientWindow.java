package client;

import javax.swing.*;
import java.awt.*;

public class ClientWindow {
    private JFrame frame;
    private JTextField textField;
    private static JTextArea textArea = new JTextArea();
    private Client client;

    public static void main(String[] args){
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try{
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    ClientWindow window = new ClientWindow();
                    window.frame.setVisible(true);
                } catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        });
    }

    public ClientWindow(){
        initialize();
        String name = JOptionPane.showInputDialog("Enter your name:");
        client = new Client(name, "localhost", 5000);
    }

    private void initialize(){
        frame = new JFrame();
        frame.setTitle("Chat");
        frame.setResizable(false);
        frame.setBounds(100,100,640,480);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout(0,0));

        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        frame.getContentPane().add(scrollPane);

        JPanel panel = new JPanel();
        frame.getContentPane().add(panel, BorderLayout.SOUTH);
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5,5));

        textField = new JTextField();
        panel.add(textField);
        textField.setColumns(44);

        JButton button = new JButton("Send");
        button.addActionListener(e -> {
            if(!textField.getText().equals("")) {
                client.send(textField.getText());
                textField.setText("");
            }
        });
        panel.add(button);

        frame.setLocationRelativeTo(null);
    }

    public static void printToConsole(String message){
        textArea.setText(textArea.getText()+message+"\n");
    }
}
