package com.foco.foco_backend.domain.report.service;

import com.foco.foco_backend.domain.report.dto.request.ReportRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportCommentService {

    private final ChatClient chatClient;

    public String generateComment(ReportRequestDTO.ReportData data) {
        log.info("[ ReportCommentService ] Generating comment for report");
        String promptText = buildPrompt(data);

        // Prompt 객체로 감싸기
        Prompt prompt = new Prompt(List.of(new UserMessage(promptText)));

        ChatResponse response = chatClient.call(prompt);
        String result = response.getResult().getOutput().getContent();
        log.info("[ ReportCommentService ] OpenAI result: " + result);
        return result;
    }

    private String buildPrompt(ReportRequestDTO.ReportData data) {
        return String.format("""
            당신은 전문 학습 코치입니다. 아래는 한 사용자의 학습 데이터입니다. 이 데이터를 바탕으로 아래 세 항목에 맞춰 결과를 JSON 형식으로 작성해주세요. \s
            말투는 따뜻하고 분석적인 톤으로, 존댓말을 사용하며 너무 딱딱하지 않게 작성해주세요. \s
            각 항목은 아래 형식과 설명을 따라 주세요:
            
            1. "학습 유형": 다음 중 하나로만 분류해 주세요. \s
            - 🐿️ 몰입형 (다람쥐): 시작은 느리지만 한번 몰입하면 깊게 집중하는 타입 \s
            - 🐢 루틴형 (거북이): 일정한 계획과 루틴을 기반으로 안정적으로 공부하는 타입 \s
            - 🐱 감성형 (고양이): 분위기나 기분에 따라 집중력이 출렁이는 타입 \s
            - 🐤 반짝형 (병아리): 짧고 순간적으로 집중한 뒤 자주 쉬며 리듬을 유지하는 타입 \s
            
            2. "특징 설명": 위 유형을 선택한 이유를 2~3줄로 설명해주세요. 숫자 기반 판단, 집중 패턴 등을 근거로 자연스럽게 작성해 주세요.
            
            3. "한줄 총평 및 추천 전략": 아래의 형식과 톤을 참고하여 하나의 문장 흐름으로 이어지는 2~3줄짜리 조언을 작성해 주세요. \s
            - 존댓말 + 따뜻한 말투 \s
            - 평가 + 조언을 함께 \s
            - 적절한 이모지 2~3개 포함 \s
            - 시간은 '몇 분'처럼 자연스럽게 바꿔 표현해도 좋음 \s
            - 줄글로, 불릿 없이
            
            사용자 학습 데이터:
            - 평균 집중 점수: %.1f점
            - 누적 공부 시간: %d초
            - 누적 집중 시간: %d초
            - 누적 쉬는 시간: %d초
            - 누적 딴짓 시간: %d초
            - 최장 집중 시간: %d초
            
            응답은 아래 형식처럼 JSON 객체로 작성해주세요:
            
            {
              "학습 유형": "...",
              "특징 설명": "...",
              "한줄 총평 및 추천 전략": "..."
            }
            """,
                data.avgFocusScore(),
                data.totalSessionSeconds(),
                data.totalFocusSeconds(),
                data.totalBreakSeconds(),
                data.totalDistractionSeconds(),
                data.longestFocusSeconds()
        );
    }
}
