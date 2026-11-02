import javax.swing.*;
import java.awt.*;
import java.io.*;

public class CustLoginPage {
    private JFrame frame;

    public CustLoginPage() {
        frame = new JFrame("Customer Portal - 4Y's Car Rental");
        frame.setSize(950, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel container = new JPanel(new GridBagLayout());
        container.setBackground(new Color(242, 242, 242));

        JPanel loginCard = new JPanel(null);
        loginCard.setPreferredSize(new Dimension(550, 520));
        loginCard.setBackground(Color.WHITE);
        loginCard.setBorder(BorderFactory.createLineBorder(new Color(153, 204, 255), 10));

        //Back Arrow
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
            new CarRentalLogin().setVisible(true);
            frame.dispose();
        });
        loginCard.add(backBtn);

        JLabel titleLabel = new JLabel("CUSTOMER LOGIN", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setBounds(0, 80, 550, 40);
        loginCard.add(titleLabel);

        //User input
        JLabel userLabel = new JLabel("Full Name:");
        userLabel.setBounds(100, 150, 150, 30);
        loginCard.add(userLabel);

        JTextField userField = new JTextField();
        userField.setBounds(100, 180, 350, 40);
        loginCard.add(userField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(100, 230, 150, 30);
        loginCard.add(passLabel);

        JPasswordField passField = new JPasswordField();
        passField.setBounds(100, 260, 350, 40);
        loginCard.add(passField);

        //Login button
        JButton loginBtn = new JButton("LOGIN");
        loginBtn.setBackground(new Color(153, 204, 255));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFont(new Font("Arial", Font.BOLD, 16));
        loginBtn.setBounds(175, 330, 200, 45);
        loginBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        loginBtn.addActionListener(e -> {
    String nameInput = userField.getText().trim(); 
    String passInput = new String(passField.getPassword()).trim();

    Customer currentCust = new Customer(nameInput, passInput);

    if (currentCust.getName().isEmpty() || currentCust.getPassword().isEmpty()) {
        JOptionPane.showMessageDialog(frame, "Please enter both name and password.");
        return;
    }

    try {
        File file = new File("customer.json");
        if (!file.exists()) {
            JOptionPane.showMessageDialog(frame, "Database not found!");
            return;
        }

        BufferedReader br = new BufferedReader(new FileReader(file));
        String content;
        boolean authenticated = false;

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        content = sb.toString();

        String searchPattern = "\"name\":\"" + currentCust.getName() + "\", \"pass\":\"" + currentCust.getPassword() + "\"";
        
        if (content.contains(searchPattern)) {
            authenticated = true;
        }

        if (authenticated) {
            Data.currentUsername = currentCust.getName();
            new CustMainPage(currentCust.getName()).setVisible(true);
            frame.dispose();
        } else {
            JOptionPane.showMessageDialog(frame, "Invalid Name or Password", "Error", JOptionPane.ERROR_MESSAGE);
        }
    } catch (Exception ex) {
        ex.printStackTrace();
    }
});
        loginCard.add(loginBtn);

        //Signup link
        JLabel signUpLink = new JLabel("New user? Create an account here", SwingConstants.CENTER);
        signUpLink.setBounds(0, 410, 550, 30);
        signUpLink.setForeground(new Color(0, 102, 204));
        signUpLink.setFont(new Font("Arial", Font.PLAIN, 14));
        signUpLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        signUpLink.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                new SignUpPage().setVisible(true);
                frame.dispose();
            }
        });
        loginCard.add(signUpLink);

        container.add(loginCard);
        frame.add(container);
    }

    public void setVisible(boolean b) { frame.setVisible(b); }
}