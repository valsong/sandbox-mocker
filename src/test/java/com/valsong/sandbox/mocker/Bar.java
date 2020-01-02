package com.valsong.sandbox.mocker;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Bar
 *
 * @author Val Song
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Bar {

    private Long id;

    private Long fooId;

    private String name;

    private Integer age;

    private Double weight;

}
