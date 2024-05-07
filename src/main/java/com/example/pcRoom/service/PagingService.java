package com.example.pcRoom.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.IntStream;

@Service
public class PagingService {
    private static final int BAR_LENGTH = 5;

    public List<Integer> getPaginationBarNumbers(int currentPageNumber, int totalPages) {
        int startNumber = Math.max(currentPageNumber - (BAR_LENGTH / 2), 0);     // 맨 처음 표시 페이지 값 // 큰 값 찾기
        int endNumber = Math.min(startNumber + BAR_LENGTH, totalPages);     // 맨 끝에 페이지 값 // total 페이지 값을 넘어가면 작은 값을 표시
        return IntStream.range(startNumber, endNumber).boxed().toList();
    }
}
