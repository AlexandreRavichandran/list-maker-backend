package com.medialistmaker.list.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Builder
@AllArgsConstructor
public class AppUserMusicListItem {

    public AppUserMusicListItem() {
        //Private constructor
    }

    @Id
    @SequenceGenerator(name = "music_list_id_sequence", sequenceName = "music_list_id_sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "music_list_id_sequence")
    private Long id;

    @NotNull(message = "Music id is mandatory")
    private Long musicId;

    @NotNull(message = "User id is mandatory")
    private Long appUserId;

    @NotNull(message = "Sorting order is mandatory")
    private Integer sortingOrder;

    private Date addedAt;
}
