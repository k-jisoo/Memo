package com.sparta.memo.dto;

import lombok.Getter;

@Getter
public class MemoRequestDto {
    private String username;
    private String contents;

    public MemoRequestDto(String username, String contents) {
        this.username = username;
        this.contents = contents;
    }

}