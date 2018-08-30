package com.cyzest.findbooks.searcher;

import com.cyzest.findbooks.searcher.kakao.KakaoBookInfo;
import com.cyzest.findbooks.searcher.kakao.KakaoBookSearchResult;
import com.cyzest.findbooks.searcher.naver.NaverBookChannel;
import com.cyzest.findbooks.searcher.naver.NaverBookInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.pojo.tester.api.FieldPredicate;
import pl.pojo.tester.api.assertion.Method;

import static pl.pojo.tester.api.assertion.Assertions.assertPojoMethodsFor;

public class ModelTest {

    @Test
    public void POJO_클래스를_검증한다() {

        assertPojoMethodsFor(BookInfo.class, FieldPredicate.exclude("isbn"))
                .testing(Method.CONSTRUCTOR, Method.GETTER, Method.SETTER).areWellImplemented();

        assertPojoMethodsFor(BookSearchCategory.class)
                .testing(Method.CONSTRUCTOR, Method.GETTER, Method.EQUALS, Method.TO_STRING).areWellImplemented();

        assertPojoMethodsFor(BookSearchResult.class).areWellImplemented();

        assertPojoMethodsFor(BookSearchTarget.class)
                .testing(Method.CONSTRUCTOR, Method.GETTER, Method.EQUALS, Method.TO_STRING).areWellImplemented();

        assertPojoMethodsFor(KakaoBookInfo.class, FieldPredicate.exclude("thumbnail"))
                .testing(Method.CONSTRUCTOR, Method.GETTER, Method.SETTER).areWellImplemented();

        assertPojoMethodsFor(KakaoBookSearchResult.class, FieldPredicate.exclude("meta"))
                .testing(Method.CONSTRUCTOR, Method.GETTER, Method.SETTER).areWellImplemented();

        assertPojoMethodsFor(NaverBookInfo.class).areWellImplemented();

        assertPojoMethodsFor(NaverBookChannel.class, FieldPredicate.exclude("items"))
                .testing(Method.CONSTRUCTOR, Method.GETTER, Method.SETTER).areWellImplemented();
    }

    @Test
    public void BookSearchSort_객채생성을_검증한다() {
        BookSearchSort bookSearchSort = BookSearchSort.ACCURACY;
        Assertions.assertEquals("accuracy", bookSearchSort.getCode());
        Assertions.assertEquals("정확도순", bookSearchSort.getDescription());
    }

    @Test
    public void OpenApiType_객채생성을_검증한다() {
        OpenApiType openApiType = OpenApiType.KAKAO;
        Assertions.assertEquals("kakao", openApiType.getCode());
        Assertions.assertEquals("카카오", openApiType.getDescription());
    }

    @Test
    public void BookInfo_isbn_필드를_검증한다() {

        String emptyString = "";

        BookInfo bookInfo = new BookInfo();

        bookInfo.setIsbn(null);
        Assertions.assertEquals(emptyString, bookInfo.getIsbn());

        bookInfo.setIsbn(emptyString);
        Assertions.assertEquals(emptyString, bookInfo.getIsbn());

        bookInfo.setIsbn(" ");
        Assertions.assertEquals(emptyString, bookInfo.getIsbn());

        bookInfo.setIsbn("12345");
        Assertions.assertEquals("12345", bookInfo.getIsbn());

        bookInfo.setIsbn(" 12345");
        Assertions.assertEquals("12345", bookInfo.getIsbn());

        bookInfo.setIsbn("12345 ");
        Assertions.assertEquals("12345", bookInfo.getIsbn());

        bookInfo.setIsbn("12345 67890");
        Assertions.assertEquals("67890", bookInfo.getIsbn());
    }

    @Test
    public void KakaoBookInfo_thumbnail_필드를_검증한다() {

        String emptyImageUrl = "http://i1.daumcdn.net/img-contents/book/2010/72x100_v2.gif";

        KakaoBookInfo kakaoBookInfo = new KakaoBookInfo();

        kakaoBookInfo.setThumbnail(null);
        Assertions.assertEquals(emptyImageUrl, kakaoBookInfo.getThumbnail());

        kakaoBookInfo.setThumbnail("");
        Assertions.assertEquals(emptyImageUrl, kakaoBookInfo.getThumbnail());

        kakaoBookInfo.setThumbnail("test");
        Assertions.assertEquals("test", kakaoBookInfo.getThumbnail());
    }

    @Test
    public void NaverBookInfo_createBookInfo_메서드를_검증한다() {

        NaverBookInfo naverBookInfo = new NaverBookInfo();

        naverBookInfo.setTitle("test");
        Assertions.assertEquals("test", naverBookInfo.createBookInfo().getTitle());

        naverBookInfo.setTitle("<b>test</b>");
        Assertions.assertEquals("test", naverBookInfo.createBookInfo().getTitle());
    }

    @Test
    public void NaverBookChannel_items_필드를_검증한다() {

        NaverBookChannel naverBookChannel = new NaverBookChannel();

        naverBookChannel.setItems(new NaverBookInfo());
        naverBookChannel.setItems(new NaverBookInfo());

        Assertions.assertEquals(2, naverBookChannel.getItems().size());
    }

}
