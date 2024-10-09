package com.haste.yzx.system.api.domain.bo.species;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class SpeciesQueryInfo   {

    private Page page;


    /**
     *  名称 eq
     */
    private String name;

    /**
     *  英文名称 eq
     */
    private String latinName;


    /**
     *  英文名称 like
     */
    private String latinNameLike;

    /**
     *  花菜树 eq
     */
    private Integer popularClassification;

    /**
     *  种植季节 eq
     */
    private Integer seasonClassification;

}
