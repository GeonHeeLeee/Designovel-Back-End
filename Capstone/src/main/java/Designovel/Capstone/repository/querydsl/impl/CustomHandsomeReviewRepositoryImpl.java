package Designovel.Capstone.repository.querydsl.impl;

import Designovel.Capstone.domain.HandsomeReviewDTO;
import Designovel.Capstone.domain.ReviewFilterDTO;
import Designovel.Capstone.repository.querydsl.CustomHandsomeReviewRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

import static Designovel.Capstone.entity.QHandsomeReview.handsomeReview;

@Slf4j
@RequiredArgsConstructor
public class CustomHandsomeReviewRepositoryImpl implements CustomHandsomeReviewRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public BooleanBuilder buildHandsomeReviewFilter(ReviewFilterDTO filterDTO) {
        BooleanBuilder builder = new BooleanBuilder();
        String productId = filterDTO.getProductId();
        Date startDate = filterDTO.getStartDate();

        builder.and(handsomeReview.productId.eq(productId));
        if (startDate != null) {
            builder.and(handsomeReview.writtenDate.goe(startDate));
        }
        return builder;
    }

    @Override
    public List<Tuple> findHandsomeReviewCountsByFilter(ReviewFilterDTO filterDTO) {
        BooleanBuilder builder = buildHandsomeReviewFilter(filterDTO);
        return jpaQueryFactory.select(
                        handsomeReview.rating,
                        handsomeReview.count())
                .from(handsomeReview)
                .where(builder)
                .groupBy(handsomeReview.rating)
                .fetch();

    }

    @Override
    public Page<HandsomeReviewDTO> findHandsomeReviewPageByFilter(ReviewFilterDTO filterDTO) {
        BooleanBuilder builder = buildHandsomeReviewFilter(filterDTO);
        Pageable pageable = PageRequest.of(filterDTO.getPage(), 10);
        if (filterDTO.getRate() != null) {
            builder.and(handsomeReview.rating.eq(filterDTO.getRate()));
        }
        List<HandsomeReviewDTO> handsomeReviewDTOList =
                jpaQueryFactory.select(Projections.constructor(HandsomeReviewDTO.class,
                                handsomeReview.reviewId,
                                handsomeReview.productId,
                                handsomeReview.orgReviewId,
                                handsomeReview.rating,
                                handsomeReview.writtenDate,
                                handsomeReview.userId,
                                handsomeReview.body,
                                handsomeReview.productColor,
                                handsomeReview.productSize,
                                handsomeReview.importSource,
                                handsomeReview.userHeight,
                                handsomeReview.userSize))
                        .from(handsomeReview)
                        .where(builder)
                        .orderBy(handsomeReview.writtenDate.desc())
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetch();

        long total = jpaQueryFactory
                .select(handsomeReview.count())
                .from(handsomeReview)
                .where(builder)
                .fetchOne();

        return new PageImpl<>(handsomeReviewDTOList, pageable, total);
    }
}
