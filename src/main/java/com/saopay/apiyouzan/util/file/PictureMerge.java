package com.saopay.apiyouzan.util.file;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import lombok.extern.slf4j.Slf4j;

/**
 * @author huangding
 * @description
 * @date 2018/11/24 15:00
 */
@Slf4j
public class PictureMerge {


    /**
     * 生成微信邀请码
     *
     * @param codeUrl 二维码url
     * @param imageLocalUrl 背景图
     * @param portraitUrl 头像url
     * @param userName 微信昵称
     */
    public static InputStream generateCode(File imageLocalUrl, String codeUrl, String portraitUrl,
        String userName) {
        log.info(codeUrl);
        log.info(portraitUrl);

        // 添加字体的属性设置
        Font font = new Font("微软雅黑", Font.PLAIN, 40);
        try {
            // 加载本地图片
            BufferedImage imageLocal = ImageIO.read(imageLocalUrl);

            // 加载用户的二维码
            BufferedImage imageCode = ImageIO.read(new URL(codeUrl));
            //加载头像图片
            BufferedImage imagePortrait = ImageIO.read(new URL(portraitUrl));

            // 以本地图片为模板
            Graphics2D g = imageLocal.createGraphics();
            // 在模板上添加用户二维码(地址,左边距,上边距,图片宽度,图片高度,未知)

            log.info("imageLocal width:{},Height:{}", imageLocal.getWidth(),
                imageLocal.getHeight());

            log.info("imagePortrait width:{},Height{}", imagePortrait.getWidth(),
                imagePortrait.getHeight());
            log.info("imageCode width:{},Height{}", imageCode.getWidth(), imageCode.getHeight());

            int width = imageLocal.getWidth();
            int height = imageLocal.getHeight();

            g.drawImage(imagePortrait, (int) (width / 2.3), (int) (height / 2.5),
                imagePortrait.getWidth(),
                imagePortrait.getHeight(), null);

            g.drawImage(imageCode, (int) (width / 3.3), height / 2,
                imageCode.getWidth(), imageCode.getHeight(), null);

            // 设置文本样式
            g.setFont(font);
            g.setColor(Color.BLACK);
            // 截取用户名称的最后一个字符
            String lastChar = userName.substring(userName.length() - 1);
            // 拼接新的用户名称
            String newUserName = userName.substring(0, 1) + "**" + lastChar + " 的邀请二维码";
            // 添加用户名称

            g.drawString(newUserName,  (int) (width / 3), (float) (height / 2.1));
            // 完成模板修改
            g.dispose();

            imageLocal.flush();
            InputStream is = null;
            try (ByteArrayOutputStream bs = new ByteArrayOutputStream(); ImageOutputStream imOut = ImageIO
                .createImageOutputStream(bs);) {
                ImageIO.write(imageLocal, "png", imOut);
                is = new ByteArrayInputStream(bs.toByteArray());
                return is;
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 返回给页面的图片地址(因为绝对路径无法访问)
        return null;
    }


    public static BufferedImage mergeImage(BufferedImage img1, BufferedImage img2,
        boolean isHorizontal, int startX, int startY) throws IOException {
        int w1 = img1.getWidth();
        int h1 = img1.getHeight();
        int w2 = img2.getWidth();
        int h2 = img2.getHeight();

        // 从图片中读取RGB
        int[] ImageArrayOne = new int[w1 * h1];
        // 逐行扫描图像中各个像素的RGB到数组中
        ImageArrayOne = img1.getRGB(0, 0, w1, h1, ImageArrayOne, 0, w1);
        int[] ImageArrayTwo = new int[w2 * h2];
        ImageArrayTwo = img2.getRGB(0, 0, w2, h2, ImageArrayTwo, 0, w2);
        // 生成新图片
        BufferedImage destImage = new BufferedImage(w1, h1 , BufferedImage.TYPE_INT_RGB);

        // 设置上半部分或左半部分的RGB
        destImage.setRGB(w1, h1, w1, h1, ImageArrayOne, 0, w1);
        // 设置下半部分的RGB
        destImage.setRGB(0, h2, w2, h2, ImageArrayTwo, 0, w2);
        destImage.flush();
        return destImage;
    }


    /**
     * 添加文字描述
     */
    public static BufferedImage drawTextInImg(BufferedImage bimage, FontText text, int left,
        int top) {
        Graphics2D g = bimage.createGraphics();
        g.setColor(getColor(text.getWm_text_color()));
        g.setBackground(Color.white);
        Font font = new Font(text.getWm_text_font(), Font.BOLD,
            text.getWm_text_size());
        g.setFont(font);
        g.drawString(text.getText(), left, top);
        g.dispose();
        return bimage;
    }


    /**
     * 获取颜色代码
     *
     * @param color color #2395439
     */
    public static Color getColor(String color) {
        if (color.charAt(0) == '#') {
            color = color.substring(1);
        }
        if (color.length() != 6) {
            return null;
        }
        try {
            int r = Integer.parseInt(color.substring(0, 2), 16);
            int g = Integer.parseInt(color.substring(2, 4), 16);
            int b = Integer.parseInt(color.substring(4), 16);
            return new Color(r, g, b);
        } catch (NumberFormatException nfe) {
            return null;
        }
    }

//    public static void main(String[] args) throws IOException {
//
//        BufferedImage imageCode = ImageIO.read(new URL(
//            "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=gQHT8DwAAAAAAAAAAS5odHRwOi8vd2VpeGluLnFxLmNvbS9xLzAybS01ekJkcEpjaWwxM3BWejFzMW0AAgTZrPtbAwQAjScA"));
//        File file = new File(
//            "E:\\MyWorkspace\\api-youzan\\src\\main\\resources\\images\\invitation_code.jpg");
//
//        BufferedImage imageLocal = ImageIO.read(file);
//
//        BufferedImage bufferedImage = mergeImage(imageLocal, imageCode, false,
//            100, 100);
//
//        ImageIO.write(bufferedImage, "png", new File("F:\\nmd.jpg"));
//
//
//    }


}
