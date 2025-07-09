package com.poco.poco_backend.domain.report.service.query;

import com.poco.poco_backend.domain.report.dto.response.ReportResponseDTO;
import com.poco.poco_backend.domain.report.service.command.ReportCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportCommentService implements ReportQueryService {

    private final ChatClient chatClient;

    public String generateComment(ReportResponseDTO.ReportData data) {
        String promptText = buildPrompt(data);

        // Prompt 객체로 감싸기
        Prompt prompt = new Prompt(List.of(new UserMessage(promptText)));

        ChatResponse response = chatClient.call(prompt);
        return response.getResult().getOutput().getContent();
    }

    private String buildPrompt(ReportResponseDTO.ReportData data) {
        return """
                다음은 학습 리포트 요약이야
                
                - 평균 집중 점수: %d점
                - 누적 집중 시간: %s
                - 쉬는 시간: %s
                - 딴짓 시간: %s
                - 최장 집중 시간: %s
                - 총 세션 수: %d개
                
                이 데이터를 바탕으로 한 줄 요약 코멘트를 작성해줘.
                문체는 친절하고 따뜻한 말투로 해줘. 너무 기계적인 말투는 피하고 자연스럽게 써줘.
                """.formatted(
                data.avgScore(),
                formatDuration(data.totalFocusTime()),
                formatDuration(data.breakTime()),
                formatDuration(data.distractionTime()),
                formatDuration(data.maxFocusTime()),
                data.sessionCount()
        );
    }

    private String formatDuration(Duration duration) {
        long minutes = duration.toMinutes();
        long hours = minutes / 60;
        long mins = minutes % 60;
        return (hours > 0 ? hours + "시간 " : "") + mins + "분";
    }


}
