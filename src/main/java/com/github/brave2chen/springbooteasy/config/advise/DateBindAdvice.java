package com.github.brave2chen.springbooteasy.config.advise;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

import java.beans.PropertyEditorSupport;
import java.util.Date;

/**
 * 全局日期绑定
 *
 * @author brave2chen
 * @since 2020-05-29
 */
@ControllerAdvice
public class DateBindAdvice {

    private static final String[] FORMATS = {"yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd"};

    @InitBinder
    public void dateBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new DatePropertyEditor());
    }

    /**
     * 日期转换器
     */
    private class DatePropertyEditor extends PropertyEditorSupport {
        @Override
        public void setAsText(String text) throws IllegalArgumentException {
            if (StringUtils.isBlank(text)) {
                return;
            }
            try {
                setValue(DateUtils.parseDate(text, FORMATS));
            } catch (Exception e) {
                throw new IllegalArgumentException("日期格式不正确 '" + text + "'", e);
            }
        }
    }
}
