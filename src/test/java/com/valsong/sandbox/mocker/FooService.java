package com.valsong.sandbox.mocker;

import java.util.List;

/**
 * FooService
 *
 * @author Val Song
 */
public interface FooService {

    static FooService getInstance() {
        return FooServiceImpl.getInstance();
    }


    List<Foo> findAll();

    List<Foo> findAllByNameLike(String name);

    Foo save(Foo foo);

    int saveAll(List<Foo> fooList);

    Foo findById(Long id);

    int deleteById(Long id);

    Foo foo();

    Foo foo(String name);

    Foo foo(Long id, String name);

    List<Bar> findAllBarListByFooId(Long id);
}
