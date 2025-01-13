package com.insomnia.parseISOMsg.utils;

import com.insomnia.multiCountry.utils.RSAUtils;
import lombok.extern.slf4j.Slf4j;
import org.jpos.iso.ISOField;
import org.jpos.iso.ISOMsg;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;

/**
 * <p>
 * 加解包工具类
 * </p>
 *
 * @author xiaowei.yang@transsnet.com
 * @since 2021/12/16 11:14 上午
 */
@Slf4j
public class PackUtil {

    public static String packHttpISOMsg(byte[] respByteArr, ISOMsg finallySendMsg) {
        if (respByteArr == null) {
            return null;
        }
        // 写入长度
        byte[] sendLen = new byte[2];
        int sendDataLen = respByteArr.length;
        sendLen[0] = (byte) ((sendDataLen >> 8) & 0xFF);
        sendLen[1] = (byte) (sendDataLen & 0xFF);
        byte[] finalByteArr = byteMerger(sendLen, respByteArr);
        log.info("组装渠道响应报文成功:{}, 长度为:{}", WrapISOMsg.toContent(finallySendMsg), finalByteArr.length);
        // 报文字节数组转成字符串
        BASE64EncoderWrap encoderWrap = new BASE64EncoderWrap();
        String tempStr = encoderWrap.encode(finalByteArr);
        String urlEncodeStr = null;
        try {
            urlEncodeStr = URLEncoder.encode(tempStr, "UTF-8");
        } catch (Exception e) {
            log.warn("packHttp urlEncoder error e", e);
        }
        return urlEncodeStr;
    }

    public static String packHttp(byte[] respByteArr, WrapISOMsg finallySendMsg) {
        if (respByteArr == null) {
            return null;
        }
        // 写入长度
        byte[] sendLen = new byte[2];
        int sendDataLen = respByteArr.length;
        sendLen[0] = (byte) ((sendDataLen >> 8) & 0xFF);
        sendLen[1] = (byte) (sendDataLen & 0xFF);
        byte[] finalByteArr = byteMerger(sendLen, respByteArr);
        log.info("组装渠道响应报文成功:{}, 长度为:{}", finallySendMsg, finalByteArr.length);
        // 报文字节数组转成字符串
        BASE64EncoderWrap encoderWrap = new BASE64EncoderWrap();
        String tempStr = encoderWrap.encode(finalByteArr);
        String urlEncodeStr = null;
        try {
            urlEncodeStr = URLEncoder.encode(tempStr, "UTF-8");
        } catch (Exception e) {
            log.warn("packHttp urlEncoder error e", e);
        }
        return urlEncodeStr;
    }

    private static byte[] byteMerger(byte[] bt1, byte[] bt2) {
        byte[] bt3 = new byte[bt1.length + bt2.length];
        System.arraycopy(bt1, 0, bt3, 0, bt1.length);
        System.arraycopy(bt2, 0, bt3, bt1.length, bt2.length);
        return bt3;
    }

    public static ISOMsg unPackRequestPackage(String sendPackage) {
        ISOMsg iosMsg = null;
        try {
            String urlDecodeStr = URLDecoder.decode(sendPackage, "UTF-8");
            BASE64DecoderWrap decoderWrap = new BASE64DecoderWrap();
            byte[] data = decoderWrap.decodeBuffer(urlDecodeStr);
            int len = data.length;
            if (len < 3) {
                // 长度小于报文头长度则丢弃
                return null;
            }
            byte[] dataTarget = new byte[len - 2];
            //丢弃2位报文请求头
            System.arraycopy(data, 2, dataTarget, 0, len - 2);
            SWISO87APackager packager = new SWISO87APackager();
            packager.setData(dataTarget);
            iosMsg = new WrapISOMsg();
            iosMsg.setPackager(packager);
            iosMsg.unpack(dataTarget);
        } catch (Exception e) {
            log.warn("解包失败,sendPackage={}", sendPackage, e);
        }
        return iosMsg;
    }

    public static String packResponsePackage(byte[] respByteArr) {
        if (respByteArr == null) {
            return null;
        }
        // 写入长度
        byte[] sendLen = new byte[2];
        int sendDataLen = respByteArr.length;
        sendLen[0] = (byte) ((sendDataLen >> 8) & 0xFF);
        sendLen[1] = (byte) (sendDataLen & 0xFF);
        byte[] finalByteArr = byteMerger(sendLen, respByteArr);
        // 报文字节数组转成字符串
        BASE64EncoderWrap encoderWrap = new BASE64EncoderWrap();
        String tempStr = encoderWrap.encode(finalByteArr);
        String urlEncodeStr = null;
        try {
            urlEncodeStr = URLEncoder.encode(tempStr, "UTF-8");
        } catch (Exception e) {
            log.warn("packHttp urlEncoder error e", e);
        }
        return urlEncodeStr;
    }

    public static String printlnMessage(ISOMsg isoMsg) {
        if (isoMsg == null) {
            return null;
        }
        StringBuffer buffer = new StringBuffer("\n<isomsg>\n  <mti id=\"MTI\" value=\"" + isoMsg.getString(FieldEnum.FIELD_0.getField()) + "\"/>\n");
        Map<Integer, Object> fieldMap = isoMsg.getChildren();
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
}
