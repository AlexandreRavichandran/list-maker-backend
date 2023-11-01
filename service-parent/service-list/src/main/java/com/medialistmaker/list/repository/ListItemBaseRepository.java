package com.medialistmaker.list.repository;

import com.medialistmaker.list.domain.ListItem;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ListItemBaseRepository<A extends ListItem> {

    List<A> getByAppUserIdOrderBySortingOrderAsc(Long appUserId);

    A getByAppUserIdAndSortingOrder(Long appUserId, Integer order);

    A getFirstByAppUserIdOrderBySortingOrderDesc(Long appUserId);
}
