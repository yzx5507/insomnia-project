package com.insomnia;

import cn.hutool.core.date.DateUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Jay  zhixin.yuan@transsnet.com
 * @project: insomnia-project
 * @description:
 * @date 2023/12/18
 */
@Slf4j
public class GenerateModelMockDataAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        PsiFile psiFile = anActionEvent.getRequiredData(CommonDataKeys.PSI_FILE);
        Editor editor = anActionEvent.getRequiredData(CommonDataKeys.EDITOR);
        PsiElement elementAtCaret = psiFile.findElementAt(editor.getCaretModel().getOffset());
        if (elementAtCaret == null) {
            log.warn("not found elementAtCaret");
            return;
        }

        PsiClass psiClass = PsiTreeUtil.getParentOfType(elementAtCaret, PsiClass.class);
        if (psiClass == null) {
            log.warn("not found psiClass");
            return;
        }

        try {
            Map<String, Object> jsonMap = new LinkedHashMap<>();
            for (PsiField field : psiClass.getAllFields()) {
                // 忽略静态字段和常量
                if (field.hasModifierProperty(PsiModifier.STATIC) || field.hasModifierProperty(PsiModifier.FINAL)) {
                    continue;
                }
                String fieldName = field.getName();
                String fieldType = field.getType().getCanonicalText();
                Object fieldValue = generateDefaultValue(fieldType);
                jsonMap.put(fieldName, fieldValue);
            }

            while (psiClass.getSuperClass() != null) {
                psiClass = psiClass.getSuperClass();
                for (PsiField field : psiClass.getAllFields()) {
                    // 忽略静态字段和常量
                    if (field.hasModifierProperty(PsiModifier.STATIC) || field.hasModifierProperty(PsiModifier.FINAL)) {
                        continue;
                    }
                    String fieldName = field.getName();
                    String fieldType = field.getType().getCanonicalText();
                    Object fieldValue = generateDefaultValue(fieldType);
                    jsonMap.putIfAbsent(fieldName, fieldValue);
                }
            }

            Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
            setSysClipboardText(gson.toJson(jsonMap));
        } catch (Exception e) {
            log.warn("generate model exception, className: {}", psiClass.getQualifiedName(), e);
        }
    }

    private Object generateDefaultValue(String fieldType) {
        // 根据类型生成简单的默认值，这里仅为示例，实际情况可能更复杂
        return switch (fieldType) {
            case "java.lang.String" -> "";
            case "int", "long","double","java.lang.Long", "java.lang.Double", "java.lang.Integer" -> 0;
            case "boolean", "java.lang.Boolean" -> false;
            case "java.util.Date" -> DateUtil.now();
            default -> null;
        };
    }

    public static void setSysClipboardText(String text) {
        log.debug("write into system clipboard text" + text);
        Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable tText = new StringSelection(text);
        clip.setContents(tText, null);
    }
}
