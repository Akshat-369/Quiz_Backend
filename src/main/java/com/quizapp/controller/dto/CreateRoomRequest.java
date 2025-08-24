package com.quizapp.controller.dto;

import lombok.Data;

@Data
public class CreateRoomRequest {
    private Long quizId;
    private Integer maxParticipants;
}
