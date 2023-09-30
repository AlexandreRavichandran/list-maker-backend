package com.medialistmaker.list.service.appusermusiclist;

import com.medialistmaker.list.domain.AppUserMusicListItem;
import com.medialistmaker.list.exception.badrequestexception.CustomBadRequestException;
import com.medialistmaker.list.exception.entityduplicationexception.CustomEntityDuplicationException;
import com.medialistmaker.list.exception.notfoundexception.CustomNotFoundException;
import com.medialistmaker.list.repository.AppUserMusicListItemRepository;
import com.medialistmaker.list.utils.CustomEntityValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
class AppUserMusicListItemServiceImplTest {

    @Mock
    AppUserMusicListItemRepository appUserMusicListItemRepository;

    @Mock
    CustomEntityValidator<AppUserMusicListItem> musicListEntityValidator;

    @InjectMocks
    AppUserMusicListItemServiceImpl appUserMusicListService;

    @Test
    void givenAppUserIdWhenGetByAppUserIdShouldReturnRelatedMusicListItemList() {

        AppUserMusicListItem firstMusicListItem = AppUserMusicListItem
                .builder()
                .musicId(1L)
                .appUserId(1L)
                .addedAt(new Date())
                .sortingOrder(1)
                .build();

        AppUserMusicListItem secondMusicListItem = AppUserMusicListItem
                .builder()
                .musicId(2L)
                .appUserId(1L)
                .addedAt(new Date())
                .sortingOrder(2)
                .build();

        AppUserMusicListItem thirdMusicListItem = AppUserMusicListItem
                .builder()
                .musicId(3L)
                .appUserId(1L)
                .addedAt(new Date())
                .sortingOrder(3)
                .build();

        List<AppUserMusicListItem> musicListItemList = List.of(firstMusicListItem, secondMusicListItem, thirdMusicListItem);

        Mockito.when(this.appUserMusicListItemRepository.getByAppUserIdOrderBySortingOrderAsc(anyLong())).thenReturn(musicListItemList);

        List<AppUserMusicListItem> testGetByAppUserId = this.appUserMusicListService.getByAppUserId(1L);

        Mockito.verify(this.appUserMusicListItemRepository).getByAppUserIdOrderBySortingOrderAsc(anyLong());
        assertEquals(3, testGetByAppUserId.size());
        assertTrue(testGetByAppUserId.containsAll(musicListItemList));
    }

    @Test
    void givenMusicListItemWhenAddMusicListItemShouldSaveAndReturnMusicListItem()
            throws CustomBadRequestException, CustomEntityDuplicationException {

        AppUserMusicListItem musicListItem = AppUserMusicListItem
                .builder()
                .musicId(1L)
                .appUserId(1L)
                .addedAt(new Date())
                .sortingOrder(1)
                .build();

        Mockito.when(this.musicListEntityValidator.validateEntity(musicListItem)).thenReturn(emptyList());
        Mockito.when(this.appUserMusicListItemRepository.getByAppUserIdAndMusicId(anyLong(), anyLong())).thenReturn(null);
        Mockito.when(this.appUserMusicListItemRepository.save(musicListItem)).thenReturn(musicListItem);

        AppUserMusicListItem testAddMusicListItem = this.appUserMusicListService.add(musicListItem);

        Mockito.verify(this.musicListEntityValidator).validateEntity(musicListItem);
        Mockito.verify(this.appUserMusicListItemRepository).getByAppUserIdAndMusicId(anyLong(), anyLong());
        Mockito.verify(this.appUserMusicListItemRepository).save(musicListItem);

        assertEquals(testAddMusicListItem, musicListItem);
    }

    @Test
    void givenInvalidMusicListItemWhenAddMusicListItemShouldThrowBadRequestException() {

        AppUserMusicListItem musicListItem = AppUserMusicListItem
                .builder()
                .musicId(1L)
                .appUserId(1L)
                .addedAt(new Date())
                .sortingOrder(1)
                .build();

        List<String> errorList = List.of("Error 1", "Error 2");
        Mockito.when(this.musicListEntityValidator.validateEntity(musicListItem)).thenReturn(errorList);

        assertThrows(CustomBadRequestException.class, () -> this.appUserMusicListService.add(musicListItem));

        Mockito.verify(this.musicListEntityValidator).validateEntity(musicListItem);

    }

    @Test
    void givenMusicListItemWithAlreadyExistingMusicIdShouldThrowEntityDuplicationException() {

        AppUserMusicListItem musicListItem = AppUserMusicListItem
                .builder()
                .musicId(1L)
                .appUserId(1L)
                .addedAt(new Date())
                .sortingOrder(1)
                .build();

        Mockito.when(this.musicListEntityValidator.validateEntity(musicListItem)).thenReturn(emptyList());
        Mockito.when(this.appUserMusicListItemRepository
                        .getByAppUserIdAndMusicId(anyLong(), anyLong()))
                .thenReturn(musicListItem);

        assertThrows(CustomEntityDuplicationException.class, () -> this.appUserMusicListService.add(musicListItem));

        Mockito.verify(this.musicListEntityValidator).validateEntity(musicListItem);
        Mockito.verify(this.appUserMusicListItemRepository).getByAppUserIdAndMusicId(anyLong(), anyLong());
    }

    @Test
    void givenIdWhenDeleteByIdShouldDeleteAndReturnRelatedMusicListItem() throws CustomNotFoundException {

        AppUserMusicListItem musicListItem = AppUserMusicListItem
                .builder()
                .musicId(1L)
                .appUserId(1L)
                .addedAt(new Date())
                .sortingOrder(1)
                .build();

        Mockito.when(this.appUserMusicListItemRepository.getReferenceById(anyLong())).thenReturn(musicListItem);

        AppUserMusicListItem testDeleteById = this.appUserMusicListService.deleteById(1L);

        Mockito.verify(this.appUserMusicListItemRepository).getReferenceById(anyLong());
        assertEquals(musicListItem, testDeleteById);
    }

    @Test
    void givenInvalidIdWhenDeleteByIdShouldThrowNotFoundException() {

        Mockito.when(this.appUserMusicListItemRepository.getReferenceById(anyLong())).thenReturn(null);

        assertThrows(CustomNotFoundException.class, () -> this.appUserMusicListService.deleteById(1L));

        Mockito.verify(this.appUserMusicListItemRepository).getReferenceById(anyLong());
    }
}