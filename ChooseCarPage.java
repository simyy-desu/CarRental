import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ChooseCarPage {
    private JFrame frame;
    private Booking data;
    public JButton backBtn, chooseBtn;
    public JPanel cardPanel;
    public Car selectedCar = null;
    public JPanel selectedPanel = null;

    public ChooseCarPage() {
        this.data = Data.bookingInfo;
        
        //Frame setting
        frame = new JFrame("Choose Car - 4Y's Car Rental");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(950, 850); 
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(new Color(245, 245, 245));

        if (data == null) {
            JOptionPane.showMessageDialog(frame, "No booking data found!");
            frame.dispose();
            return;
        }

        List<Car> allCars = Fleet.loadCars();
        String filterCategory = data.getcarClass();
        List<String> bookedCarNames = getBookedCarNames();

        //Top panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        backBtn = new JButton("<");
        backBtn.setFont(new Font("Arial", Font.BOLD, 35));
        backBtn.setForeground(new Color(30, 80, 200));
        backBtn.setContentAreaFilled(false);
        backBtn.setBorderPainted(false);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        topPanel.add(backBtn, BorderLayout.WEST);

        JLabel title = new JLabel("Choose Your " + filterCategory, SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 32));
        topPanel.add(title, BorderLayout.CENTER);
        frame.add(topPanel, BorderLayout.NORTH);

        //Center panel
        cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setOpaque(false);
        cardPanel.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 50));

        int carsAdded = 0;
        for (Car car : allCars) {
            boolean isCorrectCategory = car.category != null && car.category.trim().equalsIgnoreCase(filterCategory.trim());
            boolean isNotBooked = !bookedCarNames.contains(car.name.trim());

            if (isCorrectCategory && isNotBooked) {
                cardPanel.add(createCard(car));
                cardPanel.add(Box.createVerticalStrut(20));
                carsAdded++;
            }
        }

        if (carsAdded == 0) {
            JLabel emptyMsg = new JLabel("No cars available in " + filterCategory + " class.");
            emptyMsg.setFont(new Font("Arial", Font.ITALIC, 20));
            emptyMsg.setAlignmentX(Component.CENTER_ALIGNMENT);
            cardPanel.add(emptyMsg);
        }

        JScrollPane scrollPane = new JScrollPane(cardPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        frame.add(scrollPane, BorderLayout.CENTER);

        //Bottom Panel
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 30, 50));
        bottomPanel.setOpaque(false);
        
        chooseBtn = new JButton("Confirm Selection");
        chooseBtn.setPreferredSize(new Dimension(220, 60));
        chooseBtn.setFont(new Font("Arial", Font.BOLD, 18));
        chooseBtn.setBackground(new Color(66, 133, 244));
        chooseBtn.setForeground(Color.WHITE);
        chooseBtn.setFocusPainted(false);
        chooseBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        bottomPanel.add(chooseBtn, BorderLayout.EAST);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        //Button
        chooseBtn.addActionListener(e -> {
            if (selectedCar == null) {
                JOptionPane.showMessageDialog(frame, "Please select a car before proceeding!", "Selection Required", JOptionPane.WARNING_MESSAGE);
                return;
            }
            data.setSelectedCar(selectedCar);
            new Payment().setVisible(true);
            frame.dispose();
        });

        backBtn.addActionListener(e -> {
            new BookingPage().setVisible(true);
            frame.dispose();
        });

        frame.setVisible(true);
    }

    private List<String> getBookedCarNames() {
        List<String> bookedCars = new ArrayList<>();
        File file = new File("Pickup/PickupList.txt");

        if (!file.exists()) return bookedCars;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 2) {
                    bookedCars.add(parts[2].trim());
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading booking status: " + e.getMessage());
        }
        return bookedCars;
    }

    private JPanel createCard(Car car) {
        JPanel card = new JPanel(new BorderLayout(20, 0));
        card.setBackground(new Color(230, 230, 230));
        card.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        card.setMaximumSize(new Dimension(850, 180));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel imageLabel;
        try {
            ImageIcon icon = new ImageIcon(car.imagePath);
            Image img = icon.getImage().getScaledInstance(200, 130, Image.SCALE_SMOOTH);
            imageLabel = new JLabel(new ImageIcon(img));
        } catch (Exception e) {
            imageLabel = new JLabel("No Image");
        }

        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setOpaque(false);
        
        JLabel name = new JLabel(car.name);
        name.setFont(new Font("Arial", Font.BOLD, 22));
        
        JLabel details = new JLabel("<html>" + car.detail + "</html>");
        details.setFont(new Font("Arial", Font.PLAIN, 16));
        
        info.add(name);
        info.add(Box.createVerticalStrut(10));
        info.add(details);

        card.add(imageLabel, BorderLayout.WEST);
        card.add(info, BorderLayout.CENTER);

        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (selectedPanel != null) {
                    selectedPanel.setBackground(new Color(230, 230, 230));
                    selectedPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
                }
                selectedPanel = card;
                selectedCar = car;
                card.setBackground(new Color(138, 190, 249));
                card.setBorder(BorderFactory.createLineBorder(new Color(66, 133, 244), 3));
            }
        });
        
        return card;
    }

    public void setVisible(boolean visible) {
        frame.setVisible(visible);
    }
}