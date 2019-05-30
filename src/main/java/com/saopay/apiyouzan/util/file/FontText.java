package com.saopay.apiyouzan.util.file;

import lombok.Data;

/**
 * @author huangding
 * @description
 * @date 2018/11/24 15:04
 */
@Data
public class FontText {


    /**
     * 描述
     */
    private String text;

    private int wm_text_pos;

    /**
     * 颜色 #FFFFF
     */
    private String wm_text_color;

    /**
     * 字体大小
     */
    private Integer wm_text_size;
    /**
     * 字体  “黑体，Arial”
     */
    private String wm_text_font;

    public FontText(String text, int wm_text_pos, String wm_text_color, Integer wm_text_size,
        String wm_text_font) {
        this.text = text;
        this.wm_text_pos = wm_text_pos;
        this.wm_text_color = wm_text_color;
        this.wm_text_size = wm_text_size;
        this.wm_text_font = wm_text_font;
    }
}
