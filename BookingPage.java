import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Calendar;

public class BookingPage extends JFrame {

    private final JButton backBtn;
    private final JComboBox<String> locationCombo;
    private final JTextField pickDateField, returnDateField;
    private final JComboBox<String> pickUpTimeCombo, returnTimeCombo;
    private final JComboBox<String> classCombo;
    private final JButton searchBtn;

    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");
    private static final List<String> LOCATIONS = Arrays.asList("Kuala Lumpur", "Selangor", "Penang");
    private static final List<String> TIMES = Arrays.asList(
            "08:00 AM", "09:00 AM", "10:00 AM", "11:00 AM", "12:00 PM",
            "13:00 PM", "14:00 PM", "15:00 PM", "16:00 PM", "17:00 PM", "18:00 PM");
    private static final List<String> CAR_CLASSES = Arrays.asList("Economy", "Luxury", "RV");

    public BookingPage() {
        setTitle("Booking - 4Y's Car Rental");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(950, 850);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(245, 245, 245));

        //Top panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.setBorder(new EmptyBorder(20, 30, 0, 30));

        backBtn = new JButton("<");
        backBtn.setFont(new Font("Arial", Font.BOLD, 35));
        backBtn.setForeground(new Color(30, 80, 200));
        backBtn.setContentAreaFilled(false);
        backBtn.setBorderPainted(false);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        topPanel.add(backBtn, BorderLayout.WEST);

        JLabel title = new JLabel("Booking", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 32));
        topPanel.add(title, BorderLayout.CENTER);

        JLabel spacer = new JLabel("<");
        spacer.setFont(new Font("Arial", Font.BOLD, 35));
        spacer.setForeground(new Color(0,0,0,0));
        topPanel.add(spacer, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        //Center panel
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);

        JPanel mainBg = new JPanel(new BorderLayout());
        mainBg.setBackground(new Color(138, 190, 249));
        mainBg.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainBg.setPreferredSize(new Dimension(850, 650));

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(new Color(235, 235, 235));
        formPanel.setBorder(new EmptyBorder(40, 60, 40, 60));

        formPanel.add(createLeftLabel("Pick up and return location"));
        locationCombo = createStyledComboBox(LOCATIONS);
        locationCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        formPanel.add(locationCombo);
        formPanel.add(Box.createVerticalStrut(30));

        formPanel.add(createLeftLabel("Pick up date and time"));
        JPanel pickPanel = new JPanel(new GridLayout(1, 2, 25, 0));
        pickPanel.setOpaque(false);
        pickPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        pickDateField = createDateField();
        pickPanel.add(pickDateField);
        pickUpTimeCombo = createStyledComboBox(TIMES);
        pickPanel.add(pickUpTimeCombo);
        formPanel.add(pickPanel);
        formPanel.add(Box.createVerticalStrut(30));

        formPanel.add(createLeftLabel("Return date and time"));
        JPanel returnPanel = new JPanel(new GridLayout(1, 2, 25, 0));
        returnPanel.setOpaque(false);
        returnPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        returnDateField = createDateField();
        returnPanel.add(returnDateField);
        returnTimeCombo = createStyledComboBox(TIMES);

        returnPanel.add(returnTimeCombo);
        formPanel.add(returnPanel);
        formPanel.add(Box.createVerticalStrut(30));

        formPanel.add(createLeftLabel("Class"));
        JPanel bottomRow = new JPanel(new GridLayout(1, 2, 25, 0));
        bottomRow.setOpaque(false);
        bottomRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 55));

        classCombo = createStyledComboBox(CAR_CLASSES);
        bottomRow.add(classCombo);

        searchBtn = new JButton("Search");
        searchBtn.setFont(new Font("Arial", Font.BOLD, 22));
        searchBtn.setBackground(new Color(66, 133, 244));
        searchBtn.setForeground(Color.WHITE);
        searchBtn.setFocusPainted(false);
        searchBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        bottomRow.add(searchBtn);
        formPanel.add(bottomRow);

        mainBg.add(formPanel, BorderLayout.CENTER);
        centerWrapper.add(mainBg);
        add(centerWrapper, BorderLayout.CENTER);

        backBtn.addActionListener(e -> {
            new CustMainPage("User").setVisible(true);
            dispose();
        });

        searchBtn.addActionListener(e -> {
            String location = (String) locationCombo.getSelectedItem();
            String pickStr = pickDateField.getText().trim();
            String returnStr = returnDateField.getText().trim();

            String pickTime = (String) pickUpTimeCombo.getSelectedItem();
            String returnTime = (String) returnTimeCombo.getSelectedItem();

            if (pickStr.isEmpty() || returnStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please select both Pick-up and Return dates.", "Missing Info", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                Date pDate = SDF.parse(pickStr);
                Date rDate = SDF.parse(returnStr);
                Date today = new Date();

                if (pDate.before(today)) {
                    JOptionPane.showMessageDialog(this, "Pick-up date cannot be in the past!", "Date Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (rDate.before(pDate)) {
                    JOptionPane.showMessageDialog(this, "Return date cannot be earlier than Pick-up date!", "Date Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (pickStr.equals(returnStr)) {
                    int pickMin = convertTimeToMinutes(pickTime);
                    int returnMin = convertTimeToMinutes(returnTime);

                    if (returnMin <= pickMin) {
                        JOptionPane.showMessageDialog(this, "Return time must be AFTER pick-up time on the same day!", "Invalid Time", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                Data.bookingInfo = new Booking(
                        location, pickStr, pickTime,
                        returnStr, returnTime, (String) classCombo.getSelectedItem()
                );
                new ChooseCarPage().setVisible(true);
                dispose();

            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(this, "Invalid date format.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        setVisible(true);
    }

    private int convertTimeToMinutes(String timeStr) {
        try {
            String[] part1 = timeStr.split(" ");
            String[] hm = part1[0].split(":");
            String period = part1[1];

            int h = Integer.parseInt(hm[0]);
            int m = Integer.parseInt(hm[1]);

            if (period.equalsIgnoreCase("PM") && h != 12) h += 12;
            if (period.equalsIgnoreCase("AM") && h == 12) h = 0;

            return h * 60 + m;
        } catch (Exception e) {
            return 0;
        }
    }

    private JLabel createLeftLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Arial", Font.BOLD, 18));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        lbl.setBorder(new EmptyBorder(0, 5, 8, 0));
        return lbl;
    }

    private JTextField createDateField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Arial", Font.PLAIN, 18));
        field.setBackground(new Color(138, 190, 249));
        field.setForeground(Color.WHITE);
        field.setEditable(false);
        field.setHorizontalAlignment(SwingConstants.CENTER);
        field.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                DatePicker dp = new DatePicker(BookingPage.this);
                String picked = dp.setPickedDate();
                if (!picked.equals("")) field.setText(picked);
            }
        });
        return field;
    }

    private JComboBox<String> createStyledComboBox(List<String> items) {
        JComboBox<String> c = new JComboBox<>(items.toArray(new String[0]));
        c.setFont(new Font("Arial", Font.PLAIN, 18));
        c.setBackground(new Color(138, 190, 249));
        c.setForeground(Color.WHITE);
        c.setPreferredSize(new Dimension(100, 50));
        return c;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BookingPage::new);
    }
}

class DatePicker {
    int month = Calendar.getInstance().get(Calendar.MONTH);
    int year = Calendar.getInstance().get(Calendar.YEAR);
    Calendar today = Calendar.getInstance();
    int currentMonth = today.get(Calendar.MONTH);
    int currentYear = today.get(Calendar.YEAR);
    int currentDay = today.get(Calendar.DAY_OF_MONTH);

    JLabel l = new JLabel("", JLabel.CENTER);
    String day = "";
    JDialog d;
    JButton[] button = new JButton[49];
    JButton prev, next;

    public DatePicker(JFrame parent) {
        d = new JDialog();
        d.setModal(true);
        d.setUndecorated(true);
        d.getRootPane().setBorder(BorderFactory.createLineBorder(new Color(66, 133, 244), 2));

        String[] header = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };
        JPanel p1 = new JPanel(new GridLayout(7, 7));
        p1.setPreferredSize(new Dimension(430, 320));
        p1.setBackground(Color.WHITE);

        for (int x = 0; x < button.length; x++) {
            final int selection = x;
            button[x] = new JButton();
            button[x].setFocusPainted(false);
            button[x].setBorder(BorderFactory.createLineBorder(new Color(240, 240, 240)));
            if (x < 7) {
                button[x].setText(header[x]);
                button[x].setFont(new Font("Arial", Font.BOLD, 12));
                button[x].setForeground(new Color(66, 133, 244));
                button[x].setBackground(new Color(235, 245, 255));
            } else {
                button[x].addActionListener(ae -> {
                    day = button[selection].getActionCommand();
                    d.dispose();
                });
            }
            p1.add(button[x]);
        }

        JPanel p2 = new JPanel(new GridLayout(1, 3));
        p2.setBackground(new Color(138, 190, 249));

        prev = new JButton("<<");
        prev.setForeground(Color.WHITE);
        prev.setContentAreaFilled(false);
        prev.setBorderPainted(false);
        prev.setFont(new Font("Arial", Font.BOLD, 16));
        prev.addActionListener(ae -> { month--; displayDate(); });

        l.setForeground(Color.WHITE);
        l.setFont(new Font("Arial", Font.BOLD, 14));

        next = new JButton(">>");
        next.setForeground(Color.WHITE);
        next.setContentAreaFilled(false);
        next.setBorderPainted(false);
        next.setFont(new Font("Arial", Font.BOLD, 16));
        next.addActionListener(ae -> { month++; displayDate(); });

        p2.add(prev); p2.add(l); p2.add(next);

        d.add(p1, BorderLayout.CENTER);
        d.add(p2, BorderLayout.SOUTH);
        d.pack();
        d.setLocationRelativeTo(parent);
        displayDate();
        d.setVisible(true);
    }

    public void displayDate() {
        for (int x = 7; x < button.length; x++) {
            button[x].setText("");
            button[x].setEnabled(false);
            button[x].setBackground(Color.WHITE);
        }

        Calendar cal = Calendar.getInstance();
        cal.set(year, month, 1);

        if (month <= currentMonth && year <= currentYear) {
            prev.setEnabled(false);
            prev.setVisible(false);
        } else {
            prev.setEnabled(true);
            prev.setVisible(true);
        }

        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        int daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        for (int x = 6 + dayOfWeek, d = 1; d <= daysInMonth; x++, d++) {
            button[x].setText("" + d);
            button[x].setActionCommand("" + d);
            button[x].setEnabled(true);
            if (month == currentMonth && year == currentYear && d < currentDay) {
                button[x].setEnabled(false);
                button[x].setForeground(Color.LIGHT_GRAY);
            } else {
                button[x].setForeground(Color.DARK_GRAY);
                button[x].setBackground(Color.WHITE);
            }
        }
        l.setText(new SimpleDateFormat("MMMM yyyy").format(cal.getTime()));
    }

    public String setPickedDate() {
        if (day.equals("")) return "";
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, Integer.parseInt(day));
        return new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
    }
}