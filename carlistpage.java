import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.util.List;

public class carlistpage { 
    private JFrame frame; 
    private List<Car> inventory;
    private JPanel carListContainer;
    private final Color ACCENT_BLUE = new Color(142, 190, 247);
    private String currentStaffID;
    private String currentFilter = "all";

    public carlistpage(String staffID) {
        this.currentStaffID = staffID;
        //Read file
        inventory = Fleet.loadCars(); 
        frame = new JFrame("Staff - Fleet Management");
        frame.setSize(950, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(new Color(242, 245, 248));
        setupNavigation();
        setupMainContent();
        setupFooter();
        refreshList("all");
    }

    private void setupNavigation() {
        JPanel topNav = new JPanel(new BorderLayout());
        topNav.setBackground(Color.WHITE);
        topNav.setPreferredSize(new Dimension(0, 70));
        topNav.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));

        JButton btnBack = new JButton("<   Back");
        btnBack.setFont(new Font("Arial", Font.BOLD, 14));
        btnBack.setFocusPainted(false);
        btnBack.setContentAreaFilled(false);
        btnBack.setBorder(new EmptyBorder(0, 20, 0, 0));
        btnBack.addActionListener(e -> {
            new StaffMainPage(currentStaffID).setVisible(true);
            frame.dispose();
        });

        JLabel lblTitle = new JLabel("FLEET OVERVIEW", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));

        topNav.add(btnBack, BorderLayout.WEST);
        topNav.add(lblTitle, BorderLayout.CENTER);
        frame.add(topNav, BorderLayout.NORTH);
    }

    private void setupMainContent() {
        //Filter panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 25, 20));
        filterPanel.setOpaque(false);
        String[] categories = {"All", "Luxury", "Economy", "RV"};
        for (String c : categories) {
            JButton b = new JButton(c);
            styleButton(b);
            b.addActionListener(e -> refreshList(c.toLowerCase()));
            filterPanel.add(b);
        }

        carListContainer = new JPanel();
        carListContainer.setLayout(new BoxLayout(carListContainer, BoxLayout.Y_AXIS));
        carListContainer.setOpaque(false);
        carListContainer.setBorder(new EmptyBorder(10, 30, 20, 30));

        JScrollPane scroll = new JScrollPane(carListContainer);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(20);
        scroll.getViewport().setOpaque(false);
        scroll.setOpaque(false);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        JPanel centerContainer = new JPanel(new BorderLayout());
        centerContainer.setOpaque(false);
        centerContainer.add(filterPanel, BorderLayout.NORTH);
        centerContainer.add(scroll, BorderLayout.CENTER);
        frame.add(centerContainer, BorderLayout.CENTER);
    }

    private void setupFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 40, 25));
        footer.setOpaque(false);
        JButton btnAdd = new JButton("+ Add New Vehicle");
        btnAdd.setPreferredSize(new Dimension(200, 50));
        styleButton(btnAdd);
        btnAdd.setForeground(Color.WHITE);
        btnAdd.addActionListener(e -> new AddCarForm(this).setVisible(true));
        footer.add(btnAdd);
        frame.add(footer, BorderLayout.SOUTH);
    }

    public void addNewCarToList(Car newCar) {
        inventory.add(newCar);
        Fleet.saveCars(inventory);
        refreshList("all");
    }

    public void removeCarFromList(Car car) {
        int confirm = JOptionPane.showConfirmDialog(
            frame, 
            "Remove " + car.name + " [" + car.plateNum + "] from fleet inventory?",
            "Confirm Removal",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            inventory.remove(car);
            Fleet.saveCars(inventory);
            refreshList(currentFilter);
            JOptionPane.showMessageDialog(frame, "Vehicle removed.");
        }
    }

    private void refreshList(String filter) {
        currentFilter = filter;
        carListContainer.removeAll();
        for (Car car : inventory) {
            String cat = car.category.toLowerCase();
            if (filter.equals("all") || cat.equals(filter)) {
                carListContainer.add(createCarCard(car));
                carListContainer.add(Box.createRigidArea(new Dimension(0, 15)));
            }
        }
        carListContainer.revalidate();
        carListContainer.repaint();
    }

    private JPanel createCarCard(Car car) {
        JPanel card = new JPanel(new BorderLayout(25, 0));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            new EmptyBorder(15, 20, 15, 20)
        ));
        card.setMaximumSize(new Dimension(850, 170));
        card.setPreferredSize(new Dimension(850, 170));

        JLabel photo = new JLabel();
        photo.setPreferredSize(new Dimension(120, 90));
        photo.setOpaque(true);
        File imageFile = Fleet.resolveInventoryAsset(car.imagePath);
        if (imageFile != null && imageFile.exists()) {
            ImageIcon icon = new ImageIcon(imageFile.getAbsolutePath());
            Image img = icon.getImage().getScaledInstance(120, 90, Image.SCALE_SMOOTH);
            photo.setIcon(new ImageIcon(img));
        } else {
            photo.setBackground(new Color(230, 240, 255));
            photo.setHorizontalAlignment(SwingConstants.CENTER);
            photo.setText("NO IMAGE");
        }

        JPanel detailsPanel = new JPanel();
        detailsPanel.setOpaque(false);
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        titlePanel.setOpaque(false);

        JLabel lblName = new JLabel(car.name);
        lblName.setFont(new Font("Arial", Font.BOLD, 22));
        JLabel lblPlate = new JLabel(" [" + car.plateNum + "]");
        lblPlate.setForeground(new Color(52, 152, 219));
        lblPlate.setFont(new Font("Arial", Font.BOLD, 18));
        titlePanel.add(lblName);
        titlePanel.add(lblPlate);

        JLabel lblMeta = new JLabel(car.type + " | " + car.category.toUpperCase());
        lblMeta.setForeground(Color.GRAY);
        
        JTextArea txtDetails = new JTextArea(car.detail);
        txtDetails.setLineWrap(true);
        txtDetails.setWrapStyleWord(true);
        txtDetails.setOpaque(false);
        txtDetails.setEditable(false);

        detailsPanel.add(titlePanel);
        detailsPanel.add(lblMeta);
        detailsPanel.add(txtDetails);

        //Button
        JButton btnReport = new JButton("Report");
        styleButton(btnReport);
        btnReport.addActionListener(e -> new ReportPage(car.name, car.plateNum).setVisible(true));

        JButton btnRemove = new JButton("Remove");
        btnRemove.setBackground(new Color(220, 83, 83));
        btnRemove.setForeground(Color.WHITE);
        btnRemove.addActionListener(e -> removeCarFromList(car));

        JPanel btnWrapper = new JPanel();
        btnWrapper.setOpaque(false);
        btnWrapper.setLayout(new BoxLayout(btnWrapper, BoxLayout.Y_AXIS));
        btnWrapper.add(btnReport);
        btnWrapper.add(Box.createRigidArea(new Dimension(0, 10)));
        btnWrapper.add(btnRemove);

        card.add(photo, BorderLayout.WEST);
        card.add(detailsPanel, BorderLayout.CENTER);
        card.add(btnWrapper, BorderLayout.EAST);

        return card;
    }

    private void styleButton(JButton btn) {
        btn.setBackground(ACCENT_BLUE);
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
    }

    public void setVisible(boolean b) {
        frame.setVisible(b);
    }
}