package com.medialistmaker.list.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
@MappedSuperclass
public class AppUserListItem {

    public AppUserListItem() {
        //private constructor
    }

    @NotNull(message = "User id is mandatory")
    protected Long appUserId;

    @NotNull(message = "Sorting order is mandatory")
    protected Integer sortingOrder;

    protected Date addedAt;

}
