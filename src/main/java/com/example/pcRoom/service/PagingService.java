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

     private static final int BAR_LENGTH2 = 3; // < 1 2 3 >
     public List<Integer> pageNumbers(int currentPageNumber, int totalPages) {
         int startNumber = Math.max(currentPageNumber - (BAR_LENGTH2 / 2), 0); // 가장 앞에 올 번호
         int endNumber = Math.min(startNumber + BAR_LENGTH2, totalPages); // 마지막 번호
         return IntStream.range(startNumber,endNumber) // (시작부터, 끝까지)
                 .boxed() // 마지막 값은 빠짐
                 .toList();
     }
}
