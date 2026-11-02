import javax.swing.*;
import java.awt.*;

public class CarRentalLogin {
    private JFrame frame;

    public CarRentalLogin() {
        frame = new JFrame("4Y's Car Rental");
        frame.setSize(950, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        //Header panel
        JPanel headerPanel = new JPanel(new GridLayout(2, 1));
        headerPanel.setBackground(new Color(240, 240, 240));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(50, 0, 20, 0));

        JLabel titleLabel = new JLabel("4Y's Car Rental", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 48));
        titleLabel.setForeground(new Color(50, 50, 50));

        JLabel subtitleLabel = new JLabel("Welcome! Please Choose a Portal", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        subtitleLabel.setForeground(new Color(100, 100, 100));

        headerPanel.add(titleLabel);
        headerPanel.add(subtitleLabel);

        //Button panel
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setBackground(new Color(240, 240, 240));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JButton customerBtn = new JButton("CUSTOMER PORTAL");
        styleButton(customerBtn, new Color(138, 190, 249));
        customerBtn.addActionListener(e -> {
            new CustLoginPage().setVisible(true);
            frame.dispose();
        });

        JButton staffBtn = new JButton("STAFF PORTAL");
        styleButton(staffBtn, new Color(153, 204, 255));
        staffBtn.addActionListener(e -> {
            new StaffLoginPage().setVisible(true);
            frame.dispose();
        });

        gbc.gridy = 0; buttonPanel.add(customerBtn, gbc);
        gbc.gridy = 1; buttonPanel.add(staffBtn, gbc);

        //Footer label
        JLabel footerLabel = new JLabel("🚗  Fast • Reliable • Secure", SwingConstants.CENTER);
        footerLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        footerLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 40, 0));

        frame.add(headerPanel, BorderLayout.NORTH);
        frame.add(buttonPanel, BorderLayout.CENTER);
        frame.add(footerLabel, BorderLayout.SOUTH);
    }

    private void styleButton(JButton btn, Color color) {
        btn.setPreferredSize(new Dimension(400, 80));
        btn.setFont(new Font("Arial", Font.BOLD, 22));
        btn.setBackground(color);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color.darker(), 2),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
    }

    public void setVisible(boolean b) { frame.setVisible(b); }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CarRentalLogin().setVisible(true));
    }
}