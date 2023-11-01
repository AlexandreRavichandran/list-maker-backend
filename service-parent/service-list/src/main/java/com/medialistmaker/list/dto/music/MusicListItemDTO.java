package com.medialistmaker.list.dto.music;

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

    private String musicApiCode;

    private Long appUserId;

    private Integer sortingOrder;

    private Date addedAt;

}
