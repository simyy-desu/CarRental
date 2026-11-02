import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ReturnList { 
    private JFrame frame; 
    private JTable table;
    private DefaultTableModel model;
    private final String FILE_PATH = "Pickup/PickupList.txt"; 
    private String staffID;

    public ReturnList(String staffID) {
        this.staffID = staffID;
        frame = new JFrame("Return List Management");
        frame.setSize(1200, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 30));
        JButton btnBack = new JButton("<");
        btnBack.setFont(new Font("Arial", Font.BOLD, 28));
        btnBack.addActionListener(e -> {
            new StaffMainPage(this.staffID).setVisible(true); 
            frame.dispose();
        });
        topPanel.add(btnBack);

        JLabel title = new JLabel("Return List Management");
        title.setFont(new Font("Arial", Font.BOLD, 28));
        topPanel.add(title);
        frame.add(topPanel, BorderLayout.NORTH);

        String[] columns = {"ID", "Customer", "Car", "Category", "Date", "Time", "Location", "PlateNo", "Status", "Report"};
        
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 8 || column == 9; 
            }
        };

        table = new JTable(model);
        loadData();
        applyRowColors();

        table.setRowHeight(40);
        table.setFont(new Font("Arial", Font.PLAIN, 16));
        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(66, 133, 244)); 
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Arial", Font.BOLD, 18));

        TableColumn statusColumn = table.getColumnModel().getColumn(8);
        JComboBox<String> comboBox = new JComboBox<>(new String[]{"Pending", "Returned", "Late Return"});
        statusColumn.setCellEditor(new DefaultCellEditor(comboBox));

        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 20));
        
        JButton btnSave = new JButton("Save Changes");
        btnSave.addActionListener(e -> saveData());

        bottomPanel.add(btnSave);
        frame.add(bottomPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    private void applyRowColors() {
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, 
                boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                String status = table.getValueAt(row, 8).toString();
                if (!isSelected) {
                    if ("Late Return".equalsIgnoreCase(status)) c.setForeground(Color.RED);
                    else if ("Returned".equalsIgnoreCase(status)) c.setForeground(Color.BLUE);
                    else c.setForeground(Color.BLACK);
                }
                return c;
            }
        });
    }

   private void loadData() {
        model.setRowCount(0);
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length < 10) continue; 

                
                String plateNo = data[9].trim();

                
                if (plateNo.isEmpty() || "-".equals(plateNo) || "Pending".equalsIgnoreCase(plateNo)) {
                    continue;
                }

                Object[] rowData = new Object[10]; 
                rowData[0] = data[0].trim(); // ID
                rowData[1] = data[1].trim(); // Customer
                rowData[2] = data[2].trim(); // Car
                rowData[3] = data[3].trim(); // Category
                rowData[4] = data[6].trim(); // Return Date (from index 6)
                rowData[5] = data[7].trim(); // Return Time (from index 7)
                rowData[6] = data[8].trim(); // Location (from index 8)
                rowData[7] = plateNo;        // Assigned Plate Number
                rowData[8] = (data.length > 10) ? data[10].trim() : "Pending"; // Status
                rowData[9] = (data.length > 11) ? data[11].trim() : "";        // Report
                model.addRow(rowData);
            }
        } catch (IOException e) { 
            
        }
    }

    private void saveData() {
        if (table.isEditing()) table.getCellEditor().stopCellEditing();
        saveToFile();
        JOptionPane.showMessageDialog(frame, "Data Saved Successfully!");
    }

    private void saveToFile() {
        List<String[]> allFileData = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                allFileData.add(line.split(","));
            }
        } catch (IOException e) { }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (String[] fileRow : allFileData) {
                String fileID = fileRow[0].trim();
                boolean updated = false;

                
                for (int i = 0; i < model.getRowCount(); i++) {
                    String tableID = model.getValueAt(i, 0).toString().trim();

                    if (fileID.equals(tableID)) {
                       
                        String[] updatedRow = new String[12];
                        for (int j = 0; j < 12; j++) {
                            if (j < fileRow.length) updatedRow[j] = fileRow[j].trim();
                            else updatedRow[j] = "";
                        }
                        
                        
                        updatedRow[10] = model.getValueAt(i, 8).toString(); 
                        updatedRow[11] = model.getValueAt(i, 9).toString(); 
                        
                        bw.write(String.join(",", updatedRow));
                        bw.newLine();
                        updated = true;
                        break;
                    }
                }
                
                
                if (!updated) {
                    bw.write(String.join(",", fileRow));
                    bw.newLine();
                }
            }
        } catch (IOException e) { }
    }

    public void setVisible(boolean b) { frame.setVisible(b); }
}