package com.insomnia.parseISOMsg.utils;

import com.alibaba.fastjson2.JSON;
import com.insomnia.multiCountry.utils.RSAUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.jpos.iso.ISOField;
import org.jpos.iso.ISOMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.TreeMap;

/**
 * @author: yingxu.pi@transsnet.com
 * @date: 2020/12/17 17:14
 */
@Setter
@Getter
public class WrapISOMsg extends ISOMsg {

    private static final Logger log = LoggerFactory.getLogger(WrapISOMsg.class);

    @SneakyThrows
    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer("\n<isomsg>\n  <mti id=\"MTI\" value=\"" + this.getMTI() + "\"/>\n");
        Map<Integer, Object> fieldMap = this.getChildren();
        for (Integer key : fieldMap.keySet()) {
            if (key < 0) {
                continue;
            }
            Object val = fieldMap.get(key);
            if (val instanceof ISOField) {
                ISOField isoField = (ISOField) val;
                if (key == 2) {
                    buffer.append("  <field id=\"" + key + "\" value=\"" + RSAUtils.strMask(isoField.getValue().toString()) + "\"/>\n");
                } else {
                    buffer.append("  <field id=\"" + key + "\" value=\"" + isoField.getValue() + "\"/>\n");
                }
            } else {
                if (val instanceof ISOMsg) {
                    Map<Integer, Object> fieldMap2 = ((ISOMsg) val).getChildren();
                    for (Integer key2 : fieldMap2.keySet()) {
                        if (key2 < 0) {
                            continue;
                        }
                        Object val2 = fieldMap2.get(key2);
                        if (val2 instanceof ISOField) {
                            ISOField isoField2 = (ISOField) val2;
                            buffer.append("  <field id=\"" + key + "." + key2 + "\" value=\"" + isoField2.getValue() + "\"/>\n");
                        } else {
                            if (val2 instanceof ISOMsg) {
                                Map<Integer, Object> fieldMap3 = ((ISOMsg) val2).getChildren();
                                for (Integer key3 : fieldMap3.keySet()) {
                                    if (key3 < 0) {
                                        continue;
                                    }
                                    Object val3 = fieldMap3.get(key3);
                                    if (val3 instanceof ISOField) {
                                        ISOField isoField3 = (ISOField) val3;
                                        buffer.append("  <field id=\"" + key + "." + key2 + "." + key3 + "\" value=\"" + isoField3.getValue() + "\"/>\n");
                                    } else {
                                        buffer.append("  <field id=\"" + key3 + "\" value=\"" + val3.toString() + "\"/>\n");
                                    }
                                }
                            } else {
                                buffer.append("  <field id=\"" + key2 + "\" value=\"" + val2.toString() + "\"/>\n");
                            }
                        }
                    }
                } else {
                    buffer.append("  <field id=\"" + key + "\" value=\"" + val.toString() + "\"/>\n");
                }
            }
        }
        buffer.append("</isomsg>\n");
        return buffer.toString();
    }


    public static String toContent(ISOMsg that) {
        TreeMap<Integer, String> retMap = new TreeMap();
        Map<Integer, Object> fieldMap = that.getChildren();
        for (Integer key : fieldMap.keySet()) {
            Object val = fieldMap.get(key);
            if (val instanceof ISOField) {
                ISOField isoField = (ISOField) val;
                retMap.put(key, isoField.getValue().toString());
            } else {
                retMap.put(key, val.toString());
            }
        }
        return JSON.toJSONString(retMap);
    }

}
