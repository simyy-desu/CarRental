import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class BookingHistory extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private final String FILE_PATH = "Pickup/PickupList.txt"; 
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private String currentUsername;

    public BookingHistory(String username) {
        this.currentUsername = username;

        setTitle("My Booking History - 4Y's Car Rental");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(245, 245, 245));

        //Top panel
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 30));
        topPanel.setOpaque(false);
        
        JButton btnBack = new JButton("<");
        btnBack.setFont(new Font("Arial", Font.BOLD, 28));
        btnBack.setForeground(new Color(30, 80, 200)); 
        btnBack.setBorderPainted(false);
        btnBack.setContentAreaFilled(false);
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnBack.addActionListener(e -> {
            new CustMainPage(this.currentUsername).setVisible(true);
            dispose();
        });

        JLabel lblTitle = new JLabel("My Booking History");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        
        topPanel.add(btnBack);
        topPanel.add(lblTitle);

        //Table
        String[] columns = {"Receipt No.", "Name", "Car", "Category", "Pick Date", "Time", "Return Date", "Time", "Location", "Status"};

        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        table = new JTable(model);
        setupTableUI();
        addCancelAction(); 
        loadUserSpecificData();

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 40, 40, 40));
        
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void setupTableUI() {
        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(157, 195, 230)); 
        header.setFont(new Font("Arial", Font.BOLD, 14));
        table.setRowHeight(35);
        
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(JLabel.CENTER);
                c.setForeground(Color.BLACK); 

                Object statusObj = table.getValueAt(row, 9);
                String status = (statusObj != null) ? statusObj.toString() : "";
                
                if (column == 9) {
                    if (status.equalsIgnoreCase("Pending")) {
                        c.setForeground(new Color(255, 165, 0)); 
                    } else if (status.equalsIgnoreCase("Late Return")) {
                        c.setForeground(Color.RED); 
                    } else if (status.equalsIgnoreCase("Returned") || status.equalsIgnoreCase("Return")) {
                        c.setForeground(new Color(0, 102, 204)); 
                    }
                }

                if (isSelected) c.setBackground(new Color(220, 230, 255));
                else c.setBackground(Color.WHITE);
                
                return c;
            }
        });
    }

    private void addCancelAction() {
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());

                if (row != -1 && col == 9) {
                    String status = table.getValueAt(row, 9).toString();
                    
                    if (status.equalsIgnoreCase("Pending")) {
                        String receiptID = table.getValueAt(row, 0).toString();
                        
                        //Confirmation
                        int firstConfirm = JOptionPane.showConfirmDialog(null, 
                            "Do you want to cancel Order #" + receiptID + "?", 
                            "Cancel Order", JOptionPane.YES_NO_OPTION);
                        
                        if (firstConfirm == JOptionPane.YES_OPTION) {
                            //Double confirm
                            int secondConfirm = JOptionPane.showConfirmDialog(null, 
                                "Are you absolutely sure? This cannot be undone.", 
                                "DOUBLE CONFIRMATION", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                            
                            if (secondConfirm == JOptionPane.YES_OPTION) {
                                if (removeDataFromFile(receiptID)) {
                                    model.removeRow(row);
                                    JOptionPane.showMessageDialog(null, "Order #" + receiptID + " has been successfully removed.");
                                } else {
                                    JOptionPane.showMessageDialog(null, "Error: Could not update the file.");
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    private boolean removeDataFromFile(String receiptID) {
    List<String> remainingLines = new ArrayList<>();
    File file = new File(FILE_PATH);
    boolean found = false;

    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
        String line;
        while ((line = br.readLine()) != null) {
            if (line.trim().isEmpty()) continue;
            
            String[] parts = line.split(","); 
            
            if (parts.length > 0 && parts[0].trim().equals(receiptID.trim())) {
                found = true;
                continue;
            }
            remainingLines.add(line);
        }
    } catch (IOException e) {
        e.printStackTrace();
        return false;
    }

    if (!found) return false; 

    try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
        for (String remainingLine : remainingLines) {
            bw.write(remainingLine);
            bw.newLine();
        }
        return true;
    } catch (IOException e) {
        e.printStackTrace();
        return false;
    }
}
    private void loadUserSpecificData() {
    LocalDate today = LocalDate.now();
    model.setRowCount(0);
    File file = new File(FILE_PATH);
    if (!file.exists()) return;

    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
        String line;
        while ((line = br.readLine()) != null) {
            String l = line.trim();
            if (l.isEmpty()) continue;
            
            String[] data = l.split(","); 
            
            if (data.length >= 9 && data[1].trim().equalsIgnoreCase(currentUsername)) {
                Object[] rowData = new Object[10];
                rowData[0] = data[0].trim();
                rowData[1] = data[1].trim();
                rowData[2] = data[2].trim();
                rowData[3] = data[3].trim();
                rowData[4] = data[4].trim();
                rowData[5] = data[5].trim();
                rowData[6] = data[6].trim();
                rowData[7] = data[7].trim();
                rowData[8] = data[8].trim();
                
                String status = (data.length > 10) ? data[10].trim() : "Pending";
                
                try {
                    LocalDate returnDate = LocalDate.parse(data[6].trim(), formatter);
                    if (today.isAfter(returnDate) && status.equalsIgnoreCase("Pending")) {
                        status = "Late Return";
                    }
                } catch (Exception e) {
                }

                rowData[9] = status;
                model.addRow(rowData);
            }
        }
    } catch (IOException e) { 
        e.printStackTrace(); 
    }
}
}