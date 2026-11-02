import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;

public class CustMainPage {
    private JFrame frame;
    private String currentUsername;

    public CustMainPage(String username) {
        this.currentUsername = username;
        
        //Frame setting
        frame = new JFrame("Customer Portal - 4Y's Car Rental");
        frame.setSize(950, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(new Color(240, 240, 240));

        //Top panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 30));

        // Left side Title/Logo
        JLabel logoLabel = new JLabel("4Y's Car Rental");
        logoLabel.setFont(new Font("Arial", Font.BOLD, 24));
        logoLabel.setForeground(new Color(50, 50, 50));
        topPanel.add(logoLabel, BorderLayout.WEST);

        // Right Section: Welcome, Logout, and Avatar
        JPanel profilePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        profilePanel.setOpaque(false);

        // Text Section (Welcome + Logout)
        JPanel userTextPanel = new JPanel(new GridLayout(2, 1));
        userTextPanel.setOpaque(false);
        
        JLabel lblWelcome = new JLabel("Welcome, " + currentUsername + "!", SwingConstants.RIGHT);
        lblWelcome.setFont(new Font("Arial", Font.BOLD, 16));
        lblWelcome.setForeground(new Color(30, 80, 200));
        
        JButton btnLogout = new JButton("Logout");
        btnLogout.setForeground(Color.RED);
        btnLogout.setBorderPainted(false);
        btnLogout.setContentAreaFilled(false);
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogout.setHorizontalAlignment(SwingConstants.RIGHT);
        btnLogout.addActionListener(e -> logout());

        userTextPanel.add(lblWelcome);
        userTextPanel.add(btnLogout);

        JButton avatarBtn = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(153, 204, 255)); // Light Blue
                g2.fillOval(0, 0, 60, 60);
                g2.setColor(Color.BLACK);
                g2.setFont(new Font("Arial", Font.PLAIN, 10));
                g2.drawString("Profile", 15, 35);
            }
        };
        avatarBtn.setPreferredSize(new Dimension(60, 60));
        avatarBtn.setOpaque(false);
        avatarBtn.setContentAreaFilled(false);
        avatarBtn.setBorderPainted(false);
        avatarBtn.addActionListener(e -> showUserProfile());

        profilePanel.add(userTextPanel);
        profilePanel.add(avatarBtn);
        topPanel.add(profilePanel, BorderLayout.EAST);

        //Center panel
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(15, 0, 15, 0);

        //Book a Car
        JButton btnBook = createMenuButton("BOOK A CAR", e -> {
            new BookingPage().setVisible(true); 
            frame.dispose();
        });

        JButton btnHistory = createMenuButton("VIEW HISTORY", e -> {
            new BookingHistory(currentUsername).setVisible(true);
            frame.dispose(); 
        });

        gbc.gridy = 0;
        centerPanel.add(btnBook, gbc);
        gbc.gridy = 1;
        centerPanel.add(btnHistory, gbc);

        //Car icon
        JLabel carIcon = new JLabel("🚗");
        carIcon.setFont(new Font("Serif", Font.PLAIN, 80));
        carIcon.setBorder(BorderFactory.createEmptyBorder(0, 40, 20, 0));

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(centerPanel, BorderLayout.CENTER);
        frame.add(carIcon, BorderLayout.SOUTH);
    }

    private void showUserProfile() {
        String phone = "N/A";
        String license = "N/A";

        try (BufferedReader br = new BufferedReader(new FileReader("Cust.json"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("\"name\":\"" + currentUsername + "\"")) {
                    if (line.contains("\"phone\":\"")) {
                        phone = line.split("\"phone\":\"")[1].split("\"")[0];
                    }
                    if (line.contains("\"license\":\"")) {
                        license = line.split("\"license\":\"")[1].split("\"")[0];
                    }
                    break;
                }
            }
        } catch (Exception ex) {
            System.out.println("Could not load profile data.");
        }

        String message = String.format(
            "Account Details:\n" +
            "----------------------\n" +
            "Username: %s\n" +
            "Phone: %s\n" +
            "License: %s",
            currentUsername, phone, license
        );
        JOptionPane.showMessageDialog(frame, message, "My Profile", JOptionPane.INFORMATION_MESSAGE);
    }

    private JButton createMenuButton(String text, java.awt.event.ActionListener action) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(500, 80));
        btn.setBackground(new Color(142, 190, 247));
        btn.setFont(new Font("Arial", Font.BOLD, 22));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        if (action != null) btn.addActionListener(action);
        return btn;
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(frame, "Logout and return to Portal Selection?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            new CarRentalLogin().setVisible(true);
            frame.dispose();
        }
    }

    public void setVisible(boolean visible) {
        frame.setVisible(visible);
    }
}