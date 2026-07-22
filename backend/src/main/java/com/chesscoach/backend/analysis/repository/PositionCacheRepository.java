package com.chesscoach.backend.analysis.repository;

import com.chesscoach.backend.analysis.entity.PositionCache;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PositionCacheRepository extends JpaRepository<PositionCache,String> {
    Optional<PositionCache> findByFen(String fen);
}
