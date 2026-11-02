import javax.swing.*;
import java.awt.*;

public class StaffMainPage {
    private JFrame frame; // Internal frame reference
    private String currentStaffID;

    public StaffMainPage(String staffID) {
        this.currentStaffID = staffID;
        
        // Frame setting
        frame = new JFrame("Staff Main Page - 4Y's Car Rental");
        frame.setSize(950, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(new Color(240, 240, 240));

        // Top panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 30));

        // Profile Info and Avatar
        JPanel profilePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        profilePanel.setOpaque(false);

        // User ID + Logout Link
        JPanel userTextPanel = new JPanel(new GridLayout(2, 1));
        userTextPanel.setOpaque(false);
        
        JLabel lblUsername = new JLabel("Staff " + currentStaffID, SwingConstants.RIGHT);
        lblUsername.setFont(new Font("Arial", Font.BOLD, 14));
        
        JButton btnLogout = new JButton("Logout");
        btnLogout.setForeground(Color.RED);
        btnLogout.setBorderPainted(false);
        btnLogout.setContentAreaFilled(false);
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogout.addActionListener(e -> logout());

        userTextPanel.add(lblUsername);
        userTextPanel.add(btnLogout);

        JButton avatarBtn = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(173, 216, 230));
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
        avatarBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "Profile Details:\nStaff ID: " + currentStaffID + "\nStatus: Active Session");
        });

        profilePanel.add(userTextPanel);
        profilePanel.add(avatarBtn);
        topPanel.add(profilePanel, BorderLayout.EAST);

        // Center panel
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(15, 0, 15, 0); 

        // Fleet Overview Button
        JButton btnOverview = createMenuButton("FLEET OVERVIEW", 100, e -> {
            new carlistpage(currentStaffID).setVisible(true);
            frame.dispose();
        });

        JButton btnPickup = createMenuButton("RENTAL PICKUP LIST", 80, e -> {
            new PickupList(currentStaffID).setVisible(true);
            frame.dispose();
        });

        JButton btnReturn = createMenuButton("RENTAL RETURN LIST", 80, e -> {
            new ReturnList(currentStaffID).setVisible(true);
            frame.dispose();
        });

        gbc.gridy = 0;
        centerPanel.add(btnOverview, gbc);
        gbc.gridy = 1;
        centerPanel.add(btnPickup, gbc);
        gbc.gridy = 2;
        centerPanel.add(btnReturn, gbc);

        // Bottom
        JLabel carIcon = new JLabel("🚗");
        carIcon.setFont(new Font("Serif", Font.PLAIN, 80));
        carIcon.setBorder(BorderFactory.createEmptyBorder(0, 40, 20, 0));

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(centerPanel, BorderLayout.CENTER);
        frame.add(carIcon, BorderLayout.SOUTH);
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to logout?", "Confirm Logout", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            new CarRentalLogin().setVisible(true); 
            frame.dispose();
        }
    }

    private JButton createMenuButton(String text, int height, java.awt.event.ActionListener action) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(500, height));
        btn.setBackground(new Color(142, 190, 247));
        btn.setFont(new Font("Arial", Font.BOLD, 20));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        if (action != null) btn.addActionListener(action);
        return btn;
    }

    public void setVisible(boolean visible) {
        frame.setVisible(visible);
    }
}