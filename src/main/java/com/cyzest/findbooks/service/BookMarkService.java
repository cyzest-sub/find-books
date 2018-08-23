package com.cyzest.findbooks.service;

import com.cyzest.findbooks.dao.BookMark;
import com.cyzest.findbooks.dao.BookMarkRepository;
import com.cyzest.findbooks.dao.User;
import com.cyzest.findbooks.dao.UserRepository;
import com.cyzest.findbooks.model.BookMarkInfo;
import com.cyzest.findbooks.model.BookMarkPagingParam;
import com.cyzest.findbooks.model.BookMarkResult;
import com.cyzest.findbooks.searcher.BookInfo;
import com.cyzest.findbooks.searcher.OpenApiType;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class BookMarkService {

    private UserRepository userRepository;

    private BookMarkRepository bookMarkRepository;

    private BookSearchService bookSearchService;

    public void saveBookMark(String userId, OpenApiType openApiType, String isbn) throws Exception {

        if (StringUtils.isEmpty(isbn)) {
            throw new IllegalArgumentException();
        }

        //FIXME BasedException 적용 후 ifPresent 로 변경 할 것
        User user = userRepository.findById(userId).orElse(null);

        if (user != null) {

            List<BookMark> existBookMarks = bookMarkRepository.findByUserAndOpenApiTypeAndIsbn(user, openApiType, isbn);

            if (!CollectionUtils.isEmpty(existBookMarks)) {
                throw new IllegalAccessException();
            }

            BookInfo bookInfo = bookSearchService.getBookByIsbn(openApiType, isbn);

            if (bookInfo == null) {
                throw new IllegalAccessException();
            }

            BookMark bookMark = new BookMark();

            bookMark.setIsbn(isbn);
            bookMark.setOpenApiType(openApiType);
            bookMark.setTitle(bookInfo.getTitle());
            bookMark.setThumbnail(bookInfo.getThumbnail());
            bookMark.setRegDate(LocalDateTime.now());
            bookMark.setUser(user);

            bookMarkRepository.saveAndFlush(bookMark);
        }
    }

    public void deleteBookMark(String userId, long id) {

        userRepository.findById(userId).ifPresent(user -> {

            List<BookMark> existBookMarks = bookMarkRepository.findByUserAndId(user, id);

            if (!CollectionUtils.isEmpty(existBookMarks)) {
                bookMarkRepository.delete(existBookMarks.get(0));
            }

        });
    }

    public BookMarkResult getBookMarksByUserId(String userId, BookMarkPagingParam pagingParam) {

        BookMarkResult bookMarkResult = new BookMarkResult();

        userRepository.findById(userId).ifPresent(user -> {

            PageRequest pageRequest = PageRequest.of(
                    pagingParam.getPage() - 1, pagingParam.getSize(), pagingParam.getSort().getSort());

            Page<BookMark> bookMarksPage = bookMarkRepository.findByUser(user, pageRequest);

            List<BookMark> bookMarks = bookMarksPage.getContent();

            bookMarkResult.setTotalCount((int) bookMarksPage.getTotalElements());

            if (bookMarks != null) {
                bookMarkResult.setBookMarkInfos(bookMarks.stream().map(BookMarkInfo::new).collect(Collectors.toList()));
            }

        });

        return bookMarkResult;
    }

}
