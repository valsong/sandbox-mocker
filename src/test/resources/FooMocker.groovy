class FooMocker {

    com.valsong.sandbox.mocker.Foo mockFoo(Long id, String name) {
        return com.valsong.sandbox.mocker.Foo.builder().id(-33L).name("foo-33").build();
    }
}
