package com.insomnia.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Jay  zhixin.yuan@transsnet.com
 * @project: insomnia-project
 * @description:
 * @date 2023/11/22
 */
@Getter
@AllArgsConstructor
public enum SpringMappingEnum implements IEnum<String> {

    /**
     * spring mapping url
     */
    MAPPING_PREFIX("org.springframework.web.bind.annotation"),
    MAPPING("org.springframework.web.bind.annotation.RequestMapping"),
    ;

    private final String code;
}
