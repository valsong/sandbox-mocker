package com.valsong.sandbox.mocker;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * FooServiceImpl
 *
 * @author Val Song
 */
public class FooServiceImpl implements FooService {

    private static final Logger log = LoggerFactory.getLogger(FooServiceImpl.class);

    private FooServiceImpl() {
    }

    public static FooService getInstance() {
        return FooServiceHolder.INSTANCE;
    }

    private static class FooServiceHolder {
        private static final FooService INSTANCE = new FooServiceImpl();
    }


    private static List<Foo> fooList = new ArrayList<>();

    private static AtomicLong index = new AtomicLong();

    private ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    static {
        for (; index.get() < 10; index.incrementAndGet()) {
            long i = index.get();
            fooList.add(Foo.builder()
                    .id(i)
                    .name("foo" + i)
                    .age(28)
                    .weight(i + 100.23)
                    .build()
            );
        }
    }

    @Override
    public List<Foo> findAll() {
        return fooList;
    }

    @Override
    public List<Foo> findAllByNameLike(String name) {
        return fooList.stream()
                .filter(Objects::nonNull)
                .filter(foo -> foo.getName().contains(name))
                .collect(Collectors.toList());
    }

    @Override
    public Foo save(Foo foo) {
        if (foo != null && foo.getId() == null) {
            foo.setId(index.incrementAndGet());
        }
        return foo;
    }

    @Override
    public int saveAll(List<Foo> fooList) {
        if (fooList != null && !fooList.isEmpty()) {
            for (Foo foo : fooList) {
                save(foo);
            }
            return fooList.size();
        }
        return 0;
    }

    @Override
    public Foo findById(Long id) {
        if (id == null) {
            return null;
        }
        return fooList.stream()
                .filter(Objects::nonNull)
                .filter(f -> id.equals(f.getId()))
                .findFirst()
                .orElse(null);

    }

    @Override
    public int deleteById(Long id) {
        if (id == null) {
            return 0;
        }
        int count = 0;
        Iterator<Foo> iterator = fooList.iterator();
        while (iterator.hasNext()) {
            Foo foo = iterator.next();
            if (id.equals(foo.getId())) {
                iterator.remove();
                count++;
            }
        }
        return count;
    }

    @Override
    public Foo foo() {
        return Foo.builder().id(-1L).name("foo-1").build();
    }

    @Override
    public Foo foo(String name) {
        return Foo.builder().id(-2L).name(name).build();
    }

    @Override
    public Foo foo(Long id, String name) {
        return Foo.builder().id(id).name(name).build();
    }

    @Override
    public List<Bar> findAllBarListByFooId(Long id) {

        Thread t = new Thread(() -> {
            Foo foo = foo(id, "foo-" + id);
            log.debug("foo:[{}]", foo);
        });

        executorService.execute(t);

        return CompletableFuture.supplyAsync(() -> BarService.getInstance().findAllByFooId(id), executorService)
                .join();


    }

}
