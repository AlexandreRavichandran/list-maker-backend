package com.medialistmaker.list.service.musiclistitem;

import com.medialistmaker.list.domain.MusicListItem;
import com.medialistmaker.list.exception.badrequestexception.CustomBadRequestException;
import com.medialistmaker.list.exception.entityduplicationexception.CustomEntityDuplicationException;
import com.medialistmaker.list.exception.notfoundexception.CustomNotFoundException;
import com.medialistmaker.list.repository.MusicListItemRepository;
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
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
class MusicListItemServiceImplTest {

    @Mock
    MusicListItemRepository musicListItemRepository;

    @Mock
    CustomEntityValidator<MusicListItem> musicListEntityValidator;

    @InjectMocks
    MusicListItemServiceImpl musicListService;

    @Test
    void givenAppUserIdWhenGetByAppUserIdShouldReturnRelatedMusicListItemList() {

        MusicListItem firstMusicListItem = MusicListItem
                .builder()
                .musicId(1L)
                .appUserId(1L)
                .addedAt(new Date())
                .sortingOrder(1)
                .build();

        MusicListItem secondMusicListItem = MusicListItem
                .builder()
                .musicId(2L)
                .appUserId(1L)
                .addedAt(new Date())
                .sortingOrder(2)
                .build();

        MusicListItem thirdMusicListItem = MusicListItem
                .builder()
                .musicId(3L)
                .appUserId(1L)
                .addedAt(new Date())
                .sortingOrder(3)
                .build();

        List<MusicListItem> musicListItemList = List.of(firstMusicListItem, secondMusicListItem, thirdMusicListItem);

        Mockito.when(this.musicListItemRepository.getByAppUserIdOrderBySortingOrderAsc(anyLong())).thenReturn(musicListItemList);

        List<MusicListItem> testGetByAppUserId = this.musicListService.getByAppUserId(1L);

        Mockito.verify(this.musicListItemRepository).getByAppUserIdOrderBySortingOrderAsc(anyLong());
        assertEquals(3, testGetByAppUserId.size());
        assertTrue(testGetByAppUserId.containsAll(musicListItemList));
    }

    @Test
    void givenMusicListItemWhenAddMusicListItemShouldSaveAndReturnMusicListItem()
            throws CustomBadRequestException, CustomEntityDuplicationException {

        MusicListItem musicListItem = MusicListItem
                .builder()
                .musicId(1L)
                .appUserId(1L)
                .addedAt(new Date())
                .sortingOrder(1)
                .build();

        Mockito.when(this.musicListEntityValidator.validateEntity(musicListItem)).thenReturn(emptyList());
        Mockito.when(this.musicListItemRepository.getByAppUserIdAndMusicId(anyLong(), anyLong())).thenReturn(null);
        Mockito.when(this.musicListItemRepository.save(musicListItem)).thenReturn(musicListItem);

        MusicListItem testAddMusicListItem = this.musicListService.add(musicListItem);

        Mockito.verify(this.musicListEntityValidator).validateEntity(musicListItem);
        Mockito.verify(this.musicListItemRepository).getByAppUserIdAndMusicId(anyLong(), anyLong());
        Mockito.verify(this.musicListItemRepository).save(musicListItem);

        assertEquals(testAddMusicListItem, musicListItem);
    }

    @Test
    void givenInvalidMusicListItemWhenAddMusicListItemShouldThrowBadRequestException() {

        MusicListItem musicListItem = MusicListItem
                .builder()
                .musicId(1L)
                .appUserId(1L)
                .addedAt(new Date())
                .sortingOrder(1)
                .build();

        List<String> errorList = List.of("Error 1", "Error 2");
        Mockito.when(this.musicListEntityValidator.validateEntity(musicListItem)).thenReturn(errorList);

        assertThrows(CustomBadRequestException.class, () -> this.musicListService.add(musicListItem));

        Mockito.verify(this.musicListEntityValidator).validateEntity(musicListItem);

    }

    @Test
    void givenMusicListItemWithAlreadyExistingMusicIdShouldThrowEntityDuplicationException() {

        MusicListItem musicListItem = MusicListItem
                .builder()
                .musicId(1L)
                .appUserId(1L)
                .addedAt(new Date())
                .sortingOrder(1)
                .build();

        Mockito.when(this.musicListEntityValidator.validateEntity(musicListItem)).thenReturn(emptyList());
        Mockito.when(this.musicListItemRepository
                        .getByAppUserIdAndMusicId(anyLong(), anyLong()))
                .thenReturn(musicListItem);

        assertThrows(CustomEntityDuplicationException.class, () -> this.musicListService.add(musicListItem));

        Mockito.verify(this.musicListEntityValidator).validateEntity(musicListItem);
        Mockito.verify(this.musicListItemRepository).getByAppUserIdAndMusicId(anyLong(), anyLong());
    }

    @Test
    void givenIdWhenDeleteByIdShouldDeleteAndReturnRelatedMusicListItem() throws CustomNotFoundException {

        MusicListItem musicListItem = MusicListItem
                .builder()
                .musicId(1L)
                .appUserId(1L)
                .addedAt(new Date())
                .sortingOrder(1)
                .build();

        Mockito.when(this.musicListItemRepository.getReferenceById(anyLong())).thenReturn(musicListItem);

        MusicListItem testDeleteById = this.musicListService.deleteById(1L);

        Mockito.verify(this.musicListItemRepository).getReferenceById(anyLong());
        assertEquals(musicListItem, testDeleteById);
    }

    @Test
    void givenInvalidIdWhenDeleteByIdShouldThrowNotFoundException() {

        Mockito.when(this.musicListItemRepository.getReferenceById(anyLong())).thenReturn(null);

        assertThrows(CustomNotFoundException.class, () -> this.musicListService.deleteById(1L));

        Mockito.verify(this.musicListItemRepository).getReferenceById(anyLong());
    }


    @Test
    void givenListItemIdAndAppUserIdAndSortingNumberWhenChangeSortingOrderShouldChangeSortingOrderAndSaveAndReturnBothEditedMovieListItem()
            throws CustomBadRequestException, CustomNotFoundException {

        MusicListItem musicListItemToEdit = MusicListItem
                .builder()
                .id(1L)
                .musicId(1L)
                .appUserId(1L)
                .addedAt(new Date())
                .sortingOrder(1)
                .build();

        MusicListItem musicListItemWithNewSortingOrderEdited = MusicListItem
                .builder()
                .id(1L)
                .musicId(1L)
                .appUserId(1L)
                .addedAt(new Date())
                .sortingOrder(2)
                .build();

        MusicListItem musicListItemWithSameNewSortingOrder = MusicListItem
                .builder()
                .id(2L)
                .musicId(2L)
                .appUserId(1L)
                .addedAt(new Date())
                .sortingOrder(2)
                .build();

        MusicListItem musicListItemWithSameNewSortingOrderEdited = MusicListItem
                .builder()
                .id(2L)
                .musicId(2L)
                .appUserId(1L)
                .addedAt(new Date())
                .sortingOrder(1)
                .build();

        Mockito
                .when(this.musicListItemRepository.getByAppUserIdOrderBySortingOrderAsc(anyLong()))
                .thenReturn(List.of(musicListItemToEdit, musicListItemWithSameNewSortingOrder));

        Mockito.when(this.musicListItemRepository.getReferenceById(anyLong())).thenReturn(musicListItemToEdit);
        Mockito
                .when(this.musicListItemRepository.getByAppUserIdAndSortingOrder(anyLong(), anyInt()))
                .thenReturn(musicListItemWithSameNewSortingOrder);

        List<MusicListItem> testChangeSortingNumber =
                this.musicListService.changeSortingOrder(1L, 2);

        Mockito
                .verify(this.musicListItemRepository)
                .saveAll(List.of(musicListItemWithNewSortingOrderEdited, musicListItemWithSameNewSortingOrderEdited));
        Mockito.verify(this.musicListItemRepository).getReferenceById(anyLong());
        Mockito.verify(this.musicListItemRepository).getByAppUserIdAndSortingOrder(anyLong(), anyInt());


        assertFalse(testChangeSortingNumber.isEmpty());

        assertEquals(musicListItemWithSameNewSortingOrder.getSortingOrder(), testChangeSortingNumber.get(1).getSortingOrder());
        assertEquals(musicListItemToEdit.getSortingOrder(), testChangeSortingNumber.get(0).getSortingOrder());

        assertNotEquals(testChangeSortingNumber.get(0).getSortingOrder(), testChangeSortingNumber.get(1).getSortingOrder());
    }

    @Test
    void givenMovieListItemIdAndAppUserIdAndInvalidSortingNumberWhenChangeSortingOrderShouldThrowBadRequestException() {

        MusicListItem musicListItem = MusicListItem
                .builder()
                .musicId(1L)
                .appUserId(1L)
                .addedAt(new Date())
                .sortingOrder(1)
                .build();

        Mockito.when(this.musicListItemRepository.getReferenceById(anyLong())).thenReturn(musicListItem);

        assertThrows(
                CustomBadRequestException.class,
                () -> this.musicListService.changeSortingOrder(1L, 0)
        );

        assertThrows(
                CustomBadRequestException.class,
                () -> this.musicListService.changeSortingOrder(1L, -1)
        );

        assertThrows(
                CustomBadRequestException.class,
                () -> this.musicListService.changeSortingOrder(1L, 6)
        );

    }

    @Test
    void givenInvalidMovieListItemAndAppUserIdAndSortingNumberWhenChangeSortingOrderShouldThrowNotFoundException() {

        Mockito.when(this.musicListItemRepository.getReferenceById(anyLong())).thenReturn(null);

        assertThrows(
                CustomNotFoundException.class,
                () -> this.musicListService.changeSortingOrder(1L, 2)
        );
    }
}