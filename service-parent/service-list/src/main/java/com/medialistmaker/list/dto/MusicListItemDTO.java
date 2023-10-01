package com.medialistmaker.list.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
public class MusicListItemDTO {

    public MusicListItemDTO() {
        //Private constructor
    }

    private Long id;

    private Long musicId;

    private Long appUserId;

    private Integer sortingOrder;

    private Date addedAt;

}
