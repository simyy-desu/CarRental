import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.net.URISyntaxException;

public class Fleet {
    private static final String FILE_NAME = "fleet_inventory.json";
    public static void saveCars(List<Car> cars) {
        File inventoryFile = resolveFleetFile();
        if (inventoryFile == null) {
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(inventoryFile))) {
            writer.write("[\n");
            for (int i = 0; i < cars.size(); i++) {
                Car c = cars.get(i);
                writer.write("  {\n");
                writer.write("    \"name\": \"" + escapeJson(c.name) + "\",\n");
                writer.write("    \"type\": \"" + escapeJson(c.type) + "\",\n");
                writer.write("    \"plateNum\": \"" + escapeJson(c.plateNum) + "\",\n");
                writer.write("    \"category\": \"" + escapeJson(c.category) + "\",\n");
                writer.write("    \"detail\": \"" + escapeJson(c.detail) + "\",\n");
                writer.write("    \"imagePath\": \"" + escapeJson(c.imagePath.replace("\\", "/")) + "\"\n");
                writer.write("  }");
                if (i < cars.size() - 1) writer.write(",");
                writer.write("\n");
            }
            writer.write("]");
        } catch (IOException e) { e.printStackTrace(); }
    }

    public static List<Car> loadCars() {
        List<Car> cars = new ArrayList<>();
        File file = resolveFleetFile();
        if (!file.exists()) return cars;

        try (Scanner scanner = new Scanner(file)) {
            String name = "", type = "", plate = "", cat = "", detail = "", path = "";
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.contains("\"name\":")) name = extractValue(line);
                if (line.contains("\"type\":")) type = extractValue(line);
                if (line.contains("\"plateNum\":")) plate = extractValue(line);
                if (line.contains("\"category\":")) cat = extractValue(line);
                if (line.contains("\"detail\":")) detail = extractValue(line);
                if (line.contains("\"imagePath\":")) {
                    path = extractValue(line);
                    cars.add(new Car(name, type, detail, cat, plate, path));
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return cars;
    }

    public static File resolveInventoryAsset(String path) {
        if (path == null || path.trim().isEmpty()) {
            return null;
        }

        File directFile = new File(path);
        if (directFile.isAbsolute() || directFile.exists()) {
            return directFile;
        }

        File inventoryFile = resolveFleetFile();
        if (inventoryFile == null) {
            return directFile;
        }

        return new File(inventoryFile.getParentFile(), path);
    }

    private static String extractValue(String line) {
        try {
            int firstQuote = line.indexOf(":", 0) + 3;
            int lastQuote = line.lastIndexOf("\"");
            return unescapeJson(line.substring(firstQuote, lastQuote));
        } catch (Exception e) { return ""; }
    }

    private static File resolveFleetFile() {
        File workingDirFile = new File(FILE_NAME);
        if (workingDirFile.exists()) {
            return workingDirFile;
        }

        try {
            File codeSource = new File(
                Fleet.class.getProtectionDomain().getCodeSource().getLocation().toURI()
            );
            File baseDir = codeSource.isFile() ? codeSource.getParentFile() : codeSource;
            return new File(baseDir, FILE_NAME);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return workingDirFile;
        }
    }

    private static String escapeJson(String value) {
        if (value == null) {
            return "";
        }
        return value
            .replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("\r", "")
            .replace("\n", "<br>");
    }

    private static String unescapeJson(String value) {
        if (value == null) {
            return "";
        }
        return value
            .replace("\\\"", "\"")
            .replace("\\\\", "\\");
    }
}