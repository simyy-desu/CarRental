import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Payment { 
    private JFrame frame;
    private Booking data;
    private Car car;

    public JButton backBtn;
    public JButton paymentBtn;

    public Payment() {
        try {
            this.data = Data.bookingInfo;
            if (data == null) {
                JOptionPane.showMessageDialog(null, "Booking data not found!");
                return;
            }

            this.car = data.getSelectedCar();
            if (car == null) {
                JOptionPane.showMessageDialog(null, "No car selected!");
                return;
            }

            //Frame setting
            frame = new JFrame("Payment Detail");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(950, 800);
            frame.setLocationRelativeTo(null);
            frame.setLayout(new BorderLayout());
            frame.getContentPane().setBackground(new Color(245, 245, 245));

            //Top panel
            JPanel topPanel = new JPanel(new BorderLayout());
            topPanel.setOpaque(false);
            topPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
            
            backBtn = new JButton("<");
            backBtn.setFont(new Font("Arial", Font.BOLD, 28));
            backBtn.setForeground(new Color(30, 80, 200));
            backBtn.setContentAreaFilled(false);
            backBtn.setBorderPainted(false);
            topPanel.add(backBtn, BorderLayout.WEST);

            backBtn.addActionListener(e -> {
                new ChooseCarPage().setVisible(true);
                frame.dispose();
            });

            JLabel title = new JLabel("Payment Details", SwingConstants.CENTER);
            title.setFont(new Font("Arial", Font.BOLD, 30));
            topPanel.add(title, BorderLayout.CENTER);

            frame.add(topPanel, BorderLayout.NORTH);

            //Center panel
            JPanel centerPanel = new JPanel(new BorderLayout());
            centerPanel.setBackground(new Color(235, 235, 235));
            centerPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
            
            //Car image
            JPanel carPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            carPanel.setOpaque(false);
            carPanel.setBorder(BorderFactory.createEmptyBorder(50, 0, 0, 30));
            
            ImageIcon carIcon = new ImageIcon(car.imagePath);
            Image scaledImage = carIcon.getImage().getScaledInstance(240, 220, Image.SCALE_SMOOTH);
            JLabel carLabel = new JLabel(new ImageIcon(scaledImage));
            carPanel.add(carLabel);

            //Info
            JPanel infoPanel = new JPanel();
            infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
            infoPanel.setOpaque(false);
            infoPanel.setAlignmentX(Component.LEFT_ALIGNMENT); 

            int rentalDays = calculateDays(data.getPickDate(), data.getPickTime());
            double amount = parsePrice(car.detail); 
            double subtotal = amount * rentalDays;
            double sst = subtotal * 0.10;
            double total = subtotal + sst;

            detailRow(infoPanel, "Car Name: " + car.name);
            detailRow(infoPanel, "Category: " + data.getcarClass()); 
            detailRow(infoPanel, "Pick-up & Return Location: " + data.getLocation());
            detailRow(infoPanel, "Pick-up: " + data.getPickDate() + " (" + data.getReturnDate() + ")");
            detailRow(infoPanel, "Return: " + data.getPickTime() + " (" + data.getReturnTime() + ")");
            detailRow(infoPanel, "Rental Days: " + rentalDays + " day(s)");
            detailRow(infoPanel, String.format("Subtotal (RM): %.2f", subtotal));
            detailRow(infoPanel, String.format("SST (10%%) (RM): %.2f", sst));
            
            JLabel totalLbl = new JLabel(String.format("Total Amount (RM): %.2f", total));
            totalLbl.setFont(new Font("Arial", Font.BOLD, 18));
            totalLbl.setForeground(new Color(66, 133, 244));
            infoPanel.add(totalLbl);
            
            centerPanel.add(carPanel, BorderLayout.WEST);
            centerPanel.add(infoPanel, BorderLayout.CENTER);

            JPanel centerWrapper = new JPanel(new BorderLayout());
            centerWrapper.setOpaque(false);
            centerWrapper.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 80));
            centerWrapper.add(centerPanel, BorderLayout.CENTER);
            frame.add(centerWrapper, BorderLayout.CENTER);

            //Bottom panel
            JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            bottomPanel.setOpaque(false);
            bottomPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 30, 0));

            paymentBtn = new JButton("Choose Payment");
            paymentBtn.setPreferredSize(new Dimension(220, 50));
            paymentBtn.setFont(new Font("Arial", Font.PLAIN, 18));
            paymentBtn.setBackground(new Color(66, 133, 244));
            paymentBtn.setForeground(Color.WHITE);
            paymentBtn.setBorderPainted(false);
            paymentBtn.setFocusPainted(false);

            bottomPanel.add(paymentBtn);
            frame.add(bottomPanel, BorderLayout.SOUTH);

            paymentBtn.addActionListener(e -> {
                new ChoosePaymentPage().setVisible(true);
                frame.dispose();
            });

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Page load failed: " + e.getMessage());
        }
    }

    private double parsePrice(String detail) {
        try {
            String priceStr = detail.split("RM ")[1].split(" ")[0].replace(",", "");
            return Double.parseDouble(priceStr);
        } catch (Exception e) {
            return 0.0;
        }
    }

    private int calculateDays(String start, String end) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date d1 = sdf.parse(start);
            Date d2 = sdf.parse(end);
            long diff = Math.abs(d2.getTime() - d1.getTime());
            long diffDays = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
            return diffDays > 0 ? (int) diffDays : 1;
        } catch (ParseException e) {
            return 1;
        }
    }

    private void detailRow(JPanel panel, String label) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row.setOpaque(false);
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Arial", Font.BOLD, 14));
        row.add(lbl);
        panel.add(row);
        panel.add(Box.createVerticalStrut(8));
    }

    public void setVisible(boolean visible) {
        frame.setVisible(visible);
    }
}