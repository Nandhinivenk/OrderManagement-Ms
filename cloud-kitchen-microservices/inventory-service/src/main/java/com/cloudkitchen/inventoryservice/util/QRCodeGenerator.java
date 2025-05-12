package com.cloudkitchen.inventoryservice.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class QRCodeGenerator {
    
    private static final int QR_CODE_SIZE = 250;
    
    @Value("${qrcode.directory}")
    private String qrCodeDirectory;
    
    /**
     * Generate a QR code for an inventory item
     * 
     * @param itemId The ID of the inventory item
     * @param itemName The name of the inventory item
     * @return The path to the generated QR code image
     */
    public String generateQRCodeForInventoryItem(Long itemId, String itemName) {
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
    private String generateQRCode(String data, String fileName) {
        try {
            // Create directory if it doesn't exist
            Path dirPath = Paths.get(qrCodeDirectory);
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }
            
            // Generate QR code
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, QR_CODE_SIZE, QR_CODE_SIZE);
            
            // Save QR code to file
            Path filePath = Paths.get(qrCodeDirectory, fileName);
            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", filePath);
            
            return filePath.toString();
        } catch (WriterException | IOException e) {
            throw new RuntimeException("Error generating QR code: " + e.getMessage(), e);
        }
    }
}
