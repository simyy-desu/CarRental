import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class AddCarForm { 
    private JFrame frame; 
    private JTextField txtName, txtType, txtPlate, txtPrice, txtSeats;
    private JComboBox<String> comboCategory;
    private JLabel lblImagePreview;
    private File selectedImageFile;
    private carlistpage parentPage;
    private final Color ACCENT_BLUE = new Color(142, 190, 247);

    public AddCarForm(carlistpage parent) {
        this.parentPage = parent;
        frame = new JFrame("Vehicle Registration");
        frame.setSize(950, 850);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null); 
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(new Color(248, 249, 250));

        setupUI();
    }

    private void setupUI() {
        // Header
        JPanel header = new JPanel();
        header.setBackground(ACCENT_BLUE);
        header.setPreferredSize(new Dimension(0, 60));
        JLabel title = new JLabel("REGISTER NEW VEHICLE");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        header.add(title);
        frame.add(header, BorderLayout.NORTH);

        // Form fields
        JPanel mainContainer = new JPanel(new GridBagLayout());
        mainContainer.setOpaque(false);
        mainContainer.setBorder(new EmptyBorder(30, 50, 30, 50));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        JPanel leftPanel = new JPanel(new GridLayout(0, 1, 0, 15));
        leftPanel.setOpaque(false);
        txtName = createField("Vehicle Model Name");
        txtType = createField("Body Type");
        txtPlate = createField("Plate Number");
        txtPrice = createField("Rent Price (RM /day)");
        txtSeats = createField("Seating Capacity");
        
        comboCategory = new JComboBox<>(new String[]{"Economy", "Luxury", "RV"});
        comboCategory.setPreferredSize(new Dimension(0, 40));

        leftPanel.add(new JLabel("Vehicle Category:"));
        leftPanel.add(comboCategory);
        leftPanel.add(txtName); leftPanel.add(txtType);
        leftPanel.add(txtPlate); leftPanel.add(txtPrice); leftPanel.add(txtSeats);

        // Image Preview
        JPanel rightPanel = new JPanel(new BorderLayout(0, 15));
        rightPanel.setOpaque(false);
        lblImagePreview = new JLabel("No Image Selected", SwingConstants.CENTER);
        lblImagePreview.setPreferredSize(new Dimension(350, 200));
        lblImagePreview.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
        JButton btnUpload = new JButton("Select Vehicle Photo");
        styleButton(btnUpload);
        btnUpload.addActionListener(e -> chooseImage());
        rightPanel.add(lblImagePreview, BorderLayout.CENTER);
        rightPanel.add(btnUpload, BorderLayout.SOUTH);

        gbc.gridx = 0; gbc.weightx = 0.6; mainContainer.add(leftPanel, gbc);
        gbc.gridx = 1; gbc.weightx = 0.4; mainContainer.add(rightPanel, gbc);
        frame.add(mainContainer, BorderLayout.CENTER);

        // Footer
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 20));
        JButton btnSave = new JButton("Save Vehicle");
        btnSave.setPreferredSize(new Dimension(150, 40));
        styleButton(btnSave);
        btnSave.addActionListener(e -> handleSave());
        footer.add(btnSave);
        frame.add(footer, BorderLayout.SOUTH);
    }

    private void chooseImage() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) { 
            selectedImageFile = chooser.getSelectedFile();
            ImageIcon icon = new ImageIcon(selectedImageFile.getPath());
            Image img = icon.getImage().getScaledInstance(350, 180, Image.SCALE_SMOOTH);
            lblImagePreview.setIcon(new ImageIcon(img));
            lblImagePreview.setText("");
        }
    }

    private void handleSave() {
        if (txtName.getText().trim().isEmpty() || selectedImageFile == null) {
            JOptionPane.showMessageDialog(frame, "Model Name and Image are required!");
            return;
        }

        try {
            File photoDir = new File("photo");
            if (!photoDir.exists()) photoDir.mkdir();

            File destFile = new File(photoDir, selectedImageFile.getName());
            Files.copy(selectedImageFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            String formattedDetail = "Car Type: " + txtType.getText().trim() + "<br>" +
                                    "Seating Capacity: " + txtSeats.getText().trim() + " seats<br>" +
                                    "Rent Amount: RM " + txtPrice.getText().trim() + " /day";

            Car newCar = new Car(
                txtName.getText().trim(),
                txtType.getText().trim(),
                formattedDetail,
                (String) comboCategory.getSelectedItem(),
                txtPlate.getText().trim(),
                "photo/" + selectedImageFile.getName()
            );

            parentPage.addNewCarToList(newCar);
            frame.dispose(); 
            
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
        }
    }

    private JTextField createField(String placeholder) {
        JTextField field = new JTextField();
        field.setPreferredSize(new Dimension(0, 40));
        field.setBorder(BorderFactory.createTitledBorder(placeholder));
        return field;
    }

    private void styleButton(JButton btn) {
        btn.setBackground(ACCENT_BLUE);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
    }

    public void setVisible(boolean visible) { frame.setVisible(visible); }
}