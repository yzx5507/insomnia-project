package com.insomnia.parseISOMsg.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * ISO-8583协议字段枚举定义
 * </p>
 *
 * @author xiaowei.yang@transsnet.com
 * @since 2021/12/17 11:23 上午
 */
@Getter
@AllArgsConstructor
public enum FieldEnum {


    FIELD_0(0, "",true),
    FIELD_1(1, "",false),
    FIELD_2(2, "",true),
    FIELD_3(3, "",true),
    FIELD_4(4, "",true),
    FIELD_5(5, "",false),
    FIELD_6(6, "",false),
    FIELD_7(7, "",true),
    FIELD_8(8, "",false),
    FIELD_9(9, "",false),
    FIELD_10(10, "",false),
    FIELD_11(11, "",true),
    FIELD_12(12, "",true),
    FIELD_13(13, "",true),
    FIELD_14(14, "",false),
    FIELD_15(15, "",false),
    FIELD_16(16, "",false),
    FIELD_17(17, "",false),
    FIELD_18(18, "",false),
    FIELD_19(19, "",false),

    FIELD_20(20, "",false),
    FIELD_21(21, "",false),
    FIELD_22(22, "",true),
    FIELD_23(23, "",true),
    FIELD_24(24, "",false),
    FIELD_25(25, "",false),
    FIELD_26(26, "",false),
    FIELD_27(27, "",false),
    FIELD_28(28, "",false),
    FIELD_29(29, "",false),

    FIELD_30(30, "",false),
    FIELD_31(31, "",false),
    FIELD_32(32, "",false),
    FIELD_33(33, "",false),
    FIELD_34(34, "",false),
    FIELD_35(35, "",true),
    FIELD_36(36, "",false),
    FIELD_37(37, "",true),
    FIELD_38(38, "",false),
    FIELD_39(39, "",false),

    FIELD_40(40, "",false),
    FIELD_41(41, "TID",false),
    FIELD_42(42, "",true),
    FIELD_43(43, "",true),
    FIELD_44(44, "",false),
    FIELD_45(45, "",false),
    FIELD_46(46, "",false),
    FIELD_47(47, "",false),
    FIELD_48(48, "",false),
    FIELD_49(49, "",true),

    FIELD_50(50, "",false),
    FIELD_51(51, "",false),
    FIELD_52(52, "",false),
    FIELD_53(53, "",false),
    FIELD_54(54, "",false),
    FIELD_55(55, "RESERVED ISO",true),
    FIELD_56(56, "RESERVED ISO",false),
    FIELD_57(57, "RESERVED NATIONAL",false),
    FIELD_58(58, "RESERVED NATIONAL",false),
    FIELD_59(59, "RESERVED NATIONAL",false),

    FIELD_60(60, "RESERVED NATIONAL",false),
    FIELD_61(61, "RESERVED NATIONAL",false),
    FIELD_62(62, "RESERVED NATIONAL",false),
    FIELD_63(63, "",false),
    FIELD_64(64, "",false),
    FIELD_65(65, "",false),
    FIELD_66(66, "",false),
    FIELD_67(67, "",false),
    FIELD_68(68, "",false),
    FIELD_69(69, "",false),

    FIELD_70(70, "",false),
    FIELD_71(71, "",false),
    FIELD_72(72, "",false),
    FIELD_73(73, "",false),
    FIELD_74(74, "",false),
    FIELD_75(75, "",false),
    FIELD_76(76, "",false),
    FIELD_77(77, "DEBITS REVERSAL NUMBER",false),
    FIELD_78(78, "TRANSFER NUMBER",false),
    FIELD_79(79, "TRANSFER REVERSAL NUMBER",false),

    FIELD_80(80, "INQUIRIES NUMBER",false),
    FIELD_81(81, "AUTHORIZATION NUMBER",false),
    FIELD_82(82, "CREDITS, PROCESSING FEE AMOUNT",false),
    FIELD_83(83, "CREDITS, TRANSACTION FEE AMOUNT",false),
    FIELD_84(84, "DEBITS, PROCESSING FEE AMOUNT",false),
    FIELD_85(85, "DEBITS, TRANSACTION FEE AMOUNT",false),
    FIELD_86(86, "CREDITS, AMOUNT",false),
    FIELD_87(87, "CREDITS, REVERSAL AMOUNT",false),
    FIELD_88(88, "DEBITS, AMOUNT",false),
    FIELD_89(89, "DEBITS, REVERSAL AMOUNT",false),

    FIELD_90(90, "ORIGINAL DATA ELEMENTS",false),
    FIELD_91(91, "FILE UPDATE CODE",false),
    FIELD_92(92, "FILE SECURITY CODE",false),
    FIELD_93(93, "RESPONSE INDICATOR",false),
    FIELD_94(94, "SERVICE INDICATOR",false),
    FIELD_95(95, "REPLACEMENT AMOUNTS",false),
    FIELD_96(96, "MESSAGE SECURITY CODE",false),
    FIELD_97(97, "AMOUNT, NET SETTLEMENT",false),
    FIELD_98(98, "PAYEE",false),
    FIELD_99(99, "SETTLEMENT INSTITUTION IDENT CODE",false),

    FIELD_100(100, "RECEIVING INSTITUTION IDENT CODE",false),
    FIELD_101(101, "FILE NAME",false),
    FIELD_102(102, "ACCOUNT IDENTIFICATION 1",false),
    FIELD_103(103, "ACCOUNT IDENTIFICATION 2",false),
    FIELD_104(104, "TRANSACTION DESCRIPTION",false),
    FIELD_105(105, "RESERVED ISO USE",false),
    FIELD_106(106, "RESERVED ISO USE",false),
    FIELD_107(107, "RESERVED ISO USE",false),
    FIELD_108(108, "RESERVED ISO USE",false),
    FIELD_109(109, "RESERVED ISO USE",false),

    FIELD_110(110, "RESERVED ISO USE",false),
    FIELD_111(111, "RESERVED ISO USE",false),
    FIELD_112(112, "RESERVED PRIVATE USE",false),
    FIELD_113(113, "RESERVED PRIVATE USE",false),
    FIELD_114(114, "RESERVED PRIVATE USE",false),
    FIELD_115(115, "RESERVED PRIVATE USE",false),
    FIELD_116(116, "RESERVED PRIVATE USE",false),
    FIELD_117(117, "RESERVED PRIVATE USE",false),
    FIELD_118(118, "RESERVED PRIVATE USE",false),
    FIELD_119(119, "RESERVED PRIVATE USE",false),

    FIELD_120(120, "RESERVED PRIVATE USE",false),
    FIELD_121(121, "RESERVED PRIVATE USE",false),
    FIELD_122(122, "RESERVED PRIVATE USE",false),
    FIELD_123(123, "RESERVED PRIVATE USE",false),
    FIELD_124(124, "RESERVED PRIVATE USE",false),
    FIELD_125(125, "RESERVED PRIVATE USE",false),
    FIELD_126(126, "RESERVED PRIVATE USE",false),
    FIELD_127(127, "RESERVED PRIVATE USE",false),
    FIELD_128(128,"",false),


    ;
    /**
     * 字段域
     */
    private final int field;

    private final String desc;
    /** 卡交易必须传入 */
    private final boolean cardTradeMust;

    public static final List<Integer> CARD_TRADE_VALID_FIELD_LIST = Arrays.stream(values()).filter(x->x.cardTradeMust).map(FieldEnum::getField).collect(Collectors.toList());
}
