package com.medialistmaker.list.service.musiclistitem;

import com.medialistmaker.list.connector.music.MusicConnectorProxy;
import com.medialistmaker.list.domain.MusicListItem;
import com.medialistmaker.list.dto.music.MusicAddDTO;
import com.medialistmaker.list.dto.music.MusicDTO;
import com.medialistmaker.list.dto.music.MusicListItemAddDTO;
import com.medialistmaker.list.exception.badrequestexception.CustomBadRequestException;
import com.medialistmaker.list.exception.entityduplicationexception.CustomEntityDuplicationException;
import com.medialistmaker.list.exception.notfoundexception.CustomNotFoundException;
import com.medialistmaker.list.exception.servicenotavailableexception.ServiceNotAvailableException;
import com.medialistmaker.list.repository.MusicListItemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
class MusicListItemServiceImplTest {

    @Mock
    MusicListItemRepository musicListItemRepository;

    @Mock
    MusicConnectorProxy musicConnectorProxy;

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
    void givenAppUserIdWhenGetLatestAddedByAppUserIdShouldReturnRelatedMusicListItemList() {

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

        Mockito.when(this.musicListItemRepository.getTop3ByAppUserIdOrderByAddedAtDesc(anyLong())).thenReturn(musicListItemList);

        List<MusicListItem> testGetByAppUserId = this.musicListService.getLatestAddedByAppUserId(1L);

        Mockito.verify(this.musicListItemRepository).getTop3ByAppUserIdOrderByAddedAtDesc(anyLong());
        assertEquals(3, testGetByAppUserId.size());
        assertTrue(testGetByAppUserId.containsAll(musicListItemList));
    }

    @Test
    void givenMusicListItemAddWhenAddMusicListItemShouldSaveAndReturnMusicListItem()
            throws CustomBadRequestException, CustomNotFoundException,
            CustomEntityDuplicationException, ServiceNotAvailableException {

        MusicListItemAddDTO listItemAddDTO = new MusicListItemAddDTO();
        listItemAddDTO.setApiCode("XXXX");
        listItemAddDTO.setType(1);

        MusicDTO musicDTO = new MusicDTO();
        musicDTO.setId(1L);

        MusicListItem musicListItem = new MusicListItem();
        musicListItem.setMusicId(musicDTO.getId());
        musicListItem.setAppUserId(1L);

        MusicAddDTO musicAddDTO = new MusicAddDTO();
        musicAddDTO.setType(1);
        musicAddDTO.setApiCode("XXXX");

        Mockito.when(this.musicConnectorProxy.getMusicByApiCodeAndType(anyString(), anyInt())).thenReturn(musicDTO);
        Mockito.when(this.musicListItemRepository.getByAppUserIdAndMusicId(anyLong(), anyLong())).thenReturn(null);
        Mockito.when(this.musicConnectorProxy.saveByApiCode(musicAddDTO)).thenReturn(musicDTO);
        Mockito.when(this.musicListItemRepository.save(any())).thenReturn(musicListItem);

        MusicListItem testAddMusicListItem = this.musicListService.add(listItemAddDTO);

        Mockito.verify(this.musicConnectorProxy).getMusicByApiCodeAndType(anyString(), anyInt());
        Mockito.verify(this.musicListItemRepository).getByAppUserIdAndMusicId(anyLong(), anyLong());
        Mockito.verify(this.musicConnectorProxy).saveByApiCode(musicAddDTO);
        Mockito.verify(this.musicListItemRepository).save(any());

        assertEquals(musicDTO.getId(),testAddMusicListItem.getMusicId());
    }

    @Test
    void givenMusicListItemAddWithAlreadyExistingMusicIdWhenAddMusicListItemShouldThrowEntityDuplicationException() throws Exception {

        MusicListItemAddDTO listItemAddDTO = new MusicListItemAddDTO();
        listItemAddDTO.setApiCode("XXXX");
        listItemAddDTO.setType(1);

        MusicDTO musicDTO = new MusicDTO();
        musicDTO.setId(1L);

        MusicListItem musicListItem = new MusicListItem();
        musicListItem.setMusicId(musicDTO.getId());
        musicListItem.setAppUserId(1L);

        Mockito.when(this.musicConnectorProxy.getMusicByApiCodeAndType(anyString(), anyInt())).thenReturn(musicDTO);
        Mockito.when(this.musicListItemRepository.getByAppUserIdAndMusicId(anyLong(), anyLong())).thenReturn(musicListItem);

        assertThrows(CustomEntityDuplicationException.class, () -> this.musicListService.add(listItemAddDTO));

        Mockito.verify(this.musicConnectorProxy).getMusicByApiCodeAndType(anyString(), anyInt());
        Mockito.verify(this.musicListItemRepository).getByAppUserIdAndMusicId(anyLong(), anyLong());


    }

    @Test
    void givenMusicListItemAddWhenAddMusicListItemAndServiceNotAvailableShouldThrowServiceNotAvailableException() throws Exception {

        MusicListItemAddDTO listItemAddDTO = new MusicListItemAddDTO();
        listItemAddDTO.setApiCode("XXXX");
        listItemAddDTO.setType(2);

        MusicDTO musicDTO = new MusicDTO();
        musicDTO.setId(1L);

        MusicListItem musicListItem = new MusicListItem();
        musicListItem.setMusicId(musicDTO.getId());
        musicListItem.setAppUserId(1L);

        MusicAddDTO musicAddDTO = new MusicAddDTO();
        musicAddDTO.setType(2);
        musicAddDTO.setApiCode("XXXX");

        Mockito.when(this.musicConnectorProxy.getMusicByApiCodeAndType(anyString(), anyInt())).thenReturn(musicDTO);
        Mockito.when(this.musicListItemRepository.getByAppUserIdAndMusicId(anyLong(), anyLong())).thenReturn(null);
        Mockito.when(this.musicConnectorProxy.saveByApiCode(musicAddDTO)).thenThrow(ServiceNotAvailableException.class);

        assertThrows(ServiceNotAvailableException.class, () -> this.musicListService.add(listItemAddDTO));

        Mockito.verify(this.musicConnectorProxy).getMusicByApiCodeAndType(anyString(), anyInt());
        Mockito.verify(this.musicListItemRepository).getByAppUserIdAndMusicId(anyLong(), anyLong());
        Mockito.verify(this.musicConnectorProxy).saveByApiCode(musicAddDTO);
    }


    @Test
    void givenIdWhenDeleteByIdShouldDeleteAndReturnRelatedMusicListItem() throws Exception {

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
    void givenAppUserIdShouldGetAllMovieItemAndResetSortingOrderAndSave() {

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

        List<MusicListItem> musicListItems = List.of(firstMusicListItem, secondMusicListItem, thirdMusicListItem);

        Mockito.when(this.musicListItemRepository.getByAppUserIdOrderBySortingOrderAsc(anyLong())).thenReturn(musicListItems);

        this.musicListService.updateOrder(1L);

        Mockito.verify(this.musicListItemRepository).saveAll(musicListItems);

    }

    @Test
    void givenInvalidIdWhenDeleteByIdShouldThrowNotFoundException() {

        Mockito.when(this.musicListItemRepository.getReferenceById(anyLong())).thenReturn(null);

        assertThrows(CustomNotFoundException.class, () -> this.musicListService.deleteById(1L));

        Mockito.verify(this.musicListItemRepository).getReferenceById(anyLong());
    }

    @Test
    void givenAppUserIdAndTypeAndExistingMusicApiCodeWhenIsAlreadyInAppUserListShouldReturnTrue() throws Exception {

        MusicDTO musicDTO = new MusicDTO();
        musicDTO.setId(1L);

        MusicListItem musicListItem = new MusicListItem();
        musicListItem.setId(2L);

        Mockito.when(this.musicConnectorProxy.getMusicByApiCodeAndType(anyString(), anyInt())).thenReturn(musicDTO);
        Mockito.when(this.musicListItemRepository.getByAppUserIdAndMusicId(anyLong(), anyLong())).thenReturn(musicListItem);

        Boolean testIsAlreadyInAppUserList =
                this.musicListService.isMusicApiCodeAndTypeAlreadyInAppUserMovieList(1L, "test", 1);

        Mockito.verify(this.musicConnectorProxy).getMusicByApiCodeAndType(anyString(), anyInt());
        Mockito.verify(this.musicListItemRepository).getByAppUserIdAndMusicId(anyLong(), anyLong());
        assertEquals(Boolean.TRUE, testIsAlreadyInAppUserList);
    }

    @Test
    void givenAppUserIdAndTypeAndNonExistingMovieApiCodeWhenIsAlreadyInAppUserListShouldReturnTrue() throws Exception {

        MusicDTO musicDTO = new MusicDTO();
        musicDTO.setId(1L);

        Mockito
                .when(this.musicConnectorProxy.getMusicByApiCodeAndType(anyString(), anyInt()))
                .thenThrow(CustomNotFoundException.class);

        Boolean testIsAlreadyInAppUserList =
                this.musicListService.isMusicApiCodeAndTypeAlreadyInAppUserMovieList(1L, "test", 1);

        assertEquals(Boolean.FALSE, testIsAlreadyInAppUserList);
    }

    @Test
    void givenAppUserIdAndTypeAndNonExistingMovieApiCodeWhenIsAlreadyInAppUserListAndServiceNotAvailableShouldReturnTrue()
            throws Exception {

        Mockito
                .when(this.musicConnectorProxy.getMusicByApiCodeAndType(anyString(), anyInt()))
                .thenThrow(ServiceNotAvailableException.class);

        assertThrows(ServiceNotAvailableException.class,
                () -> this.musicListService.
                        isMusicApiCodeAndTypeAlreadyInAppUserMovieList(1L, "test", 1));

    }

}