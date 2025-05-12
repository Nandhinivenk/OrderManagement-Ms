package com.cloudkitchen.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Utility class for generating QR codes
 */
public class QRCodeGenerator {

    private static final int QR_CODE_SIZE = 250;
    private static final String QR_CODE_DIR = "./qrcodes";

    /**
     * Generate a QR code for an inventory item
     *
     * @param itemId The ID of the inventory item
     * @param itemName The name of the inventory item
     * @return The path to the generated QR code image
     */
    public static String generateQRCodeForInventoryItem(int itemId, String itemName) {
        String qrCodeData = String.format("INVENTORY:%d:%s", itemId, itemName);
        String fileName = "inventory_" + itemId + ".png";

        return generateQRCode(qrCodeData, fileName);
    }

    /**
     * Generate a QR code with the given data and save it to a file
     *
     * @param data The data to encode in the QR code
     * @param fileName The name of the file to save the QR code to
     * @return The path to the generated QR code image
     */
    private static String generateQRCode(String data, String fileName) {
        try {
            // Create directory if it doesn't exist
            Path dirPath = Paths.get(QR_CODE_DIR);
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }

            // For now, just create an empty file as a placeholder
            // since we don't have the QR code generation libraries
            Path filePath = Paths.get(QR_CODE_DIR, fileName);
            Files.createFile(filePath);

            System.out.println("Created placeholder QR code file: " + filePath);
            return filePath.toString();
        } catch (IOException e) {
            System.err.println("Error generating QR code: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
