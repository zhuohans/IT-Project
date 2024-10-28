package com.haste.yzx.common.domain.bo;

import lombok.Data;

@Data
public class ToEmail {

    private String[] tos;

    private String subject;

    private String content;
}
