package com.valsong.sandbox.mocker;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


/**
 * MockFooTest
 * 执行mock测试，需要PackageAndLoadFooMockerTest来加载mock组件
 *
 * @author Val Song
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MockFooTest {

    private static final Logger log = LoggerFactory.getLogger(MockFooTest.class);

    FooService fooService = FooService.getInstance();

    /**
     * 注意该测试方法会一直运行
     * 除非调用PackageAndLoadFooMockerTest进行mock才能终止
     */
    @Test
    public void test01_mockFooTest() {

        List<Foo> allFooList;
        List<Foo> findAllByNameLikeList;
        Foo foo1;
        Foo foo2;
        Foo foo3;

        while (true) {
            allFooList = fooService.findAll();
            findAllByNameLikeList = fooService.findAllByNameLike("foo");
            foo1 = fooService.foo();
            foo2 = fooService.foo("foo-2");
            foo3 = fooService.foo(-3L, "foo-3");

            log.info("allFooList:[{}]", allFooList);
            log.info("findAllByNameLikeList:[{}]", findAllByNameLikeList);
            log.info("foo1:[{}]", foo1);
            log.info("foo2:[{}]", foo2);
            log.info("foo3:[{}]", foo3);

            if (findAllByNameLikeList.size() == 3 && foo1.getId().equals(-11L) && foo2.getId().equals(-22L) && foo3.getId().equals(-33L)) {
                break;
            }

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
            }

        }

        Assert.assertEquals(3, allFooList.size());
        Assert.assertEquals(3, findAllByNameLikeList.size());

        Assert.assertEquals(Long.valueOf(-11L), foo1.getId());
        Assert.assertEquals(Long.valueOf(-22L), foo2.getId());
        Assert.assertEquals(Long.valueOf(-33L), foo3.getId());

    }

    @Test
    public void test02_mockMultiThreadTest() {

        long start = System.currentTimeMillis();
        for (long i = 0; i < 10; i++) {
            List<Bar> barList = fooService.findAllBarListByFooId(i);
            log.debug("barList:[{}]", barList);
        }

        long cost = System.currentTimeMillis() - start;

        Assert.assertTrue(cost >= 5000L);

    }

}
