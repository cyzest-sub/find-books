package com.cyzest.findbooks.controller;

import com.cyzest.findbooks.common.EnumCodePropertyEditor;
import com.cyzest.findbooks.common.Paging;
import com.cyzest.findbooks.model.BookMarkResult;
import com.cyzest.findbooks.service.BookMarkService;
import com.cyzest.findbooks.searcher.OpenApiType;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.beans.PropertyEditor;

@Slf4j
@Controller
@AllArgsConstructor
public class BookMarkController {

    private final PropertyEditor openApiTypePropertyEditor = new EnumCodePropertyEditor<>(OpenApiType.class);

    private BookMarkService bookMarkService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(OpenApiType.class, openApiTypePropertyEditor);
    }

    @PostMapping("/bookmark")
    @ResponseBody public String saveBookMark(
            @RequestParam OpenApiType openApiType, @RequestParam String isbn, Authentication authentication) throws Exception {

        bookMarkService.saveBookMark(authentication.getName(), openApiType, isbn);

        return "success";
    }

    @DeleteMapping("/bookmark/{id}")
    @ResponseBody public String deleteBookMark(@PathVariable long id, Authentication authentication) {

        bookMarkService.deleteBookMark(authentication.getName(), id);

        return "success";
    }

    @GetMapping("/bookmark")
    public String getBookMarks(
            Authentication authentication, Model model,
            @PageableDefault(size = 9, page = 1, sort = "regDate", direction = Sort.Direction.DESC) Pageable pageable) {

        BookMarkResult bookMarkResult = bookMarkService.getBookMarksByUserId(authentication.getName(), pageable);

        model.addAttribute("paging", new Paging(pageable.getPageNumber(), pageable.getPageSize(), bookMarkResult.getTotalCount()));
        model.addAttribute("bookMarkInfos", bookMarkResult.getBookMarkInfos());

        return "bookmark";
    }

}
