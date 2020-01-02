package com.valsong.sandbox.mocker;

import java.util.List;


/**
 * BarService
 *
 * @author Val Song
 */
public interface BarService {

    static BarService getInstance() {
        return BarServiceImpl.getInstance();
    }

    List<Bar> findAll();

    List<Bar> findAllByNameLike(String name);

    Bar save(Bar bar);

    int saveAll(List<Bar> fooList);

    Bar findById(Long id);

    List<Bar> findAllByFooId(Long fooId);

    int deleteById(Long id);

}
