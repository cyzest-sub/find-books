package com.cyzest.findbooks.dao;

import com.cyzest.findbooks.searcher.OpenApiType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookMarkRepository extends JpaRepository<BookMark, Long> {

    @Query("SELECT b FROM BookMark b WHERE b.user = ?1")
    Page<BookMark> findByUser(User user, Pageable pageable);

    @Query("SELECT b FROM BookMark b WHERE b.user = ?1 and b.openApiType = ?2 and b.isbn = ?3")
    List<BookMark> findByUserAndOpenApiTypeAndIsbn(User user, OpenApiType openApiType, String isbn);

    @Query("SELECT b FROM BookMark b WHERE b.user = ?1 and b.id = ?2")
    List<BookMark> findByUserAndId(User user, Long id);

}
