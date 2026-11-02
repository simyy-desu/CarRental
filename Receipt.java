import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Receipt { 
    private JFrame frame; 
    private Booking data;
    private Car car; 

    public JButton returnBtn;

    public Receipt(Booking data) {
        if (data == null) {
            this.data = Data.bookingInfo;
        } else {
            this.data = data;
        }

        if (this.data == null || this.data.getSelectedCar() == null) {
            JOptionPane.showMessageDialog(null, "Error: Booking data not found. Returning to Dashboard.");
            new CustMainPage(Data.currentUsername).setVisible(true);
            return;
        }

        this.car = this.data.getSelectedCar();

        //Frame setting
        frame = new JFrame("Receipt - 4Y's Car Rental");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 800);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(new Color(245, 245, 245));

        //Top panel
        JPanel topPanel = new JPanel();
        topPanel.setOpaque(false);
        topPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 10, 0));
        
        JLabel title = new JLabel("BOOKING RECEIPT", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 36));
        title.setForeground(new Color(40, 40, 40));
        topPanel.add(title);
        frame.add(topPanel, BorderLayout.NORTH);

        //Center panel
        JPanel receiptContainer = new JPanel(new BorderLayout());
        receiptContainer.setBackground(Color.WHITE);
        receiptContainer.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(20, 100, 20, 100),
            BorderFactory.createLineBorder(new Color(200, 200, 200), 2)
        ));

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        int rentalDays = calculateDays(this.data.getPickDate(), this.data.getPickTime());
        double amountPerDay = parsePrice(car.detail);
        double subtotal = amountPerDay * rentalDays;
        double sst = subtotal * 0.10;
        double total = subtotal + sst;

        String receiptNo = randomNo();
        Data.receiptNo = receiptNo;

        saveToPickupList(receiptNo);

        detailRow(contentPanel, "Transaction Date: " + new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()));
        detailRow(contentPanel, "Receipt No: " + Data.receiptNo);
        detailRow(contentPanel, "Customer Name: " + Data.currentUsername);
        detailRow(contentPanel, "Car Reserved: " + car.name);
        detailRow(contentPanel, "Category: " + this.data.getcarClass());
        detailRow(contentPanel, "Location: " + this.data.getLocation());
        detailRow(contentPanel, "Period: " + this.data.getPickDate() + " to " + this.data.getPickTime());
        detailRow(contentPanel, "Duration: " + rentalDays + " Day(s)");
        contentPanel.add(Box.createVerticalStrut(5));
        
        JSeparator line = new JSeparator();
        contentPanel.add(line);
        contentPanel.add(Box.createVerticalStrut(10));

        detailRow(contentPanel, String.format("Rental Charge: RM %.2f", subtotal));
        detailRow(contentPanel, String.format("SST (10%%): RM %.2f", sst));
        
        JLabel totalLabel = new JLabel(String.format("TOTAL PAID: RM %.2f", total));
        totalLabel.setFont(new Font("Arial", Font.BOLD, 22));
        totalLabel.setForeground(new Color(30, 120, 30));
        totalLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(totalLabel);

        contentPanel.add(Box.createVerticalStrut(5));
        detailRow(contentPanel, "Payment Method: " + this.data.getPaymentMethod());
        detailRow(contentPanel, "Status: SUCCESSFUL");

        receiptContainer.add(contentPanel, BorderLayout.CENTER);
        frame.add(receiptContainer, BorderLayout.CENTER);

        //Bottom panel
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 40, 0));

        returnBtn = new JButton("Return to Dashboard");
        returnBtn.setPreferredSize(new Dimension(250, 50));
        returnBtn.setFont(new Font("Arial", Font.BOLD, 16));
        returnBtn.setBackground(new Color(66, 133, 244));
        returnBtn.setForeground(Color.WHITE);
        returnBtn.setFocusPainted(false);
        
        returnBtn.addActionListener(e -> {
            new CustMainPage(Data.currentUsername).setVisible(true);
            frame.dispose();
        });

        bottomPanel.add(returnBtn);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private void saveToPickupList(String id){
        String filePath = "Pickup/PickupList.txt";
        File file = new File(filePath);

        if (!file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))){
            String line = String.format("%s, %s, %s, %s, %s, %s, %s, %s, %s, Pending", 
                id.trim(), 
                Data.currentUsername, 
                car.name, 
                data.getcarClass(), 
                data.getPickDate(), 
                data.getReturnDate(), 
                data.getPickTime(), 
                data.getReturnTime(), 
                data.getLocation()
            );
            bw.write(line);
            bw.newLine();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Failed to save booking history.");
        }
    }

    private double parsePrice(String detail) {
        try {
            String priceStr = detail.split("RM ")[1].split(" ")[0].replace(",", "");
            return Double.parseDouble(priceStr);
        } catch (Exception e) { return 0.0; }
    }

    private int calculateDays(String start, String end) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date d1 = sdf.parse(start);
            Date d2 = sdf.parse(end);
            long diff = Math.abs(d2.getTime() - d1.getTime());
            long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
            return (int) (days == 0 ? 1 : days);
        } catch (ParseException e) { return 1; }
    }

    private String randomNo(){
        StringBuilder receiptNo = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            receiptNo.append((int) (Math.random() * 10));
        }
        return receiptNo.toString();
    }

    private void detailRow(JPanel panel, String label) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row.setOpaque(false);
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Arial", Font.PLAIN, 16));
        row.add(lbl);
        panel.add(row);
        panel.add(Box.createVerticalStrut(10));
    }

    public void setVisible(boolean visible) {
        frame.setVisible(visible);
    }
}