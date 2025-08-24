package com.quizapp.controller.dto;

import lombok.Data;

@Data
public class CreateQuizRequest {
    private String title;
    private String description;
    private Integer timePerQuestion;
}
