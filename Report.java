import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Report {
    private static final String FILE_NAME = "maintenance_reports.json";

    public static void saveReport(String car, String plate, String type, String desc) {
        List<String> existingReports = loadRawReports();
        
        String newReport = "  {\n" +
                "    \"carName\": \"" + car + "\",\n" +
                "    \"plateNum\": \"" + plate + "\",\n" +
                "    \"issueType\": \"" + type + "\",\n" +
                "    \"description\": \"" + desc.replace("\n", "\\n").replace("\"", "\\\"") + "\",\n" +
                "    \"date\": \"" + new java.util.Date().toString() + "\"\n" +
                "  }";

        existingReports.add(newReport);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            writer.write("[\n");
            for (int i = 0; i < existingReports.size(); i++) {
                writer.write(existingReports.get(i));
                if (i < existingReports.size() - 1) writer.write(",");
                writer.write("\n");
            }
            writer.write("]");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<String> loadRawReports() {
        List<String> reports = new ArrayList<>();
        File file = new File(FILE_NAME);
        if (!file.exists()) return reports;

        try (Scanner scanner = new Scanner(file)) {
            StringBuilder sb = new StringBuilder();
            while (scanner.hasNextLine()) sb.append(scanner.nextLine());
            
            String content = sb.toString().trim();
            if (content.length() <= 2) return reports;

            String inner = content.substring(1, content.length() - 1);
            String[] parts = inner.split("(?<=\\}),");
            for (String p : parts) reports.add(p.trim());
            
        } catch (Exception e) { e.printStackTrace(); }
        return reports;
    }
}
