import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.io.*;

public class SignUpPage {
    private JFrame frame; 

    public SignUpPage() {
        // Window setting
        frame = new JFrame("Customer Registration - 4Y's Car Rental");
        frame.setSize(950, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        // Main background
        JPanel background = new JPanel(new GridBagLayout());
        background.setBackground(new Color(242, 242, 242));

        // Central Form Card
        JPanel panel = new JPanel(null);
        panel.setPreferredSize(new Dimension(550, 650));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new LineBorder(new Color(153, 204, 255), 10, true));

        JButton backBtn = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(153, 204, 255));
                g2.setStroke(new BasicStroke(3));
                g2.drawPolyline(new int[]{30, 10, 30}, new int[]{10, 25, 40}, 3);
                g2.drawLine(10, 25, 45, 25);
            }
        };
        backBtn.setBounds(20, 20, 60, 50);
        backBtn.setContentAreaFilled(false);
        backBtn.setBorderPainted(false);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> {
            new CustLoginPage().setVisible(true);
            frame.dispose();
        });
        panel.add(backBtn);

        JLabel title = new JLabel("CREATE ACCOUNT", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(new Color(100, 100, 100));
        title.setBounds(0, 80, 550, 40);
        panel.add(title);

        // Positioning
        int labelX = 100;
        int fieldWidth = 350;
        Font labelFont = new Font("Arial", Font.BOLD, 14);

        // Full Name
        JLabel nameLabel = new JLabel("Full Name:");
        nameLabel.setFont(labelFont);
        nameLabel.setBounds(labelX, 150, 130, 30);
        panel.add(nameLabel);

        JTextField nameField = new JTextField();
        nameField.setBounds(labelX, 180, fieldWidth, 40);
        panel.add(nameField);

        // Phone Number
        JLabel phoneLabel = new JLabel("Phone Number: e.g.(01234567890)");
        phoneLabel.setFont(labelFont);
        phoneLabel.setBounds(labelX, 230, 300, 30);
        panel.add(phoneLabel);

        JTextField phoneField = new JTextField();
        phoneField.setBounds(labelX, 260, fieldWidth, 40);
        panel.add(phoneField);

        // Driving License
        JLabel licenseLabel = new JLabel("License No: e.g.(123456789012)");
        licenseLabel.setFont(labelFont);
        licenseLabel.setBounds(labelX, 310, 300, 30);
        panel.add(licenseLabel);

        JTextField licenseField = new JTextField();
        licenseField.setBounds(labelX, 340, fieldWidth, 40);
        panel.add(licenseField);

        // Password
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(labelFont);
        passLabel.setBounds(labelX, 390, 200, 30);
        panel.add(passLabel);

        JPasswordField passField = new JPasswordField();
        passField.setBounds(labelX, 420, fieldWidth, 40);
        panel.add(passField);

        // Action Button
        JButton signupBtn = new JButton("REGISTER");
        signupBtn.setBounds(175, 500, 200, 50);
        signupBtn.setBackground(new Color(153, 204, 255));
        signupBtn.setForeground(Color.WHITE);
        signupBtn.setFont(new Font("Arial", Font.BOLD, 16));
        signupBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        signupBtn.addActionListener(e -> {
            String fullName = nameField.getText().trim();
            String phone = phoneField.getText().trim();
            String license = licenseField.getText().trim();
            String password = new String(passField.getPassword());

            if (fullName.isEmpty() || phone.isEmpty() || license.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please complete all fields.");
                return;
            }

            if (!phone.matches("^\\d{10,11}$")) {
                JOptionPane.showMessageDialog(frame, "Phone number must be 10-11 digits only!", "Invalid Phone Number", JOptionPane.ERROR_MESSAGE);
                phoneField.requestFocus();
                return;
            }

            if (!license.matches("^\\d{12}$")) {
                JOptionPane.showMessageDialog(frame, "License number must be exactly 12 digits!", "Invalid License Number", JOptionPane.ERROR_MESSAGE);
                licenseField.requestFocus();
                return;
            }

            if (password.length() < 6 || password.length() > 11) {
                JOptionPane.showMessageDialog(frame, "Password must be 6-11 characters long!", "Invalid Password", JOptionPane.ERROR_MESSAGE);
                passField.requestFocus();
                return;
            }

           try {
             File file = new File("customer.json");
             StringBuilder content = new StringBuilder();
        
            if (file.exists() && file.length() > 0) {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line);
            }
            br.close();

            int lastBracket = content.lastIndexOf("]");
            if (lastBracket != -1) {
                content.deleteCharAt(lastBracket);
                if (content.toString().trim().length() > 1) {
                    content.append(",");
                }
            }
        } else {
            content.append("[");
        }

        // New user entry
        String newUser = String.format(
            "{\"name\":\"%s\", \"pass\":\"%s\", \"phone\":\"%s\", \"license\":\"%s\"}",
            fullName, password, phone, license
        );
        content.append(newUser).append("]");

        FileWriter fw = new FileWriter(file, false);
        fw.write(content.toString());
        fw.close();

        JOptionPane.showMessageDialog(frame, "Registration Successful!");
        new CustLoginPage().setVisible(true);
        frame.dispose();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Error saving data.");
            }
        });
        panel.add(signupBtn);

        background.add(panel);
        frame.add(background);
    }

    public void setVisible(boolean b) {
        frame.setVisible(b);
    }
}