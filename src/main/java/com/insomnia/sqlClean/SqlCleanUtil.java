package com.insomnia.sqlClean;

import com.github.vertical_blank.sqlformatter.SqlFormatter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Jay  zhixin.yuan@transsnet.com
 * @project: insomnia-project
 * @description:
 * @date 2024/11/14
 */
@Slf4j
public class SqlCleanUtil {

    public static void main(String[] args) {
        String sql = """
                CREATE TABLE `pos_cloud_momo_order` (
                  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                  `channel` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '渠道',
                  `tenant_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '租户号',
                  `sn` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '设备号',
                  `order_id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '订单号',
                  `order_status` tinyint(1) NOT NULL COMMENT '订单状态 1:pending 2:成功 3:失败',
                  `order_amount` varchar(12) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '交易金额',
                  `payer_account` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '付款账户名',
                  `payer_phone` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '付款手机号',
                  `resp_code` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '渠道返回码',
                  `resp_msg` text COLLATE utf8mb4_unicode_ci COMMENT '渠道返回信息',
                  `network` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '运营商',
                  `transaction_time` timestamp NOT NULL COMMENT '交易发起时间',
                  `complete_time` timestamp NULL DEFAULT NULL COMMENT '交易完成时间',
                  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除 0：否 1：是',
                  `gmt_created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                  `gmt_modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
                  `stan` varchar(6) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'stan',
                  `rrn` varchar(12) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'rrn',
                  `tid` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'tid',
                  `mid` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'mid',
                  `reference` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '路由id',
                  `filter` tinyint NOT NULL DEFAULT '0' COMMENT '是否是需要过滤',
                  `remark` text COLLATE utf8mb4_unicode_ci COMMENT '备注',
                  `terminal_order_id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '终端订单ID',
                  `pending_times` int NOT NULL DEFAULT '0' COMMENT 'pending 次数',
                  PRIMARY KEY (`id`),
                  UNIQUE KEY `uk_order_id` (`order_id`),
                  KEY `idx_sn_gmt_created_status` (`sn`,`gmt_created`,`order_status`) USING BTREE,
                  KEY `idx_tenant_createtime` (`tenant_id`,`gmt_created`) USING BTREE,
                  KEY `idx_created_id_reference` (`gmt_created`,`id`,`reference`)
                ) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='POS momo云收单订单表';
                """;
        saveIntoClipboard(formatSql(sql));
    }

    public static void saveIntoClipboard(String formatSql) {
        log.info("format sql:\n{}", formatSql);
//        Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
//        StringSelection stringSelection = new StringSelection(formatSql);
//        clip.setContents(stringSelection, null);
    }

    public static String formatSql(String sql) {
        try {
            String formatSql = SqlFormatter.format(sql);
            formatSql = formatSql.replaceAll("`", "")
                    .replaceAll(" COLLATE utf8mb4_unicode_ci", "")
                    .replaceAll(" DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci", "")
                    .replaceAll("ON\n  UPDATE\n    ", "ON UPDATE CURRENT_TIMESTAMP ");
            return formatSql;
        } catch (Exception e) {
            log.error("format sql error", e);
            return null;
        }
    }
}
