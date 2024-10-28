package com.haste.yzx.common.utils;

import cn.hutool.core.util.StrUtil;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Hashtable;

@Slf4j
public class QrUtil {
    // QR code width
    private static final int QRCODE_SIZE = 430;
    // Encoding
    private static final String CHARSET = "utf-8";
    // QR code drawing height offset, reserved space for writing text description of the QR code
    private static final int OFFSET_HEIGHT = 25;
    // QR code title font
    private static final String TITLE_FONT = "SansSerif";
    // Title prefix
    private static final String TITLE_PREFIX = "ID: ";

    /**
     * Generate QR code.
     *
     * @param content Content
     * @return Image
     */
    public BufferedImage buildQrCodeImage(String content) {
        return createImage(content, content);
    }

    /**
     * Generate QR code.
     *
     * @param content Scanned content
     * @param title   QR code title
     * @return QR code image
     */
    private BufferedImage createImage(String content, String title) {
        // Hashtable is thread-safe, similar to HashMap
        Hashtable hints = new Hashtable();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
        hints.put(EncodeHintType.MARGIN, 1);

        BitMatrix bitMatrix = null;
        try {
            bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, QRCODE_SIZE, QRCODE_SIZE, hints);
        } catch (WriterException e) {
            log.error("Error generating QR code.. content [{}]", content, e);
            throw new RuntimeException("Error generating QR code. Please try again later.");
        }

        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
        log.info("QR code generated successfully!");
        // If there is a title, combine it with the QR code and title
        if (StrUtil.isNotBlank(title)) {
            return drawDetailForQR(image, title);
        }
        // Directly return the generated QR code
        return image;
    }

    /**
     * Draw QR code description.
     *
     * @param source The source QR code image
     * @param title  QR code title
     * @return The combined image
     */
    private BufferedImage drawDetailForQR(BufferedImage source, String title) {
        // Create a new template image
        BufferedImage bufferedImage = new BufferedImage(QRCODE_SIZE, QRCODE_SIZE + OFFSET_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = bufferedImage.createGraphics();
        // Draw rectangular background
        graphics.setColor(Color.white);
        graphics.fillRect(0, 0, QRCODE_SIZE, OFFSET_HEIGHT);
        // Draw description text
        Font font = new Font(TITLE_FONT, Font.PLAIN, 22);
        graphics.setColor(Color.black);
        graphics.setFont(font);
//        graphics.drawString(TITLE_PREFIX + title, 20, OFFSET_HEIGHT - 2);
        // Draw the QR code
        graphics.drawImage(source, 0, OFFSET_HEIGHT, QRCODE_SIZE, QRCODE_SIZE, null);
        graphics.dispose();
        return bufferedImage;
    }
}