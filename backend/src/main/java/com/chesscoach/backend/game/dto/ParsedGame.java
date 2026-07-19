package com.chesscoach.backend.game.dto;

import java.time.LocalDate;
import java.util.List;

//once this record is created for a particular pgn, it will be immutable as records are immutable classes
//no setters make sure the data won't be changed
public record ParsedGame(
        String whitePlayer,
        String blackPlayer,
        String event,
        LocalDate gameDate,
        String result,
        List<String> moves,
        List<String> fens
){}
