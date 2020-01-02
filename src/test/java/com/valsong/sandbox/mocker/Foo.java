package com.valsong.sandbox.mocker;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Foo
 *
 * @author Val Song
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Foo {

    private Long id;

    private String name;

    private Integer age;

    private Double weight;

}
