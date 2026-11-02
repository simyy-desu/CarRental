import javax.swing.*;
import java.awt.*;
import java.io.*;

public class StaffLoginPage {
    private JFrame frame;

    public StaffLoginPage() {
        // Window Setup
        frame = new JFrame("Staff Portal - 4Y's Car Rental");
        frame.setSize(950, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        // Main background panel
        JPanel container = new JPanel(new GridBagLayout());
        container.setBackground(new Color(242, 242, 242));

        // Central Login Card
        JPanel loginCard = new JPanel(null);
        loginCard.setPreferredSize(new Dimension(550, 480));
        loginCard.setBackground(Color.WHITE);
        loginCard.setBorder(BorderFactory.createLineBorder(new Color(153, 204, 255), 10));

        // Back Arrow
        JButton backBtn = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(153, 204, 255));
                g2.setStroke(new BasicStroke(3));
                // Drawing the hand-drawn style arrow
                g2.drawPolyline(new int[]{30, 10, 30}, new int[]{10, 25, 40}, 3);
                g2.drawLine(10, 25, 45, 25);
            }
        };
        backBtn.setBounds(20, 20, 60, 50);
        backBtn.setContentAreaFilled(false);
        backBtn.setBorderPainted(false);
        backBtn.setFocusPainted(false);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> {
            new CarRentalLogin().setVisible(true);
            frame.dispose();
        });
        loginCard.add(backBtn);

        // Title
        JLabel titleLabel = new JLabel("STAFF LOGIN", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(new Color(100, 100, 100));
        titleLabel.setBounds(0, 80, 550, 40);
        loginCard.add(titleLabel);

        // Input Fields
        JLabel userLabel = new JLabel("Full Name:");
        userLabel.setBounds(100, 150, 150, 30);
        userLabel.setFont(new Font("Arial", Font.BOLD, 14));
        loginCard.add(userLabel);

        JTextField userField = new JTextField();
        userField.setBounds(100, 180, 350, 40);
        loginCard.add(userField);

        JLabel passLabel = new JLabel("Staff Password:");
        passLabel.setBounds(100, 230, 150, 30);
        passLabel.setFont(new Font("Arial", Font.BOLD, 14));
        loginCard.add(passLabel);

        JPasswordField passField = new JPasswordField();
        passField.setBounds(100, 260, 350, 40);
        loginCard.add(passField);

        // Login Button
        JButton loginBtn = new JButton("LOGIN");
        loginBtn.setBackground(new Color(153, 204, 255));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFont(new Font("Arial", Font.BOLD, 16));
        loginBtn.setBounds(175, 340, 200, 45);
        loginBtn.setFocusPainted(false);
        loginBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        loginBtn.addActionListener(e -> {
            String nameInput = userField.getText().trim(); 
            String passInput = new String(passField.getPassword()).trim();
            Staff currentStaff = new Staff(nameInput, passInput);

            if (currentStaff.getName().isEmpty() || currentStaff.getPassword().isEmpty()) {
              JOptionPane.showMessageDialog(frame, "Please enter both name and password.");
              return;
            }
            try {
                File file = new File("staff.json");
                if (!file.exists()) {
                    JOptionPane.showMessageDialog(frame, "staff.json not found!");
                    return;
                }

                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;
                boolean authenticated = false;

                while ((line = br.readLine()) != null) {
                    if (line.contains("\"name\":\"" + nameInput + "\"") && 
                        line.contains("\"pass\":\"" + passInput + "\"")) {
                        authenticated = true;
                        break;
                    }
                }
                br.close();

               if (authenticated) {
                 new StaffMainPage(currentStaff.getName()).setVisible(true);
                 frame.dispose();
               } else {
                    JOptionPane.showMessageDialog(frame, "Wrong staff name or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        loginCard.add(loginBtn);

        container.add(loginCard);
        frame.add(container);
    }

    public void setVisible(boolean b) { 
        frame.setVisible(b); 
    }
}