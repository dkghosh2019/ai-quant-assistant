package com.lancy.aichat.dto;

public record TradeAnalysis(
        String sentiment,
        String setupQuality,
        Double confidenceScore,
        String riskAssessment,
        String improvementSuggestion
) {}
