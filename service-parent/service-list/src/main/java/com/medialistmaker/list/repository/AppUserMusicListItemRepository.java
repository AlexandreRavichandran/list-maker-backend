package com.medialistmaker.list.repository;

import com.medialistmaker.list.domain.AppUserMusicListItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppUserMusicListItemRepository extends JpaRepository<AppUserMusicListItem, Long> {

    List<AppUserMusicListItem> getByAppUserIdOrderBySortingOrderAsc(Long appUserId);

    AppUserMusicListItem getByAppUserIdAndMusicId(Long appUserId, Long musicId);
}
