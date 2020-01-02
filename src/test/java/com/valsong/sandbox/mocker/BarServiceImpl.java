package com.valsong.sandbox.mocker;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * BarServiceImpl
 *
 * @author Val Song
 */
public class BarServiceImpl implements BarService {

    private BarServiceImpl() {
    }

    public static BarService getInstance() {
        return BarServiceHolder.INSTANCE;
    }

    private static class BarServiceHolder {
        private static final BarService INSTANCE = new BarServiceImpl();
    }


    private static List<Bar> barList = new ArrayList<>();

    private static AtomicLong index = new AtomicLong();

    static {

        for (int fooId = 0; fooId < 10; fooId++) {
            final long finalFooId = fooId;
            for (int j = 0; j < 10; j++) {
                long i = index.getAndIncrement();
                barList.add(Bar.builder()
                        .id(i)
                        .fooId(finalFooId)
                        .name("bar" + i)
                        .age(28)
                        .weight(i + 100.23)
                        .build()
                );
            }
        }

    }

    @Override
    public List<Bar> findAll() {
        return barList;
    }

    @Override
    public List<Bar> findAllByNameLike(String name) {
        return barList.stream()
                .filter(Objects::nonNull)
                .filter(bar -> bar.getName().contains(name))
                .collect(Collectors.toList());
    }

    @Override
    public Bar save(Bar bar) {
        if (bar != null && bar.getId() == null) {
            bar.setId(index.incrementAndGet());
        }
        return bar;
    }

    @Override
    public int saveAll(List<Bar> barList) {
        if (barList != null && !barList.isEmpty()) {
            for (Bar bar : barList) {
                save(bar);
            }
            return barList.size();
        }
        return 0;
    }

    @Override
    public Bar findById(Long id) {
        if (id == null) {
            return null;
        }
        return barList.stream()
                .filter(Objects::nonNull)
                .filter(u -> id.equals(u.getId()))
                .findFirst()
                .orElse(null);

    }


    @Override
    public List<Bar> findAllByFooId(Long fooId) {
        if (fooId == null) {
            return null;
        }
        return barList.stream()
                .filter(Objects::nonNull)
                .filter(b -> fooId.equals(b.getFooId()))
                .collect(Collectors.toList());
    }


    @Override
    public int deleteById(Long id) {
        if (id == null) {
            return 0;
        }
        int count = 0;
        Iterator<Bar> iterator = barList.iterator();
        while (iterator.hasNext()) {
            Bar bar = iterator.next();
            if (id.equals(bar.getId())) {
                iterator.remove();
                count++;
            }
        }
        return count;
    }


}
