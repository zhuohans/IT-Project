package com.haste.yzx.common.domain.bo;

import lombok.Data;

@Data
public class ToEmail {
    /**
     * Email recipients can be multiple people
     */
    private String[] tos;
    /**
     * Email Subject
     */
    private String subject;
    /**
     * Email Content
     */
    private String content;
}
