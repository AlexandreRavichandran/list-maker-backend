package com.medialistmaker.music.repository;

import com.medialistmaker.music.domain.Music;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MusicRepository extends JpaRepository<Music, Long> {

    List<Music> getByType(Integer type);

    @Query("SELECT m FROM Music m WHERE m.id IN(:musicIds)")
    List<Music> getByIds(@Param("musicIds") List<Long> musicIds);

    Music getByApiCodeAndType(String apiCode, Integer type);
}
