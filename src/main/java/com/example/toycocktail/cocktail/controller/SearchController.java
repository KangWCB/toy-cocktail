package com.example.toycocktail.cocktail.controller;

import com.example.toycocktail.cocktail.dto.SearchCond;
import com.example.toycocktail.cocktail.dto.SearchResponse;
import com.example.toycocktail.cocktail.service.SearchService;
import com.example.toycocktail.common.config.security.CurrentUser;
import com.example.toycocktail.member.model.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/search")
public class SearchController {
    private final SearchService searchService;

    @GetMapping
    public Page<SearchResponse> search(@RequestBody SearchCond searchCond, @CurrentUser Member member,
                                       Pageable pageable){
        Page<SearchResponse> result;
        if (member == null) {
            result = searchService.search(searchCond, pageable);
        }else{
            result = searchService.search(searchCond, pageable,member.getId());
        }
        return result;
    }
}
