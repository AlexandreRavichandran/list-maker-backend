package com.medialistmaker.list.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class MusicListItem extends ListItem {

    public MusicListItem() {
        //Private constructor
    }

    @Builder
    public MusicListItem(Long appUserId, Integer sortingOrder, Date addedAt, Long id, Long musicId) {
        super(appUserId, sortingOrder, addedAt);
        this.id = id;
        this.musicId = musicId;
    }

    @Id
    @SequenceGenerator(name = "music_list_id_sequence", sequenceName = "music_list_id_sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "music_list_id_sequence")
    private Long id;

    @NotNull(message = "Music id is mandatory")
    private Long musicId;

}
