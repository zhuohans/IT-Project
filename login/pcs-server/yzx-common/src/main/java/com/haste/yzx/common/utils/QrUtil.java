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
    //二维码宽度
    private static final int QRCODE_SIZE = 430;
    //编码
    private static final String CHARSET = "utf-8";
    // 二维码绘制高度偏移量，留出空间写文字描述二维码信息
    private static final int OFFSET_HEIGHT = 25;
    //二维码标题字体
    private static final String TITLE_FONT = "黑体";
    //标题前缀
    private static final String TITLE_PREFIX = "编号：";

    /**
     * 生成二维码.
     *
     * @param content 内容
     * @return 图片
     */
    public BufferedImage buildQrCodeImage(String content) {
        return createImage(content, content);
    }

    /**
     * 生成二维码.
     *
     * @param content 扫描成功的内容
     * @param title 二维码标题
     * @return 二维码图片
     */
    private BufferedImage createImage(String content, String title) {
        //等同于hashmap,hashtable是线程安全的
        Hashtable hints = new Hashtable();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
        hints.put(EncodeHintType.MARGIN, 1);

        BitMatrix bitMatrix = null;
        try {
            bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, QRCODE_SIZE, QRCODE_SIZE, hints);
        } catch (WriterException e) {
            log.error("生成二维码异常了..content【{}】", content, e);
            throw new RuntimeException("生成二维码信息异常，请稍后重试");
        }

        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
        log.info("生成二维码成功！");
        //有标题，合成带标题的二维码
        if (StrUtil.isNotBlank(title)) {
            return drawDetailForQR(image, title);
        }
        //直接返回生成的二维码
        return image;
    }

    /**
     * 绘制二维码描述信息.
     *
     * @param source 源二维码图片
     * @param title  二维码标题
     * @return 合成后的图片
     */
    private BufferedImage drawDetailForQR(BufferedImage source, String title) {
        //新建模板图片
        BufferedImage bufferedImage = new BufferedImage(QRCODE_SIZE, QRCODE_SIZE + OFFSET_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = bufferedImage.createGraphics();
        //绘制矩形背景
        graphics.setColor(Color.white);
        graphics.fillRect(0, 0, QRCODE_SIZE, OFFSET_HEIGHT);
        //绘制描述信息
        Font font = new Font(TITLE_FONT, Font.PLAIN, 22);
        graphics.setColor(Color.black);
        graphics.setFont(font);
//        graphics.drawString(TITLE_PREFIX + title, 20, OFFSET_HEIGHT - 2);
        //绘制二维码
        graphics.drawImage(source, 0, OFFSET_HEIGHT, QRCODE_SIZE, QRCODE_SIZE, null);
        graphics.dispose();
        return bufferedImage;
    }
}
