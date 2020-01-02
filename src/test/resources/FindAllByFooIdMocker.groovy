import com.valsong.sandbox.mocker.Bar

import java.util.concurrent.atomic.AtomicLong

class FindAllByFooIdMocker {

    private AtomicLong index = new AtomicLong(0L);

    List<Bar> mockFindAllByFooId(Long fooId) {
        List<Bar> barList = new ArrayList<>();
        barList.add(Bar.builder().id(index.decrementAndGet()).name("bar" + index.get()).fooId(fooId).build());
        barList.add(Bar.builder().id(index.decrementAndGet()).name("bar" + index.get()).fooId(fooId).build());
        barList.add(Bar.builder().id(index.decrementAndGet()).name("bar" + index.get()).fooId(fooId).build());
        return barList;
    }
}
