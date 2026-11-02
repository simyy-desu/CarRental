import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ReportPage { 
    private JFrame frame; 
    private final Color ACCENT_BLUE = new Color(142, 190, 247);
    private JTextArea txtDescription;
    private JRadioButton rbService, rbRepair, rbMaintenance;

    public ReportPage(String carName, String plateNum) {
        //Frame setting
        frame = new JFrame("Report Vehicle Issue");
        frame.setSize(950, 800);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(new Color(248, 249, 250));

        //Header panel
        JPanel header = new JPanel();
        header.setBackground(ACCENT_BLUE);
        header.setPreferredSize(new Dimension(0, 60));
        JLabel title = new JLabel("REPORT FOR SERVICE");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        header.add(title);
        frame.add(header, BorderLayout.NORTH);

        //Body panel
        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBorder(new EmptyBorder(30, 40, 30, 40));
        body.setOpaque(false);

        JLabel lblCarInfo = new JLabel("Reporting: " + carName + " [" + plateNum + "]");
        lblCarInfo.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblCarInfo.setAlignmentX(Component.LEFT_ALIGNMENT);
        body.add(lblCarInfo);
        body.add(Box.createRigidArea(new Dimension(0, 20)));

        JLabel lblType = new JLabel("ISSUE TYPE");
        lblType.setFont(new Font("SansSerif", Font.BOLD, 11));
        lblType.setForeground(Color.GRAY);
        lblType.setAlignmentX(Component.LEFT_ALIGNMENT);
        body.add(lblType);

        rbService = new JRadioButton("Service Required");
        rbRepair = new JRadioButton("Repair Needed");
        rbMaintenance = new JRadioButton("Routine Maintenance");
        
        ButtonGroup group = new ButtonGroup();
        group.add(rbService);
        group.add(rbRepair);
        group.add(rbMaintenance);

        JRadioButton[] rbs = {rbService, rbRepair, rbMaintenance};
        for (JRadioButton rb : rbs) {
            rb.setOpaque(false);
            rb.setFocusPainted(false);
            rb.setAlignmentX(Component.LEFT_ALIGNMENT);
            body.add(rb);
        }

        body.add(Box.createRigidArea(new Dimension(0, 25)));

        JLabel lblDesc = new JLabel("DESCRIBE PROBLEM");
        lblDesc.setFont(new Font("SansSerif", Font.BOLD, 11));
        lblDesc.setForeground(Color.GRAY);
        lblDesc.setAlignmentX(Component.LEFT_ALIGNMENT);
        body.add(lblDesc);
        body.add(Box.createRigidArea(new Dimension(0, 5)));

        txtDescription = new JTextArea(8, 20);
        txtDescription.setLineWrap(true);
        txtDescription.setWrapStyleWord(true);
        txtDescription.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(210, 210, 210)),
            new EmptyBorder(10, 10, 10, 10)
        ));
        
        JScrollPane scroll = new JScrollPane(txtDescription);
        scroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        body.add(scroll);

        frame.add(body, BorderLayout.CENTER);

        //Footer panel
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 20));
        footer.setOpaque(false);

        //Cancel button
        JButton btnCancel = new JButton("Cancel");
        btnCancel.setContentAreaFilled(false);
        btnCancel.setBorderPainted(false);
        btnCancel.setFocusPainted(false);
        btnCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancel.addActionListener(e -> frame.dispose());

        //Submit button
        JButton btnSubmit = new JButton("Submit Report");
        btnSubmit.setPreferredSize(new Dimension(150, 45));
        styleBlueButton(btnSubmit);
        btnSubmit.setForeground(Color.WHITE);
        
        btnSubmit.addActionListener(e -> {
            String selectedType = "";
            if (rbService.isSelected()) selectedType = "Service Required";
            else if (rbRepair.isSelected()) selectedType = "Repair Needed";
            else if (rbMaintenance.isSelected()) selectedType = "Routine Maintenance";

            String description = txtDescription.getText().trim();

            if (selectedType.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please select an issue type.", "Input Error", JOptionPane.ERROR_MESSAGE);
            } else if (description.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please describe the problem.", "Input Error", JOptionPane.ERROR_MESSAGE);
                txtDescription.requestFocus();
            } else {
                Report.saveReport(carName, plateNum, selectedType, description);
                
                JOptionPane.showMessageDialog(frame, "Report submitted and recorded for " + carName);
                frame.dispose();
            }
        });

        footer.add(btnCancel);
        footer.add(btnSubmit);
        frame.add(footer, BorderLayout.SOUTH);
    }

    private void styleBlueButton(JButton btn) {
        btn.setBackground(ACCENT_BLUE);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(true);
        btn.setFocusPainted(false);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
    }


    public void setVisible(boolean visible) {
        frame.setVisible(visible);
    }
}