package com.cyzest.findbooks.service;

import com.cyzest.findbooks.dao.BookMark;
import com.cyzest.findbooks.dao.BookMarkRepository;
import com.cyzest.findbooks.dao.User;
import com.cyzest.findbooks.dao.UserRepository;
import com.cyzest.findbooks.model.BookMarkInfo;
import com.cyzest.findbooks.model.BookMarkResult;
import com.cyzest.findbooks.searcher.BookInfo;
import com.cyzest.findbooks.searcher.OpenApiType;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class BookMarkService {

    private UserRepository userRepository;

    private BookMarkRepository bookMarkRepository;

    private BookSearchService bookSearchService;

    @Transactional
    public void saveBookMark(String userId, OpenApiType openApiType, String isbn) throws Exception {

        if (StringUtils.isEmpty(isbn)) {
            throw new IllegalArgumentException();
        }

        User user = userRepository.findById(userId);

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
            bookMark.setRegDate(new Date());
            bookMark.setUser(user);

            bookMarkRepository.saveAndFlush(bookMark);
        }
    }

    @Transactional
    public void deleteBookMark(String userId, long id) {

        User user = userRepository.findById(userId);

        if (user != null) {

            List<BookMark> existBookMarks = bookMarkRepository.findByUserAndId(user, id);

            if (!CollectionUtils.isEmpty(existBookMarks)) {
                bookMarkRepository.delete(existBookMarks.get(0));
            }
        }
    }

    @Transactional(readOnly = true)
    public BookMarkResult getBookMarksByUserId(String userId, Pageable pageable) {

        BookMarkResult bookMarkResult = new BookMarkResult();

        User user = userRepository.findById(userId);

        if (user != null) {

            PageRequest pageRequest = new PageRequest(
                    pageable.getPageNumber() - 1, pageable.getPageSize(), pageable.getSort());

            Page<BookMark> bookMarksPage = bookMarkRepository.findByUser(user, pageRequest);

            List<BookMark> bookMarks = bookMarksPage.getContent();

            bookMarkResult.setTotalCount((int)bookMarksPage.getTotalElements());

            if (bookMarks != null) {
                bookMarkResult.setBookMarkInfos(bookMarks.stream().map(bookMark -> {
                    BookMarkInfo bookMarkInfo = new BookMarkInfo();
                    bookMarkInfo.setId(bookMark.getId());
                    bookMarkInfo.setOpenApiType(bookMark.getOpenApiType());
                    bookMarkInfo.setIsbn(bookMark.getIsbn());
                    bookMarkInfo.setTitle(bookMark.getTitle());
                    bookMarkInfo.setThumbnail(bookMark.getThumbnail());
                    bookMarkInfo.setRegDate(bookMark.getRegDate());
                    return bookMarkInfo;
                }).collect(Collectors.toList()));
            }
        }

        return bookMarkResult;
    }

}
