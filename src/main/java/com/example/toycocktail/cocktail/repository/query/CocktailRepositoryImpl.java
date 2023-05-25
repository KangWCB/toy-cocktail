package com.example.toycocktail.cocktail.repository.query;

import com.example.toycocktail.cocktail.dto.SearchCond;
import com.example.toycocktail.cocktail.model.Alcoholic;
import com.example.toycocktail.cocktail.model.Cocktail;
import com.example.toycocktail.cocktail.model.QCocktail;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class CocktailRepositoryImpl implements CocktailRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    QCocktail qCocktail = QCocktail.cocktail;

    @Override
    public Page<Cocktail> findBySearchCond(SearchCond searchCond, Pageable pageable) {
        List<Cocktail> result = queryFactory.select(qCocktail)
                .from(qCocktail)
                .where(containName(searchCond.getName()),
                        isAlcoholic(searchCond.isAlcoholic()))
                .orderBy(qCocktail.views.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        return new PageImpl<>(result,pageable, result.size());
    }

    private BooleanExpression containName(String name){
        if(StringUtils.isEmpty(name))
            return null;
        return qCocktail.name.contains(name);
    }

    private BooleanBuilder isAlcoholic(boolean isAlcoholic) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (isAlcoholic == true)
            booleanBuilder.or(qCocktail.alcoholic.eq(Alcoholic.BASIC));
        else
            booleanBuilder.or(qCocktail.alcoholic.eq(Alcoholic.NON));
        booleanBuilder.or(qCocktail.alcoholic.eq(Alcoholic.OPTIONAL));
        return booleanBuilder;
    }

}
