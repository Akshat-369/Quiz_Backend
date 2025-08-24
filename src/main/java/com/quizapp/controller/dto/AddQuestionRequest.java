package com.quizapp.controller.dto;

import lombok.Data;

@Data
public class AddQuestionRequest {
    private String questionText;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private char correctOption;
}
