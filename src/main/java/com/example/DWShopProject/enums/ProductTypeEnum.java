package com.example.DWShopProject.enums;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum ProductTypeEnum {


    // 소분류 (상의)
    HOODIES(1L, "후드티셔츠", ParentTypeEnum.TOPS),
    SHORT_SLEEVE_TSHIRTS(2L, "반소매티셔츠", ParentTypeEnum.TOPS),
    LONG_SLEEVE_TSHIRTS(3L, "긴소매티셔츠", ParentTypeEnum.TOPS),
    KNIT_SWEATERS(4L, "니트/스웨터", ParentTypeEnum.TOPS),
    COLLAR_TSHIRTS(5L, "카라티셔츠", ParentTypeEnum.TOPS),
    SWEATSHIRTS(6L, "맨투맨", ParentTypeEnum.TOPS),
    SHIRTS_BLOUSES(7L, "셔츠/블라우스", ParentTypeEnum.TOPS),
    SPORTSWEAR_TOPS(8L, "스포츠웨어", ParentTypeEnum.TOPS),

    // 소분류 (하의)
    COTTON_PANTS(9L, "코튼팬츠", ParentTypeEnum.BOTTOMS),
    DENIM_PANTS(10L, "데님팬츠", ParentTypeEnum.BOTTOMS),
    JOGGER_PANTS(11L, "조거팬츠", ParentTypeEnum.BOTTOMS),
    SUIT_SLACKS(12L, "슈트/슬랙스", ParentTypeEnum.BOTTOMS),
    JUMPSUITS(13L, "점프슈트", ParentTypeEnum.BOTTOMS),
    LEGGINGS(14L, "레깅스", ParentTypeEnum.BOTTOMS),
    SKIRTS(15L, "치마", ParentTypeEnum.BOTTOMS),
    DRESSES(16L, "원피스", ParentTypeEnum.BOTTOMS),
    SPORTSWEAR_BOTTOMS(17L, "스포츠웨어", ParentTypeEnum.BOTTOMS),

    // 소분류 (아우터)
    MOUSTANGS(18L, "무스탕", ParentTypeEnum.OUTERWEAR),
    SUITS(19L, "슈트", ParentTypeEnum.OUTERWEAR),
    ANORAK_JACKETS(20L, "아노락재킷", ParentTypeEnum.OUTERWEAR),
    SHORT_PADDING(21L, "숏패딩", ParentTypeEnum.OUTERWEAR),
    LONG_PADDING(22L, "롱패딩", ParentTypeEnum.OUTERWEAR),
    CARDIGANS(23L, "가디건", ParentTypeEnum.OUTERWEAR),
    COATS(24L, "코트", ParentTypeEnum.OUTERWEAR),

    // 소분류 (신발)
    DRESS_SHOES(25L, "구두", ParentTypeEnum.SHOES),
    CROCS(26L, "크록스", ParentTypeEnum.SHOES),
    RUNNING_SHOES(27L, "런닝화", ParentTypeEnum.SHOES),
    SLIPPERS(28L, "슬리퍼", ParentTypeEnum.SHOES),
    FLATS(29L, "단화", ParentTypeEnum.SHOES),
    BOOTS(30L, "부츠", ParentTypeEnum.SHOES),
    SPORTS_SHOES(31L, "스포츠신발", ParentTypeEnum.SHOES),

    // 소분류 (액세서리)
    HATS(32L, "모자", ParentTypeEnum.ACCESSORIES),
    BAGS(33L, "가방", ParentTypeEnum.ACCESSORIES),
    RINGS_BRACELETS(34L, "반지/팔찌", ParentTypeEnum.ACCESSORIES),
    NECKLACES(35L, "목걸이", ParentTypeEnum.ACCESSORIES),
    EARRINGS(36L, "귀걸이", ParentTypeEnum.ACCESSORIES),
    SUNGLASSES_GLASSES(37L, "선글라스/안경", ParentTypeEnum.ACCESSORIES),
    WATCHES(38L, "시계", ParentTypeEnum.ACCESSORIES);

    private final Long id;
    private final String displayName;
    private final ParentTypeEnum parentTypeEnum;

    ProductTypeEnum(Long id, String displayName, ParentTypeEnum parentTypeEnum) {
        this.id = id;
        this.displayName = displayName;
        this.parentTypeEnum = parentTypeEnum;
    }

    public Long getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public ParentTypeEnum getParentTypeEnum() {
        return parentTypeEnum;
    }

    public static ProductTypeEnum getById(Long id) {
        for (ProductTypeEnum type : values()) {
            if (type.getId().equals(id)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid id: " + id);
    }
}
