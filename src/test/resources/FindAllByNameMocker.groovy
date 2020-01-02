class FindAllByNameMocker {

    java.util.List<com.valsong.sandbox.mocker.Foo> mockFindAllByNameLike(String name) {
        java.util.List<com.valsong.sandbox.mocker.Foo> fooList = new java.util.ArrayList<com.valsong.sandbox.mocker.Foo>();
        fooList.add(com.valsong.sandbox.mocker.Foo.builder().id(-1L).name("foo-1").build());
        fooList.add(com.valsong.sandbox.mocker.Foo.builder().id(-2L).name("foo-2").build());
        fooList.add(com.valsong.sandbox.mocker.Foo.builder().id(-3L).name("foo-3").build());
        return fooList;
    }
}
