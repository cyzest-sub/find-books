package com.cyzest.findbooks.searcher.naver;

import lombok.Data;

import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class NaverBookChannel {

    private String title;
    private String link;
    private String description;
    private Date lastBuildDate;
    private Integer total;
    private Integer start;
    private Integer display;

    @XmlElement(name = "item")
    private List<NaverBookInfo> items;

    public void setItems(NaverBookInfo naverBookInfo) {
        if (naverBookInfo != null) {
            if (items == null) {
                items = new ArrayList<>();
            }
            items.add(naverBookInfo);
        }
    }

}
