package com.haste.yzx.system.api.domain.bo.species;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SpeciesQueryInfo {

    private Page page;

    /**
     *  Name (exact match)
     */
    private String name;

    /**
     *  Latin Name (exact match)
     */
    private String latinName;

    /**
     *  Latin Name (partial match)
     */
    private String latinNameLike;

    /**
     *  Popular Classification (e.g., Flower, Vegetable, Tree) (exact match)
     */
    private Integer popularClassification;

    /**
     *  Planting Season Classification (exact match)
     */
    private Integer seasonClassification;
}