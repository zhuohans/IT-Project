package com.haste.yzx.system.api.domain.bo.species;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class SpeciesQueryInfo   {

    private Page page;


    /**
     *  Name eq
     */
    private String name;

    /**
     *  Latin name eq
     */
    private String latinName;


    /**
     *   Latin name like
     */
    private String latinNameLike;

    /**
     *  Popular classification eq
     */
    private Integer popularClassification;

    /**
     *  Planting season eq
     */
    private Integer seasonClassification;

}
