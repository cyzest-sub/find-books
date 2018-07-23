package com.cyzest.findbooks.searcher.naver;

import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement(name = "rss")
public class NaverBookSearchResult {

    private NaverBookChannel channel;

}
