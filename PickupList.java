import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.io.*;
import java.util.Stack;
import java.util.ArrayList;
import java.util.List;

public class PickupList { 
    private JFrame frame; 
    private JTable table;
    private DefaultTableModel model;
    private final String FILE_PATH = "Pickup/PickupList.txt"; 
    private Stack<Object[][]> historyStack = new Stack<>();
    private String staffID;

    public PickupList(String staffID) {
        this.staffID = staffID;
        frame = new JFrame("Pickup List Management");
        frame.setSize(1000, 800);
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

        JLabel title = new JLabel("Pickup List Management");
        title.setFont(new Font("Arial", Font.BOLD, 28));
        topPanel.add(title);
        frame.add(topPanel, BorderLayout.NORTH);

        String[] columns = {"ID", "Customer", "Car", "Category", "Pickup Date", "Pickup Time", "Location", "PlateNo"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        table = new JTable(model);
        loadData();
        styleTable();

        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 20));
        JButton btnConfirmPickup = new JButton("Confirm & Enter Plate Number");
        btnConfirmPickup.setPreferredSize(new Dimension(300, 50));
        btnConfirmPickup.setBackground(new Color(66, 133, 244));
        btnConfirmPickup.setForeground(Color.WHITE);
        btnConfirmPickup.setFont(new Font("Arial", Font.BOLD, 14));
        btnConfirmPickup.addActionListener(e -> processPickupAction());

        JButton btnUndo = new JButton("Undo");
        btnUndo.addActionListener(e -> performUndo());
        
        JButton btnSave = new JButton("Save Changes");
        btnSave.addActionListener(e -> saveData());

        bottomPanel.add(btnConfirmPickup);
        bottomPanel.add(btnUndo);
        bottomPanel.add(btnSave);
        frame.add(bottomPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    private void styleTable() {
        table.setRowHeight(40);
        table.setFont(new Font("Arial", Font.PLAIN, 16));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 18));
        header.setBackground(new Color(66, 133, 244));
        header.setForeground(Color.WHITE);
    }

    private void loadData() {
        model.setRowCount(0);
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length < 11) continue; // Ensure the line has the Status column

                // Get the Status from index 10 in the file
                String currentStatus = data[10].trim();

                // Logic: If the car is already "Returned" or "Late Return", 
                // do not show it in the Pickup List.
                if ("Returned".equalsIgnoreCase(currentStatus) || "Late Return".equalsIgnoreCase(currentStatus)) {
                    continue;
                }

                Object[] rowData = new Object[8];
                rowData[0] = data[0].trim();
                rowData[1] = data[1].trim();
                rowData[2] = data[2].trim();
                rowData[3] = data[3].trim();
                rowData[4] = data[4].trim();
                rowData[5] = data[5].trim();
                rowData[6] = data[8].trim(); 
                rowData[7] = (data.length > 9) ? data[9].trim() : "-"; 
                model.addRow(rowData);
            }
        } catch (IOException e) { 
            // Silent catch maintained as per your original code
        }
    }

    private void processPickupAction() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Please select a row first.");
            return;
        }
        String plateNum = JOptionPane.showInputDialog(frame, "Assign Plate Number:");
        if (plateNum != null && !plateNum.trim().isEmpty()) {
            saveStateToHistory(); 
            model.setValueAt(plateNum.toUpperCase(), selectedRow, 7);
            saveDataInternal(); 
        }
    }

    private void saveData() {
        saveStateToHistory();
        saveDataInternal();
        JOptionPane.showMessageDialog(frame, "Changes saved successfully!");
    }

    private void saveDataInternal() {
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
                        
                        // Copy EVERYTHING currently in the file first (preserves Report info)
                        for (int j = 0; j < 12; j++) {
                            if (j < fileRow.length) updatedRow[j] = fileRow[j].trim();
                            else updatedRow[j] = "";
                        }

                        // Update Plate (9)
                        String plateValue = model.getValueAt(i, 7).toString();
                        updatedRow[9] = plateValue; 
                        
                        // Update Status (10) only if plate is assigned
                        if (!plateValue.equals("-") && !plateValue.equalsIgnoreCase("Pending")) {
                            updatedRow[10] = "Picked Up"; 
                        }

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
    private void saveStateToHistory() {
        Object[][] stateCopy = new Object[model.getRowCount()][model.getColumnCount()];
        for (int i = 0; i < model.getRowCount(); i++) {
            for (int j = 0; j < model.getColumnCount(); j++) {
                stateCopy[i][j] = model.getValueAt(i, j).toString(); 
            }
        }
        historyStack.push(stateCopy);
    }

    private void performUndo() {
        if (!historyStack.isEmpty()) {
            Object[][] lastState = historyStack.pop();
            model.setRowCount(0);
            for (Object[] row : lastState) model.addRow(row);
            saveDataInternal(); 
            JOptionPane.showMessageDialog(frame, "Undo successful!");
        } else {
            JOptionPane.showMessageDialog(frame, "Nothing to undo.");
        }
    }

    public void setVisible(boolean b) { frame.setVisible(b); }
}