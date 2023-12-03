package com.medialistmaker.list.repository;

import com.medialistmaker.list.domain.MusicListItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MusicListItemRepository extends JpaRepository<MusicListItem, Long>, ListItemBaseRepository<MusicListItem> {

    MusicListItem getByAppUserIdAndMusicId(Long appUserId, Long musicId);

    List<MusicListItem> getByMusicId(Long id);
}
