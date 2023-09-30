package com.medialistmaker.list.repository;

import com.medialistmaker.list.domain.AppUserMusicListItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserMusicListItemRepository extends JpaRepository<AppUserMusicListItem, Long>, AppUserListItemBaseRepository<AppUserMusicListItem> {

    AppUserMusicListItem getByAppUserIdAndMusicId(Long appUserId, Long musicId);
}
