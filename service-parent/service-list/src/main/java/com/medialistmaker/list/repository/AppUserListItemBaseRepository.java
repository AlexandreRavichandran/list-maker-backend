package com.medialistmaker.list.repository;

import com.medialistmaker.list.domain.AppUserListItem;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppUserListItemBaseRepository<A extends AppUserListItem> {

    List<A> getByAppUserIdOrderBySortingOrderAsc(Long appUserId);

    A getByAppUserIdAndSortingOrder(Long appUserId, Integer order);
}
