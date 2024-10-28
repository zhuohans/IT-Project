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

    private static final String CHARSET = "utf-8";
    // The height offset of the QR code drawing, leaving space for writing text to describe the QR code information
    private static final int OFFSET_HEIGHT = 25;
    // QR code title font
    private static final String TITLE_FONT = "黑体";
    // Title Prefix
    private static final String TITLE_PREFIX = "Prefix: ";

    /**
     * Generate a QR code
     *
     * @param content Content
     * @return image
     */
    public BufferedImage buildQrCodeImage(String content) {
        return createImage(content, content);
    }

    /**
     * Generate a QR code
     *
     * @param content Scanned content successfully
     * @param title QR code title
     * @return QR code image
     */
    private BufferedImage createImage(String content, String title) {
        // Equivalent to hashmap, hashtable is thread-safe
        Hashtable hints = new Hashtable();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
        hints.put(EncodeHintType.MARGIN, 1);

        BitMatrix bitMatrix = null;
        try {
            bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, QRCODE_SIZE, QRCODE_SIZE, hints);
        } catch (WriterException e) {
            log.error("The QR code generation is abnormal..content【{}】", content, e);
            throw new RuntimeException("The generated QR code information is abnormal, please try again later");
        }

        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
        log.info("The QR code was generated successfully!");
        // With title, synthesize QR code with title
        if (StrUtil.isNotBlank(title)) {
            return drawDetailForQR(image, title);
        }
        // Directly return the generated QR code
        return image;
    }

    /**
     * Draw a QR code description information.
     *
     * @param source Source QR code image
     * @param title  QR code title
     * @return The synthesized picture
     */
    private BufferedImage drawDetailForQR(BufferedImage source, String title) {
        // Create a new template image
        BufferedImage bufferedImage = new BufferedImage(QRCODE_SIZE, QRCODE_SIZE + OFFSET_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = bufferedImage.createGraphics();
        // Draw a rectangular background
        graphics.setColor(Color.white);
        graphics.fillRect(0, 0, QRCODE_SIZE, OFFSET_HEIGHT);
        // Drawing description information
        Font font = new Font(TITLE_FONT, Font.PLAIN, 22);
        graphics.setColor(Color.black);
        graphics.setFont(font);
//        graphics.drawString(TITLE_PREFIX + title, 20, OFFSET_HEIGHT - 2);
        // Draw a QR code
        graphics.drawImage(source, 0, OFFSET_HEIGHT, QRCODE_SIZE, QRCODE_SIZE, null);
        graphics.dispose();
        return bufferedImage;
    }
}
