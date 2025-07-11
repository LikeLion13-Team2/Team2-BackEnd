package com.poco.poco_backend.domain.quote.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Slf4j
@Service
public class QuoteService {

    private final List<String> quotes = List.of(
            "오늘 걷지 않으면 내일은 뛰어야 한다.",
            "공부는 미래의 나를 위한 최고의 투자다.",
            "노력은 배신하지 않는다.",
            "할 수 있다고 믿는 순간, 절반은 이룬 것이다. – 시어도어 루스벨트",
            "천재는 1%의 영감과 99%의 노력이다. – 에디슨",
            "성공은 준비와 기회의 만남이다. – 세네카",
            "당신이 공부를 멈춘 날, 성장은 멈춘다.",
            "쉬운 길은 없다. 하지만 의미 있는 길은 있다.",
            "지금 흘리는 땀은 미래의 너를 웃게 할 것이다.",
            "남보다 더 노력하지 않고는 남보다 더 나은 결과를 바랄 수 없다.",
            "시작이 반이다. – 아리스토텔레스",
            "실패는 성공으로 가는 또 다른 방법일 뿐이다.",
            "꿈은 이루어지지 않는다. 이루게 만든다.",
            "내일의 성공은 오늘의 공부에 달려 있다.",
            "지금 포기하면 어제의 노력이 무의미해진다.",
            "계속 도전하면 언젠가는 길이 생긴다.",
            "진짜 실력은 조용히 쌓인다.",
            "어제보다 나은 내가 되자.",
            "공부는 재능보다 꾸준함이 이긴다.",
            "될 때까지 하면 된다.",
            "다른 사람이 자고 있을 때 공부하라. – 벤자민 프랭클린",
            "성공한 사람은 포기하지 않았던 사람이다.",
            "지식은 힘이다. – 프랜시스 베이컨",
            "인생은 시험의 연속이다. 공부는 그 준비다.",
            "고통 없이는 얻는 것도 없다. – 벤자민 프랭클린",
            "할 수 있는 이유를 찾고, 못할 이유를 버려라.",
            "성공은 결국 꾸준함에서 온다.",
            "1시간 먼저 시작하면 1년 앞서간다.",
            "공부는 하기 싫을 때 하는 것이 진짜 공부다.",
            "네가 책장을 넘길 때, 운명이 바뀐다."
    );

    public String getRandomQuote() {
        int index = new Random().nextInt(quotes.size());
        return quotes.get(index);
    }

}
