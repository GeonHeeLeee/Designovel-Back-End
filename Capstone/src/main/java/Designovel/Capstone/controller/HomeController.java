package Designovel.Capstone.controller;

import Designovel.Capstone.domain.ProductFilterDTO;
import Designovel.Capstone.domain.TopBrandDTO;
import Designovel.Capstone.entity.ProductRanking;
import Designovel.Capstone.service.product.ProductRankingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "메인 화면", description = "메인 화면(통계) API")
@RequestMapping("/home")
public class HomeController {
    private final ProductRankingService productRankingService;

    @Operation(summary = "쇼핑몰 별 Top 10 브랜드 조회", description = "노출 지수 기준으로 Top 10 브랜드 반환",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Json 형식으로 반환 - key: top10BrandList",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TopBrandDTO.class)))
            })
    @GetMapping("/brand")
    public ResponseEntity<Map<String, List<TopBrandDTO>>> getTop10Brands(@ModelAttribute ProductFilterDTO filterDTO) {
        List<TopBrandDTO> top10BrandList = productRankingService.getTop10BrandsByMallType(filterDTO);
        return ResponseEntity.ok(Collections.singletonMap("top10BrandList", top10BrandList));
    }

    @Operation(summary = "쇼핑몰 별 가격대 상품 조회", description = "해당 쇼핑몰의 가격대별 상품 반환")
    @GetMapping("/price/{mallTypeId}")
    public ResponseEntity<Map<String, List<ProductRanking>>> getProductListByPriceRanges(@PathVariable("mallTypeId") String mallTypeId) {
        Map<String, List<ProductRanking>> productListByPriceRanges = productRankingService.getProductListByPriceRanges(mallTypeId);
        return ResponseEntity.ok(productListByPriceRanges);
    }

}
