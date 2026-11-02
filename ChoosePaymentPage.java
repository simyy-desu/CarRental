import javax.swing.*;
import java.awt.*;

public class ChoosePaymentPage {
    private JFrame frame;
    private Booking data;

    public JButton backBtn;
    public JButton payBtn;
    public JRadioButton visaBtn, creditCardBtn, masterCardBtn, ewalletBtn;
    public ButtonGroup paymentGroup;

    public ChoosePaymentPage() {
        this.data = Data.bookingInfo;

        //Frame setting
        frame = new JFrame("Choose Payment");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(950, 800);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(new Color(245, 245, 245));

        //Top panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        //Back button
        backBtn = new JButton("<");
        backBtn.setFont(new Font("Arial", Font.BOLD, 28));
        backBtn.setForeground(new Color(30, 80, 200));
        backBtn.setContentAreaFilled(false);
        backBtn.setBorderPainted(false);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        topPanel.add(backBtn, BorderLayout.WEST);

        //Title
        JLabel title = new JLabel("Choose Payment", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 30));
        topPanel.add(title, BorderLayout.CENTER);

        frame.add(topPanel, BorderLayout.NORTH);

        //Center panel
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(50, 80, 50, 80));

        visaBtn = new JRadioButton("VISA");
        creditCardBtn = new JRadioButton("Credit Card");
        masterCardBtn = new JRadioButton("Master Card");
        ewalletBtn = new JRadioButton("E-wallet");

        paymentGroup = new ButtonGroup();
        paymentGroup.add(visaBtn);
        paymentGroup.add(creditCardBtn);
        paymentGroup.add(masterCardBtn);
        paymentGroup.add(ewalletBtn);

        customizeRadioButton(visaBtn);
        customizeRadioButton(creditCardBtn);
        customizeRadioButton(masterCardBtn);
        customizeRadioButton(ewalletBtn);

        centerPanel.add(visaBtn);
        centerPanel.add(Box.createVerticalStrut(40));
        centerPanel.add(creditCardBtn);
        centerPanel.add(Box.createVerticalStrut(40));
        centerPanel.add(masterCardBtn);
        centerPanel.add(Box.createVerticalStrut(40));
        centerPanel.add(ewalletBtn);

        frame.add(centerPanel, BorderLayout.CENTER);

        // Bottom panel
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 30, 0));

        payBtn = new JButton("Pay");
        payBtn.setPreferredSize(new Dimension(220, 50));
        payBtn.setFont(new Font("Arial", Font.PLAIN, 18));
        payBtn.setBackground(new Color(66, 133, 244));
        payBtn.setForeground(Color.WHITE);
        payBtn.setBorderPainted(false);
        payBtn.setFocusPainted(false);
        payBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        bottomPanel.add(payBtn);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        backBtn.addActionListener(e -> {
            new Payment().setVisible(true);
            frame.dispose();
        });

        payBtn.addActionListener(e -> {
            if (paymentGroup.getSelection() == null) {
                JOptionPane.showMessageDialog(frame, "Please select a payment method!", "Selection Required", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String paymentMethod = "";
            if (visaBtn.isSelected()) paymentMethod = "VISA";
            else if (creditCardBtn.isSelected()) paymentMethod = "Credit Card";
            else if (masterCardBtn.isSelected()) paymentMethod = "Master Card";
            else if (ewalletBtn.isSelected()) paymentMethod = "E-wallet";

            data.setPaymentMethod(paymentMethod);

            new Receipt(data).setVisible(true);
            frame.dispose();
        });
    }

    private void customizeRadioButton(JRadioButton btn) {
        btn.setFont(new Font("Arial", Font.PLAIN, 23));
        btn.setOpaque(false);
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    public void setVisible(boolean visible) {
        frame.setVisible(visible);
    }
}